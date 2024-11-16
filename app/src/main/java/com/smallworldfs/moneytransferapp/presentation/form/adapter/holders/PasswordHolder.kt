package com.smallworldfs.moneytransferapp.presentation.form.adapter.holders

import android.content.res.ColorStateList
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.children
import com.google.android.material.textfield.TextInputLayout.END_ICON_PASSWORD_TOGGLE
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.databinding.FormStrongPasswordHolderBinding
import com.smallworldfs.moneytransferapp.databinding.FormTextHolderBinding
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field
import com.smallworldfs.moneytransferapp.presentation.form.adapter.HolderViewModel
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import com.smallworldfs.moneytransferapp.utils.forms.SubType

class PasswordHolder(binding: FormTextHolderBinding, holderViewModel: HolderViewModel) :
    BaseTextHolder(binding, holderViewModel) {

    private lateinit var strongPasswordView: FormStrongPasswordHolderBinding
    private var strongPasswordEmpty = false

    override fun initialize(field: Field, lastField: Boolean) {
        super.initialize(field, lastField)
        strongPasswordEmpty = field.keyValue == "invalid"
        binding.input.endIconMode = END_ICON_PASSWORD_TOGGLE
        binding.input.setEndIconTintList(ContextCompat.getColorStateList(binding.root.context, R.drawable.password_toggle_tint))

        binding.input.hint = field.placeholder
        binding.inputEdit.inputType = InputType.TYPE_CLASS_TEXT
        binding.inputEdit.transformationMethod = PasswordTransformationMethod.getInstance()
        binding.inputEdit.setText(STRING_EMPTY)
        binding.inputEdit.contentDescription = field.name
        field.value = STRING_EMPTY

        if (field.subtype == SubType.STRONG_PASSWORD) {
            if (!::strongPasswordView.isInitialized) {
                strongPasswordView = FormStrongPasswordHolderBinding.inflate(LayoutInflater.from(binding.root.context), binding.additionalView as ViewGroup, false)
            }

            binding.inputEdit.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus && binding.inputEdit.text != null) {
                    binding.inputEdit.text?.let {
                        binding.inputEdit.setSelection(it.length)
                    }
                    field.errorMessage = STRING_EMPTY
                    binding.input.error = null
                    binding.input.isErrorEnabled = false
                    strongPasswordView.passwordStrengthRequirements.setTextColor(
                        ContextCompat
                            .getColor(binding.root.context, R.color.dark_grey_text)
                    )
                    strongPasswordView.passwordStrengthLevel.setTextColor(
                        ContextCompat
                            .getColor(binding.root.context, R.color.dark_grey_text)
                    )
                }
            }

            if (strongPasswordEmpty) {
                strongPasswordView.passwordStrengthRequirements.setTextColor(
                    ContextCompat
                        .getColor(binding.root.context, R.color.colorRedError)
                )
            }

            /*  field.keyValue?.let {
                  if (it == "invalid") {
                      strongPasswordView.passwordStrengthRequirements.setTextColor(ContextCompat
                              .getColor(binding.root.context, R.color.colorRedError))
                      strongPasswordView.passwordStrengthLevel.setTextColor(ContextCompat
                              .getColor(binding.root.context, R.color.colorRedError))
                  }
              }*/

            field.keyValue = "invalid"

            field.attributes?.let { attributes ->
                if (attributes.max.isNotEmpty()) {
                    val filters = ArrayList(listOf<InputFilter>())
                    filters.addAll(binding.inputEdit.filters)
                    filters.add(InputFilter.LengthFilter(Integer.valueOf(field.attributes.max)))
                    val filtersToAdd = arrayOfNulls<InputFilter>(filters.size)
                    var idx = 0
                    for (inputFilter in filters) {
                        filtersToAdd[idx++] = inputFilter
                    }
                    binding.inputEdit.filters = filtersToAdd
                }

                strongPasswordView.passwordWeakStrength.progressDrawable = ContextCompat.getDrawable(binding.root.context, R.drawable.password_strength_style)
                strongPasswordView.passwordGoodStrength.progressDrawable = ContextCompat.getDrawable(binding.root.context, R.drawable.password_strength_style)
                strongPasswordView.passwordStrongStrength.progressDrawable = ContextCompat.getDrawable(binding.root.context, R.drawable.password_strength_style)

                strongPasswordView.passwordStrengthLevel.text = binding.root.context.getString(R.string.password_weak)

                if (attributes.textRequirements != null) {
                    strongPasswordView.passwordStrengthRequirements.text = attributes.textRequirements
                }

                binding.inputEdit.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {}

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                    override fun onTextChanged(input: CharSequence?, start: Int, before: Int, count: Int) {
                        setPasswordLevel(viewModel.getPasswordStrength(input.toString(), attributes), strongPasswordView)
                    }
                })

                if (!(binding.additionalView as ViewGroup).children.contains(strongPasswordView.root)) {
                    binding.additionalView.addView(strongPasswordView.root)
                }
            }
        }
    }

    fun setPasswordLevel(level: Int, strongPasswordView: FormStrongPasswordHolderBinding) {
        field.keyValue = if (level >= 2) {
            "valid"
        } else {
            "invalid"
        }
        val progressColor: Int
        val strengthLevel: String
        var passwordRequirementsColor = ContextCompat.getColor(binding.root.context, R.color.dark_grey_text)
        when (level) {
            1 -> {
                strongPasswordView.passwordWeakStrength.progress = 100
                strongPasswordView.passwordGoodStrength.progress = 0
                strongPasswordView.passwordStrongStrength.progress = 0
                progressColor = ContextCompat.getColor(binding.root.context, R.color.weak)
                passwordRequirementsColor = ContextCompat.getColor(binding.root.context, R.color.weak)
                strongPasswordView.passwordWeakStrength.progressTintList = ColorStateList.valueOf(progressColor)
                strongPasswordView.passwordGoodStrength.progressTintList = ColorStateList.valueOf(ContextCompat.getColor(binding.root.context, R.color.grey_background_new_beneficiary))
                strongPasswordView.passwordStrongStrength.progressTintList = ColorStateList.valueOf(ContextCompat.getColor(binding.root.context, R.color.grey_background_new_beneficiary))
                strengthLevel = binding.root.context.getString(R.string.password_weak)
                strongPasswordView.passwordStrengthLevel.setTextColor(progressColor)
            }
            2 -> {
                strongPasswordView.passwordWeakStrength.progress = 100
                strongPasswordView.passwordGoodStrength.progress = 100
                strongPasswordView.passwordStrongStrength.progress = 0
                progressColor = ContextCompat.getColor(binding.root.context, R.color.good)
                strongPasswordView.passwordWeakStrength.progressTintList = ColorStateList.valueOf(progressColor)
                strongPasswordView.passwordGoodStrength.progressTintList = ColorStateList.valueOf(progressColor)
                strongPasswordView.passwordStrongStrength.progressTintList = ColorStateList.valueOf(ContextCompat.getColor(binding.root.context, R.color.grey_background_new_beneficiary))
                strengthLevel = binding.root.context.getString(R.string.password_good)
                strongPasswordView.passwordStrengthLevel.setTextColor(progressColor)
            }
            3 -> {
                strongPasswordView.passwordWeakStrength.progress = 100
                strongPasswordView.passwordGoodStrength.progress = 100
                strongPasswordView.passwordStrongStrength.progress = 100
                progressColor = ContextCompat.getColor(binding.root.context, R.color.strong)
                strongPasswordView.passwordWeakStrength.progressTintList = ColorStateList.valueOf(progressColor)
                strongPasswordView.passwordGoodStrength.progressTintList = ColorStateList.valueOf(progressColor)
                strongPasswordView.passwordStrongStrength.progressTintList = ColorStateList.valueOf(progressColor)
                strengthLevel = binding.root.context.getString(R.string.password_strong)
                strongPasswordView.passwordStrengthLevel.setTextColor(progressColor)
            }
            0 -> {
                strongPasswordView.passwordWeakStrength.progress = 0
                strongPasswordView.passwordGoodStrength.progress = 0
                strongPasswordView.passwordStrongStrength.progress = 0
                progressColor = ContextCompat.getColor(binding.root.context, R.color.grey_background_new_beneficiary)
                strongPasswordView.passwordWeakStrength.progressTintList = ColorStateList.valueOf(progressColor)
                strongPasswordView.passwordGoodStrength.progressTintList = ColorStateList.valueOf(progressColor)
                strongPasswordView.passwordStrongStrength.progressTintList = ColorStateList.valueOf(progressColor)
                strengthLevel = binding.root.context.getString(R.string.password_weak)
                strongPasswordView.passwordStrengthLevel.setTextColor(ContextCompat.getColor(binding.root.context, R.color.dark_grey_text))
            }
            else -> {
                strongPasswordView.passwordWeakStrength.progress = 0
                strongPasswordView.passwordGoodStrength.progress = 0
                strongPasswordView.passwordStrongStrength.progress = 0
                progressColor = ContextCompat.getColor(binding.root.context, R.color.grey_background_new_beneficiary)
                strongPasswordView.passwordWeakStrength.progressTintList = ColorStateList.valueOf(progressColor)
                strongPasswordView.passwordGoodStrength.progressTintList = ColorStateList.valueOf(progressColor)
                strongPasswordView.passwordStrongStrength.progressTintList = ColorStateList.valueOf(progressColor)
                strengthLevel = binding.root.context.getString(R.string.password_weak)
                strongPasswordView.passwordStrengthLevel.setTextColor(ContextCompat.getColor(binding.root.context, R.color.dark_grey_text))
            }
        }
        strongPasswordView.passwordStrengthRequirements.setTextColor(passwordRequirementsColor)
        strongPasswordView.passwordStrengthLevel.text = strengthLevel
    }
}
