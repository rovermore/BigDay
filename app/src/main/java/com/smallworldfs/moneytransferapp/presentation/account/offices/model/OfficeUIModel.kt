package com.smallworldfs.moneytransferapp.presentation.account.offices.model

import androidx.compose.runtime.Stable
import com.smallworldfs.moneytransferapp.presentation.common.coordinates.SWCoordinates
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import java.io.Serializable

@Stable
data class OfficeUIModel(
    val address: String = STRING_EMPTY,
    val city: String = STRING_EMPTY,
    val country: String = STRING_EMPTY,
    val cp: String = STRING_EMPTY,
    val email: String = STRING_EMPTY,
    val fax: String = STRING_EMPTY,
    val festive: String = STRING_EMPTY,
    var id: String = STRING_EMPTY,
    val location: SWCoordinates = SWCoordinates.NotDefined,
    var miles: String = STRING_EMPTY,
    val name: String = STRING_EMPTY,
    var officeCode: String = STRING_EMPTY,
    val phone: String = STRING_EMPTY,
    val province: String = STRING_EMPTY,
    val timetable1: String = STRING_EMPTY,
    val timetable2: String = STRING_EMPTY,
    var url: String = STRING_EMPTY
) : Serializable
