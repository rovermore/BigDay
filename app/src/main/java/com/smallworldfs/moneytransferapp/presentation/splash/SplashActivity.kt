package com.smallworldfs.moneytransferapp.presentation.splash

import android.os.Bundle
import android.os.Environment
import androidx.activity.viewModels
import androidx.multidex.BuildConfig
import com.contentsquare.android.Contentsquare
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.show
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.showForceUpdateDialog
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.showSplashErrorDialog
import com.smallworldfs.moneytransferapp.base.presentation.viewmodel.EventObserver
import com.smallworldfs.moneytransferapp.databinding.ActivitySplashBinding
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericActivity
import com.smallworldfs.moneytransferapp.modules.documents.repository.DocumentsRepository
import com.smallworldfs.moneytransferapp.utils.BUILD_CONFIG_FLAVOR_MOCK
import com.smallworldfs.moneytransferapp.utils.BUILD_CONFIG_FLAVOR_PRE
import com.smallworldfs.moneytransferapp.utils.BUILD_CONFIG_FLAVOR_PRO
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import com.smallworldfs.moneytransferapp.utils.gone
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : GenericActivity() {

    private val viewModel: SplashViewModel by viewModels()

    @Inject
    lateinit var splashNavigator: SplashNavigator

    private var _binding: ActivitySplashBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupObservers()
        setupView()
        setupInitialConfigurationOfTheApp()

        viewModel.loadData()
    }

    private fun setupObservers() {
        viewModel.updateRequired.observe(
            this,
            EventObserver {
                showForceUpdateDialog()
            },
        )

        viewModel.userLoggedIn.observe(
            this,
            EventObserver { appToken ->
                splashNavigator.navigateToLoginActivity(appToken)
            },
        )

        viewModel.newInstallation.observe(
            this,
            EventObserver {
                splashNavigator.navigateToOnBoardActivity()
            },
        )

        viewModel.notExistingUser.observe(
            this,
            EventObserver {
                splashNavigator.navigateToWelcomeActivity()
            },
        )

        viewModel.loading.observe(
            this,
            EventObserver { loading ->
                if (loading) binding.activitySplashProgressBar.gone()
                else binding.activitySplashProgressBar.show()
            },
        )

        viewModel.error.observe(
            this,
            EventObserver {
                showSplashErrorDialog()
            },
        )
    }

    fun setupView() {
        if (BuildConfig.FLAVOR == BUILD_CONFIG_FLAVOR_PRO) binding.activitySplashTextViewVersion.gone() else binding.activitySplashTextViewVersion.show()
        when (BuildConfig.FLAVOR) {
            BUILD_CONFIG_FLAVOR_MOCK -> binding.activitySplashTextViewVersion.text = getString(R.string.app_name_mock)
            BUILD_CONFIG_FLAVOR_PRE -> binding.activitySplashTextViewVersion.text = getString(R.string.app_name_pre)
            BUILD_CONFIG_FLAVOR_PRO -> binding.activitySplashTextViewVersion.text = STRING_EMPTY
        }
    }

    private fun setupInitialConfigurationOfTheApp() {
        // Stop tracking id of ContentSquare until the user start session
        Contentsquare.optOut(this)

        // Remove all images in cache available
        // TODO: Remove when migrate documents repository to new architecture
        if (BuildConfig.FLAVOR != BUILD_CONFIG_FLAVOR_MOCK) {
            DocumentsRepository.getInstance().removeAllImages(getExternalFilesDir(Environment.DIRECTORY_PICTURES))
        }
    }
}
