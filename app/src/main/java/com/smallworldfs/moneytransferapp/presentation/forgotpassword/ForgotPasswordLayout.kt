package com.smallworldfs.moneytransferapp.presentation.forgotpassword

import android.text.TextUtils
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.SmallWorldApplication.Companion.getStr
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ScreenName
import com.smallworldfs.moneytransferapp.compose.colors.colorBlueOcean
import com.smallworldfs.moneytransferapp.compose.colors.colorBlueOceanDark
import com.smallworldfs.moneytransferapp.compose.colors.colorGreenMain
import com.smallworldfs.moneytransferapp.compose.colors.darkGreyText
import com.smallworldfs.moneytransferapp.compose.colors.neutral0
import com.smallworldfs.moneytransferapp.presentation.base.ErrorType
import com.smallworldfs.moneytransferapp.presentation.common.countries.CountryUIModel
import com.smallworldfs.moneytransferapp.presentation.forgotpassword.ForgotPasswordActivity.Companion.ONE_SECOND
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import com.smallworldfs.moneytransferapp.utils.Utils
import com.smallworldfs.moneytransferapp.compose.dialogs.SWInfoDialog
import com.smallworldfs.moneytransferapp.compose.widgets.CountryFlagsLazyRow
import com.smallworldfs.moneytransferapp.compose.widgets.SWButton
import com.smallworldfs.moneytransferapp.compose.widgets.SWTopError
import com.smallworldfs.moneytransferapp.compose.widgets.SWImageFlag
import com.smallworldfs.moneytransferapp.compose.widgets.SWSearchBar
import com.smallworldfs.moneytransferapp.compose.widgets.SWText
import com.smallworldfs.moneytransferapp.compose.widgets.SWTextField
import kotlinx.coroutines.launch

@Composable
fun ForgotPasswordLayout(
    listener: ForgotPasswordListener,
    viewModel: ForgotPasswordViewModel = viewModel()
) {
    val showErrorView by viewModel.errorForgotPassword.collectAsStateWithLifecycle()
    val successForgotPassword by viewModel.successForgotPassword.collectAsStateWithLifecycle()
    val countrySelected by viewModel.countrySelected.collectAsStateWithLifecycle()
    val countries by viewModel.countries.collectAsStateWithLifecycle()
    val showCountries by viewModel.showCountries.collectAsStateWithLifecycle()
    var emailInput by rememberSaveable { mutableStateOf(STRING_EMPTY) }

    Column(
        modifier = Modifier
            .background(neutral0)
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()),
    ) {
        if (showErrorView !is ErrorType.None) {
            val errorDescription =
                when (showErrorView) {
                    is ErrorType.FieldError -> stringResource(R.string.empty_field)
                    is ErrorType.EmailScoreNotPassed -> stringResource(R.string.invalid_email)
                    else -> stringResource(R.string.generic_error_view_subtitle)
                }

            listener.registerEventCallback(
                "formKo",
                "error_validation_email",
                "login",
            )

            SWTopError(
                body = errorDescription
            ) {
                viewModel.hideErrorView()
            }
        }

        if (successForgotPassword) {
            listener.registerEventCallback(
                "formOk",
                STRING_EMPTY,
                "login"
            )
            listener.trackScreenName(ScreenName.RECOVER_PASSCODE_EMAIL_DIALOG.value)
            SWInfoDialog(
                title = stringResource(id = R.string.action_done_transactional_calculator),
                content = stringResource(id = R.string.email_sent_toast_text),
                positiveText = stringResource(id = R.string.accept_text),
                positiveAction = {
                    listener.registerEventCallback("click_accept", "", "")
                    viewModel.hideDialog()
                    listener.onBackAction()
                },
                dismissAction = {
                    viewModel.hideDialog()
                    listener.onBackAction()
                }
            )
        }

        Toolbar(
            registerEventCallback = { eventAction, eventLabel, formType ->
                listener.registerEventCallback(eventAction, eventLabel, formType)
            },
            onBackAction = { listener.onBackAction() },
        )

        Information()

        CountryFlagWithArrowIcon(
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            registerEventCallback = { eventAction, eventLabel, formType ->
                listener.registerEventCallback(eventAction, eventLabel, formType)
            },
            countrySelected = countrySelected,
            showCountries = showCountries,
            showCountriesCallback = { viewModel.showCountries(it) }
        )

        CountrySearch(
            onItemClicked = { viewModel.updateCountrySelected(it) },
            registerEventCallback = { eventAction, eventLabel, formType ->
                listener.registerEventCallback(eventAction, eventLabel, formType)
            },
            countries = countries,
            showCountries = showCountries,
            showCountriesCallback = { viewModel.showCountries(it) }
        )

        Spacer(modifier = Modifier.weight(1f))

        EmailInputText(
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            onEmailInput = {
                emailInput = it
            },
        )

        SendButton(
            modifier = Modifier
                .width(180.dp)
                .padding(bottom = 48.dp)
                .align(Alignment.CenterHorizontally),
        ) {
            if (TextUtils.isEmpty(emailInput)) {
                listener.registerEventCallback("formKo", getStr(R.string.empty_field), "login")
                viewModel.showErrorView(ErrorType.FieldError(STRING_EMPTY))
            } else if (!Utils.isValidEmail(emailInput)) {
                listener.registerEventCallback("formKo", getStr(R.string.invalid_email), "login")
                viewModel.showErrorView(ErrorType.EmailScoreNotPassed(STRING_EMPTY))
            } else {
                viewModel.hideErrorView()
                viewModel.requestForgotPassword(emailInput)
            }
        }
    }
}

