package com.smallworldfs.moneytransferapp.domain.migrated.base

import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field

typealias Action = () -> Unit
typealias ErrorAction = (Error) -> Unit
typealias FieldAction = (Field) -> Unit
