package com.smallworldfs.moneytransferapp.base.presentation.ui.extensions

import com.google.android.material.textfield.TextInputEditText

fun TextInputEditText.isEmpty(): Boolean = text.toString().trim { it <= ' ' }.isEmpty()

fun TextInputEditText.isNotEmpty(): Boolean = !isEmpty()
