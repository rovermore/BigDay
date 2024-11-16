package com.smallworldfs.moneytransferapp.modules.transactional.domain.service

import com.smallworldfs.moneytransferapp.data.base.network.NetworkDatasource
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.server.GenericKeyValueDropContent
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.MoreField
import retrofit2.Response
import rx.Observable

class GenericDropContentNetworkDatasource(
    private val service: GenericDropContentService
) : NetworkDatasource() {

    fun getDropContent(url: String): Observable<Response<GenericKeyValueDropContent>> =
        executeCall(service.getDropContent(url))

    fun getMoreFieldsBasedOnRequestData(url: String): Observable<Response<MoreField>> =
        executeCall(service.getMoreFields(url))
}
