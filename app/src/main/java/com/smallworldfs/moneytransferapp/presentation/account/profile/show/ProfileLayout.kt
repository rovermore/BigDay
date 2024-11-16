package com.smallworldfs.moneytransferapp.presentation.account.profile.show

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.compose.colors.black
import com.smallworldfs.moneytransferapp.compose.colors.blueAccentColor
import com.smallworldfs.moneytransferapp.compose.colors.defaultGreyLightBackground
import com.smallworldfs.moneytransferapp.compose.colors.lightGreyText
import com.smallworldfs.moneytransferapp.compose.colors.mainBlue
import com.smallworldfs.moneytransferapp.compose.dialogs.SWInfoDialog
import com.smallworldfs.moneytransferapp.compose.widgets.SWErrorScreenLayout
import com.smallworldfs.moneytransferapp.compose.widgets.SWImageFlag
import com.smallworldfs.moneytransferapp.compose.widgets.SWText
import com.smallworldfs.moneytransferapp.compose.widgets.SWTopAppBar
import com.smallworldfs.moneytransferapp.domain.migrated.base.Action
import com.smallworldfs.moneytransferapp.presentation.account.profile.edit.EditProfileActivity
import com.smallworldfs.moneytransferapp.presentation.account.profile.show.model.ProfileUIModel
import com.smallworldfs.moneytransferapp.presentation.base.ErrorType
import com.smallworldfs.moneytransferapp.presentation.common.countries.CountryData
import com.smallworldfs.moneytransferapp.utils.Constants

@Composable
fun ProfileLayout(
    listener: ProfileLayoutListener,
    viewModel: ProfileViewModel = viewModel(),
) {

    val profileUIModel by viewModel.requestInformationLiveData.collectAsStateWithLifecycle()
    val error by viewModel.errorRequestInformationLiveData.collectAsStateWithLifecycle()
    val showDialog by viewModel.showDialog.collectAsStateWithLifecycle()

    Content(
        profileUIModel = profileUIModel,
        listener = listener,
        onNavigationCallback = { viewModel.showDialog() },
        onDialogActionClick = {
            viewModel.requestInformation()
            viewModel.hideDialog()
        },
        showDialog = showDialog,
        retryCallback = {
            viewModel.hideErrorView()
            viewModel.requestInformation()
        },
        error = error,

    )
}

@Composable
fun Content(
    profileUIModel: ProfileUIModel,
    listener: ProfileLayoutListener,
    error: ErrorType,
    onNavigationCallback: Action,
    onDialogActionClick: Action,
    showDialog: Boolean,
    retryCallback: Action
) {

    val context = LocalContext.current

    val editProfileLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            onNavigationCallback()
        }
    }

    if (showDialog) {
        SWInfoDialog(
            title = stringResource(id = R.string.info_text),
            content = stringResource(id = R.string.edit_profile_ok_text),
            positiveText = stringResource(id = R.string.accept_text),
            positiveAction = { onDialogActionClick() },
            negativeText = stringResource(id = R.string.cancel),
            dismissAction = { onDialogActionClick() }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(defaultGreyLightBackground)
    ) {
        SWTopAppBar(
            barTitle = stringResource(id = R.string.profile_title),
            onBackPressed = { listener.onBackAction() },
            trailingIcon = Icons.Default.Edit,
            onTrailingIconPressed = {
                val intent = Intent(context, EditProfileActivity::class.java)
                editProfileLauncher.launch(intent)
            },
            trailingIconEvent = "click_edit",
            registerEventCallback = { listener.registerEventCallBack("click_back") },
        )
        if (error !is ErrorType.None) {
            SWErrorScreenLayout {
                retryCallback()
            }
        } else {
            ConstraintLayout(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
            ) {
                val (blueBox, infoCards) = createRefs()
                Box(
                    modifier = Modifier
                        .background(mainBlue)
                        .fillMaxWidth()
                        .size(60.dp)
                        .constrainAs(blueBox) {}
                )
                Column(
                    modifier = Modifier
                        .constrainAs(infoCards) {
                            top.linkTo(blueBox.top, 12.dp)
                        }
                ) {
                    AccountCard(profileUIModel = profileUIModel)
                    AddressCard(profileUIModel = profileUIModel)
                }
            }
        }
    }
}

