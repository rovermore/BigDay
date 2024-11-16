package com.smallworldfs.moneytransferapp.presentation.promotions

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.BrazeEventName
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.BrazeEventProperty
import com.smallworldfs.moneytransferapp.compose.colors.black
import com.smallworldfs.moneytransferapp.compose.colors.blueAccentColor
import com.smallworldfs.moneytransferapp.compose.colors.colorBlueOcean
import com.smallworldfs.moneytransferapp.compose.colors.defaultGreyLightBackground
import com.smallworldfs.moneytransferapp.compose.colors.greySeparator
import com.smallworldfs.moneytransferapp.compose.colors.transparent
import com.smallworldfs.moneytransferapp.compose.colors.neutral0
import com.smallworldfs.moneytransferapp.compose.dialogs.SWInputDialog
import com.smallworldfs.moneytransferapp.compose.dialogs.SWWarningDialog
import com.smallworldfs.moneytransferapp.compose.widgets.SWButton
import com.smallworldfs.moneytransferapp.compose.widgets.SWCircularLoader
import com.smallworldfs.moneytransferapp.compose.widgets.SWErrorView
import com.smallworldfs.moneytransferapp.compose.widgets.SWText
import com.smallworldfs.moneytransferapp.compose.widgets.SWTextButton
import com.smallworldfs.moneytransferapp.compose.widgets.SWTopAppBar
import com.smallworldfs.moneytransferapp.domain.migrated.base.Action
import com.smallworldfs.moneytransferapp.presentation.base.ErrorType
import com.smallworldfs.moneytransferapp.presentation.promotions.listener.PromotionsActivityListener
import com.smallworldfs.moneytransferapp.presentation.promotions.model.PromotionUIModel
import com.smallworldfs.moneytransferapp.utils.Constants
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

@Composable
fun PromotionsLayout(
    listener: PromotionsActivityListener,
    viewModel: PromotionsViewModel = viewModel()
) {
    val promotions by viewModel.promotions.collectAsStateWithLifecycle()
    val showAddCodeDialog by viewModel.showAddCodeDialog.collectAsStateWithLifecycle()
    val loading by viewModel.loading.collectAsStateWithLifecycle()
    val promotionInvalid by viewModel.promotionInvalid.collectAsStateWithLifecycle()
    val validPromotion by viewModel.validPromotion.collectAsStateWithLifecycle()
    val genericError by viewModel.error.collectAsStateWithLifecycle()

    Content(
        onBackPressed = { listener.onBackPressed() },
        registerEvent = { eventAction, eventLabel ->
            listener.registerEventCallback(eventAction, eventLabel)
        },
        promotionList = promotions,
        onPromotionSelected = { viewModel.selectPromotion(it) },
        onAddPromotionCode = { viewModel.showAddCodeDialog() },
        showAddCodeDialog = showAddCodeDialog,
        onPromotionCodeAdded = {
            viewModel.checkPromotion(it)
            viewModel.hideAddCodeDialog()
        },
        onDismissDialog = { viewModel.hideAddCodeDialog() },
        loading = loading,
        promotionInvalid = promotionInvalid,
        onPromotionInvalidDialogDismiss = { viewModel.hidePromotionInvalidError() },
        onDoneAction = {
            val properties = HashMap<String, String>()
            properties[BrazeEventProperty.DESTINATION_COUNTRY.value] = viewModel.destinationCountry.value
            properties[BrazeEventProperty.PROMOTIONAL_CODE.value] = viewModel.selectedPromotion.value.code

            listener.registerBrazeEventCallback(BrazeEventName.SELECT_PROMOTIONAL_CODE.value, properties)
            listener.registerEventCallback("click_done", STRING_EMPTY)

            viewModel.updateSelectedPromotion()

            listener.onDoneAction()
        },
        genericErrorType = genericError,
        closeErrorViewAction = { viewModel.hideGenericErrorView() },
    )

    LaunchedEffect(validPromotion) {
        if (validPromotion != STRING_EMPTY) {
            listener.registerEventCallback("formOk", STRING_EMPTY)
        }
    }
}

