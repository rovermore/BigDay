package com.smallworldfs.moneytransferapp.presentation.account.offices.detail

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ScreenCategory
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.UserActionEvent
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericActivity
import com.smallworldfs.moneytransferapp.presentation.account.offices.model.OfficeUIModel
import com.smallworldfs.moneytransferapp.utils.INPUT_STATE
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OfficeDetailActivity : GenericActivity(), OfficeDetailsCallbacks {

    private val viewModel: OfficeDetailViewModel by viewModels()

    @Inject
    lateinit var navigator: OfficeDetailNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            OfficeDetailVM(
                officeCallbacks = this,
                office = (intent?.getSerializableExtra(INPUT_STATE) as OfficeUIModel?) ?: OfficeUIModel()
            )
        }

        viewModel.getUserLocation((intent?.getSerializableExtra(INPUT_STATE) as OfficeUIModel?)?.location ?: OfficeUIModel().location)
    }

    override fun registerEvent(eventAction: String) {
        trackEvent(
            UserActionEvent(
                ScreenCategory.DASHBOARD.value,
                eventAction,
                "",
                getHierarchy(""),
            ),
        )
    }

    override fun onPhoneClicked(phone: String) {
        navigator.navigateToPhone(phone)
    }

    override fun onMailClicked(mail: String) {
        navigator.navigateToEmail(mail)
    }

    override fun onIndicationsClicked(officeSelected: OfficeUIModel) {
        navigator.navigateToUrl(
            "http://maps.google.com/maps?daddr=" +
                officeSelected.location.latitude + "," + officeSelected.location.longitude,
        )
    }

    override fun clickBack() {
        registerEvent("click_back")
        onBackPressed()
    }
}
