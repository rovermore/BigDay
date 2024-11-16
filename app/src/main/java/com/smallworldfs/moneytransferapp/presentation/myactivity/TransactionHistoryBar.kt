package com.smallworldfs.moneytransferapp.presentation.myactivity

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.TabRow
import androidx.compose.material.Text
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smallworldfs.moneytransferapp.compose.colors.unSelectedBarBackgroundColor
import com.smallworldfs.moneytransferapp.compose.colors.colorBlueOceanDark
import com.smallworldfs.moneytransferapp.compose.colors.defaultGreyLightBackground
import com.smallworldfs.moneytransferapp.compose.colors.mediumBlue
import com.smallworldfs.moneytransferapp.compose.colors.selectedBarBackgroundColor
import com.smallworldfs.moneytransferapp.compose.colors.unSelectedBarTextColor
import com.smallworldfs.moneytransferapp.compose.colors.neutral0
import com.smallworldfs.moneytransferapp.compose.widgets.SWText
import com.smallworldfs.moneytransferapp.presentation.myactivity.model.MonthlyTransactionHistoryDetail
import com.smallworldfs.moneytransferapp.presentation.myactivity.model.TransactionHistoryBarUIModel
import com.smallworldfs.moneytransferapp.presentation.status.transaction.model.TransactionUIModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TransactionHistoryBar(transactionHistoryBarUIModel: TransactionHistoryBarUIModel, monthlyTransactions: HashMap<String, ArrayList<TransactionUIModel>>, onActionShowTransactionDetail: (TransactionUIModel) -> Unit) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { transactionHistoryBarUIModel.transactions.size })
    val selectedTabIndex by remember { derivedStateOf { pagerState.currentPage } }
    var selectedTransaction: MonthlyTransactionHistoryDetail?
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()),
    ) {
        Column(modifier = Modifier.background(mediumBlue)) {
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = mediumBlue,
                contentColor = mediumBlue,
                divider = { },
                indicator = { tabPositions ->
                    if (selectedTabIndex < tabPositions.size) {
                        TabRowDefaults.Indicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                            color = neutral0,
                        )
                    }
                },
            ) {
                transactionHistoryBarUIModel.transactions.onEachIndexed { index, transaction ->
                    if (transaction.value.monthlyTotal.equals(transactionHistoryBarUIModel.monthlyMax))
                        selectedTransaction = transaction.value
                    Tab(
                        modifier = Modifier
                            .background(mediumBlue)
                            .padding(vertical = 10.dp),
                        text = {
                            BarView(
                                Modifier
                                    .padding(vertical = 5.dp)
                                    .weight(1f),
                                transaction, transactionHistoryBarUIModel.monthlyMax,
                                selected = selectedTabIndex == index,
                            )
                        },
                        selected = selectedTabIndex == index,
                        selectedContentColor = neutral0,
                        onClick = {
                            selectedTransaction = transaction.value
                            scope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                    )
                }
            }
        }
        Box(
            Modifier
                .fillMaxSize()
                .background(defaultGreyLightBackground),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(mediumBlue),
            )

            HorizontalPager(
                modifier = Modifier.padding(top = 20.dp, start = 10.dp, end = 10.dp), state = pagerState,
            ) { index ->
                selectedTransaction = transactionHistoryBarUIModel.transactions.values.toList()[index]
                selectedTransaction?.let { transaction -> TransactionHistoryBarSummary(transaction, monthlyTransactions[transaction.month], onActionShowTransactionDetail) }
            }
        }
    }
}

@Composable
fun BarView(modifier: Modifier, transaction: Map.Entry<String, MonthlyTransactionHistoryDetail>, monthlyMax: Double, selected: Boolean) {
    val animatedTextColor by animateColorAsState(
        targetValue = if (selected) neutral0 else unSelectedBarTextColor,
        tween(500, delayMillis = 200, easing = FastOutSlowInEasing), label = "bar_background_color",
    )
    Column(
        modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        SWText(fontSize = 10.sp, text = String.format("%.2f", transaction.value.monthlyTotal), fontWeight = FontWeight.SemiBold, color = animatedTextColor, maxLines = 1)

        VerticallyAnimatedBar(((transaction.value.monthlyTotal / monthlyMax) * 130).dp, selected)

        Text(fontSize = 12.sp, modifier = Modifier.padding(top = 10.dp), text = transaction.key.uppercase(), fontWeight = FontWeight.SemiBold, color = animatedTextColor)
    }
}

@Composable
fun VerticallyAnimatedBar(height: Dp, selected: Boolean) {
    var shouldAnimateTransactionStats by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(key1 = height) { if (height > 0.dp) shouldAnimateTransactionStats = true }
    val heightStateAnimate by animateDpAsState(
        targetValue = if (shouldAnimateTransactionStats) height else 0.dp,
        tween(1000, delayMillis = 200, easing = LinearEasing), label = "vertical_bar_anim",
    )

    val animatedTransactionStatsBarBackgroundColor by animateColorAsState(
        targetValue = if (selected) neutral0 else colorBlueOceanDark,
        tween(500, delayMillis = 200, easing = LinearEasing), label = "transaction_stats_bar_background_color",
    )

    val animatedBarBackgroundColor by animateColorAsState(
        targetValue = if (selected) selectedBarBackgroundColor else unSelectedBarBackgroundColor,
        tween(500, delayMillis = 200, easing = FastOutSlowInEasing), label = "transaction_background_bar",
    )

    Box(
        modifier = Modifier
            .padding(top = 10.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(animatedBarBackgroundColor)
            .width(25.dp)
            .height(130.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .size(heightStateAnimate)
                .align(Alignment.BottomCenter)
                .background(animatedTransactionStatsBarBackgroundColor)
                .animateContentSize(),
        )
    }
}
