package com.smallworldfs.moneytransferapp.presentation.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.compose.colors.blueBackgroundWelcome
import com.smallworldfs.moneytransferapp.compose.colors.colorBlueOcean
import com.smallworldfs.moneytransferapp.compose.colors.colorGreenMain
import com.smallworldfs.moneytransferapp.compose.colors.neutral0
import com.smallworldfs.moneytransferapp.compose.widgets.SWCircularLoader
import com.smallworldfs.moneytransferapp.compose.widgets.SWErrorView
import com.smallworldfs.moneytransferapp.compose.widgets.SWText
import com.smallworldfs.moneytransferapp.presentation.base.ErrorType
import com.smallworldfs.moneytransferapp.presentation.login.model.UserUIModel
import com.smallworldfs.moneytransferapp.utils.Constants
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(),
    greetingTextDay: String,
    regionClickListeners: NewUserLoginScreenClickListeners,
    registerEvent: (String) -> Unit,
    loginActivityCallbacks: LoginActivityCallbacks
) {
    val selectedCountry by viewModel.selectedCountry.collectAsStateWithLifecycle()
    val existingUser by viewModel.existingUser.collectAsStateWithLifecycle()
    val image = Constants.COUNTRY.FLAG_IMAGE_ASSETS +
        selectedCountry.iso3 +
        Constants.COUNTRY.FLAG_IMAGE_EXTENSION
    val userIndicator = String.format(greetingTextDay, existingUser?.name)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val userLogged by viewModel.userLogged.collectAsStateWithLifecycle()
    val userEmail by viewModel.userEmail.collectAsStateWithLifecycle()
    val loading by viewModel.loading.collectAsStateWithLifecycle()
    val biometricsEnabled by viewModel.biometricsEnabled.collectAsStateWithLifecycle()
    val passcodeEnabled by viewModel.passcodeEnabled.collectAsStateWithLifecycle()
    val loginError by viewModel.loginError.collectAsStateWithLifecycle()
    val showError by viewModel.errorView.collectAsStateWithLifecycle()

    if (loginError !is ErrorType.None)
        loginActivityCallbacks.onError(loginError, selectedCountry.iso3)

    LaunchedEffect(key1 = userLogged) {
        if (userLogged.name.isNotEmpty())
            loginActivityCallbacks.userLoggedIn(userLogged)
    }
    LaunchedEffect(key1 = userEmail) {
        if (userEmail.isNotEmpty())
            viewModel.isBiometricsEnabled()
    }
    LaunchedEffect(key1 = biometricsEnabled) {
        if (biometricsEnabled)
            loginActivityCallbacks.setUpBiometricAccess()
    }
    LaunchedEffect(key1 = passcodeEnabled) {
        if (passcodeEnabled)
            loginActivityCallbacks.setupPasscode()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(neutral0),
    ) {
        Column {
            DiscoverRatesHeader {
                loginActivityCallbacks.onTryItHeaderClicked()
            }

            LogoBanner()

            Box(modifier = Modifier.padding(30.dp)) {
                when (uiState) {
                    LoginUIState.EXISTING_USER -> {
                        StoredUserLoginScreen(
                            image, userIndicator,
                            onForgotPasswordClicked = {
                                loginActivityCallbacks.navigateToForgotPasswordActivity()
                            },
                            onChangeAccountClicked = {
                                registerEvent("click_change_account")
                                viewModel.changeAccount()
                            },
                        ) { password ->
                            existingUser?.let {
                                viewModel.onActionNewLogin(
                                    it.email,
                                    password.toCharArray(),
                                    it.country.countries[0].iso3,
                                )
                            }
                        }
                    }

                    LoginUIState.NEW_USER -> {
                        NewUserLoginScreen(
                            loginError, regionClickListeners, viewModel,
                        )
                    }
                }
            }

            RegisterContainer { loginActivityCallbacks.navigateToRegisterActivity(userLogged?.country?.countries?.firstOrNull()?.iso3 ?: STRING_EMPTY) }
        }

        if (loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { },
            ) {
                SWCircularLoader(
                    size = 32.dp,
                    color = colorGreenMain
                )
            }
        }

        if (loginError is ErrorType.LoginCredentials) {
            SWErrorView(stringResource(id = R.string.error_login_title), loginError.message) { viewModel.removeError() }
        } else if (showError) {
            SWErrorView { viewModel.removeError() }
        }
    }
}

@Composable
fun RegisterContainer(onClick: () -> Unit = {}) {
    Row {
        SWText(
            text = stringResource(id = R.string.get_started_not_account),
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .padding(end = 10.dp),
            textAlign = TextAlign.End, color = colorBlueOcean, fontWeight = FontWeight.Bold,
        )
        SWText(
            text = stringResource(id = R.string.get_started_login_button).uppercase(),
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .padding(start = 10.dp)
                .clickable { onClick() },
            textAlign = TextAlign.Start, color = colorGreenMain, fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
fun LogoBanner() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .background(colorGreenMain),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            modifier = Modifier
                .width(130.dp)
                .height(46.dp),
            painter = painterResource(id = R.drawable.ic_splash_logo),
            contentDescription = "logo",
        )
    }
}

@Composable
fun DiscoverRatesHeader(onClick: () -> Unit = {}) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = blueBackgroundWelcome)
            .clickable { onClick() },
    ) {
        val (text, arrow) = createRefs()
        SWText(
            modifier = Modifier
                .constrainAs(text) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
                .padding(20.dp),
            text = stringResource(id = R.string.take_a_look),
            fontSize = 18.sp,
            color = neutral0,
            fontWeight = FontWeight.ExtraBold,
        )

        Image(
            modifier = Modifier
                .constrainAs(arrow) {
                    end.linkTo(parent.end, 16.dp)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
                .rotate(270f),
            painter = painterResource(id = R.drawable.ic_chevron_right),
            contentDescription = "bar_button",
        )
    }
}

@Preview(showBackground = true, widthDp = 420)
@Composable
fun LoginScreenPreview() {
    LoginScreen(
        greetingTextDay = "Good Evening Mark", registerEvent = {},
        regionClickListeners = object : NewUserLoginScreenClickListeners {
            override fun onRegionClicked() {}
            override fun onForgotPasswordClicked() {}
            override fun onSubmitButtonClicked() {}
            override fun setFieldError(eventLabel: String) {}
        },
        loginActivityCallbacks = object : LoginActivityCallbacks {
            override fun userLoggedIn(user: UserUIModel) {}
            override fun navigateToRegisterActivity(iso: String) {}
            override fun navigateToForgotPasswordActivity() {}
            override fun onTryItHeaderClicked() {}
            override fun setUpBiometricAccess() {}
            override fun setupPasscode() {}
            override fun onError(errorType: ErrorType, iso3: String) {}
        },
    )
}
