package com.smallworldfs.moneytransferapp.compose.widgets.form

import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field
import com.smallworldfs.moneytransferapp.utils.INT_ZERO
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

fun Field.getError(): String {
    var errorToShow = STRING_EMPTY
    if (this.childs != null && this.childs.size > INT_ZERO) {
        this.childs.asReversed().forEach { child ->
            if (!child.errorMessage.isNullOrEmpty()) {
                errorToShow = child.errorMessage
            }
        }
    }
    if (!this.errorMessage.isNullOrEmpty()) {
        errorToShow = this.errorMessage
    }
    return errorToShow
}
