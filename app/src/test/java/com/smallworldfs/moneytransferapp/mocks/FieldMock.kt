package com.smallworldfs.moneytransferapp.mocks

import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field

object FieldMock {

    private val field = Field("type", "subtype", "value")

    val arrayFields = arrayListOf(field, field)
}
