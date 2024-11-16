package com.smallworldfs.moneytransferapp.presentation.form.selector

import android.widget.ImageView
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import java.io.Serializable

data class FormSelectorItem(
    val key: String = STRING_EMPTY,
    val value: String = STRING_EMPTY,
    val drawable: Int? = null,
    val urlDrawable: String? = null,
    val scaleType: ImageView.ScaleType? = null,
    val requestCode: String? = null
) : Serializable
