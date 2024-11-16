package com.smallworldfs.moneytransferapp.base.data.net.api

import com.smallworldfs.moneytransferapp.base.presentation.viewmodel.Error

data class ApiException(var error: Error? = null) : Throwable()
