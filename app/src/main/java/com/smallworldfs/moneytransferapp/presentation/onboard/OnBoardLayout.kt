package com.smallworldfs.moneytransferapp.presentation.onboard

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.compose.colors.blueAccentColor
import com.smallworldfs.moneytransferapp.compose.colors.colorBlueOceanDark
import com.smallworldfs.moneytransferapp.compose.colors.colorGrayLight
import com.smallworldfs.moneytransferapp.compose.colors.colorGreenMain
import com.smallworldfs.moneytransferapp.compose.widgets.DotsIndicator
import com.smallworldfs.moneytransferapp.compose.widgets.SWButton
import com.smallworldfs.moneytransferapp.compose.widgets.SWText
import com.smallworldfs.moneytransferapp.presentation.freeuser.country_selection.CountrySelectionActivity
import com.smallworldfs.moneytransferapp.utils.findActivity

@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnBoardLayout(
    onButtonClicked: (page: Int) -> Unit,
    onActionPageSelected: (page: Int) -> Unit
) {

    val activity = LocalContext.current.findActivity()

    val pagerState = rememberPagerState()
    var slideImage by rememberSaveable { mutableIntStateOf(0) }
    onActionPageSelected(slideImage)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(modifier = Modifier.background(color = colorGreenMain)) {
            Image(
                painterResource(R.drawable.ic_splash_logo),
                contentDescription = "company logo",
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                alignment = Alignment.Center,
            )
        }

        HorizontalPager(
            count = 3,
            state = pagerState,
        ) { page ->
            when (page) {
                0 -> {
                    slideImage = 0
                    OnBoardItem(
                        R.string.page_1_on_board_title,
                        R.string.page_1_on_board_subtitle,
                        R.drawable.android_onboard_icn_stressfree,
                    )
                }

                1 -> {
                    slideImage = 1
                    OnBoardItem(
                        R.string.page_2_on_board_title,
                        R.string.page_2_on_board_subtitle,
                        R.drawable.android_onboard_icn_security,
                    )
                }

                2 -> {
                    slideImage = 2
                    OnBoardItem(
                        R.string.page_3_on_board_title,
                        R.string.page_3_on_board_subtitle,
                        R.drawable.android_onboard_icn_csupport,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.padding(32.dp))

        DotsIndicator(
            totalDots = 3,
            selectedIndex = pagerState.currentPage,
            selectedColor = blueAccentColor,
            unSelectedColor = colorGrayLight,
        )

        Spacer(modifier = Modifier.weight(1f))

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            SWButton(
                modifier = Modifier.padding(bottom = 32.dp),
                onClick = {
                    onButtonClicked(slideImage)
                    val i = Intent(activity, CountrySelectionActivity::class.java)
                    activity?.startActivity(i)
                    activity?.overridePendingTransition(R.anim.from_right_to_left, R.anim.from_position_to_left)
                    activity?.finish()
                },
                text = stringResource(id = R.string.get_started),
                shape = RoundedCornerShape(50),
                textColor = colorBlueOceanDark,
            )
        }
    }
}

@Composable
fun OnBoardItem(title: Int, subtitle: Int, image: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        SWText(
            text = stringResource(id = title),
            modifier = Modifier
                .padding(start = 32.dp, end = 32.dp, top = 8.dp, bottom = 2.dp),
            color = colorBlueOceanDark,
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
        )
        SWText(
            text = stringResource(id = subtitle),
            modifier = Modifier
                .padding(start = 32.dp, end = 32.dp, top = 2.dp, bottom = 4.dp),
            color = colorBlueOceanDark,
            fontWeight = FontWeight.SemiBold,
        )
        Image(
            painterResource(id = image),
            contentDescription = "carrousel image",
            alignment = Alignment.Center,
            modifier = Modifier.padding(top = 62.dp),
        )
    }
}

@Preview(showBackground = true, name = "Onboarding preview", widthDp = 420)
@Composable
fun DefaultPreview() {
    OnBoardLayout({}, {})
}
