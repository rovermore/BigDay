package com.smallworldfs.moneytransferapp.base.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.AnalyticsEvent
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.AnalyticsSender
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ScreenEvent
import com.smallworldfs.moneytransferapp.base.presentation.viewmodel.BaseExtraData
import com.smallworldfs.moneytransferapp.base.presentation.viewmodel.BaseState
import com.smallworldfs.moneytransferapp.base.presentation.viewmodel.BaseStateViewModel
import com.smallworldfs.moneytransferapp.base.presentation.viewmodel.ViewModelFactory
import java.io.Serializable
import javax.inject.Inject

abstract class BaseStateFragment<S : Serializable, VM : BaseStateViewModel<S, *>, B : ViewBinding>(private val viewModelClass: Class<VM>) : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory<VM>

    @Inject
    lateinit var analyticsSender: AnalyticsSender

    abstract val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> B

    lateinit var binding: B

    private lateinit var vm: VM

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = bindingInflater.invoke(inflater, container, false)
        return binding.root
    }

    /**
     * The view model is instantiated on fragment resume.
     */
    override fun onResume() {
        super.onResume()
        activity?.let {
            initializeViewModel(it, this)
        }
    }

    /**
     * Destroy the view and unbind the observers from view model
     */
    override fun onPause() {
        super.onPause()
        vm.unBindObservables(this)
    }

    private fun initializeViewModel(fragmentActivity: FragmentActivity, fragment: Fragment? = null) {

        val inputState: S? = setInputState()

        vm = ViewModelProvider(this, viewModelFactory).get(viewModelClass)

        onViewModelInitialized(vm)

        vm.onStart(inputState?.let { BaseState.Normal(it) })

        vm.observableState.observe(fragment ?: fragmentActivity, Observer(this::onDataUpdated))
    }

    /**
     * Called when view model trigger an update view event
     */
    private fun onDataUpdated(state: BaseState<S>) {
        onStateNormal(state.data)
        if (state is BaseState.Alternative) {
            onStateAlternative(state.dataAlternative)
        } else if (state is BaseState.Error) {
            onStateError(state.dataError)
        }
    }

    /**
     * Called when come params from other activity
     */
    abstract fun setInputState(): S?

    /**
     * Called once the view model have been provided. Here must go the view set up
     */
    abstract fun onViewModelInitialized(viewModel: VM)

    /**
     * Called when view model trigger an update view event
     */
    abstract fun onStateNormal(data: S)

    /**
     * Called when view model trigger a updateAlternativeState event
     */
    abstract fun onStateAlternative(data: BaseExtraData)

    /**
     * Called when view model trigger an error event
     */
    abstract fun onStateError(error: Throwable)

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
}
