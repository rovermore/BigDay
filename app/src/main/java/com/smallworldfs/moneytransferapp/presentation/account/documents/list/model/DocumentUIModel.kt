package com.smallworldfs.moneytransferapp.presentation.account.documents.list.model

import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import java.io.Serializable

open class DocumentUIModel : Serializable {
    var doc: String = STRING_EMPTY
    var uid: String = STRING_EMPTY
    var status: DocumentStatus = DocumentStatus.UNDEFINED
    var statusText: String = STRING_EMPTY
    var description: String = STRING_EMPTY
    var title: String = STRING_EMPTY
    var block: Boolean = false
    var upload: Boolean = false
    var buttons: List<DocumentButtonUIModel> = listOf()
    var number: String = STRING_EMPTY
    var name: String = STRING_EMPTY
    var expirationDate: String = STRING_EMPTY

    enum class DocumentStatus {
        MISSING, UNDER_REVIEW, APPROVED, EXPIRED, REJECTED, UNDEFINED
    }
}

sealed class DocumentIdUIModel : DocumentUIModel() {

    object Passport : DocumentIdUIModel(), Serializable

    object Id : DocumentIdUIModel(), Serializable

    object Cedula : DocumentIdUIModel(), Serializable

    object Aliencard : DocumentIdUIModel(), Serializable

    object CrewMemberID : DocumentIdUIModel(), Serializable

    object Dni : DocumentIdUIModel(), Serializable

    object DriversLicense : DocumentIdUIModel(), Serializable

    object FNPermit : DocumentIdUIModel(), Serializable

    object GreenCardUSCis : DocumentIdUIModel(), Serializable

    object LMACard : DocumentIdUIModel(), Serializable

    object NationalIdCard : DocumentIdUIModel(), Serializable

    object Naturalization : DocumentIdUIModel(), Serializable

    object OrangeCard : DocumentIdUIModel(), Serializable

    object PermanentRegistrationCard : DocumentIdUIModel(), Serializable

    object RegistrationCard : DocumentIdUIModel(), Serializable

    object StateIdentification : DocumentIdUIModel(), Serializable

    object StateIdCard : DocumentIdUIModel(), Serializable

    object UsaMilitaryIdCard : DocumentIdUIModel(), Serializable

    object Other : DocumentIdUIModel(), Serializable

    override fun toString(): String {
        return when (this) {
            is Passport -> "PASSPORT"
            is Id -> "ID"
            is Cedula -> "CEDULA"
            is Aliencard -> "ALIENCARD"
            is CrewMemberID -> "CREW_MEMBER_ID"
            is Dni -> "DNI"
            is DriversLicense -> "DRIVERS_LICENSE"
            is FNPermit -> "F_N_PERMIT"
            is GreenCardUSCis -> "GREEN_CARD_USCIS"
            is LMACard -> "LMA_CARD"
            is NationalIdCard -> "NATIONAL_ID_CARD"
            is Naturalization -> "NATURALIZATION"
            is OrangeCard -> "ORANGE_CARD"
            is PermanentRegistrationCard -> "PERMANENT_REGISTRATION_CARD"
            is RegistrationCard -> "REGISTRATION_CARD"
            is StateIdentification -> "STATE_IDENTIFICATION"
            is StateIdCard -> "STATE_ID_CARD"
            is UsaMilitaryIdCard -> "USA_MILITARY_ID_CARD"
            else -> ""
        }
    }
}

data class DocumentButtonUIModel(
    var text: String = STRING_EMPTY,
    var color: Int = R.color.main_blue,
    val url: String = STRING_EMPTY
) : Serializable

open class ComplianceDocUIModel(
    var mtn: String = STRING_EMPTY,
    var type: ComplianceType = ComplianceType.OTHER,
    var subtype: String = STRING_EMPTY,
    var documentTypeSelected: String = STRING_EMPTY
) : DocumentUIModel(), Serializable {

    enum class ComplianceType {
        AGGREGATE_RULE,
        BLOCKED_FOR_COMPLIANCE_REASON,
        CERTIFIED_KYC_REQUIRED,
        CLEARANCE_NEEDED,
        CLIENT_MATCHING_OVERFLOW,
        CLIENT_RELATION_REQUIRED,
        COMPLIANCE_FORM,
        DIGITAL_COPY_MISSING_FOR_PRIMARY_ID,
        ELECTRONIC_ID,
        ID_MISSING_OR_EXPIRED,
        ID_NOT_VERIFIED,
        KYC,
        KYC_REQUIRED,
        LETTER,
        MINIMUM_AGE,
        MISSING_DATA,
        MNI,
        NON_FACETOFACE,
        FACE_VERIFICATION,
        POLITICALLY_EXPOSED_PERSON,
        PROOF_OF_DOMICILE,
        PROOF_OF_FUNDS,
        PROOF_OF_OCCUPATION,
        PURPOSE_REQUIRED,
        SOURCE_REQUIRED,
        TAX_CODE_DOCUMENT,
        VERIFICATION_NEEDED,
        WATCHLIST,
        WRONG_DELIVERY_METHOD,
        EITHER_ID_IS_MISSING_OR_EXPIRED_OR_DATE_OF_BIRTH_AND_CITY_OF_BIRTH_ARE_MISSING,
        SIMILAR_CLIENT_ON_WATCHLIST,
        OTHER;
    }

    fun copyComplianceDoc(
        mtn: String = this.mtn,
        type: ComplianceType = this.type,
        subtype: String = this.subtype,
        doc: String = this.doc,
        id: String = this.uid,
        status: DocumentStatus = this.status,
        statusText: String = this.statusText,
        translateDescription: String = this.description,
        translateTitle: String = this.title,
        block: Boolean = this.block,
        upload: Boolean = this.upload,
        buttons: List<DocumentButtonUIModel> = this.buttons,
        number: String = this.number,
        name: String = this.name,
        expirationDate: String = this.expirationDate,
        documentTypeSelected: String = this.documentTypeSelected,
    ): ComplianceDocUIModel {
        return ComplianceDocUIModel(mtn, type, subtype, documentTypeSelected).apply {
            this.doc = doc
            this.uid = id
            this.status = status
            this.statusText = statusText
            this.description = translateDescription
            this.title = translateTitle
            this.block = block
            this.upload = upload
            this.buttons = buttons
            this.number = number
            this.name = name
            this.expirationDate = expirationDate
            this.documentTypeSelected = documentTypeSelected
        }
    }

    class FullValidationUIModel : ComplianceDocUIModel(type = ComplianceType.ID_MISSING_OR_EXPIRED) {
        fun copyFullValidation(
            mtn: String = this.mtn,
            subtype: String = this.subtype,
            doc: String = this.doc,
            id: String = this.uid,
            status: DocumentStatus = this.status,
            statusText: String = this.statusText,
            translateDescription: String = this.description,
            translateTitle: String = this.title,
            block: Boolean = this.block,
            upload: Boolean = this.upload,
            buttons: List<DocumentButtonUIModel> = this.buttons,
            number: String = this.number,
            name: String = this.name,
            expirationDate: String = this.expirationDate,
            documentTypeSelected: String = this.documentTypeSelected
        ): FullValidationUIModel {
            return FullValidationUIModel().apply {
                this.mtn = mtn
                this.subtype = subtype
                this.doc = doc
                this.uid = id
                this.status = status
                this.statusText = statusText
                this.description = translateDescription
                this.title = translateTitle
                this.block = block
                this.upload = upload
                this.buttons = buttons
                this.number = number
                this.name = name
                this.expirationDate = expirationDate
                this.documentTypeSelected = documentTypeSelected
            }
        }
    }
}
