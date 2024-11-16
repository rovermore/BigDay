package com.smallworldfs.moneytransferapp.mocks.response

import com.smallworldfs.moneytransferapp.presentation.form.selector.FormSelectorItem

object FormSelectorItemMock {

    private val formSelectorItem = FormSelectorItem(
        "key",
        "value",
        1,
        "urlDrawawable",
        null,
        "requestCode"
    )

    val formSelectorItemList = listOf(formSelectorItem, formSelectorItem)
}
