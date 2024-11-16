package com.smallworldfs.moneytransferapp.compose.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Attributes
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import com.smallworldfs.moneytransferapp.compose.colors.colorGrayLight
import com.smallworldfs.moneytransferapp.compose.colors.darkGreyText
import com.smallworldfs.moneytransferapp.compose.colors.goodPasswordColor
import com.smallworldfs.moneytransferapp.compose.colors.strongPasswordColor
import com.smallworldfs.moneytransferapp.compose.colors.weakPasswordColor

@Composable
fun SWPasswordStrengthBar(
    modifier: Modifier,
    password: String,
    attributes: Attributes,
    auxText: String = STRING_EMPTY,
) {

    val state = getPasswordStrength(password, attributes)

    Column(
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Divider(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(
                        when (state) {
                            SWPasswordStrengthBarState.WEAK -> weakPasswordColor
                            SWPasswordStrengthBarState.GOOD -> goodPasswordColor
                            SWPasswordStrengthBarState.STRONG -> strongPasswordColor
                            else -> colorGrayLight
                        }
                    )
                    .weight(1f),
                thickness = 4.dp
            )
            Divider(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(
                        when (state) {
                            SWPasswordStrengthBarState.GOOD -> goodPasswordColor
                            SWPasswordStrengthBarState.STRONG -> strongPasswordColor
                            else -> colorGrayLight
                        }
                    )
                    .weight(1f),
                thickness = 4.dp
            )
            Divider(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(
                        when (state) {
                            SWPasswordStrengthBarState.STRONG -> strongPasswordColor
                            else -> colorGrayLight
                        }
                    )
                    .weight(1f),
                thickness = 4.dp
            )
        }
        Row {
            Spacer(Modifier.weight(1f))
            SWText(
                modifier = Modifier
                    .padding(
                        top = 4.dp
                    ),
                text = stringResource(id = state.stringId),
                color = darkGreyText,
            )
        }
        Row {
            SWText(
                modifier = Modifier
                    .padding(
                        top = 16.dp
                    ),
                text = auxText,
                textAlign = TextAlign.Start
            )
        }
    }
}

enum class SWPasswordStrengthBarState(val stringId: Int) {
    WEAK(R.string.password_weak),
    GOOD(R.string.password_good),
    STRONG(R.string.password_strong),
    NONE(R.string.password_weak)
}

private fun getPasswordStrength(password: String, passwordAttributes: Attributes): SWPasswordStrengthBarState {
    val passRequirements = ArrayList<String>()
    var alphaNum = false
    var specialChar = false
    var upperCase = false
    if (password.isNotEmpty() && passwordAttributes.isInitialized) {
        if (passwordAttributes.min != null && password.isNotEmpty() && password.length >= Integer.valueOf(passwordAttributes.min)) {
            passRequirements.add("min")
        }
        if (passwordAttributes.max != null && password.isNotEmpty() && password.length <= Integer.valueOf(passwordAttributes.max)) {
            passRequirements.add("max")
        }
        if (passwordAttributes.recommended != null && password.length >= Integer.valueOf(passwordAttributes.recommended)) {
            passRequirements.add("recommended")
        }
        if (passwordAttributes.isAlphaNum) {
            alphaNum = true
            if (password.matches(".*[a-zA-Z].*".toRegex()) && password.matches("(.)*(\\d)(.)*".toRegex())) {
                passRequirements.add("alpha_num")
            }
        }
        if (passwordAttributes.isSpecialChar) {
            specialChar = true
            if (password.matches(".*[:?!@#$%^&*(),.;\\-_'+/=\"].*".toRegex())) {
                passRequirements.add("special_char")
            }
        }
        if (passwordAttributes.isUpperCase) {
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
                        SWPasswordStrengthBarState.STRONG
                    } else {
                        SWPasswordStrengthBarState.GOOD
                    }
                } else {
                    SWPasswordStrengthBarState.WEAK
                }
            }

            if (alphaNum && specialChar) {
                return if (passRequirements.contains("alpha_num") && passRequirements.contains("special_char")) {
                    if (passRequirements.contains("recommended")) {
                        SWPasswordStrengthBarState.STRONG
                    } else {
                        SWPasswordStrengthBarState.GOOD
                    }
                } else {
                    SWPasswordStrengthBarState.WEAK
                }
            }

            if (alphaNum && upperCase) {
                return if (passRequirements.contains("alpha_num") && passRequirements.contains("upper_case")) {
                    if (passRequirements.contains("recommended")) {
                        SWPasswordStrengthBarState.STRONG
                    } else {
                        SWPasswordStrengthBarState.GOOD
                    }
                } else {
                    SWPasswordStrengthBarState.WEAK
                }
            }

            if (specialChar && upperCase) {
                return if (passRequirements.contains("upper_case") && passRequirements.contains("special_char")) {
                    if (passRequirements.contains("recommended")) {
                        SWPasswordStrengthBarState.STRONG
                    } else {
                        SWPasswordStrengthBarState.GOOD
                    }
                } else {
                    SWPasswordStrengthBarState.WEAK
                }
            }

            if (alphaNum) {
                return if (passRequirements.contains("alpha_num")) {
                    if (passRequirements.contains("recommended")) {
                        SWPasswordStrengthBarState.STRONG
                    } else {
                        SWPasswordStrengthBarState.GOOD
                    }
                } else {
                    SWPasswordStrengthBarState.WEAK
                }
            }

            if (specialChar) {
                return if (passRequirements.contains("special_char")) {
                    if (passRequirements.contains("recommended")) {
                        SWPasswordStrengthBarState.STRONG
                    } else {
                        SWPasswordStrengthBarState.GOOD
                    }
                } else {
                    SWPasswordStrengthBarState.WEAK
                }
            }

            if (upperCase) {
                return if (passRequirements.contains("upper_case")) {
                    if (passRequirements.contains("recommended")) {
                        SWPasswordStrengthBarState.STRONG
                    } else {
                        SWPasswordStrengthBarState.GOOD
                    }
                } else {
                    SWPasswordStrengthBarState.WEAK
                }
            }

            return if (passRequirements.contains("recommended")) {
                SWPasswordStrengthBarState.STRONG
            } else {
                SWPasswordStrengthBarState.GOOD
            }
        }
        if (password.isNotEmpty()) {
            return SWPasswordStrengthBarState.WEAK
        }
    }
    return SWPasswordStrengthBarState.NONE
}

@Preview(showBackground = true, widthDp = 420)
@Composable
fun SWPasswordStrengthBarPreview() {
    SWPasswordStrengthBar(
        modifier = Modifier,
        password = "Password123",
        attributes = Attributes()
    )
}
