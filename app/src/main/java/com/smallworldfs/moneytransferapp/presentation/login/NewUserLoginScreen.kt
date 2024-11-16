package com.smallworldfs.moneytransferapp.presentation.login

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ScreenName
import com.smallworldfs.moneytransferapp.compose.colors.blueAccentColor
import com.smallworldfs.moneytransferapp.compose.colors.colorBlueOcean
import com.smallworldfs.moneytransferapp.compose.colors.colorBlueOceanDark
import com.smallworldfs.moneytransferapp.compose.colors.colorGreenMain
import com.smallworldfs.moneytransferapp.compose.colors.colorRedError
import com.smallworldfs.moneytransferapp.compose.colors.darkGreyText
import com.smallworldfs.moneytransferapp.compose.colors.neutral0
import com.smallworldfs.moneytransferapp.compose.widgets.SWButton
import com.smallworldfs.moneytransferapp.compose.widgets.SWPasswordFieldWithLabel
import com.smallworldfs.moneytransferapp.compose.widgets.SWRegionPicker
import com.smallworldfs.moneytransferapp.compose.widgets.SWText
import com.smallworldfs.moneytransferapp.compose.widgets.SWTextField
import com.smallworldfs.moneytransferapp.presentation.base.ErrorType
import com.smallworldfs.moneytransferapp.presentation.common.countries.CountryUIModel
import com.smallworldfs.moneytransferapp.presentation.common.countries.SearchCountryActivity
import com.smallworldfs.moneytransferapp.presentation.login.LoginActivity.Companion.SEND_MONEY_FROM
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

@Composable
fun NewUserLoginScreen(
    loginError: ErrorType,
    regionClickListeners: NewUserLoginScreenClickListeners,
    viewModel: LoginViewModel = viewModel()
) {

    var emailError by remember {
        mutableStateOf(STRING_EMPTY)
    }
    var passwordError by remember {
        mutableStateOf(STRING_EMPTY)
    }

    LaunchedEffect(key1 = loginError) {
        if (loginError is ErrorType.EntityValidationError) {
            loginError.fields.forEach {
                if (it.field == "email") {
                    emailError = it.error.first()
                    regionClickListeners.setFieldError("error_validation_email")
                } else if (it.field == "password") {
                    regionClickListeners.setFieldError("error_validation_pass")
                    passwordError = it.error.first()
                }
            }
        } else {
            emailError = STRING_EMPTY
            passwordError = STRING_EMPTY
        }
    }
    NewUserLoginScreenLayout(viewModel, regionClickListeners, emailError, passwordError)
}

@Composable
fun NewUserLoginScreenLayout(viewModel: LoginViewModel, regionClickListeners: NewUserLoginScreenClickListeners, emailError: String, passwordError: String) {
    val context = LocalContext.current
    val selectedCountry by viewModel.selectedCountry.collectAsStateWithLifecycle()
    val countryList by viewModel.originCountries.collectAsStateWithLifecycle()

    val countryFlag: String = selectedCountry.logo
    var email by rememberSaveable { mutableStateOf(STRING_EMPTY) }
    var text by rememberSaveable { mutableStateOf(STRING_EMPTY) }
    val startSearch = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            if (it.resultCode == Activity.RESULT_OK) {
                val country = it.data!!.getParcelableExtra<CountryUIModel>(SearchCountryActivity.SELECTED_COUNTRY_KEY)!!
                viewModel.updateSelectedCountry(country)
            }
        }
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
    ) {
        SWRegionPicker(flagUrl = countryFlag, text = selectedCountry.name, "login_country_view") {
            regionClickListeners.onRegionClicked()
            val intent = Intent(context, SearchCountryActivity::class.java).apply {
                putExtra(
                    SearchCountryActivity.TITLE_KEY,
                    context.getString(
                        R.string.sending_from_search_title,
                    ),
                )
                putExtra(
                    SearchCountryActivity.TYPE,
                    SEND_MONEY_FROM,
                )
                val countries = ArrayList(countryList)
                putParcelableArrayListExtra(SearchCountryActivity.COUNTRIES_KEY, countries)
                putExtra(SearchCountryActivity.ANALITYCS_TAG, ScreenName.SEND_MONEY_FROM_SCREEN.value)
            }
            startSearch.launch(intent)
        }

        SWTextField(
            value = email,
            modifier = Modifier
                .padding(top = 10.dp)
                .background(neutral0)
                .fillMaxWidth(),
            onValueChange = {
                email = it
            },
            label = { Text(text = stringResource(id = R.string.email), fontSize = 14.sp) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = neutral0,
                cursorColor = blueAccentColor,
                focusedIndicatorColor = blueAccentColor,
                focusedLabelColor = blueAccentColor,
                unfocusedLabelColor = darkGreyText,
            ),
            textStyle = TextStyle.Default.copy(fontSize = 16.sp),
            singleLine = true,
        )
        if (emailError.isNotEmpty()) {
            SWText(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp),
                text = emailError, fontSize = 12.sp,
                color = colorRedError,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold,
            )
        }

        SWPasswordFieldWithLabel(text) { value ->
            text = value
        }

        if (passwordError.isNotEmpty()) {
            SWText(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp),
                text = passwordError, fontSize = 12.sp,
                color = colorRedError,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold,
            )
        }

        Box(
            modifier = Modifier.align(Alignment.End),
        ) {
            SWText(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .clickable {
                        regionClickListeners.onForgotPasswordClicked()
                    },
                text = stringResource(id = R.string.forgot_password_login_button),
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
            )
        }

        SWButton(
            modifier = Modifier
                .width(130.dp)
                .padding(top = 35.dp),
            backgroundColor = colorGreenMain,
            contentColor = colorBlueOceanDark,
            text = stringResource(id = R.string.sign_in).uppercase(),
            textModifier = Modifier.padding(vertical = 4.dp),
            onClick = {
                regionClickListeners.onSubmitButtonClicked()
                viewModel.onActionNewLogin(
                    email,
                    text.toCharArray(),
                    selectedCountry.iso3,
                )
            },
            textColor = colorBlueOcean,
            fontSize = 16.sp,
            shape = RoundedCornerShape(50),
            fontWeight = FontWeight.ExtraBold,
        )
    }
}
