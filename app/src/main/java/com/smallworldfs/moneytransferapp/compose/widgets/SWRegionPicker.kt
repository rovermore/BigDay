package com.smallworldfs.moneytransferapp.compose.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.text.style.TextAlign.Companion.Start
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.compose.colors.black
import com.smallworldfs.moneytransferapp.compose.colors.darkGreyText
import com.smallworldfs.moneytransferapp.compose.colors.neutral0

@Composable
fun SWRegionPicker(
    flagUrl: String,
    text: String,
    contentDescription: String,
    onClickListener: () -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .clickable { onClickListener() }
            .fillMaxWidth()
            .padding(10.dp),
    ) {
        val (imageFlag, countryName, arrowIcon, bottomLine) = createRefs()

        SWImageFlag(
            modifier = Modifier
                .fillMaxWidth(0.1f)
                .size(40.dp)
                .constrainAs(imageFlag) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                },
            imageUrl = flagUrl,
        )

        SWText(
            text = text,
            modifier = Modifier
                .background(neutral0)
                .constrainAs(countryName) {
                    top.linkTo(parent.top)
                    start.linkTo(imageFlag.end, margin = 10.dp)
                    end.linkTo(arrowIcon.start)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                }
                .semantics { this.contentDescription = contentDescription },
            fontSize = 18.sp,
            textAlign = Start,
            fontWeight = SemiBold,
            color = black,
        )

        Spacer(
            modifier = Modifier
                .padding(top = 5.dp)
                .height(1.dp)
                .background(darkGreyText)
                .constrainAs(bottomLine) {
                    top.linkTo(countryName.bottom)
                    start.linkTo(imageFlag.end, margin = 10.dp)
                    end.linkTo(arrowIcon.start)
                    width = Dimension.fillToConstraints
                },
        )

        Image(
            modifier = Modifier
                .size(35.dp)
                .constrainAs(arrowIcon) {
                    end.linkTo(parent.end)
                },
            painter = painterResource(id = R.drawable.account_icn_arrowactivitycard),
            contentDescription = "arrow_icon",
        )
    }
}

@Preview(showBackground = true, widthDp = 420)
@Composable
fun SWTextInputWithIconPreview() {
    SWRegionPicker("", "", "", {})
}
