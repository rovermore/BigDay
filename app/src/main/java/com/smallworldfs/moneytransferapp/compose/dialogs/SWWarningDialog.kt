package com.smallworldfs.moneytransferapp.compose.dialogs

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.compose.colors.black
import com.smallworldfs.moneytransferapp.compose.colors.colorGrayLight
import com.smallworldfs.moneytransferapp.compose.colors.colorRedError
import com.smallworldfs.moneytransferapp.compose.colors.neutral0
import com.smallworldfs.moneytransferapp.compose.widgets.SWButton
import com.smallworldfs.moneytransferapp.compose.widgets.SWText
import com.smallworldfs.moneytransferapp.domain.migrated.base.Action
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

@Composable
fun SWWarningDialog(
    title: String = STRING_EMPTY,
    content: String = STRING_EMPTY,
    positiveText: String = STRING_EMPTY,
    positiveAction: Action = { },
    dismissAction: Action
) {
    Dialog(
        onDismissRequest = { dismissAction() },
    ) {
        ConstraintLayout {
            val (icon, layout) = createRefs()

            Image(
                painter = painterResource(R.drawable.popup_icn_error),
                contentDescription = "avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .border(8.dp, neutral0, CircleShape)
                    .constrainAs(icon) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .zIndex(1f)
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
            ) {
                Box(modifier = Modifier.size(32.dp))

                if (title.isNotEmpty()) {
                    SWText(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        text = title,
                        color = black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                }

                if (content.isNotEmpty()) {
                    SWText(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        text = content,
                        color = colorGrayLight,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    )
                }

                if (positiveText.isNotEmpty()) {
                    SWButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        shape = RoundedCornerShape(8.dp),
                        onClick = {
                            positiveAction()
                        },
                        text = positiveText,
                        backgroundColor = colorRedError,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        textModifier = Modifier.padding(0.dp),
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 420)
@Composable
private fun SWWarningDialogPreview() {
    SWWarningDialog(
        "Title",
        "Content",
        "Positive Text",
        {},
        {}
    )
}
