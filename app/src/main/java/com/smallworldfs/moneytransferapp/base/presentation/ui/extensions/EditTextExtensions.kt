package com.smallworldfs.moneytransferapp.base.presentation.ui.extensions

import android.widget.EditText

fun EditText.setTextIfHasChanged(text: String) {
    if (this.text.toString() != text) this.setText(text)
}
