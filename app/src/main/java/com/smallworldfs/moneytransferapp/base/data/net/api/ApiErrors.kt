package com.smallworldfs.moneytransferapp.base.data.net.api

class ApiErrors {
    companion object {
        const val GENERIC_ERROR = 400 or 500
        const val VALIDATION_ERROR = 422
        const val UNAUTHORIZED_ERROR = 403
    }
}
