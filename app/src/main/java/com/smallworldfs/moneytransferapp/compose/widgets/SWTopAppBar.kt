package com.smallworldfs.moneytransferapp.compose.widgets

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.smallworldfs.moneytransferapp.domain.migrated.base.Action
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import com.smallworldfs.moneytransferapp.compose.colors.mainBlue
import com.smallworldfs.moneytransferapp.compose.colors.neutral0

@Composable
fun SWTopAppBar(
    barTitle: String = STRING_EMPTY,
    trailingIcon: ImageVector? = null,
    trailingIconEvent: String = STRING_EMPTY,
    onBackPressed: Action,
    onTrailingIconPressed: Action = {},
    registerEventCallback: (String) -> Unit = { }
) {

    TopAppBar(
        backgroundColor = mainBlue,
        title = {
            SWText(
                text = barTitle,
                fontWeight = FontWeight.Bold,
                color = neutral0,
                fontSize = 18.sp
            )
        },
        navigationIcon = {
            IconButton(
                modifier = Modifier
                    .semantics {
                        contentDescription = "return_button"
                    },
                onClick = {
                    onBackPressed.invoke()
                    registerEventCallback("click_back")
                }
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "return_button", tint = neutral0)
            }
        },
        actions = {
            trailingIcon?.let {
                IconButton(
                    onClick = {
                        onTrailingIconPressed()
                        registerEventCallback(trailingIconEvent)
                    }
                ) {
                    Icon(it, contentDescription = "action_button", tint = neutral0)
                }
            }
        }
    )
}

@Preview(showBackground = true, widthDp = 420)
@Composable
fun SWTopAppBarPreview() {
    SWTopAppBar(
        barTitle = "Title",
        onBackPressed = {},
        registerEventCallback = {}
    )
}
