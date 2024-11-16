package com.smallworldfs.moneytransferapp.presentation.settings

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityOptionsCompat
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.compose.colors.darkGreyText
import com.smallworldfs.moneytransferapp.compose.colors.defaultGreyControl
import com.smallworldfs.moneytransferapp.compose.colors.mediumBlue
import com.smallworldfs.moneytransferapp.compose.dialogs.SWInfoDialog
import com.smallworldfs.moneytransferapp.compose.widgets.SWText
import com.smallworldfs.moneytransferapp.compose.widgets.SWTitleDescriptionSwitch
import com.smallworldfs.moneytransferapp.domain.migrated.base.Action
import com.smallworldfs.moneytransferapp.modules.changepassword.ui.ChangePasswordActivity
import com.smallworldfs.moneytransferapp.presentation.settings.model.ContactInfo
import com.smallworldfs.moneytransferapp.presentation.settings.model.QuickLoginSettingsUIModel

@Composable
fun SettingsLayout(
    isNotificationChecked: Boolean,
    listener: SettingsCallbacks,
    quickLoginSettingsUiModel: QuickLoginSettingsUIModel,
    showDialog: Boolean,
    showDialogCallback: Action,
    hideDialogCallback: Action
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
    ) {
        val context = LocalContext.current

        val isPasscodeChecked = quickLoginSettingsUiModel.passCodeActive
        val isBiometricChecked = quickLoginSettingsUiModel.biometricsActive

        val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                showDialogCallback()
            }
        }

        if (showDialog) {
            SWInfoDialog(
                title = stringResource(id = R.string.action_done_transactional_calculator),
                content = stringResource(id = R.string.change_password_ok_text),
                positiveText = stringResource(id = R.string.ok),
                positiveAction = { hideDialogCallback() },
                dismissAction = { hideDialogCallback() }
            )
        }

        Column(
            Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp),
        ) {

            SWText(modifier = Modifier.padding(top = 16.dp), text = stringResource(id = R.string.notifications_group_title), color = mediumBlue, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)

            SWTitleDescriptionSwitch(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                text = stringResource(id = R.string.notifications_field_title),
                description = stringResource(id = R.string.notifications_description_title),
                isChecked = isNotificationChecked,
                listener = { isChecked: Boolean -> listener.onNotificationSwitchToggle(isChecked) },
            )

            Divider(modifier = Modifier.padding(top = 16.dp), color = defaultGreyControl, thickness = 1.dp)

            SWText(modifier = Modifier.padding(top = 16.dp), text = stringResource(id = R.string.account_group_title), color = mediumBlue, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)

            SWText(
                text = stringResource(id = R.string.account_change_password_title),
                modifier = Modifier
                    .padding(top = 16.dp)
                    .clickable {
                        val intent = Intent(context, ChangePasswordActivity::class.java)
                        val options = ActivityOptionsCompat.makeCustomAnimation(context, R.anim.from_right_to_left, R.anim.from_position_to_left)
                        launcher.launch(intent, options)
                    },
                color = darkGreyText, fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
            )

            Divider(modifier = Modifier.padding(top = 16.dp), color = defaultGreyControl, thickness = 1.dp)

            if (quickLoginSettingsUiModel.biometricsVisible) {
                SWTitleDescriptionSwitch(
                    Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.login_with_biometrics),
                    isChecked = isBiometricChecked,
                    listener = { isChecked: Boolean -> listener.onBiometricSwitchToggle(isChecked) },
                )

                Divider(modifier = Modifier.padding(top = 16.dp), color = defaultGreyControl, thickness = 1.dp)
            }

            if (quickLoginSettingsUiModel.passCodeVisible) {
                SWTitleDescriptionSwitch(
                    Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.login_with_passcode),
                    isChecked = isPasscodeChecked,
                    listener = { isChecked: Boolean -> listener.onChangePasscodeSwitchToggle(isChecked) },
                )

                Divider(modifier = Modifier.padding(top = 16.dp), color = defaultGreyControl, thickness = 1.dp)
            }

            if (quickLoginSettingsUiModel.changePassCodeVisible) {
                SWText(
                    text = stringResource(id = R.string.change_passcode),
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .clickable { listener.onChangePasscodeSwitchToggle(true) },
                    color = darkGreyText, fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                )

                Divider(modifier = Modifier.padding(top = 16.dp), color = defaultGreyControl, thickness = 1.dp)
            }

            SWText(modifier = Modifier.padding(top = 16.dp), text = stringResource(id = R.string.info_group_title), color = mediumBlue, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun SettingsContactList(settingsList: List<ContactInfo>, onSettingClickListener: (String) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 16.dp),
    ) {
        items(settingsList) { contact ->
            SettingContactItem(contact, onSettingClickListener)
        }
    }
}

@Composable
fun SettingContactItem(contact: ContactInfo, onSettingClickListener: (String) -> Unit) {
    Column {
        SWText(
            modifier = Modifier
                .padding(top = 16.dp)
                .clickable { onSettingClickListener(contact.url) },
            text = contact.title, fontSize = 16.sp, color = darkGreyText,
            fontWeight = FontWeight.SemiBold,
        )

        Divider(modifier = Modifier.padding(top = 16.dp), color = defaultGreyControl, thickness = 1.dp)
    }
}
