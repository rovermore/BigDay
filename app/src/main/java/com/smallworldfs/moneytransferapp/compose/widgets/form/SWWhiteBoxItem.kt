package com.smallworldfs.moneytransferapp.compose.widgets.form

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.smallworldfs.moneytransferapp.compose.colors.darkGrey
import com.smallworldfs.moneytransferapp.compose.widgets.SWHtmlText
import com.smallworldfs.moneytransferapp.compose.widgets.SWText
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import com.smallworldfs.moneytransferapp.utils.forms.SubType

@Composable
fun SWWhiteBoxItem(
    field: Field,
) {
    if (field.subtype == SubType.RICH_TEXT) {
        SWHtmlText(
            modifier = Modifier.semantics { this.contentDescription = field.name },
            text = field.value,
        )
    } else SWText(text = field.value ?: STRING_EMPTY, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = darkGrey)
}

@Preview(showBackground = true, name = "Group phone item preview", widthDp = 420)
@Composable
fun SWWhiteBoxItemPreview() {
    SWWhiteBoxItem(
        Field(
            "whitebox",
            "richtext",
            "Would you like to know how we use your data? Find more information on our new <a href=\"https://www.smallworldfs.com/filemanager/userfiles/legal/swfs-group-privacy-notice-en.pdf\" target=\"blank\">Privacy Policy.</a>",
        ),
    )
}