@Composable
fun Toolbar(
    registerEventCallback: (eventAction: String, eventLabel: String, formType: String) -> Unit,
    onBackAction: () -> Unit
) {
    Row {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Image(
                modifier = Modifier
                    .width(48.dp)
                    .height(48.dp)
                    .padding(start = 16.dp)
                    .clickable {
                        registerEventCallback(
                            "click_back_help_password",
                            STRING_EMPTY,
                            STRING_EMPTY,
                        )
                        onBackAction()
                    },
                painter = painterResource(id = R.drawable.ic_action_arrow_back_grey),
                contentDescription = "back_button",
            )
        }
    }
}

@Composable
fun Information() {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        SWText(
            modifier = Modifier
                .fillMaxWidth(),
            text = stringResource(id = R.string.need_help_with_password),
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            color = darkGreyText,
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        SWText(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 48.dp,
                ),
            text = stringResource(id = R.string.need_help_with_password_subtitle),
            fontSize = 16.sp,
        )
    }
}

@Composable
fun CountryFlagWithArrowIcon(
    modifier: Modifier,
    registerEventCallback: (eventAction: String, eventLabel: String, formType: String) -> Unit,
    countrySelected: CountryUIModel,
    showCountries: Boolean,
    showCountriesCallback: (Boolean) -> Unit
) {
    val startRotation = -180f
    val endRotation = 0f

    var rotation by rememberSaveable { mutableFloatStateOf(startRotation) }
    val rotationAnimation = remember { Animatable(rotation) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(showCountries) {
        if (!showCountries) {
            rotation = startRotation
            scope.launch {
                rotationAnimation.animateTo(
                    targetValue = rotation,
                    animationSpec = tween(250, easing = LinearEasing),
                )
            }
        }
    }

    Row(
        modifier = modifier,
    ) {
        SWImageFlag(
            modifier = Modifier
                .padding(top = 64.dp),
            imageUrl = countrySelected.logo,
        )
        Icon(
            modifier = Modifier
                .padding(top = 92.dp, start = 8.dp)
                .graphicsLayer {
                    rotationZ = rotationAnimation.value
                }
                .clickable {
                    rotation = if (rotation == startRotation) {
                        endRotation
                    } else {
                        startRotation
                    }
                    showCountriesCallback(rotation == endRotation)
                    scope.launch {
                        rotationAnimation.animateTo(
                            targetValue = rotation,
                            animationSpec = tween(100, easing = LinearEasing),
                        )
                    }
                    registerEventCallback(
                        "click_origin_country_drop_down",
                        STRING_EMPTY,
                        STRING_EMPTY,
                    )
                },
            painter = painterResource(id = R.drawable.login_icn_flagfinder),
            contentDescription = "expand_country",
        )
    }
}

@Composable
fun CountrySearch(
    onItemClicked: (CountryUIModel) -> Unit,
    registerEventCallback: (eventAction: String, eventLabel: String, formType: String) -> Unit,
    countries: List<CountryUIModel>,
    showCountries: Boolean,
    showCountriesCallback: (Boolean) -> Unit
) {
    var filteredCountries by rememberSaveable { mutableStateOf(countries) }
    var lastTimeEventSend by rememberSaveable { mutableLongStateOf(0L) }

    if (showCountries) {
        SWSearchBar(
            modifier = Modifier
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 64.dp,
                ),
            backgroundColor = neutral0,
            onValueChange = { filter ->
                filteredCountries = if (filter.isNotEmpty()) {
                    countries.filter { it.name.contains(filter, true) }
                } else {
                    countries
                }
                if (filter.length > 2 && System.currentTimeMillis() - lastTimeEventSend > ONE_SECOND) {
                    registerEventCallback(
                        "search_origin_country",
                        filter, "search",
                    )
                    lastTimeEventSend = System.currentTimeMillis()
                }
            },
        )

        CountryFlagsLazyRow(
            filteredCountries,
            {
                onItemClicked(it)
                registerEventCallback(
                    "click_origin_country_list",
                    it.iso3,
                    STRING_EMPTY,
                )
                showCountriesCallback(false)
            },
            modifier = Modifier
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 64.dp,
                ),
        )
    }
}

@Composable
fun EmailInputText(
    modifier: Modifier,
    onEmailInput: (String) -> Unit
) {

    var text by rememberSaveable { mutableStateOf("") }

    Row(
        modifier = modifier,
    ) {
        SWTextField(
            value = text,
            modifier = Modifier
                .background(neutral0)
                .fillMaxWidth()
                .padding(
                    top = 32.dp,
                    bottom = 96.dp,
                    start = 16.dp,
                    end = 16.dp,
                ),
            onValueChange = {
                text = it
                onEmailInput(it)
            },
            placeholder = { Text(stringResource(id = R.string.email)) },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = neutral0,
            ),
        )
    }
}

@Composable
fun SendButton(
    modifier: Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier,
    ) {
        SWButton(
            modifier = Modifier
                .width(180.dp)
                .padding(bottom = 48.dp),
            backgroundColor = colorGreenMain,
            contentColor = colorBlueOceanDark,
            text = stringResource(id = R.string.send_text_button),
            textModifier = Modifier.padding(vertical = 4.dp),
            onClick = { onClick() },
            textColor = colorBlueOcean,
            fontSize = 14.sp,
            shape = RoundedCornerShape(50),
        )
    }
}

@Preview(showBackground = true, widthDp = 420)
@Composable
fun Preview() {
    ForgotPasswordLayout(
        listener = object : ForgotPasswordListener {
            override fun registerEventCallback(eventAction: String, eventLabel: String, formType: String) {}
            override fun onBackAction() {}
            override fun trackScreenName(screenName: String) {}
        }
    )
}
