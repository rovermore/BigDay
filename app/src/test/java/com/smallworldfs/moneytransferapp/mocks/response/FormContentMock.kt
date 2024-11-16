package com.smallworldfs.moneytransferapp.mocks.response

import com.smallworldfs.moneytransferapp.data.form.model.FormContent

object FormContentMock {

    private val dataItem = HashMap<String, String>().apply {
        put("string", "string")
        put("string", "string")
    }

    private val data = listOf(dataItem, dataItem)

    val formContent = FormContent(data)
}
