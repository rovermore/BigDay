package com.smallworldfs.moneytransferapp.utils

import android.text.TextUtils
import javax.inject.Inject

class UrlUtils @Inject constructor() {

    fun formUrlFromApiRef(
        baseUrl: String,
        fields: ArrayList<String>,
        valueTrigger: String
    ): String {
        val builder = StringBuilder()
        builder.append(baseUrl)
        builder.append("?")
        for (param in fields) {
            if (param.contains("=") && param.split("=").toTypedArray().size > 1 &&
                param.split("=").toTypedArray()[1].isNotEmpty()
            ) {
                builder.append(param)
                builder.append("&")
            } else {
                if (!TextUtils.isEmpty(valueTrigger)) {
                    builder.append(param.plus(valueTrigger))
                    builder.append("&")
                }
            }
        }
        var formattedUrl = builder.toString().substring(0, builder.toString().length - 1)
        if (formattedUrl.contains("http://")) {
            formattedUrl = formattedUrl.replace("http://", "https://")
        }
        return formattedUrl
    }
}
