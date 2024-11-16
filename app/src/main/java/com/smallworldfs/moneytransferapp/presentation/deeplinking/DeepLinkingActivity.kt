package com.smallworldfs.moneytransferapp.presentation.deeplinking

import android.os.Bundle
import androidx.activity.viewModels
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.base.presentation.viewmodel.EventObserver
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericActivity
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DeepLinkingActivity : GenericActivity() {

    private val viewModel: DeepLinkingViewModel by viewModels()

    @Inject
    lateinit var deepLinkingNavigator: DeepLinkingNavigator

    private val RESET_PASSWORD_SCHEME = "password/reset"
    private val EMAIL_VALIDATION_SCHEME = "emailvalidation"

    var deepLinkUrl: String = STRING_EMPTY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent?.data?.toString()?.let { deepLinkUrl = it }
        setupObservers()
        setupView()
    }

    private fun setupObservers() {
        viewModel.userLogged.observe(
            this,
            EventObserver {
                deepLinkingNavigator.navigateToHomeActivity()
            }
        )

        viewModel.userNotLogged.observe(
            this,
            EventObserver {
                deepLinkingNavigator.navigateToSplashActivity()
            }
        )
    }

    private fun setupView() {
        val view = this.window.decorView
        view.setBackgroundColor(getColor(R.color.main_blue))

        if (deepLinkUrl.contains(RESET_PASSWORD_SCHEME)) {
            val splittedUrl: Array<String> = deepLinkUrl.split("/").toTypedArray()

            var position = 0
            for (field in splittedUrl) {
                if (field == "reset") {
                    break
                }
                position++
            }

            if (splittedUrl.size >= position + 1) {
                val userResetToken = splittedUrl[position + 1]
                deepLinkingNavigator.navigateToResetPasswordActivity(userResetToken)
            }
        }

        if (deepLinkUrl.contains(EMAIL_VALIDATION_SCHEME))
            viewModel.checkUserIsLoggedIn()
    }
}
