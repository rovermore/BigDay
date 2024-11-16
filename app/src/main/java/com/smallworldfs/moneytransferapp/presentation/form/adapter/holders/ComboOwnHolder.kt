package com.smallworldfs.moneytransferapp.presentation.form.adapter.holders

import android.content.Context
import android.text.InputType
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.SmallWorldApplication
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.AnalyticsSender
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ScreenName
import com.smallworldfs.moneytransferapp.databinding.FormComboHolderBinding
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericDialog
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field
import com.smallworldfs.moneytransferapp.presentation.form.adapter.GenericButtonAction
import com.smallworldfs.moneytransferapp.presentation.form.adapter.HolderViewModel
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

class ComboOwnHolder(binding: FormComboHolderBinding, private var genericButtonAction: GenericButtonAction, holderViewModel: HolderViewModel, private val context: Context) : BaseHolder<FormComboHolderBinding>(binding, holderViewModel) {

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface DaggerHiltEntryPoint {
        fun analyticsHandler(): AnalyticsSender
    }

    var analyticsSender: AnalyticsSender

    init {
        val hiltEntryPoint = EntryPointAccessors.fromApplication(
            SmallWorldApplication.app,
            GenericDialog.DaggerHiltEntryPoint::class.java,
        )
        analyticsSender = hiltEntryPoint.analyticsHandler()
    }

    override fun initialize(field: Field, lastField: Boolean) {

        super.initialize(field, lastField)

        binding.inputEdit.inputType = InputType.TYPE_NULL

        binding.input.hint = "${field.placeholder} ${if (field.isRequired) STRING_EMPTY else SmallWorldApplication.getStr(R.string.optional_tag)}"

        binding.inputEdit.setText(field.value)
        binding.inputEdit.contentDescription = field.name

        // Check if there is an error in the field, if not, check the children, if not, hide errors in edit text
        var errorToShow = STRING_EMPTY
        if (!field.errorMessage.isNullOrEmpty()) {
            errorToShow = field.errorMessage
        }
        if (errorToShow.isNotEmpty()) {
            binding.input.error = errorToShow
            binding.input.isErrorEnabled = true
        } else {
            binding.input.error = null
            binding.input.isErrorEnabled = false
        }

        binding.button.setOnClickListener {
            genericButtonAction.onClick(adapterPosition, field.name)
            when (field.name) {
                "mtn_purpose" -> analyticsSender.trackScreen(ScreenName.TRANSACTION_PURPOSE_SCREEN.value)
                "clientRelation" -> analyticsSender.trackScreen(ScreenName.RELATIONSHIP_WITH_BENEFICIARY_SCREEN.value)
                "state" -> analyticsSender.trackScreen(ScreenName.REGION_SCREEN.value)
                "paymentMethod" -> analyticsSender.trackScreen(ScreenName.PAYMENT_METHOD_SCREEN.value)
                "sourceoffunds" -> analyticsSender.trackScreen(ScreenName.SOURCE_FUNDS_SCREEN.value)
                "ocupation" -> analyticsSender.trackScreen(ScreenName.OCCUPATION_SCREEN.value)
            }
        }
    }
}
