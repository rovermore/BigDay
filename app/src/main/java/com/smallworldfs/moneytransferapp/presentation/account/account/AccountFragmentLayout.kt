package com.smallworldfs.moneytransferapp.presentation.account.account

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.compose.colors.blueAccentColor
import com.smallworldfs.moneytransferapp.compose.colors.colorRedError
import com.smallworldfs.moneytransferapp.compose.colors.defaultGreyLightBackground
import com.smallworldfs.moneytransferapp.compose.colors.neutral0
import com.smallworldfs.moneytransferapp.compose.widgets.AccountUserHeader
import com.smallworldfs.moneytransferapp.compose.widgets.SWAccountMenuItem
import com.smallworldfs.moneytransferapp.compose.widgets.SWTopError
import com.smallworldfs.moneytransferapp.compose.widgets.SWMenuBlockItem
import com.smallworldfs.moneytransferapp.compose.widgets.SWText
import com.smallworldfs.moneytransferapp.domain.migrated.base.Action
import com.smallworldfs.moneytransferapp.presentation.account.account.model.AccountMenuItemUIModel
import com.smallworldfs.moneytransferapp.presentation.account.account.model.AccountMenuUIModel
import com.smallworldfs.moneytransferapp.presentation.account.account.model.MenuIcon
import com.smallworldfs.moneytransferapp.presentation.base.ErrorType
import com.smallworldfs.moneytransferapp.presentation.login.model.UserUIModel
import com.smallworldfs.moneytransferapp.utils.Constants

@Composable
fun AccountFragmentLayout(
    callbacks: AccountFragmentsCallbacks,
    viewModel: AccountViewModel = viewModel()
) {
    val user by viewModel.user.collectAsStateWithLifecycle()
    val menus by viewModel.accountMenu.collectAsStateWithLifecycle()
    val loading by viewModel.loading.collectAsStateWithLifecycle()
    val error by viewModel.accountMenuError.collectAsStateWithLifecycle()
    val logout by viewModel.logout.collectAsStateWithLifecycle()

    Content(
        user,
        menus,
        loading,
        error,
        { itemClicked -> callbacks.onItemClicked(itemClicked) },
        { viewModel.hideErrorView() },
        { callbacks.showLogoutDialog { viewModel.logout() } },
        { userStatus -> callbacks.onUserClicked(userStatus) }
    )

    LaunchedEffect(menus) {
        callbacks.showTabIndicator(menus.blocks.sumOf { it.numNewInfo })
    }
    LaunchedEffect(logout) {
        if (logout) callbacks.onLogoutCompleted()
    }
}

@Composable
private fun Content(
    user: UserUIModel,
    menus: AccountMenuUIModel,
    loading: Boolean,
    error: ErrorType,
    onItemClicked: (AccountMenuItemUIModel) -> Unit,
    onCloseError: Action,
    showLogoutDialog: Action,
    onUserClicked: (String) -> Unit
) {

    Box(modifier = Modifier.fillMaxSize()) {

        if (loading)
            CircularProgressIndicator(
                modifier = Modifier
                    .width(64.dp)
                    .align(Alignment.Center),
                color = blueAccentColor
            )

        if (error !is ErrorType.None)
            SWTopError(
                body = error.message
            ) {
                onCloseError()
            }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = defaultGreyLightBackground)
        ) {

            when (user.status) {
                Constants.UserType.APPROVED,
                Constants.UserType.PDT_MAIL,
                Constants.UserType.PDT_SMS,
                Constants.UserType.UNDER_REVIEW -> AccountUserHeader(user = user, onUserClicked = onUserClicked)
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                content = {
                    items(menus.blocks) { block ->
                        SWMenuBlockItem(
                            block = block,
                            onItemClicked = onItemClicked
                        )
                    }
                },
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                content = {
                    items(menus.rows) { item ->
                        SWAccountMenuItem(
                            modifier = Modifier
                                .padding(start = 8.dp, end = 8.dp),
                            item = item,
                            onItemClicked = onItemClicked,
                        )
                    }
                },
            )

            Spacer(modifier = Modifier.padding(top = 4.dp))

            LogoutItem(showLogoutDialog)
        }
    }
}

@Composable
private fun LogoutItem(showLogoutDialog: Action) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(start = 8.dp, end = 8.dp)
            .clickable {
                showLogoutDialog()
            },
        elevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .background(neutral0),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp),
                tint = colorRedError,
                painter = painterResource(id = R.drawable.account_icn_logout),
                contentDescription = "logout_button"
            )
            SWText(
                text = stringResource(id = R.string.logout_button),
                color = colorRedError,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 420)
@Composable
fun AccountFragmentLayoutPreview() {
    Content(
        user = UserUIModel(),
        menus = AccountMenuUIModel(
            rows = mutableListOf(
                AccountMenuItemUIModel(
                    icon = MenuIcon.Icon(resourceId = R.drawable.account_icn_settings2),
                    description = "Description"
                )
            )
        ),
        loading = false,
        error = ErrorType.None,
        onItemClicked = {},
        onCloseError = {},
        showLogoutDialog = {},
        onUserClicked = {},
    )
}
