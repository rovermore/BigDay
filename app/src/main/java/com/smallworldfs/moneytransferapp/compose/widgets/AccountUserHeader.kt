package com.smallworldfs.moneytransferapp.compose.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.compose.colors.mainBlue
import com.smallworldfs.moneytransferapp.compose.colors.mainBlueDark1
import com.smallworldfs.moneytransferapp.compose.colors.neutral0
import com.smallworldfs.moneytransferapp.presentation.login.model.UserUIModel
import com.smallworldfs.moneytransferapp.utils.Constants
import com.smallworldfs.moneytransferapp.utils.STRING_SPACE

@Composable
fun AccountUserHeader(
    modifier: Modifier = Modifier,
    user: UserUIModel,
    onUserClicked: (String) -> Unit
) {
    Box(
        modifier = modifier
            .semantics { contentDescription = "account_name_button" }
            .padding(8.dp)
            .fillMaxWidth()
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        mainBlueDark1,
                        mainBlue
                    )
                )
            )
            .clickable { onUserClicked(user.status) }
    ) {
        Row(
            modifier = modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (user.country.countries.isNotEmpty())
                SWImageFlag(
                    imageUrl = Constants.COUNTRY.FLAG_IMAGE_ASSETS + user.country.countries.first().iso3 +
                        Constants.COUNTRY.FLAG_IMAGE_EXTENSION,
                    size = 38.dp,
                    modifier = Modifier.padding(8.dp)
                )

            Column(
                modifier = modifier.padding(start = 16.dp)
            ) {
                SWText(
                    text = user.name.plus(STRING_SPACE).plus(user.surname),
                    fontSize = 18.sp,
                    color = neutral0
                )
                SWText(
                    text = user.email,
                    fontSize = 14.sp,
                    color = neutral0
                )
            }

            Spacer(modifier = Modifier.width(90.dp))

            Image(
                painter = painterResource(id = R.drawable.ic_chevron_right),
                contentDescription = "",
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 420)
@Composable
fun AccountUserHeaderPreview() {
    AccountUserHeader(
        user = UserUIModel(
            name = "Name",
            surname = "Surname",
            email = "Email"
        ),
        onUserClicked = {}
    )
}
