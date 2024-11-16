package com.smallworldfs.moneytransferapp.compose.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.compose.style.SWStartListItemStyle
import com.smallworldfs.moneytransferapp.compose.style.SWStartListItemStyle.Empty
import com.smallworldfs.moneytransferapp.compose.style.SWStartListItemStyle.FlagImage
import com.smallworldfs.moneytransferapp.compose.style.SWStartListItemStyle.None
import com.smallworldfs.moneytransferapp.compose.style.SWStartListItemStyle.PlaceHolder
import com.smallworldfs.moneytransferapp.compose.style.SWStartListItemStyle.Star
import com.smallworldfs.moneytransferapp.compose.style.SWStartListItemStyle.TickBox

@Composable
fun StartListItem(
    startListItemStyle: SWStartListItemStyle
) {
    when (startListItemStyle) {
        is PlaceHolder -> {
            Image(
                modifier = Modifier
                    .size(60.dp)
                    .padding(end = 16.dp),
                painter = painterResource(id = R.drawable.box_list_item), contentDescription = "",
            )
        }

        is TickBox ->
            Checkbox(
                modifier = Modifier
                    .padding(end = 16.dp),
                checked = startListItemStyle.isChecked,
                onCheckedChange = { startListItemStyle.onCheckBoxClicked(it) },
            )

        is FlagImage -> {
            SWImageFlag(
                size = 24.dp,
                imageUrl = startListItemStyle.imageUrl,
            )
        }

        is Star ->
            Image(
                modifier = Modifier
                    .padding(end = 16.dp),
                painter = painterResource(id = R.drawable.linear_star), contentDescription = "",
            )

        is Empty -> {
            Image(
                modifier = Modifier
                    .padding(end = 16.dp),
                painter = painterResource(id = R.drawable.bold_warning_2), contentDescription = "",
            )
        }

        is None -> {}
    }
}
