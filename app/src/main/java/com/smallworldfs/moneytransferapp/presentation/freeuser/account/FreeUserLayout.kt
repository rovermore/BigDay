package com.smallworldfs.moneytransferapp.presentation.freeuser.account

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.compose.colors.blueAccentColor
import com.smallworldfs.moneytransferapp.compose.colors.darkGreyText
import com.smallworldfs.moneytransferapp.compose.colors.defaultGreyLightBackground
import com.smallworldfs.moneytransferapp.compose.colors.greySeparator
import com.smallworldfs.moneytransferapp.domain.migrated.base.Action
import com.smallworldfs.moneytransferapp.modules.login.domain.model.User
import com.smallworldfs.moneytransferapp.presentation.account.account.model.AccountMenuItemUIModel
import com.smallworldfs.moneytransferapp.presentation.account.account.model.AccountMenuUIModel
import com.smallworldfs.moneytransferapp.presentation.base.ErrorType
import com.smallworldfs.moneytransferapp.utils.Constants
import com.smallworldfs.moneytransferapp.compose.widgets.SWAccountMenuItem
import com.smallworldfs.moneytransferapp.compose.widgets.SWTopError
import com.smallworldfs.moneytransferapp.compose.widgets.SWImageFlag
import com.smallworldfs.moneytransferapp.compose.widgets.SWText

@Composable
fun FreeUserLayout(
    onLoginClick: Action,
    onSignUpClick: Action,
    onAccountClick: Action,
    onItemClicked: (AccountMenuItemUIModel) -> Unit,
    viewModel: FreeUserViewModel = viewModel()
) {
    val user by viewModel.user.collectAsStateWithLifecycle()
    val options by viewModel.accountMenu.collectAsStateWithLifecycle()
    val error by viewModel.accountError.collectAsStateWithLifecycle()

    Content(
        onLoginClick = {
            onLoginClick()
            viewModel.clearUser()
        },
        onSignUpClick = {
            onSignUpClick()
            viewModel.clearUser()
        },
        onAccountClick = {
            onAccountClick()
            viewModel.clearUser()
        },
        onItemClicked = onItemClicked,
        user = user,
        options = options,
        error = error,
        hideErrorView = { viewModel.hideErrorView() },
    )
}

@Composable
private fun Content(
    onLoginClick: Action,
    onSignUpClick: Action,
    onAccountClick: Action,
    onItemClicked: (AccountMenuItemUIModel) -> Unit,
    user: User,
    options: AccountMenuUIModel,
    error: ErrorType,
    hideErrorView: Action,
) {

    Column(
        modifier = Modifier
            .background(defaultGreyLightBackground)
            .fillMaxSize()
    ) {
        if (error !is ErrorType.None) {
            SWTopError(
                body = stringResource(R.string.generic_error_view_subtitle)
            ) {
                hideErrorView()
            }
        }
        Account(
            user = user,
            onLoginClick = { onLoginClick() },
            onSignUpClick = { onSignUpClick() },
            onAccountClick = { onAccountClick() }
        )
        Menu(
            options = options,
            onItemClicked = onItemClicked,
        )
    }
}

@Composable
private fun Account(
    user: User,
    onLoginClick: Action,
    onSignUpClick: Action,
    onAccountClick: Action,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onAccountClick() }
    ) {
        Row(
            modifier = Modifier
                .background(greySeparator),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SWImageFlag(
                modifier = Modifier
                    .padding(8.dp)
                    .size(24.dp),
                imageUrl = Constants.COUNTRY.FLAG_IMAGE_ASSETS + user.country?.firstKey() + Constants.COUNTRY.FLAG_IMAGE_EXTENSION
            )
            Column(
                verticalArrangement = Arrangement.Center
            ) {
                SWText(
                    modifier = Modifier
                        .padding(start = 4.dp, top = 8.dp),
                    text = stringResource(id = R.string.unknown_user_text),
                    color = darkGreyText,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Row(
                    modifier = Modifier
                        .padding(start = 4.dp, bottom = 8.dp),
                ) {
                    SWText(
                        modifier = Modifier
                            .clickable { onLoginClick() },
                        text = stringResource(id = R.string.status_limited_user_log_in),
                        color = blueAccentColor,
                        fontWeight = FontWeight.Bold,
                    )
                    SWText(
                        modifier = Modifier
                            .padding(horizontal = 4.dp),
                        text = stringResource(id = R.string.or),
                        color = darkGreyText,
                        fontWeight = FontWeight.Bold
                    )
                    SWText(
                        modifier = Modifier
                            .clickable { onSignUpClick() },
                        text = stringResource(id = R.string.status_limited_user_sign_up),
                        color = blueAccentColor,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                modifier = Modifier
                    .padding(end = 16.dp),
                painter = painterResource(R.drawable.onboarding_btn_more2),
                tint = darkGreyText,
                contentDescription = "back_arrow"
            )
        }
    }
}

@Composable
private fun Menu(
    options: AccountMenuUIModel,
    onItemClicked: (AccountMenuItemUIModel) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .padding(top = 4.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        items(options.rows) { item ->
            SWAccountMenuItem(
                item = item,
                onItemClicked = {
                    onItemClicked(item)
                },
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 420)
@Composable
fun Preview() {
    Content(
        {},
        {},
        {},
        {},
        User(),
        AccountMenuUIModel(),
        ErrorType.None,
        {}
    )
}
