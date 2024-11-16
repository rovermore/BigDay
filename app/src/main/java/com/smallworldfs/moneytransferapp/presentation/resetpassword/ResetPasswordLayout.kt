package com.smallworldfs.moneytransferapp.presentation.resetpassword

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.compose.colors.colorBlueOcean
import com.smallworldfs.moneytransferapp.compose.colors.darkGreyText
import com.smallworldfs.moneytransferapp.compose.colors.neutral0
import com.smallworldfs.moneytransferapp.domain.migrated.base.Action
import com.smallworldfs.moneytransferapp.presentation.base.ErrorType
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import com.smallworldfs.moneytransferapp.compose.dialogs.SWInfoDialog
import com.smallworldfs.moneytransferapp.compose.widgets.SWButton
import com.smallworldfs.moneytransferapp.compose.widgets.SWTopError
import com.smallworldfs.moneytransferapp.compose.widgets.SWPasswordInputTextField
import com.smallworldfs.moneytransferapp.compose.widgets.SWPasswordStrengthBar
import com.smallworldfs.moneytransferapp.compose.widgets.SWText
import com.smallworldfs.moneytransferapp.compose.widgets.SWWhiteToolbar

@Composable
fun ResetPasswordLayout(
    userToken: String,
    onBackAction: Action,
) {
    ResetPasswordMainView(
        userToken = userToken,
        onBackAction = onBackAction,
    )
}

@Composable
fun ResetPasswordMainView(
    viewModel: ResetPasswordViewModel = viewModel(),
    userToken: String,
    onBackAction: Action,
) {
    val showErrorView by viewModel.errorResetPassword.collectAsStateWithLifecycle()
    val successResetPassword by viewModel.successResetPassword.collectAsStateWithLifecycle()
    val passwordDescriptionText by viewModel.passwordRequirementsText.collectAsStateWithLifecycle()
    val passwordAttributes by viewModel.passwordAttributes.collectAsStateWithLifecycle()
    val errorDescription by rememberSaveable { mutableStateOf(STRING_EMPTY) }
    var passwordInputValue by rememberSaveable { mutableStateOf(STRING_EMPTY) }

    Column(
        modifier = Modifier
            .background(neutral0)
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()),
    ) {
        if (showErrorView !is ErrorType.None) {
            SWTopError(
                body = errorDescription.ifEmpty { stringResource(id = R.string.generic_error_view_subtitle) }
            ) {
                viewModel.hideErrorView()
            }
        }

        if (successResetPassword) {
            SWInfoDialog(
                title = stringResource(id = R.string.action_done_transactional_calculator),
                content = stringResource(id = R.string.password_reset_success_text),
                positiveText = stringResource(id = R.string.accept_text),
                positiveAction = {
                    viewModel.hideDialog()
                    onBackAction()
                },
                dismissAction = {
                    viewModel.hideDialog()
                    onBackAction()
                }
            )
        }

        SWWhiteToolbar { onBackAction() }

        Information()

        SWPasswordInputTextField(
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            onPasswordInput = {
                passwordInputValue = it
            },
        )

        SWPasswordStrengthBar(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(
                    top = 8.dp,
                    start = 16.dp,
                    end = 16.dp,
                ),
            password = passwordInputValue,
            attributes = passwordAttributes,
            auxText = passwordDescriptionText.ifEmpty { "Your password must have at least: 8 characters, one uppercase letter, one number and one special character" }
        )

        Spacer(modifier = Modifier.weight(1f))

        ResetButton(
            onResetButtonClick = {
                if (passwordInputValue.isEmpty()) {
                    viewModel.showErrorView(ErrorType.FieldError(STRING_EMPTY))
                } else {
                    viewModel.hideErrorView()
                    viewModel.resetPassword(passwordInputValue, userToken)
                }
            }
        )
    }
}

@Composable
fun Information() {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        SWText(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 8.dp,
                    end = 8.dp
                ),
            text = stringResource(id = R.string.forgotten_password_reset_title),
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            color = darkGreyText,
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        SWText(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 48.dp,
                    start = 8.dp,
                    end = 8.dp
                ),
            text = stringResource(id = R.string.forgotten_password_reset_subtitle),
            fontSize = 16.sp,
        )
    }
}

@Composable
fun ResetButton(
    onResetButtonClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {
        SWButton(
            modifier = Modifier
                .width(180.dp)
                .padding(
                    bottom = 48.dp
                ),
            text = stringResource(id = R.string.reset_password_button_text),
            shape = RoundedCornerShape(50),
            textColor = colorBlueOcean,
            onClick = { onResetButtonClick() }
        )
    }
}

@Preview(showBackground = true, widthDp = 420)
@Composable
fun ResetPasswordLayoutPreview() {
    ResetPasswordLayout(
        onBackAction = {},
        userToken = STRING_EMPTY,
    )
}
