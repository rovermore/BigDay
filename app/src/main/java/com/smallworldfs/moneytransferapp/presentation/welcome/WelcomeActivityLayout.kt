package com.smallworldfs.moneytransferapp.presentation.welcome

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.compose.colors.blueBackgroundWelcome
import com.smallworldfs.moneytransferapp.compose.colors.colorBlueOceanDark
import com.smallworldfs.moneytransferapp.compose.colors.colorGreenMain
import com.smallworldfs.moneytransferapp.compose.colors.transparent
import com.smallworldfs.moneytransferapp.compose.widgets.SWButton
import com.smallworldfs.moneytransferapp.compose.widgets.SWOutlinedButton
import com.smallworldfs.moneytransferapp.compose.widgets.SWText

@Composable
fun WelcomeLayout(
    navigator: WelcomeNavigator,
    registerEventCallback: (event: String) -> Unit
) {
    Column(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize(),
    ) {
        WelcomeHeader(Modifier.weight(1f), navigator)

        WelcomeSWImage()

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        )

        SWText(
            modifier = Modifier.padding(horizontal = 36.dp),
            text = stringResource(id = R.string.welcome_text),
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.weight(1f))

        WelcomeButtons(navigator, registerEventCallback)
    }
}

@Composable
fun WelcomeHeader(modifier: Modifier, navigator: WelcomeNavigator) {
    SWButton(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        backgroundColor = blueBackgroundWelcome,
        shape = RoundedCornerShape(0),
        textModifier = modifier,
        text = stringResource(id = R.string.take_a_look),
        fontSize = 18.sp,
        onClick = {
            navigator.navigateToTransferCountrySelection()
        },
    ) {
        Icon(
            modifier = Modifier
                .size(18.dp, 18.dp)
                .rotate(270f),
            painter = painterResource(id = R.drawable.ic_chevron_right),
            contentDescription = null
        )
    }
}

@Composable
fun WelcomeSWImage() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(
                color = colorResource(R.color.colorGreenMain)
            )
    ) {
        Image(
            modifier = Modifier
                .width(128.dp)
                .height(48.dp)
                .align(Alignment.Center),
            painter = painterResource(id = R.drawable.ic_splash_logo),
            contentDescription = "logo"
        )
    }
}

@Composable
fun WelcomeButtons(navigator: WelcomeNavigator, registerEventCallback: (event: String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SWButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            backgroundColor = colorGreenMain,
            contentColor = colorBlueOceanDark,
            text = stringResource(id = R.string.create_account_button),
            fontSize = 18.sp,
            textModifier = Modifier.padding(vertical = 4.dp),
            onClick = {

                registerEventCallback("click_create_account")
                navigator.navigateToSignup()
            },
            textColor = colorBlueOceanDark
        )

        SWOutlinedButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            contentColor = colorBlueOceanDark,
            border = BorderStroke(1.dp, colorBlueOceanDark),
            textModifier = Modifier.padding(vertical = 4.dp),
            text = stringResource(id = R.string.login_button_text),
            fontSize = 18.sp,
            onClick = {
                registerEventCallback("click_login")
                navigator.navigateToLogin()
            },
            textColor = colorBlueOceanDark,
            backgroundColor = transparent
        )
    }
}

@Preview(showBackground = true, widthDp = 420)
@Composable
fun Preview() {
    WelcomeLayout(WelcomeNavigator(WelcomeActivity())) {}
}
