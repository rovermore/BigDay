package com.smallworldfs.moneytransferapp.utils

import android.content.res.Resources

fun Int.dpToPixels() = (this * Resources.getSystem().displayMetrics.density).toInt()
