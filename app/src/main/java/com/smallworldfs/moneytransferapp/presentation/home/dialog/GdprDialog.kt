package com.smallworldfs.moneytransferapp.presentation.home.dialog

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.compose.colors.black
import com.smallworldfs.moneytransferapp.compose.colors.colorBlueOcean
import com.smallworldfs.moneytransferapp.compose.colors.colorGrayLight
import com.smallworldfs.moneytransferapp.compose.colors.colorGreenMain
import com.smallworldfs.moneytransferapp.compose.colors.neutral0
import com.smallworldfs.moneytransferapp.compose.widgets.SWButton
import com.smallworldfs.moneytransferapp.compose.widgets.SWText
import com.smallworldfs.moneytransferapp.domain.migrated.base.Action
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.QuickReminderMessage
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

@Composable
fun GdprDialog(
    onCancel: Action = {},
    onAccept: Action = {},
    title: String = STRING_EMPTY,
    messages: List<QuickReminderMessage>,
    isOld: Boolean = false,
    positiveText: String = STRING_EMPTY,
    negativeText: String = STRING_EMPTY,
    trackEvent: Action
) {
    Dialog(
        onDismissRequest = { onCancel() }
    ) {
        ConstraintLayout(
            modifier = Modifier.semantics { contentDescription = "dialog_area" }
        ) {
            val (icon, layout) = createRefs()

            Image(
                painter = painterResource(R.drawable.login_icn_logo),
                contentDescription = "avatar",
                contentScale = ContentScale.Inside,
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(20.dp))
                    // .border(8.dp, white, CircleShape)
                    .constrainAs(icon) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .zIndex(1f),
            )

            Column(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(8.dp))
                    .background(neutral0)
                    .fillMaxWidth()
                    .constrainAs(layout) {
                        top.linkTo(icon.top, 32.dp)
                    }
                    .clip(RoundedCornerShape(12.dp)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Box(modifier = Modifier.size(32.dp))

                SWText(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = stringResource(id = R.string.quick_reminder_popup_title),
                    color = black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                )

                SWText(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = title,
                    color = colorGrayLight,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    content = {
                        items(messages) { message ->
                            Column {

                                if (message.title.isNotEmpty())
                                    SWText(
                                        modifier = Modifier.padding(start = 16.dp),
                                        text = message.title,
                                        fontWeight = FontWeight.Bold
                                    )
                                if (message.description.isNotEmpty())
                                    SWText(
                                        modifier = Modifier.padding(start = 16.dp),
                                        text = message.description,
                                    )
                                if (message.title.isNotEmpty() || message.description.isNotEmpty())
                                    Divider(
                                        color = colorGrayLight,
                                        modifier = Modifier
                                            .fillMaxWidth() // fill the max height
                                            .width(1.dp)
                                    )
                            }
                        }
                    },
                )

                SWButton(
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .semantics { contentDescription = "quick_reminder_continue_button" },
                    shape = RoundedCornerShape(50),
                    onClick = {
                        onAccept()
                        if (isOld) trackEvent()
                    },
                    text = positiveText.ifEmpty { stringResource(id = R.string.continue_text_button) },
                    backgroundColor = colorGreenMain,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    textModifier = Modifier.padding(0.dp),
                    textColor = colorBlueOcean
                )

                if (!isOld)
                    SWText(
                        modifier = Modifier
                            .padding(top = 20.dp, bottom = 20.dp)
                            .clickable { onCancel() },
                        text = negativeText.ifEmpty { stringResource(id = R.string.continue_text_button) },
                        fontSize = 14.sp,
                    )
            }
        }
    }
}
@Preview(showBackground = true, widthDp = 420)
@Composable
fun GdprDialogPreview() {
    GdprDialog(
        {},
        {},
        STRING_EMPTY,
        listOf(
            QuickReminderMessage("title", "description kjsdfksfdlkjgfsdkjg"),
            QuickReminderMessage("title", "description kjsdfksfdlkjgfsdkjg"),
            QuickReminderMessage("title", "description kjsdfksfdlkjgfsdkjg"),
            QuickReminderMessage("title", "description kjsdfksfdlkjgfsdkjg"),
            QuickReminderMessage("title", "description kjsdfksfdlkjgfsdkjg"),
        ),
        true,
        STRING_EMPTY,
        STRING_EMPTY,
        {}
    )
}
