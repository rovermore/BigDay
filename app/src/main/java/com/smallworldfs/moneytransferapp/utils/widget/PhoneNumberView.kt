package com.smallworldfs.moneytransferapp.utils.widget

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.loadCircularImage
import com.smallworldfs.moneytransferapp.databinding.FormGroupPhoneBinding
import com.smallworldfs.moneytransferapp.presentation.common.countries.CountryUIModel
import com.smallworldfs.moneytransferapp.utils.Constants

class PhoneNumberView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    var binding: FormGroupPhoneBinding? = null
    lateinit var country: CountryUIModel
        private set

    init {
        binding = FormGroupPhoneBinding.inflate(
            LayoutInflater.from(context),
            this,
            true,
        )
    }

    fun setup(
        hint: String,
        countrySelectorListener: () -> Unit,
        editTextListener: (String) -> Unit,
    ) {
        binding?.let {
            it.formGroupPhoneConstraintLayoutCodeSelector.setOnClickListener {
                countrySelectorListener.invoke()
            }
            it.formGroupPhoneTextInputLayoutEditText.doAfterTextChanged { editable ->
                editable?.let {
                    editTextListener.invoke(it.toString())
                }
            }
            it.formGroupPhoneTextInputLayoutPhone.hint = hint
            it.formGroupPhoneTextInputLayoutEditText.inputType = InputType.TYPE_CLASS_NUMBER
        }
    }

    fun setCountry(country: CountryUIModel) {
        this.country = country
        binding?.let {
            it.formGroupPhoneImageViewCountryChip.loadCircularImage(
                context,
                R.drawable.placeholder_country_adapter,
                Constants.COUNTRY.FLAG_IMAGE_ASSETS + country.iso3 + Constants.COUNTRY.FLAG_IMAGE_EXTENSION,
            )

            it.formGroupPhoneTextViewPrefix.text = country.prefix
        }
    }

    fun hideError() {
        binding?.let {
            it.formGroupPhoneTextInputLayoutEditText.setTextColor(ContextCompat.getColor(context, R.color.dark_grey_text))
            it.formGroupPhoneTextInputLayoutPhone.error = null
        }
    }

    fun showError() {
        binding?.let {
            it.formGroupPhoneTextInputLayoutEditText.setTextColor(ContextCompat.getColor(context, R.color.colorRedError))
            it.formGroupPhoneTextInputLayoutPhone.error = " "
        }
    }
}
