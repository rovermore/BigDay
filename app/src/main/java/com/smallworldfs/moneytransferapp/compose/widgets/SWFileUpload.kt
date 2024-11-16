package com.smallworldfs.moneytransferapp.compose.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.compose.colors.blueBackgroundWelcome
import com.smallworldfs.moneytransferapp.compose.colors.colorRedError
import com.smallworldfs.moneytransferapp.compose.colors.documentUploadedGreen
import com.smallworldfs.moneytransferapp.domain.migrated.base.Action
import com.smallworldfs.moneytransferapp.presentation.account.documents.upload.UploadDocumentsActivity.Companion.FRONT
import com.smallworldfs.moneytransferapp.utils.INT_ONE
import com.smallworldfs.moneytransferapp.utils.INT_ZERO
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import java.util.Locale

@Composable
fun SWFileUpload(
    title: String = STRING_EMPTY,
    name: String,
    errorMessage: String,
    value: String,
    placeholder: String,
    onClick: Action
) {

    val text = if (title.isNotEmpty()) {
        title
    } else {
        name.substring(INT_ZERO, INT_ONE).uppercase(
            Locale.getDefault()
        ) + name.substring(INT_ONE, name.length)
    }

    val imageId = when {
        value.isNotEmpty() && errorMessage.isEmpty() -> R.drawable.icn_upload_ok
        errorMessage.isNotEmpty() -> R.drawable.hardregister_icn_attachdocfail
        else -> R.drawable.icn_upload_new
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable { onClick() }
            .semantics {
                this.contentDescription =
                    if (name == FRONT) "front_upload_button"
                    else "back_upload_button"
            },
    ) {

        Image(
            painterResource(imageId),
            contentDescription = "company logo",
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            alignment = Alignment.Center,
        )

        Column {
            SWText(
                text = text,
                modifier = Modifier
                    .fillMaxWidth(),
            )

            when {
                value.isNotEmpty() && errorMessage.isEmpty() ->
                    SWText(
                        text = stringResource(id = R.string.image_loaded_successfully),
                        color = documentUploadedGreen,
                    )
                errorMessage.isNotEmpty() ->
                    SWText(
                        text = placeholder,
                        color = colorRedError,
                    )
                else ->
                    SWText(
                        text = placeholder,
                        color = blueBackgroundWelcome
                    )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 420)
@Composable
fun SWFileUploadPreview() {
    SWFileUpload(STRING_EMPTY, STRING_EMPTY, STRING_EMPTY, STRING_EMPTY, STRING_EMPTY,) {}
}
