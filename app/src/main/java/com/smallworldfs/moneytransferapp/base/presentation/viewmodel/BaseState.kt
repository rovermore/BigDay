package com.smallworldfs.moneytransferapp.base.presentation.viewmodel

import java.io.Serializable

sealed class BaseState<T>(val data: T) : Serializable {

    /**
     * State that represents the current state of a view.
     * @constructor T is the state model of the view
     */
    class Normal<T>(data: T) : BaseState<T>(data = data)

    /**
     * State that represents an alternative state of a view.
     * @constructor T is the state model of the view, data represents the current state of the view, dataAlternative represents extra dataAlternative to handle the alternative state
     */
    class Alternative<T>(data: T, val dataAlternative: BaseExtraData = BaseExtraData()) : BaseState<T>(data)

    /**
     * State that represents an error state of a view
     * @constructor T is the state model of the view, data represents the current state of the view, error represents the error who produced this state
     */
    class Error<T>(data: T, val dataError: Throwable) : BaseState<T>(data)
}

data class BaseExtraData(val type: Int = DEFAULT_ID, val extraData: Any? = null) : Serializable {
    companion object {
        const val DEFAULT_ID = 0
    }
}
