package com.smallworldfs.moneytransferapp.modules.transactional.domain.service

import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.server.GenericKeyValueDropContent
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.MoreField
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url
import rx.Observable

interface GenericDropContentService {

    @GET
    fun getDropContent(@Url url: String?): Observable<Response<GenericKeyValueDropContent>?>?

    @GET
    fun getMoreFields(@Url url: String?): Observable<Response<MoreField>?>?
}
