package com.smallworldfs.moneytransferapp.base.presentation.ui.extensions

import android.view.View
import androidx.viewbinding.ViewBinding

fun View.hide() {
    this.visibility = View.GONE
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun ViewBinding.hide() {
    this.root.visibility = View.GONE
}

fun ViewBinding.show() {
    this.root.visibility = View.VISIBLE
}
