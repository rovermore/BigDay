package com.smallworldfs.moneytransferapp.data.countries.model

data class GetStatesResponse(val data: Data) {
    data class Data(val states: List<State>?)
    data class State(val code: String?, val name: String?, val logo: Logo?, val active: Boolean?)
    data class Logo(val url: String?)
}