@Composable
fun Content(
    onBackPressed: Action,
    registerEvent: (eventAction: String, eventLabel: String) -> Unit,
    promotionList: List<PromotionUIModel>,
    showAddCodeDialog: Boolean,
    loading: Boolean,
    promotionInvalid: ErrorType,
    onPromotionSelected: (PromotionUIModel) -> Unit,
    onAddPromotionCode: Action,
    onPromotionCodeAdded: (String) -> Unit,
    onDismissDialog: Action,
    onPromotionInvalidDialogDismiss: Action,
    onDoneAction: Action,
    genericErrorType: ErrorType,
    closeErrorViewAction: Action
) {
    Column(
        modifier = Modifier
            .background(defaultGreyLightBackground)
            .fillMaxSize(),
    ) {
        SWTopAppBar(
            barTitle = stringResource(id = R.string.promotional_codes_title),
            onBackPressed = { onBackPressed() },
        )

        if (loading) {
            SWCircularLoader(
                size = 32.dp,
            )
        }

        if (genericErrorType != ErrorType.None) {
            SWErrorView(
                onCloseIconClick = closeErrorViewAction,
            )
        }

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            if (showAddCodeDialog) {
                SWInputDialog(
                    modifier = Modifier
                        .semantics { contentDescription = "promotion_text" },
                    description = stringResource(id = R.string.enter_promotional_code_dialog_tile),
                    placeholder = stringResource(id = R.string.promotional_code_content_text),
                    showIcon = false,
                    positiveText = stringResource(id = R.string.ok),
                    positiveAction = {
                        registerEvent("click_save", STRING_EMPTY)
                        onPromotionCodeAdded(it)
                    },
                    dismissAction = { onDismissDialog() },
                )
            }

            if (promotionInvalid !is ErrorType.None) {
                SWWarningDialog(
                    title = stringResource(id = R.string.generic_title_error),
                    content = stringResource(id = R.string.invalid_promotion_text),
                    positiveText = stringResource(id = R.string.accept_text),
                    positiveAction = { onPromotionInvalidDialogDismiss() },
                    dismissAction = { onPromotionInvalidDialogDismiss() },
                )
                registerEvent("formKo", STRING_EMPTY)
            }

            Row(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .background(neutral0)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                SWText(
                    modifier = Modifier
                        .padding(start = 16.dp),
                    text = stringResource(id = R.string.add_promo_title_text),
                    color = black,
                    fontSize = 14.sp,
                )

                SWTextButton(
                    modifier = Modifier
                        .padding(start = 8.dp),
                    text = stringResource(id = R.string.add_promo_code_text),
                    colorText = blueAccentColor,
                    fontSize = 14.sp,
                    clickAction = {
                        registerEvent("add_code", STRING_EMPTY)
                        onAddPromotionCode()
                    },
                )
            }

            SWText(
                modifier = Modifier
                    .padding(top = 32.dp),
                text = stringResource(id = R.string.text_select_promotion),
                fontSize = 14.sp,
            )

            Column(
                modifier = Modifier
                    .padding(top = 32.dp)
                    .background(neutral0),
            ) {
                promotionList.forEach {

                    var dateText = stringResource(id = R.string.zero_quantity)

                    if (it.daysRemaining >= 1) {
                        dateText = if (it.daysRemaining == 1) {
                            stringResource(id = R.string.expires_in_singular, it.daysRemaining.toString())
                        } else {
                            stringResource(id = R.string.expires_in_plural, it.daysRemaining.toString())
                        }
                    }

                    val discount = when (it.discountType) {
                        Constants.PROMOTIONS.FEE_AMOUNT -> stringResource(id = R.string.fee_amount_promotion_text, it.discount, it.currency)
                        Constants.PROMOTIONS.FEE_PERCENT -> stringResource(id = R.string.no_fees_promotion_text)
                        Constants.PROMOTIONS.TOTAL_AMOUNT -> stringResource(id = R.string.total_amount_promotion_text, it.discount, it.currency)
                        Constants.PROMOTIONS.TOTAL_PERCENT -> stringResource(id = R.string.total_percent_promotion_text, it.discount)
                        else -> STRING_EMPTY
                    }

                    PromotionItem(
                        name = stringResource(id = R.string.promotion_text, it.code),
                        date = dateText,
                        discount = discount,
                        promotion = it,
                        onItemSelected = { promotion ->
                            onPromotionSelected(promotion)
                            registerEvent("click_save", STRING_EMPTY)
                        },
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            SWButton(
                modifier = Modifier
                    .padding(vertical = 16.dp),
                text = stringResource(id = R.string.done_action),
                textColor = colorBlueOcean,
                textModifier = Modifier
                    .padding(horizontal = 48.dp),
                shape = CircleShape,
                onClick = { onDoneAction() },
            )
        }
    }
}

@Composable
fun PromotionItem(
    name: String,
    date: String,
    discount: String,
    promotion: PromotionUIModel,
    onItemSelected: (PromotionUIModel) -> Unit
) {

    var selected by rememberSaveable { mutableStateOf(promotion.selected) }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                SWText(
                    modifier = Modifier
                        .padding(start = 8.dp, top = 16.dp),
                    text = name,
                    color = black,
                    fontSize = 14.sp,
                    fontStyle = FontStyle.Italic,
                )
                SWText(
                    modifier = Modifier
                        .padding(start = 8.dp, top = 8.dp, bottom = 16.dp),
                    text = date,
                    fontSize = 14.sp,
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            SWText(
                modifier = Modifier
                    .padding(end = 8.dp),
                text = discount,
                fontSize = 14.sp,
                color = blueAccentColor,
            )

            Box(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(30.dp)
                    .border(1.dp, blueAccentColor, CircleShape)
                    .clip(CircleShape)
                    .background(transparent)
                    .clickable {
                        promotion.apply { selected = !selected }
                        onItemSelected(promotion)
                    },
            ) {
                if (selected) {
                    Image(
                        painter = painterResource(R.drawable.ic_check_indicator_blue),
                        contentDescription = "",
                        modifier = Modifier
                            .align(Alignment.Center),
                        colorFilter = ColorFilter.tint(blueAccentColor),
                    )
                }
            }
        }

        Divider(
            modifier = Modifier
                .background(greySeparator)
                .fillMaxWidth()
                .height(1.dp),
        )
    }
}

@Preview(showBackground = true, widthDp = 420)
@Composable
fun PromotionsLayoutPreview() {
    Content(
        onBackPressed = {},
        registerEvent = { _, _ -> },
        promotionList = emptyList(),
        onPromotionCodeAdded = {},
        onAddPromotionCode = {},
        showAddCodeDialog = false,
        onPromotionSelected = {},
        onDismissDialog = {},
        onPromotionInvalidDialogDismiss = {},
        onDoneAction = {},
        promotionInvalid = ErrorType.None,
        loading = false,
        genericErrorType = ErrorType.None,
        closeErrorViewAction = {},
    )
}
