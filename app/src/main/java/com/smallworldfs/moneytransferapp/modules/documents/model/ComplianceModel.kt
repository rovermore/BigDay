package com.smallworldfs.moneytransferapp.modules.documents.model

import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import java.io.Serializable

data class ComplianceModel(
    val id: String = STRING_EMPTY,
    val mtn: String = STRING_EMPTY,
    val complianceType: String = STRING_EMPTY,
    val complianceSubtype: String = STRING_EMPTY,
    val title: String = STRING_EMPTY,
    val description: String = STRING_EMPTY
) : Serializable {

    // Old constructor compatibility
    constructor(compliance: Compliance) : this(
        if (compliance.id != null) compliance.id else STRING_EMPTY,
        if (compliance.mtn != null) compliance.mtn else STRING_EMPTY,
        if (compliance.complianceType != null) compliance.complianceType else STRING_EMPTY,
        if (compliance.title != null) compliance.title else STRING_EMPTY,
        if (compliance.description != null) compliance.description else STRING_EMPTY
    )
}
