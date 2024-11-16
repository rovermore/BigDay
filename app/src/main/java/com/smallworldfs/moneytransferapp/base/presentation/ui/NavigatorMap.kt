package com.smallworldfs.moneytransferapp.base.presentation.ui

import com.smallworldfs.moneytransferapp.base.presentation.viewmodel.Success
import com.smallworldfs.moneytransferapp.modules.home.presentation.ui.activity.HomeActivity
import com.smallworldfs.moneytransferapp.modules.status.presentation.ui.activity.LivezillaActivity
import com.smallworldfs.moneytransferapp.modules.status.presentation.ui.activity.SendEmailActivity
import com.smallworldfs.moneytransferapp.modules.status.presentation.ui.activity.SendEmailLimitedUserActivity
import com.smallworldfs.moneytransferapp.modules.web.presentation.SWWebViewActivity
import com.smallworldfs.moneytransferapp.presentation.forgotpassword.ForgotPasswordActivity
import com.smallworldfs.moneytransferapp.presentation.login.LoginActivity
import com.smallworldfs.moneytransferapp.presentation.onboard.OnBoardActivity
import com.smallworldfs.moneytransferapp.presentation.quicklogin.PassCodeActivity
import com.smallworldfs.moneytransferapp.presentation.resetpassword.ResetPasswordActivity
import com.smallworldfs.moneytransferapp.presentation.welcome.WelcomeActivity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigatorMap @Inject constructor() : HashMap<Class<*>, Success>() {

    init {
        put(OnBoardActivity::class.java, Success.ON_BOARDING_ACTIVITY)
        put(WelcomeActivity::class.java, Success.WELCOME_ACTIVITY)
        put(LoginActivity::class.java, Success.LOGIN_ACTIVITY)
        put(PassCodeActivity::class.java, Success.PASS_CODE_ACTIVITY)
        put(ForgotPasswordActivity::class.java, Success.FORGOT_PASSWORD_ACTIVITY)
        put(ResetPasswordActivity::class.java, Success.RESET_PASSWORD_ACTIVITY)
        put(HomeActivity::class.java, Success.HOME_ACTIVITY)
        put(SendEmailActivity::class.java, Success.SEND_EMAIL_ACTIVITY)
        put(SendEmailLimitedUserActivity::class.java, Success.SEND_EMAIL_LIMITED_USER_ACTIVITY)
        put(LivezillaActivity::class.java, Success.LIVEZILLA_ACTIVITY)
        put(SWWebViewActivity::class.java, Success.SWWEBVIEW_ACTIVITY)
    }
}
