package com.smallworldfs.moneytransferapp.domain.migrated.form.repository

import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.presentation.form.selector.FormSelectorItem

interface FormRepository {
    fun getFieldContentFromNetwork(url: String): OperationResult<List<FormSelectorItem>, Error>
}
