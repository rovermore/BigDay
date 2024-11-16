package com.smallworldfs.moneytransferapp.data.login.network

import com.smallworldfs.moneytransferapp.data.form.model.FormContent
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface FormService {

    @GET
    fun getFormContent(@Url url: String): Call<FormContent>
}
