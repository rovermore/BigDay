package com.smallworldfs.moneytransferapp.domain.model

import com.google.gson.annotations.SerializedName
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field
import java.io.Serializable

data class FormModel(
    var groups: ArrayList<GroupModel> = arrayListOf(),

    @SerializedName("inputs")
    var fields: ArrayList<Field>? = null
) : Serializable
