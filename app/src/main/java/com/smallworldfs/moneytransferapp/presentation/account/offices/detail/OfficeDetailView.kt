package com.smallworldfs.moneytransferapp.presentation.account.offices.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.presentation.account.offices.model.OfficeUIModel
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import com.smallworldfs.moneytransferapp.compose.colors.defaultGreyLightBackground

@Composable
fun OfficeDetailView(
    office: OfficeUIModel,
    onPhoneClicked: (String) -> Unit,
    onMailClicked: (String) -> Unit,
    registerEvent: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(defaultGreyLightBackground)
    ) {

        if (office.phone.isNotEmpty())
            DetailItem(
                key = stringResource(id = R.string.office_detail_phone_label),
                value = office.phone,
                isClickable = true,
                onClicked = {
                    registerEvent("click_phone")
                    onPhoneClicked(office.phone)
                },
            )
        if (office.fax.isNotEmpty())
            DetailItem(
                key = stringResource(id = R.string.office_detail_fax_label),
                value = office.fax,
                isClickable = false,
                onClicked = { }
            )
        if (office.email.isNotEmpty())
            DetailItem(
                key = stringResource(id = R.string.office_detail_email_label),
                value = office.email,
                isClickable = true,
                onClicked = {
                    registerEvent("click_email")
                    onMailClicked(office.email)
                }
            )
        val timetable =
            when {
                office.timetable1.isNotEmpty() && office.timetable2.isNotEmpty() -> "${office.timetable1}, ${office.timetable2}"
                office.timetable1.isNotEmpty() -> office.timetable1
                office.timetable2.isNotEmpty() -> office.timetable2
                else -> STRING_EMPTY
            }
        if (timetable.isNotEmpty())
            DetailItem(
                key = stringResource(id = R.string.office_detail_opening_label),
                value = timetable,
                isClickable = false,
                onClicked = { }
            )
    }
}
