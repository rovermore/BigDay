package com.smallworldfs.moneytransferapp.presentation.form.adapter

import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

interface GenericButtonAction {

    fun onClick(position: Int, keyValue: String = STRING_EMPTY)
}
