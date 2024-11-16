package com.smallworldfs.moneytransferapp.base.presentation.ui

import android.Manifest
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.afollestad.materialdialogs.MaterialDialog
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.base.presentation.navigator.BaseNavigator
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.AnalyticsActivityLifecycleCallback
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.AnalyticsEvent
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.AnalyticsSender
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ScreenEvent
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ScreenName
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.UserActionEvent
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.PositiveCallback
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.showDeviceRootedDialog
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.showGenericErrorDialog
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.showNoConnectionOrBadResponseDialog
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.showSessionExpiredDialog
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.showSingleActionErrorDialog
import com.smallworldfs.moneytransferapp.base.presentation.viewmodel.BaseViewModel
import com.smallworldfs.moneytransferapp.base.presentation.viewmodel.Error
import com.smallworldfs.moneytransferapp.base.presentation.viewmodel.ErrorType
import com.smallworldfs.moneytransferapp.base.presentation.viewmodel.State
import com.smallworldfs.moneytransferapp.base.presentation.viewmodel.Success
import com.smallworldfs.moneytransferapp.base.presentation.viewmodel.ViewModelFactory
import com.smallworldfs.moneytransferapp.databinding.ErrorScreenLayoutBinding
import com.smallworldfs.moneytransferapp.databinding.GenericErrorLayoutBinding
import com.smallworldfs.moneytransferapp.databinding.LoadingLayoutBinding
import com.smallworldfs.moneytransferapp.domain.migrated.capabilities.CapabilityChecker
import com.smallworldfs.moneytransferapp.modules.home.presentation.navigator.HomeNavigator
import com.smallworldfs.moneytransferapp.modules.notifications.domain.handler.PushPrimeClickHandler
import com.smallworldfs.moneytransferapp.presentation.common.session.SessionHandler.Companion.SESSION_EXPIRED_ACTION
import com.smallworldfs.moneytransferapp.utils.KeyboardHandler
import com.smallworldfs.moneytransferapp.utils.registerBroadcastReceiver
import javax.inject.Inject

abstract class BaseAppCompatActivity<T : BaseViewModel<N>, N : BaseNavigator, B : ViewBinding>(private val viewModelClass: Class<T>) : AppCompatActivity() {

    /**
     * View Binding variables
     */
    protected lateinit var binding: B

    abstract val bindingInflater: (LayoutInflater) -> B

    private lateinit var errorView: GenericErrorLayoutBinding

    private lateinit var errorLayout: ErrorScreenLayoutBinding

    private lateinit var loadingLayout: LoadingLayoutBinding

    lateinit var errorLayoutParent: ViewGroup

    /**
     * View Model variables
     */
    lateinit var viewModel: T

    @Inject
    lateinit var viewModelFactory: ViewModelFactory<T>

    private var mSessionExpiredDialog: MaterialDialog? = null

    var dialog: Dialog? = null

    @Inject
    lateinit var loader: Loader

    @Inject
    lateinit var navigator: N

    @Inject
    lateinit var keyboardHandler: KeyboardHandler

    @Inject
    lateinit var analyticsSender: AnalyticsSender

    @Inject
    lateinit var analyticsActivityLifecycleCallback: AnalyticsActivityLifecycleCallback

    @Inject
    lateinit var capabilityChecker: CapabilityChecker

