package com.smallworldfs.moneytransferapp.presentation.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.ExtraBold
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.compose.colors.colorBlueOcean
import com.smallworldfs.moneytransferapp.compose.colors.colorBlueOceanDark
import com.smallworldfs.moneytransferapp.compose.colors.colorGreenMain
import com.smallworldfs.moneytransferapp.compose.colors.darkGreyText
import com.smallworldfs.moneytransferapp.compose.colors.mediumBlue
import com.smallworldfs.moneytransferapp.compose.widgets.SWButton
import com.smallworldfs.moneytransferapp.compose.widgets.SWImageFlag
import com.smallworldfs.moneytransferapp.compose.widgets.SWPasswordFieldWithLabel
import com.smallworldfs.moneytransferapp.compose.widgets.SWText

@Composable
fun StoredUserLoginScreen(
    flagUrl: String,
    userIndicator: String,
    onForgotPasswordClicked: () -> Unit,
    onChangeAccountClicked: () -> Unit,
    onSubmitButtonClicked: (String) -> Unit
) {
    var text by rememberSaveable { mutableStateOf("") }

    Column(
        horizontalAlignment = CenterHorizontally,
        modifier = Modifier.fillMaxWidth(),
    ) {
        SWImageFlag(
            modifier = Modifier.padding(top = 16.dp),
            imageUrl = flagUrl,
        )

        SWText(
            text = userIndicator,
            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
            fontSize = 18.sp,
            fontWeight = ExtraBold,
            color = darkGreyText,
        )

        SWText(
            text = stringResource(id = R.string.change_account_button_text),
            modifier = Modifier
                .padding(8.dp)
                .clickable { onChangeAccountClicked() },
            fontSize = 16.sp,
            fontWeight = SemiBold,
            color = mediumBlue,
        )

        SWPasswordFieldWithLabel(text) { value ->
            text = value
        }

        Box(
            modifier = Modifier.align(Alignment.End),
        ) {
            SWText(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .clickable { onForgotPasswordClicked() },
                text = stringResource(id = R.string.forgot_password_login_button),
                fontSize = 14.sp,
                fontWeight = SemiBold,
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
                onSubmitButtonClicked(text)
            },
            textColor = colorBlueOcean,
            fontSize = 16.sp,
            shape = RoundedCornerShape(50),
            fontWeight = ExtraBold,
        )
    }
}

@Preview(showBackground = true, widthDp = 420)
@Composable
fun StoredUserLoginScreenPreview() {
    StoredUserLoginScreen("", "Good Evening Mark", {}, {}, {})
}
