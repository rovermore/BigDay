package com.smallworldfs.moneytransferapp.presentation.custom_views.transaction

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.SmallWorldApplication
import com.smallworldfs.moneytransferapp.databinding.TransactionStatusInfoViewBinding
import com.smallworldfs.moneytransferapp.domain.migrated.base.Action
import com.smallworldfs.moneytransferapp.presentation.status.transaction.model.SecondaryAction
import com.smallworldfs.moneytransferapp.presentation.status.transaction.model.TransactionUIModel
import com.smallworldfs.moneytransferapp.utils.gone
import com.smallworldfs.moneytransferapp.utils.visible

class TransactionStatusInfoView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    var binding: TransactionStatusInfoViewBinding? = null

    private var onCancelButtonClick: Action = {}
    private var onShowDetailsClick: Action = {}
    private var onPayNowClick: Action = {}
    private var onLeftButtonClick: Action = {}
    private var onRightButtonClick: Action = {}

    init {
        binding = TransactionStatusInfoViewBinding.inflate(
            LayoutInflater.from(context),
            this,
            true,
        )
    }

    fun setEventListener(
        onCancelButtonClick: Action = {},
        onShowDetailsClick: Action = {},
        onPayNowClick: Action = {},
        onLeftButtonClick: Action = {},
        onRightButtonClick: Action = {},
    ) {
        this.onCancelButtonClick = onCancelButtonClick
        this.onShowDetailsClick = onShowDetailsClick
        this.onPayNowClick = onPayNowClick
        this.onLeftButtonClick = onLeftButtonClick
        this.onRightButtonClick = onRightButtonClick
    }

    fun bind(transaction: TransactionUIModel) {
        binding?.let { binding ->

            val shouldShowTwoButtons = !transaction.isChallenge && transaction.isPaymentMethodChangeable()
            val singleLeftButtonText: String
            var rightButtonText = ""

            var iconButton: Drawable? = null

            if (transaction.isPaymentMethodChangeable()) {
                rightButtonText = SmallWorldApplication.getStr(R.string.bank_transfer)
                singleLeftButtonText = transaction.secondaryAction.buttonText
            } else {
                transaction.secondaryAction.icon?.let {
                    iconButton = ContextCompat.getDrawable(context, transaction.secondaryAction.icon)
                }
                singleLeftButtonText = transaction.secondaryAction.buttonText
            }

            binding.transactionStatusLabel.text = transaction.statusText
            transaction.statusIcon?.let {
                binding.transactionStatusLabel.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(context, it), null, null, null)
            }

            binding.transactionInfoContent.text = transaction.statusMsg
            binding.contentTransactionInfoCardInfoContent.visibility = if (transaction.statusMsg.isEmpty()) GONE else VISIBLE

            binding.statusTransactionInfoCard.visible()

            if (shouldShowTwoButtons) {
                binding.actionButtonsContainer.visible()
                binding.twoButtonContainer.visible()
                binding.leftButtonAction.text = singleLeftButtonText
                binding.rightButtonAction.text = rightButtonText
            } else {
                binding.twoButtonContainer.gone()
                if (singleLeftButtonText.isEmpty()) {
                    binding.actionButtonsContainer.gone()
                } else {
                    binding.singleButtonContainer.visible()
                    if (transaction.cancellable) {
                        binding.cancelTransactionButton.visible()
                        binding.showDetailsButton.gone()
                        binding.payNowButton.gone()
                        binding.cancelTransactionButton.text = singleLeftButtonText
                        iconButton?.let {
                            binding.cancelTransactionButton.setCompoundDrawablesWithIntrinsicBounds(iconButton, null, null, null)
                        }
                        binding.cancelTransactionButton.setOnClickListener {
                            onCancelButtonClick.invoke()
                        }
                    } else if (transaction.secondaryAction.type == SecondaryAction.Type.AWAITING_BANK_TRANSFER) {
                        binding.cancelTransactionButton.gone()
                        binding.showDetailsButton.visible()
                        binding.payNowButton.gone()
                        binding.showDetailsButton.text = singleLeftButtonText
                        binding.showDetailsButton.setCompoundDrawablesWithIntrinsicBounds(iconButton, null, null, null)
                        binding.showDetailsButton.setOnClickListener {
                            onShowDetailsClick.invoke()
                        }
                    } else {
                        binding.cancelTransactionButton.gone()
                        binding.showDetailsButton.gone()
                        binding.payNowButton.visible()
                        binding.payNowButton.text = singleLeftButtonText
                        binding.payNowButton.setCompoundDrawablesWithIntrinsicBounds(iconButton, null, null, null)
                        binding.payNowButton.setOnClickListener {
                            onPayNowClick.invoke()
                        }
                    }
                }
            }
            binding.leftButtonAction.setOnClickListener {
                onLeftButtonClick.invoke()
            }

            binding.rightButtonAction.setOnClickListener {
                onRightButtonClick.invoke()
            }
        }
    }
}
