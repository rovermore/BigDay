package com.smallworldfs.moneytransferapp.presentation.softregister.credentials

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
import com.google.android.material.textfield.TextInputLayout
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.databinding.FormStrongPasswordHolderBinding
import com.smallworldfs.moneytransferapp.databinding.FormTextHolderBinding
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Attributes
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import java.util.ArrayList

class PasswordViewUtils(binding: FormTextHolderBinding, attributes: Attributes?, strongPasswordReached: StrongPasswordReached) {

    private lateinit var strongPasswordView: FormStrongPasswordHolderBinding
    private var strongPasswordEmpty = false

    interface StrongPasswordReached {
        fun onStrongPasswordReached()
        fun onWeakPasswordAdded()
    }

    init {
        binding.input.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
        binding.input.setEndIconTintList(
            ContextCompat.getColorStateList(
                binding.root.context,
                R.drawable.password_toggle_tint
            )
        )

        binding.input.hint = binding.root.context.getString(R.string.password)
        binding.inputEdit.contentDescription = "passwordField"
        binding.inputEdit.inputType = InputType.TYPE_CLASS_TEXT
        binding.inputEdit.transformationMethod = PasswordTransformationMethod.getInstance()
        binding.inputEdit.setText(STRING_EMPTY)

        if (!::strongPasswordView.isInitialized) {
            strongPasswordView = FormStrongPasswordHolderBinding.inflate(
                LayoutInflater.from(
                    binding.root.context
                ),
                binding.additionalView as ViewGroup, false
            )
        }

        binding.inputEdit.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && binding.inputEdit.text != null) {
                binding.inputEdit.text?.let {
                    binding.inputEdit.setSelection(it.length)
                }
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

        attributes?.let { attributes ->
            if (attributes.max.isNotEmpty()) {
                val filters = ArrayList(listOf<InputFilter>())
                filters.addAll(binding.inputEdit.filters)
                filters.add(InputFilter.LengthFilter(Integer.valueOf(attributes.max)))
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
                    setPasswordLevel(binding, getPasswordStrength(input.toString(), attributes), strongPasswordView, strongPasswordReached)
                }
            })

            if (!(binding.additionalView as ViewGroup).children.contains(strongPasswordView.root)) {
                binding.additionalView.addView(strongPasswordView.root)
            }
        }
    }

    fun setPasswordLevel(binding: FormTextHolderBinding, level: Int, strongPasswordView: FormStrongPasswordHolderBinding, strongPasswordReached: StrongPasswordReached) {
        if (level >= 2) {
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
                strongPasswordReached.onWeakPasswordAdded()
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
                strongPasswordReached.onWeakPasswordAdded()
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
                strongPasswordReached.onStrongPasswordReached()
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
                strongPasswordReached.onWeakPasswordAdded()
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
                strongPasswordReached.onWeakPasswordAdded()
            }
        }
        strongPasswordView.passwordStrengthRequirements.setTextColor(passwordRequirementsColor)
        strongPasswordView.passwordStrengthLevel.text = strengthLevel
    }

    fun getPasswordStrength(password: String?, passwordRequirements: Attributes?): Int {
        val passRequirements = ArrayList<String>()
        var alphaNum = false
        var specialChar = false
        var upperCase = false
        if (password != null && passwordRequirements != null) {
            if (passwordRequirements.min != null && password.isNotEmpty() && password.length >= Integer.valueOf(passwordRequirements.min)) {
                passRequirements.add("min")
            }
            if (passwordRequirements.max != null && password.isNotEmpty() && password.length <= Integer.valueOf(passwordRequirements.max)) {
                passRequirements.add("max")
            }
            if (passwordRequirements.recommended != null && password.length >= Integer.valueOf(passwordRequirements.recommended)) {
                passRequirements.add("recommended")
            }
            if (passwordRequirements.isAlphaNum) {
                alphaNum = true
                if (password.matches(".*[a-zA-Z].*".toRegex()) && password.matches("(.)*(\\d)(.)*".toRegex())) {
                    passRequirements.add("alpha_num")
                }
            }
            if (passwordRequirements.isSpecialChar) {
                specialChar = true
                if (password.matches(".*[:?!@#$%^&*(),.;\\-_'+/=\"].*".toRegex())) {
                    passRequirements.add("special_char")
                }
            }
            if (passwordRequirements.isUpperCase) {
                upperCase = true
                if (password.matches(".*[A-Z].*".toRegex())) {
                    passRequirements.add("upper_case")
                }
            }
        }

        if (passRequirements.isNotEmpty()) {
            if (passRequirements.contains("min") && passRequirements.contains("max")) {
                if (alphaNum && specialChar && upperCase) {
                    return if (passRequirements.contains("alpha_num") &&
                        passRequirements.contains("special_char") &&
                        passRequirements.contains("upper_case")
                    ) {
                        if (passRequirements.contains("recommended")) {
                            3
                        } else {
                            2
                        }
                    } else {
                        1
                    }
                }

                if (alphaNum && specialChar) {
                    return if (passRequirements.contains("alpha_num") && passRequirements.contains("special_char")) {
                        if (passRequirements.contains("recommended")) {
                            3
                        } else {
                            2
                        }
                    } else {
                        1
                    }
                }

                if (alphaNum && upperCase) {
                    return if (passRequirements.contains("alpha_num") && passRequirements.contains("upper_case")) {
                        if (passRequirements.contains("recommended")) {
                            3
                        } else {
                            2
                        }
                    } else {
                        1
                    }
                }

                if (specialChar && upperCase) {
                    return if (passRequirements.contains("upper_case") && passRequirements.contains("special_char")) {
                        if (passRequirements.contains("recommended")) {
                            3
                        } else {
                            2
                        }
                    } else {
                        1
                    }
                }

                if (alphaNum) {
                    return if (passRequirements.contains("alpha_num")) {
                        if (passRequirements.contains("recommended")) {
                            3
                        } else {
                            2
                        }
                    } else {
                        1
                    }
                }

                if (specialChar) {
                    return if (passRequirements.contains("special_char")) {
                        if (passRequirements.contains("recommended")) {
                            3
                        } else {
                            2
                        }
                    } else {
                        1
                    }
                }

                if (upperCase) {
                    return if (passRequirements.contains("upper_case")) {
                        if (passRequirements.contains("recommended")) {
                            3
                        } else {
                            2
                        }
                    } else {
                        1
                    }
                }

                return if (passRequirements.contains("recommended")) {
                    3
                } else {
                    2
                }
            }
            if (password!!.isNotEmpty()) {
                return 1
            }
        }
        return 0
    }
}
