package com.smallworldfs.moneytransferapp.base.presentation.ui.extensions

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import coil.load
import coil.transform.CircleCropTransformation

fun ImageView.loadImage(resId: Int) {
    this.load(resId)
}

fun ImageView.loadImage(path: String?) {
    if (!path.isNullOrBlank()) {
        this.load(path)
    }
}

fun ImageView.loadImage(uri: Uri) {
    this.load(uri)
}

fun ImageView.loadImage(context: Context, placeholder: Int, path: String?) {
    if (!path.isNullOrBlank()) {
        this.load(path) {
            error(placeholder)
            fallback(placeholder)
        }
    } else {
        loadImage(placeholder)
    }
}

fun ImageView.loadCircularImage(context: Context, placeholder: Int, path: String?) {
    if (!path.isNullOrBlank()) {
        this.load(path) {
            error(placeholder)
            fallback(placeholder)
            transformations(CircleCropTransformation())
        }
    } else {
        loadCircularImage(context, placeholder, placeholder)
    }
}

fun ImageView.loadCircularImage(context: Context, placeholder: Int, drawable: Int) {
    this.load(drawable) {
        error(placeholder)
        fallback(placeholder)
        transformations(CircleCropTransformation())
    }
}