@Composable
fun AccountCard(
    profileUIModel: ProfileUIModel
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
    ) {
        Column(
            horizontalAlignment = Alignment.Start
        ) {
            SWText(
                modifier = Modifier
                    .padding(8.dp),
                text = stringResource(id = R.string.profile_my_account_block_title),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = lightGreyText,
            )
            ProfileItem(
                title = stringResource(id = R.string.profile_client_id_title),
                content = profileUIModel.id.ifEmpty { "-" },
            )
            ProfileItem(
                title = stringResource(id = R.string.profile_name_title),
                content = profileUIModel.name.ifEmpty { "-" },
            )
            ProfileItem(
                title = stringResource(id = R.string.profile_second_name_title),
                content = profileUIModel.secondName.ifEmpty { "-" },
            )
            ProfileItem(
                title = stringResource(id = R.string.profile_surname_title),
                content = profileUIModel.surname.ifEmpty { "-" },
            )
            ProfileItem(
                title = stringResource(id = R.string.profile_second_surname_title),
                content = profileUIModel.secondSurname.ifEmpty { "-" },
            )
            ProfileItem(
                title = stringResource(id = R.string.profile_phone_title),
                content = profileUIModel.phone.ifEmpty { "-" },
            )
            ProfileItem(
                title = stringResource(id = R.string.profile_mobile_title),
                content = profileUIModel.mobile.ifEmpty { "-" },
            )
            ProfileItem(
                title = stringResource(id = R.string.profile_birth_date_title),
                content = profileUIModel.birthDate.ifEmpty { "-" },
            )
        }
    }
}

@Composable
fun AddressCard(
    profileUIModel: ProfileUIModel
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
    ) {
        Column(
            horizontalAlignment = Alignment.Start
        ) {
            SWText(
                modifier = Modifier
                    .padding(8.dp),
                text = stringResource(id = R.string.profile_address_block_title),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = lightGreyText,
            )
            CountryProfileFlag(
                title = stringResource(id = R.string.profile_country_title),
                content = if (profileUIModel.country.isNotEmpty()) profileUIModel.country.first() else CountryData()
            )
            ProfileItem(
                title = stringResource(id = R.string.profile_cp_title),
                content = profileUIModel.cp.ifEmpty { "-" },
            )
            ProfileItem(
                title = stringResource(id = R.string.profile_street_number_title),
                content = profileUIModel.streetNumber.ifEmpty { "-" },
            )
            ProfileItem(
                title = stringResource(id = R.string.profile_building_name_title),
                content = profileUIModel.buildingName.ifEmpty { "-" },
            )
            ProfileItem(
                title = stringResource(id = R.string.profile_address_title),
                content = profileUIModel.address.ifEmpty { "-" },
            )
            ProfileItem(
                title = stringResource(id = R.string.profile_city_title),
                content = profileUIModel.city.ifEmpty { "-" },
            )
        }
    }
}

@Composable
fun ProfileItem(
    title: String,
    content: String
) {
    Column(
        modifier = Modifier
            .padding(start = 8.dp, bottom = 4.dp)
            .fillMaxWidth()
    ) {
        SWText(
            color = blueAccentColor,
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
        SWText(
            color = black,
            text = content,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun CountryProfileFlag(
    title: String,
    content: CountryData
) {
    Column(
        modifier = Modifier
            .padding(start = 8.dp, bottom = 4.dp)
            .fillMaxWidth()
    ) {
        SWText(
            color = blueAccentColor,
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SWImageFlag(
                modifier = Modifier
                    .size(24.dp),
                imageUrl = Constants.COUNTRY.FLAG_IMAGE_ASSETS + content.iso3 + Constants.COUNTRY.FLAG_IMAGE_EXTENSION
            )
            SWText(
                modifier = Modifier
                    .padding(start = 4.dp),
                color = black,
                text = content.name.ifEmpty { "-" },
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 420)
@Composable
fun Preview() {
    Content(
        ProfileUIModel(),
        object : ProfileLayoutListener {
            override fun registerEventCallBack(eventName: String) {}
            override fun onBackAction() {}
        },
        ErrorType.None,
        {},
        {},
        false,
        {}
    )
}
