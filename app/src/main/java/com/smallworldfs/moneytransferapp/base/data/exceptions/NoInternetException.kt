package com.smallworldfs.moneytransferapp.base.data.exceptions

import java.io.IOException

/**
 * Exception for no internet connection
 */
class NoInternetException(
    message: String = "No internet connection is available"
) : IOException(message)
