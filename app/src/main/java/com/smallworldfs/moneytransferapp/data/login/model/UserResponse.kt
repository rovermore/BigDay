package com.smallworldfs.moneytransferapp.data.login.model

import com.google.gson.annotations.SerializedName
import com.smallworldfs.moneytransferapp.modules.login.domain.model.Gdpr

data class UserResponse(
    val data: Data
) {
    data class Data(val user: User)

    data class User(
        val id: Int,
        val email: String,
        @SerializedName("client_id")
        val clientId: String?,
        val name: String?,
        @SerializedName("second_name")
        val secondName: String?,
        val surname: String?,
        @SerializedName("second_surname")
        val secondSurname: String?,
        val phone: String?,
        @SerializedName("building_name")
        val buildingName: String?,
        val uuid: String,
        val status: String,
        @SerializedName("email_scoring")
        val emailScoring: String?,
        @SerializedName("birth_date")
        val birthDate: String?,
        val defaultLanguage: String?,
        val mobile: String?,
        val address: String?,
        val cp: String?,
        val city: String?,
        val state: String?,
        val country: String,
        @SerializedName("city_birth")
        val cityBirth: String?,
        @SerializedName("state_birth")
        val stateBirth: String?,
        @SerializedName("country_destination")
        val countryDestination: String?,
        val nationality: String?,
        val gender: String?,
        val streetType: String?,
        val streetName: String?,
        val streetNumber: String?,
        val ocupation: String?,
        val taxCode: String?,
        val ssn: String?,
        val mobilePhoneCountry: String?,
        @SerializedName("app_token")
        val appTokken: String?,
        val validatedEmail: Boolean,
        val kountsessid: String?,
        val flinksState: Int?,
        val gdpr: Gdpr?,
        @SerializedName("freshchat_id")
        val freshchatId: String?,
        @SerializedName("finished_transactions")
        val finishedTransactions: String?,
        @SerializedName("user_token")
        val userToken: String,
        val emailFraud: Boolean,
        var receiveNewsletters: String?,
        var receiveStatusTrans: String?,
        var authenticationStatus: String?
    )
}
