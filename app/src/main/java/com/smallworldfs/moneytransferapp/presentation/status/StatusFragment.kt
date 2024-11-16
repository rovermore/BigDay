package com.smallworldfs.moneytransferapp.presentation.status

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ScreenCategory
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.UserActionEvent
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericFragment
import com.smallworldfs.moneytransferapp.modules.home.presentation.navigator.HomeNavigator
import com.smallworldfs.moneytransferapp.modules.home.presentation.ui.activity.HomeActivity
import com.smallworldfs.moneytransferapp.presentation.status.transaction.model.SecondaryAction
import com.smallworldfs.moneytransferapp.presentation.status.transaction.model.TransactionUIModel
import com.smallworldfs.moneytransferapp.utils.widget.timer.sms.SWTimer
import com.smallworldfs.moneytransferapp.utils.widget.timer.sms.SWTimerFragmentLifecycleCallback
import com.smallworldfs.moneytransferapp.utils.widget.timer.sms.SWTimerProvider
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StatusFragment : GenericFragment(), SWTimerProvider {

    companion object {
        const val SCREEN_NAME = "STATUS_FRAGMENT"
    }

    @Inject
    lateinit var navigatorNew: StatusNavigatorNew

    private val cancelTimer = SWTimer()
    private val swTimerFragmentLifecycleCallback = SWTimerFragmentLifecycleCallback

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {

                StatusLayout(
                    cancelTimer = cancelTimer,
                    listener = object : Callbacks {
                        override fun onCardClicked(transaction: TransactionUIModel) {
                            registerEvent("click_recent_transaction_list")
                            navigatorNew.navigateToTransactionStatusDetail(transaction.mtn, transaction.offline)
                        }

                        override fun onActionButtonClicked(transactionUIModel: TransactionUIModel) {
                            setSecondaryAction(transactionUIModel)
                        }

                        override fun onSendMoneyButtonClicked() {
                            (requireActivity() as HomeActivity).switchToSendToTab()
                        }

                        override fun onOrderHistoryClicked() {
                            navigatorNew.navigateToMyActivityActivity()
                        }
                    },
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parentFragmentManager.registerFragmentLifecycleCallbacks(swTimerFragmentLifecycleCallback, true)
    }

    private fun setSecondaryAction(transaction: TransactionUIModel) {
        when (transaction.secondaryAction.type) {
            SecondaryAction.Type.AWAITING_PAYMENT -> {
                HomeNavigator.navigateToPayNowActivity(requireActivity(), transaction.mtn)
            }

            SecondaryAction.Type.AWAITING_BANK_TRANSFER -> {
                navigatorNew.navigateToTransferDetails(transaction)
            }

            else -> {}
        }
    }

    private fun registerEvent(eventAction: String) {
        val event = UserActionEvent(ScreenCategory.DASHBOARD.value, eventAction, "", getHierarchy(""), "", "", "")
        trackEvent(event)
    }

    override fun getSwTimer() = cancelTimer
}

interface Callbacks {
    fun onCardClicked(transaction: TransactionUIModel)

    fun onActionButtonClicked(transaction: TransactionUIModel)

    fun onSendMoneyButtonClicked()

    fun onOrderHistoryClicked()
}
