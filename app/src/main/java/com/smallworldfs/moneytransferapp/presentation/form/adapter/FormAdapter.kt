package com.smallworldfs.moneytransferapp.presentation.form.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.base.presentation.viewmodel.ViewModelFactory
import com.smallworldfs.moneytransferapp.databinding.FormButtonHolderBinding
import com.smallworldfs.moneytransferapp.databinding.FormCheckBoxHolderBinding
import com.smallworldfs.moneytransferapp.databinding.FormComboCountryHolderBinding
import com.smallworldfs.moneytransferapp.databinding.FormComboHolderBinding
import com.smallworldfs.moneytransferapp.databinding.FormFileDocumentBinding
import com.smallworldfs.moneytransferapp.databinding.FormGroupPhoneBinding
import com.smallworldfs.moneytransferapp.databinding.FormGroupTextBinding
import com.smallworldfs.moneytransferapp.databinding.FormSectionHeaderBinding
import com.smallworldfs.moneytransferapp.databinding.FormSwitchHolderBinding
import com.smallworldfs.moneytransferapp.databinding.FormTextButtonHolderBinding
import com.smallworldfs.moneytransferapp.databinding.FormTextHolderBinding
import com.smallworldfs.moneytransferapp.databinding.FormWhiteBoxHolderBinding
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field
import com.smallworldfs.moneytransferapp.presentation.base.ErrorType
import com.smallworldfs.moneytransferapp.presentation.form.adapter.holders.BaseHolder
import com.smallworldfs.moneytransferapp.presentation.form.adapter.holders.ButtonHolder
import com.smallworldfs.moneytransferapp.presentation.form.adapter.holders.CheckBoxHolder
import com.smallworldfs.moneytransferapp.presentation.form.adapter.holders.ComboCountryHolder
import com.smallworldfs.moneytransferapp.presentation.form.adapter.holders.ComboOwnHolder
import com.smallworldfs.moneytransferapp.presentation.form.adapter.holders.EmailHolder
import com.smallworldfs.moneytransferapp.presentation.form.adapter.holders.FileDocumentHolder
import com.smallworldfs.moneytransferapp.presentation.form.adapter.holders.GroupDateHolder
import com.smallworldfs.moneytransferapp.presentation.form.adapter.holders.GroupPhoneHolder
import com.smallworldfs.moneytransferapp.presentation.form.adapter.holders.GroupTextHolder
import com.smallworldfs.moneytransferapp.presentation.form.adapter.holders.PasswordHolder
import com.smallworldfs.moneytransferapp.presentation.form.adapter.holders.SectionHeaderHolder
import com.smallworldfs.moneytransferapp.presentation.form.adapter.holders.SwitchHolder
import com.smallworldfs.moneytransferapp.presentation.form.adapter.holders.TextButtonHolder
import com.smallworldfs.moneytransferapp.presentation.form.adapter.holders.TextHolder
import com.smallworldfs.moneytransferapp.presentation.form.adapter.holders.UnknownHolder
import com.smallworldfs.moneytransferapp.presentation.form.adapter.holders.WhiteBoxHolder
import com.smallworldfs.moneytransferapp.utils.forms.ViewType
import javax.inject.Inject

class FormAdapter @Inject constructor() : RecyclerView.Adapter<RecyclerView.ViewHolder>(), GenericButtonAction {

    @Inject
    lateinit var context: Context

    @Inject
    lateinit var viewModelFactory: ViewModelFactory<FormViewModel>

    @Inject
    lateinit var formNavigator: FormNavigator

    @Inject
    lateinit var holderViewModel: HolderViewModel

    lateinit var viewModel: FormViewModel

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = when (viewType) {
        ViewType.PASSWORD_VIEW -> PasswordHolder(FormTextHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false), holderViewModel)

        ViewType.TEXT_VIEW -> TextHolder(FormTextHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false), holderViewModel)

        ViewType.EMAIL_VIEW -> EmailHolder(FormTextHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false), holderViewModel)

        ViewType.COMBO_COUNTRY_VIEW -> ComboCountryHolder(FormComboCountryHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false), formNavigator, holderViewModel)

        ViewType.COMBO_OWN_VIEW -> ComboOwnHolder(FormComboHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false), this, holderViewModel, context)

        ViewType.SECTION_HEADER_VIEW -> SectionHeaderHolder(FormSectionHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false), holderViewModel)

        ViewType.GROUP_DATE_VIEW -> GroupDateHolder(FormComboHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false), this, holderViewModel)

        ViewType.GROUP_PHONE_VIEW -> GroupPhoneHolder(FormGroupPhoneBinding.inflate(LayoutInflater.from(parent.context), parent, false), this, holderViewModel)

        ViewType.GROUP_TEXT_VIEW -> GroupTextHolder(FormGroupTextBinding.inflate(LayoutInflater.from(parent.context), parent, false), this, holderViewModel)

        ViewType.BUTTON_VIEW -> ButtonHolder(FormButtonHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false), this, holderViewModel)

        ViewType.TEXT_BUTTON_VIEW -> TextButtonHolder(FormTextButtonHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false), this, holderViewModel)

        ViewType.CHECK_BOX -> CheckBoxHolder(FormCheckBoxHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false), holderViewModel)

        ViewType.SWITCH_VIEW -> SwitchHolder(FormSwitchHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false), holderViewModel)

        ViewType.WHITEBOX -> WhiteBoxHolder(FormWhiteBoxHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false), holderViewModel)

        ViewType.FILE_DOCUMENT -> FileDocumentHolder(FormFileDocumentBinding.inflate(LayoutInflater.from(parent.context), parent, false), this, holderViewModel)

        else -> UnknownHolder(View(parent.context))
    }

    override fun getItemCount(): Int = viewModel.getFieldsCount()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val field = viewModel.getItem(position)

        if (holder is UnknownHolder) {
            return
        }

        (holder as BaseHolder<*>).initialize(field, position == viewModel.getLastEditablePosition())
    }

    override fun getItemViewType(position: Int): Int = viewModel.getItemType(position)

    fun initViewModel(activity: AppCompatActivity) {
        viewModel = ViewModelProvider(activity, viewModelFactory)[FormViewModel::class.java]
    }

    fun getEvents(): LiveData<String> = viewModel.getAction()

    fun setFormList(fields: List<Field>) {
        viewModel.setList(fields)
        notifyDataSetChanged()
    }

    fun updateFormList(field: Field, position: Int) {
        viewModel.updateItem(field, position)
        notifyItemChanged(position)
    }

    fun addFieldToList(field: Field) {
        viewModel.addItem(field)
        notifyDataSetChanged()
    }

    fun getFormList(): List<Field> {
        return viewModel.getFormList()
    }

    fun getFormData(checkErrors: Boolean = true): HashMap<String, String> {
        return if (!checkErrors) {
            viewModel.sendForm()
        } else {
            if (!viewModel.checkForm(context.getString(R.string.empty_field))) {
                viewModel.sendForm()
            } else {
                notifyDataSetChanged()
                HashMap()
            }
        }
    }

    fun setErrors(errors: List<ErrorType.FieldError>) {
        val formList = getFormList()
        errors.forEach { error ->
            formList.find { it.name == error.field }?.let { field ->
                field.isHidden = false
                field.errorMessage = error.error.first()
                viewModel.updateItem(field, formList.indexOf(field))
                notifyDataSetChanged()
            }
        }
    }

    override fun onClick(position: Int, keyValue: String) {
        viewModel.onClick(position, keyValue)
    }
}
