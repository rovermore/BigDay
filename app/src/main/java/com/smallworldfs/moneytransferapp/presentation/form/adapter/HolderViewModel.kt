package com.smallworldfs.moneytransferapp.presentation.form.adapter

import androidx.lifecycle.ViewModel
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Attributes
import javax.inject.Inject

class HolderViewModel @Inject constructor() : ViewModel() {

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
