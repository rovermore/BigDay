package com.smallworldfs.moneytransferapp.presentation.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.compose.colors.darkGreyText
import com.smallworldfs.moneytransferapp.compose.colors.defaultGreyControl
import com.smallworldfs.moneytransferapp.compose.colors.lightGreyText
import com.smallworldfs.moneytransferapp.compose.widgets.SWCircularLoader
import com.smallworldfs.moneytransferapp.compose.widgets.SWErrorScreenLayout
import com.smallworldfs.moneytransferapp.compose.widgets.SWTopError
import com.smallworldfs.moneytransferapp.compose.widgets.SWText
import com.smallworldfs.moneytransferapp.compose.widgets.SWTopAppBar
import com.smallworldfs.moneytransferapp.presentation.base.ErrorType

@Composable
fun SettingsScreenContainer(viewModel: SettingsViewModel = viewModel(), listener: SettingsCallbacks) {
    val settingState by viewModel.settingsState.collectAsStateWithLifecycle()
    val quickLoginSettings by viewModel.quickLoginSettings.collectAsStateWithLifecycle()
    val isNotificationChecked = settingState.appSettings.notifications
    val isErrorView by viewModel.requestSettingsError.collectAsStateWithLifecycle()
    val isBiometricError = viewModel.biometricsError.collectAsStateWithLifecycle().value != ErrorType.None
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    val showDialog by viewModel.showDialog.collectAsStateWithLifecycle()

    Column {
        SWTopAppBar(barTitle = stringResource(id = R.string.settings), onBackPressed = { listener.onBackButtonPressed() })

        if (isBiometricError) {
            SWTopError { listener.onClosedIconClicked() }
        }
        if (isErrorView != ErrorType.None) {
            SWErrorScreenLayout { viewModel.getSettings() }
        } else if (settingState.appInfo.contactInfo.isNotEmpty()) {
            SettingsLayout(
                isNotificationChecked,
                listener,
                quickLoginSettings,
                showDialog,
                { viewModel.showDialog() },
                { viewModel.hideDialog() }
            )

            SettingsContactList(settingState.appInfo.contactInfo) { url: String -> listener.onSettingsItemClicked(url) }

            AppVersionLayout(settingState.appInfo.appVersion)
        }

        if (isLoading) {
            SWCircularLoader()
        }
    }
}

@Composable
private fun AppVersionLayout(appVersion: String) {
    SWText(modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp), text = stringResource(id = R.string.info_app_version_title), color = darkGreyText, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)

    SWText(modifier = Modifier.padding(top = 8.dp, start = 16.dp, end = 16.dp), text = appVersion, color = lightGreyText, fontSize = 14.sp, textAlign = TextAlign.Start)

    Divider(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp), color = defaultGreyControl, thickness = 1.dp)
}
