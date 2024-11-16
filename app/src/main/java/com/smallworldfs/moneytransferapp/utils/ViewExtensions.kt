package com.smallworldfs.moneytransferapp.utils

import android.view.View
import androidx.constraintlayout.widget.Group
import androidx.viewbinding.ViewBinding

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun ViewBinding.gone() {
    this.root.visibility = View.GONE
}

fun ViewBinding.visible() {
    this.root.visibility = View.VISIBLE
}

fun Group.setAllOnClickListener(listener: View.OnClickListener) {
    referencedIds.forEach { id ->
        rootView.findViewById<View>(id).setOnClickListener(listener)
    }
}

fun View.visibleIf(condition: Boolean) = if (condition) visible() else gone()
