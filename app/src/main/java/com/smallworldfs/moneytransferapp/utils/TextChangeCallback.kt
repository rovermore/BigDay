package com.smallworldfs.moneytransferapp.utils

import android.text.Editable
import android.text.TextWatcher

interface TextChangeCallback : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(editable: Editable) {}
}
