package com.smallworldfs.moneytransferapp.presentation.myactivity

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.smallworldfs.moneytransferapp.domain.migrated.base.Action
import androidx.lifecycle.viewmodel.compose.viewModel
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.compose.colors.blueAccentColor
import com.smallworldfs.moneytransferapp.compose.colors.mediumBlue
import com.smallworldfs.moneytransferapp.compose.colors.unSelectedBarBackgroundColor
import com.smallworldfs.moneytransferapp.compose.colors.neutral0
import com.smallworldfs.moneytransferapp.compose.widgets.SWErrorScreenLayout
import com.smallworldfs.moneytransferapp.compose.widgets.SWText
import com.smallworldfs.moneytransferapp.compose.widgets.SWTopAppBar
import com.smallworldfs.moneytransferapp.presentation.myactivity.model.TransactionHistoryBarUIModel
import com.smallworldfs.moneytransferapp.presentation.status.transaction.model.TransactionUIModel
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

@Composable
fun TransactionHistoryLayout(
    viewModel: TransactionHistoryViewModel = viewModel(),
    onBackActionCallback: Action,
    onActionShowTransactionDetail: (TransactionUIModel) -> Unit,
    onSendMoneyClicked: () -> Unit,
) {
    val transactionHistory by viewModel.transactionHistory.collectAsStateWithLifecycle()
    val monthlyTransactions by viewModel.monthlyTransactions.collectAsStateWithLifecycle()
    val loading by viewModel.loading.collectAsStateWithLifecycle()
    val error by viewModel.getTransactionsError.collectAsStateWithLifecycle()

    TransactionHistoryContent(transactionHistory, monthlyTransactions, loading, error, onSendMoneyClicked, onBackActionCallback, onActionShowTransactionDetail) { viewModel.getTransactions() }
}

@Composable
fun TransactionHistoryContent(
    transactionHistory: TransactionHistoryBarUIModel,
    monthlyTransactions: HashMap<String, ArrayList<TransactionUIModel>>,
    loading: Boolean,
    error: Boolean,
    onSendMoneyClicked: () -> Unit,
    onBackActionCallback: Action,
    onActionShowTransactionDetail: (TransactionUIModel) -> Unit,
    onRetryClicked: () -> Unit
) {
    Column {
        SWTopAppBar(barTitle = stringResource(id = R.string.my_activity_title), onBackPressed = { onBackActionCallback() })
        Box(Modifier.fillMaxSize()) {
            if (error)
                SWErrorScreenLayout { onRetryClicked() }
            else if (loading) {
                ActivityLoadingScreen()
            } else if (transactionHistory.transactions.isEmpty()) {
                EmptyActivityView { onSendMoneyClicked() }
            } else {
                TransactionHistoryBar(transactionHistoryBarUIModel = transactionHistory, monthlyTransactions = monthlyTransactions, onActionShowTransactionDetail = onActionShowTransactionDetail)
            }
        }
    }
}

@Composable
fun ActivityLoadingScreen() {
    Box(Modifier.fillMaxSize()) {
        Row(Modifier.background(mediumBlue)) {
            for (i in 1 until 6) {
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    SWText(fontSize = 12.sp, text = STRING_EMPTY, fontWeight = FontWeight.SemiBold, color = neutral0)

                    Box(
                        modifier = Modifier
                            .padding(top = 10.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(unSelectedBarBackgroundColor)
                            .width(25.dp)
                            .height(130.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .size(65.dp)
                                .align(Alignment.BottomCenter)
                                .background(neutral0)
                                .animateContentSize(),
                        )
                    }

                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp),
                    )
                }
            }
        }

        CircularProgressIndicator(
            modifier = Modifier
                .width(64.dp)
                .align(Alignment.Center),
            color = blueAccentColor,
        )
    }
}