    private val sessionReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            showSessionExpiredDialog { dialog ->
                val eventCategory = dialog.category
                trackEvent(
                    UserActionEvent(
                        eventCategory = eventCategory,
                        eventAction = "click_accept",
                        hierarchy = "${ScreenName.MODAL_SESSION_EXPIRED.value}_$eventCategory",
                    ),
                )
                viewModel.clearUser()
                HomeNavigator.navigateToLoginActivity(this@BaseAppCompatActivity, true)
            }
        }
    }

    private val inAppMessageReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                capabilityChecker.requestPermissions(
                    this@BaseAppCompatActivity,
                    Manifest.permission.POST_NOTIFICATIONS,
                    onGrantedPermissions = { _, _ -> },
                )
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = bindingInflater.invoke(layoutInflater)

        setContentView(binding.root)

        errorView = GenericErrorLayoutBinding.inflate(LayoutInflater.from(this), binding.root as ViewGroup, false)

        viewModel = ViewModelProvider(this, viewModelFactory).get(viewModelClass)

        navigator.setActivity(this)

        handleLoadingView()
        handleCommonErrors()

        configureView()
        configureViewModel()
        extractIntentData()

        viewModel.initRiskified()

        initActivity()
    }

    override fun onResume() {
        super.onResume()

        registerBroadcastReceiver(inAppMessageReceiver, IntentFilter(PushPrimeClickHandler.REQUEST_NOTIFICATION_PERMISSION), RECEIVER_NOT_EXPORTED)
        registerBroadcastReceiver(sessionReceiver, IntentFilter(SESSION_EXPIRED_ACTION), RECEIVER_NOT_EXPORTED)

        // Log crashlytics events
        FirebaseCrashlytics.getInstance().log(javaClass.name)

        viewModel.resumeViewModel()
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(sessionReceiver)
        unregisterReceiver(inAppMessageReceiver)
    }

    override fun onStop() {
        super.onStop()
        viewModel.releaseRiskified()
        dialog?.let {
            if (it.isShowing) {
                it.dismiss()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        item.let {
            return when (it.itemId) {
                android.R.id.home -> {
                    onBackPressed()
                    true
                }
                else -> super.onOptionsItemSelected(it)
            }
        }
    }

    fun trackScreen(screenName: String) {
        analyticsSender.trackScreen(screenName)
    }

    fun trackEvent(analyticsEvent: AnalyticsEvent) {
        analyticsSender.trackEvent(analyticsEvent)
    }

    fun getHierarchy(screenName: String): String {
        val screenEvent: ScreenEvent = if (screenName.isEmpty()) analyticsSender.getScreenEventProperties(this.javaClass.simpleName) else analyticsSender.getScreenEventProperties(screenName)
        return screenEvent.hierarchy
    }

    fun getEventCategory(screenName: String): String {
        val screenEvent: ScreenEvent = if (screenName.isEmpty()) analyticsSender.getScreenEventProperties(this.javaClass.simpleName) else analyticsSender.getScreenEventProperties(screenName)
        return screenEvent.screenCategory
    }

    private fun handleLoadingView() {
        viewModel.getState().observe(
            this,
            {
                when (it) {
                    is State.Loading<*> -> {
                        hideErrorLayout()
                        showLoadingLayout()
                    }
                    is State.Error -> hideLoadingLayout()
                    is State.Success<*> -> {
                        hideLoadingLayout()
                        hideErrorLayout()
                    }
                }
            },
        )
    }

    private fun handleCommonErrors() {
        viewModel.getState().observe(
            this,
            {
                if (it is State.Success<*>) {
                    if (it.data == Success.INTENT_ERROR) {
                        dialog?.let { currentDialog ->
                            if (currentDialog.isShowing) {
                                currentDialog.dismiss()
                            }
                        }
                        dialog = showSingleActionErrorDialog(getString(R.string.generic_title_error), getString(R.string.generic_subtitle_error), null)
                    }
                }
                if (it is State.Error) {
                    when (it.data.errorType) {
                        ErrorType.GENERIC ->
                            showGenericError(it.data.title, it.data.subtitle, it.data.error)
                        ErrorType.POPUP -> {
                            dialog?.let { currentDialog ->
                                if (currentDialog.isShowing) {
                                    currentDialog.dismiss()
                                }
                            }
                            dialog = when (it.data.error) {
                                Error.DEVICE_ROOTED -> showDeviceRootedDialog()
                                Error.UNKNOWN_ERROR -> showGenericErrorDialog(
                                    getString(R.string.generic_title_error), getString(R.string.generic_error_view_text), getString(R.string.accept_text),
                                    object : PositiveCallback {
                                        override fun onClick(dialog: Dialog) {
                                            dialog.dismiss()
                                        }
                                    },
                                    null, null,
                                )

                                Error.BAD_RESPONSE -> showGenericErrorDialog(
                                    getString(R.string.generic_title_error), getString(R.string.generic_error_view_text), getString(R.string.accept_text),
                                    object : PositiveCallback {
                                        override fun onClick(dialog: Dialog) {
                                            dialog.dismiss()
                                        }
                                    },
                                    null, null,
                                )

                                Error.NO_CONNECTION -> showNoConnectionOrBadResponseDialog(getString(R.string.no_connection_available_message), getString(R.string.no_connection_available_content)) {
                                    dialog?.dismiss()
                                    retryConnection()
                                }

                                Error.SESSION_EXPIRED -> showSessionExpiredDialog { dialog ->
                                    dialog.dismiss()
                                    HomeNavigator.navigateToLoginActivity(this@BaseAppCompatActivity, true)
                                }

                                Error.CHANGE_PASSWORD -> showGenericErrorDialog(
                                    getString(R.string.generic_title_error),
                                    getString(R.string._account_settings_change_password_error),
                                    getString(R.string.accept_text),
                                    object : PositiveCallback {
                                        override fun onClick(dialog: Dialog) {
                                            dialog.dismiss()
                                        }
                                    },
                                    null,
                                    null,
                                )

                                else -> showGenericErrorDialog(
                                    getString(R.string.generic_title_error),
                                    getString(R.string.generic_error_view_text),
                                    getString(R.string.accept_text),
                                    object : PositiveCallback {
                                        override fun onClick(dialog: Dialog) {
                                            dialog.dismiss()
                                        }
                                    },
                                    null,
                                    null,
                                )
                            }
                        }
                        ErrorType.FORM -> {
                        }
                        ErrorType.CUSTOM -> showErrorLayout()
                    }
                }
            },
        )
    }

    fun showGenericError(title: String, subtitle: String, error: Error) {
        val treeObserver = binding.root.viewTreeObserver
        treeObserver.addOnGlobalLayoutListener(
            object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    setupGenericErrorView(title, subtitle, error)
                    binding.root.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            },
        )

        if (!(binding.root as ViewGroup).children.contains(errorView.root)) {
            (binding.root as ViewGroup).addView(errorView.root)
        } else {
            setupGenericErrorView(title, subtitle, error)
        }
    }

    private fun setupGenericErrorView(title: String, subtitle: String, error: Error) {
        errorView.errorView.translationY = (-errorView.errorView.height).toFloat()

        errorView.errorTitle.text = when (error) {
            Error.LOGIN_INCORRECT -> getString(R.string.error_login_title)
            else -> if (title.isEmpty()) {
                getString(R.string.generic_error_view_action_subtitle)
            } else {
                title
            }
        }

        errorView.errorSubtitle.text = if (subtitle.isEmpty()) {
            getString(R.string.generic_subtitle_error)
        } else {
            subtitle
        }

        errorView.errorView.visibility = View.VISIBLE
        errorView.errorView.animate().translationYBy(errorView.errorView.height.toFloat()).setDuration(300).start()

        errorView.closeError.setOnClickListener {
            errorView.errorView.animate().translationYBy((-errorView.errorView.height).toFloat()).setDuration(300).start()
        }
    }

    private fun showErrorLayout() {
        if (::errorLayoutParent.isInitialized) {
            if (!::errorLayout.isInitialized) {
                errorLayout = ErrorScreenLayoutBinding.inflate(LayoutInflater.from(this), errorLayoutParent, false)
                errorLayout.retryButton.setOnClickListener {
                    retry()
                }
            }
            if (!errorLayoutParent.children.contains(errorLayout.root)) {
                errorLayoutParent.addView(errorLayout.root)
            }
        }
    }

    private fun hideErrorLayout() {
        if (::errorLayoutParent.isInitialized && ::errorLayout.isInitialized) {
            if (errorLayoutParent.children.contains(errorLayout.root)) {
                errorLayoutParent.removeView(errorLayout.root)
            }
        }
    }

    private fun showLoadingLayout() {
        if (::errorLayoutParent.isInitialized) {
            if (!::loadingLayout.isInitialized) {
                loadingLayout = LoadingLayoutBinding.inflate(LayoutInflater.from(this), errorLayoutParent, false)
            }
            if (!errorLayoutParent.children.contains(loadingLayout.root)) {
                errorLayoutParent.addView(loadingLayout.root)
            }
        }
    }

    fun hideLoadingLayout() {
        if (::errorLayoutParent.isInitialized && ::loadingLayout.isInitialized) {
            if (errorLayoutParent.children.contains(loadingLayout.root)) {
                errorLayoutParent.removeView(loadingLayout.root)
            }
        }
    }

    abstract fun configureView()
    abstract fun configureViewModel()
    abstract fun extractIntentData()
    abstract fun initActivity()

    open fun retry() {}
    open fun retryConnection() {}
}
