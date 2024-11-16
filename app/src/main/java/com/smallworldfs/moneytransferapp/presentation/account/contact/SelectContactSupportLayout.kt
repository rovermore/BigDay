package com.smallworldfs.moneytransferapp.presentation.account.contact

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.compose.colors.darkGreyText
import com.smallworldfs.moneytransferapp.compose.colors.defaultGreyLightBackground
import com.smallworldfs.moneytransferapp.compose.colors.neutral0
import com.smallworldfs.moneytransferapp.compose.widgets.SWTopError
import com.smallworldfs.moneytransferapp.compose.widgets.SWText
import com.smallworldfs.moneytransferapp.compose.widgets.SWTopAppBar
import com.smallworldfs.moneytransferapp.domain.migrated.base.Action
import com.smallworldfs.moneytransferapp.modules.support.model.ContactSupportInfoUIModel
import com.smallworldfs.moneytransferapp.presentation.base.ErrorType
import com.smallworldfs.moneytransferapp.presentation.login.model.UserUIModel

@Composable
fun SelectContactSupportLayout(
    listener: SelectContactSupportListener,
    viewModel: SelectContactSupportViewModel = viewModel()
) {

    val error by viewModel.error.collectAsStateWithLifecycle()
    val user by viewModel.currentUser.collectAsStateWithLifecycle()
    val contactInfoSupport by viewModel.contactSupportInfo.collectAsStateWithLifecycle()

    Content(
        listener = listener,
        error = error,
        currentUser = user,
        contactInfoSupport = contactInfoSupport,
        hideErrorView = { viewModel.hideErrorView() },
    )
}

@Composable
private fun Content(
    listener: SelectContactSupportListener,
    error: ErrorType,
    currentUser: UserUIModel,
    contactInfoSupport: ContactSupportInfoUIModel,
    hideErrorView: Action
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(defaultGreyLightBackground)
    ) {
        SWTopAppBar(
            barTitle = stringResource(id = R.string.contact_small_world_menu),
            onBackPressed = { listener.onBackPressed() },
        )
        if (error !is ErrorType.None) {
            SWTopError(
                stringResource(R.string.generic_error_view_subtitle),
            ) {
                hideErrorView()
            }
        }
        ContactOption(
            icon = painterResource(id = R.drawable.ic_contact_faqs),
            title = stringResource(id = R.string.contact_faqs),
            onItemClick = { listener.onFaqsClick() },
        )
        ContactOption(
            icon = painterResource(id = R.drawable.ic_contact_chat),
            title = stringResource(R.string.contact_chat),
            onItemClick = { listener.onChatClick() }
        )
        ContactOption(
            icon = painterResource(id = R.drawable.ic_contact_mail),
            title = stringResource(R.string.contact_support_email_us),
            onItemClick = { listener.onEmailClick(currentUser, contactInfoSupport) }
        )
    }
}

@Composable
fun ContactOption(
    icon: Painter,
    title: String,
    onItemClick: Action
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(1.dp)
            .clickable { onItemClick() },
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(neutral0),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .padding(
                        start = 8.dp,
                        end = 8.dp
                    ),
                tint = darkGreyText,
                painter = icon,
                contentDescription = title
            )
            SWText(
                modifier = Modifier
                    .padding(start = 4.dp),
                text = title,
                color = darkGreyText,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 420)
@Composable
fun Preview() {
    Content(
        object : SelectContactSupportListener {
            override fun onFaqsClick() {}
            override fun onChatClick() {}
            override fun onEmailClick(userUIModel: UserUIModel, contactSupportInfoUIModel: ContactSupportInfoUIModel) {}
            override fun onBackPressed() {}
        },
        ErrorType.None,
        UserUIModel(),
        ContactSupportInfoUIModel(),
        {}
    )
}
