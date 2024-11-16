package com.smallworldfs.moneytransferapp.domain.migrated.account.documents.model

import com.smallworldfs.moneytransferapp.modules.home.domain.model.DocumentButton
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import java.util.Date

open class DocumentDTO {
    var name: String = STRING_EMPTY
    var changeDate: String = STRING_EMPTY
    var clientId: String = STRING_EMPTY
    var expirationDate: Long = Date().time
    var uid: String = STRING_EMPTY
    var issuer: Issuer = Issuer()
    var number: String = STRING_EMPTY
    var status: Status = Status()
    var block: Boolean = false
    var upload: Boolean = false
    var buttons: List<DocumentButton> = listOf()
    var updatedAt: String = STRING_EMPTY
    var current: String = STRING_EMPTY
    var doc: String = STRING_EMPTY
}

sealed class DocumentIdDTO : DocumentDTO() {
    object Passport : DocumentIdDTO()
    object Id : DocumentIdDTO()
    object Cedula : DocumentIdDTO()
    object Aliencard : DocumentIdDTO()
    object CrewMemberID : DocumentIdDTO()
    object Dni : DocumentIdDTO()
    object DriversLicense : DocumentIdDTO()
    object FNPermit : DocumentIdDTO()
    object GreenCardUSCis : DocumentIdDTO()
    object LMACard : DocumentIdDTO()
    object NationalIdCard : DocumentIdDTO()
    object Naturalization : DocumentIdDTO()
    object OrangeCard : DocumentIdDTO()
    object PermanentRegistrationCard : DocumentIdDTO()
    object RegistrationCard : DocumentIdDTO()
    object StateIdentification : DocumentIdDTO()
    object StateIdCard : DocumentIdDTO()
    object UsaMilitaryIdCard : DocumentIdDTO()
    object Other : DocumentIdDTO()
}

class Status(
    val id: DocumentStatusDTO = DocumentStatusDTO.UNDEFINED,
    val name: String = STRING_EMPTY
)

enum class DocumentStatusDTO {
    MISSING, EXPIRED, REJECTED, UNDER_REVIEW, APPROVED, UNDEFINED
}

class Issuer(
    val iso3: String = STRING_EMPTY,
    val countryName: String = STRING_EMPTY,
    val stateName: String = STRING_EMPTY,
    val origin: String = STRING_EMPTY,
    val date: Long = Date().time
)

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

    override fun toString(): String {
        return super.toString()
    }
    companion object {
        @JvmStatic
        fun toEnum(value: String?): ComplianceType {
            value?.let {
                return when (it) {
                    AGGREGATE_RULE.toString() -> AGGREGATE_RULE
                    BLOCKED_FOR_COMPLIANCE_REASON.toString() -> BLOCKED_FOR_COMPLIANCE_REASON
                    CERTIFIED_KYC_REQUIRED.toString() -> CERTIFIED_KYC_REQUIRED
                    CLEARANCE_NEEDED.toString() -> CLEARANCE_NEEDED
                    CLIENT_MATCHING_OVERFLOW.toString() -> CLIENT_MATCHING_OVERFLOW
                    CLIENT_RELATION_REQUIRED.toString() -> CLIENT_RELATION_REQUIRED
                    DIGITAL_COPY_MISSING_FOR_PRIMARY_ID.toString() -> DIGITAL_COPY_MISSING_FOR_PRIMARY_ID
                    ID_MISSING_OR_EXPIRED.toString() -> ID_MISSING_OR_EXPIRED
                    ID_NOT_VERIFIED.toString() -> ID_NOT_VERIFIED
                    KYC.toString() -> KYC
                    KYC_REQUIRED.toString() -> KYC_REQUIRED
                    LETTER.toString() -> LETTER
                    MINIMUM_AGE.toString() -> MINIMUM_AGE
                    MISSING_DATA.toString() -> MISSING_DATA
                    MNI.toString() -> MNI
                    NON_FACETOFACE.toString() -> NON_FACETOFACE
                    FACE_VERIFICATION.toString() -> FACE_VERIFICATION
                    POLITICALLY_EXPOSED_PERSON.toString() -> POLITICALLY_EXPOSED_PERSON
                    PROOF_OF_DOMICILE.toString() -> PROOF_OF_DOMICILE
                    PROOF_OF_FUNDS.toString() -> PROOF_OF_FUNDS
                    PROOF_OF_OCCUPATION.toString() -> PROOF_OF_OCCUPATION
                    PURPOSE_REQUIRED.toString() -> PURPOSE_REQUIRED
                    SOURCE_REQUIRED.toString() -> SOURCE_REQUIRED
                    TAX_CODE_DOCUMENT.toString() -> TAX_CODE_DOCUMENT
                    VERIFICATION_NEEDED.toString() -> VERIFICATION_NEEDED
                    WATCHLIST.toString() -> WATCHLIST
                    WRONG_DELIVERY_METHOD.toString() -> WRONG_DELIVERY_METHOD
                    EITHER_ID_IS_MISSING_OR_EXPIRED_OR_DATE_OF_BIRTH_AND_CITY_OF_BIRTH_ARE_MISSING.toString() -> EITHER_ID_IS_MISSING_OR_EXPIRED_OR_DATE_OF_BIRTH_AND_CITY_OF_BIRTH_ARE_MISSING
                    SIMILAR_CLIENT_ON_WATCHLIST.toString() -> SIMILAR_CLIENT_ON_WATCHLIST
                    else -> OTHER
                }
            } ?: return OTHER
        }
    }
}

enum class ComplianceSubtype {
    FRONT,
    BACK,
    COMPLIANCE_FORM,
    PROOF_OF_OCCUPATION,
    PROOF_OF_DOMICILE,
    KYC_REQUIRED,
    LETTER,
    NON_FACETOFACE,
    FACE_VERIFICATION,
    PROOF_OF_FUNDS,
    CERTIFIED_KYC_REQUIRED,
    TAX_CODE_DOCUMENT,
    KYC,
    ATC,
    OTHER;

    override fun toString(): String {
        return super.toString()
    }

    companion object {
        @JvmStatic
        fun toEnum(value: String?): ComplianceSubtype {
            value?.let {
                return when (it) {
                    FRONT.toString() -> FRONT
                    BACK.toString() -> BACK
                    COMPLIANCE_FORM.toString() -> COMPLIANCE_FORM
                    PROOF_OF_OCCUPATION.toString() -> PROOF_OF_OCCUPATION
                    PROOF_OF_DOMICILE.toString() -> PROOF_OF_DOMICILE
                    KYC_REQUIRED.toString() -> KYC_REQUIRED
                    LETTER.toString() -> LETTER
                    NON_FACETOFACE.toString() -> NON_FACETOFACE
                    FACE_VERIFICATION.toString() -> FACE_VERIFICATION
                    PROOF_OF_FUNDS.toString() -> PROOF_OF_FUNDS
                    CERTIFIED_KYC_REQUIRED.toString() -> CERTIFIED_KYC_REQUIRED
                    TAX_CODE_DOCUMENT.toString() -> TAX_CODE_DOCUMENT
                    KYC.toString() -> KYC
                    ATC.toString() -> ATC
                    else -> OTHER
                }
            } ?: return OTHER
        }
    }
}

sealed class ComplianceDocDTO(
    val mtn: String = STRING_EMPTY,
    val type: ComplianceType = ComplianceType.OTHER,
    val subtype: ComplianceSubtype = ComplianceSubtype.OTHER,
    val title: String = STRING_EMPTY,
    val description: String = STRING_EMPTY
) : DocumentDTO() {

    class AggregateRule(mtn: String, subtype: ComplianceSubtype, title: String, description: String) : ComplianceDocDTO(
        mtn,
        ComplianceType.AGGREGATE_RULE,
        subtype,
        title,
        description,
    )

    class BlockedForComplianceReason(mtn: String, subtype: ComplianceSubtype, title: String, description: String) : ComplianceDocDTO(
        mtn,
        ComplianceType.BLOCKED_FOR_COMPLIANCE_REASON,
        subtype,
        title,
        description,
    )

    class CertifiedKycRequired(mtn: String, subtype: ComplianceSubtype, title: String, description: String) : ComplianceDocDTO(
        mtn,
        ComplianceType.CERTIFIED_KYC_REQUIRED,
        subtype,
        title,
        description,
    )

    class ClearanceNeeded(mtn: String, subtype: ComplianceSubtype, title: String, description: String) : ComplianceDocDTO(
        mtn,
        ComplianceType.CLEARANCE_NEEDED,
        subtype,
        title,
        description,
    )

    class ClientMatchingOverflow(mtn: String, subtype: ComplianceSubtype, title: String, description: String) : ComplianceDocDTO(
        mtn,
        ComplianceType.CLIENT_MATCHING_OVERFLOW,
        subtype,
        title,
        description,
    )

    class ClientRelationRequired(mtn: String, subtype: ComplianceSubtype, title: String, description: String) : ComplianceDocDTO(
        mtn,
        ComplianceType.CLIENT_RELATION_REQUIRED,
        subtype,
        title,
        description,
    )

    class ComplianceForm(mtn: String, subtype: ComplianceSubtype, title: String, description: String) : ComplianceDocDTO(
        mtn,
        ComplianceType.COMPLIANCE_FORM,
        subtype,
        title,
        description,
    )

    class DigitalCopyMissingForPrimaryId(mtn: String, subtype: ComplianceSubtype, title: String, description: String) : ComplianceDocDTO(
        mtn,
        ComplianceType.DIGITAL_COPY_MISSING_FOR_PRIMARY_ID,
        subtype,
        title,
        description,
    )

    class ElectronicId(mtn: String, subtype: ComplianceSubtype, title: String, description: String) : ComplianceDocDTO(
        mtn,
        ComplianceType.ELECTRONIC_ID,
        subtype,
        title,
        description,
    )

    class IdMissingOrExpired(mtn: String, subtype: ComplianceSubtype, title: String, description: String) : ComplianceDocDTO(
        mtn,
        ComplianceType.ID_MISSING_OR_EXPIRED,
        subtype,
        title,
        description,
    )

    class IdNotVerified(mtn: String, subtype: ComplianceSubtype, title: String, description: String) : ComplianceDocDTO(
        mtn,
        ComplianceType.ID_NOT_VERIFIED,
        subtype,
        title,
        description,
    )

    class Kyc(mtn: String, subtype: ComplianceSubtype, title: String, description: String) : ComplianceDocDTO(
        mtn,
        ComplianceType.KYC,
        subtype,
        title,
        description,
    )

    class KycRequired(mtn: String, subtype: ComplianceSubtype, title: String, description: String) : ComplianceDocDTO(
        mtn,
        ComplianceType.KYC_REQUIRED,
        subtype,
        title,
        description,
    )

    class Letter(mtn: String, subtype: ComplianceSubtype, title: String, description: String) : ComplianceDocDTO(
        mtn,
        ComplianceType.LETTER,
        subtype,
        title,
        description,
    )

    class MinimumAge(mtn: String, subtype: ComplianceSubtype, title: String, description: String) : ComplianceDocDTO(
        mtn,
        ComplianceType.MINIMUM_AGE,
        subtype,
        title,
        description,
    )

    class MissingData(mtn: String, subtype: ComplianceSubtype, title: String, description: String) : ComplianceDocDTO(
        mtn,
        ComplianceType.MISSING_DATA,
        subtype,
        title,
        description,
    )

    class Mni(mtn: String, subtype: ComplianceSubtype, title: String, description: String) : ComplianceDocDTO(
        mtn,
        ComplianceType.MNI,
        subtype,
        title,
        description,
    )

    class NonFacetoface(mtn: String, subtype: ComplianceSubtype, title: String, description: String) : ComplianceDocDTO(
        mtn,
        ComplianceType.NON_FACETOFACE,
        subtype,
        title,
        description,
    )

    class FaceVerification(mtn: String, subtype: ComplianceSubtype, title: String, description: String) : ComplianceDocDTO(
        mtn,
        ComplianceType.FACE_VERIFICATION,
        subtype,
        title,
        description,
    )

    class PoliticallyExposedPerson(mtn: String, subtype: ComplianceSubtype, title: String, description: String) : ComplianceDocDTO(
        mtn,
        ComplianceType.POLITICALLY_EXPOSED_PERSON,
        subtype,
        title,
        description,
    )

    class ProofOfDomicile(mtn: String, subtype: ComplianceSubtype, title: String, description: String) : ComplianceDocDTO(
        mtn,
        ComplianceType.PROOF_OF_DOMICILE,
        subtype,
        title,
        description,
    )

    class ProofOfFunds(mtn: String, subtype: ComplianceSubtype, title: String, description: String) : ComplianceDocDTO(
        mtn,
        ComplianceType.PROOF_OF_FUNDS,
        subtype,
        title,
        description,
    )

    class ProofOfOccupation(mtn: String, subtype: ComplianceSubtype, title: String, description: String) : ComplianceDocDTO(
        mtn,
        ComplianceType.PROOF_OF_OCCUPATION,
        subtype,
        title,
        description,
    )

    class PurposeRequired(mtn: String, subtype: ComplianceSubtype, title: String, description: String) : ComplianceDocDTO(
        mtn,
        ComplianceType.PURPOSE_REQUIRED,
        subtype,
        title,
        description,
    )

    class SourceRequired(mtn: String, subtype: ComplianceSubtype, title: String, description: String) : ComplianceDocDTO(
        mtn,
        ComplianceType.SOURCE_REQUIRED,
        subtype,
        title,
        description,
    )

    class TaxCodeDocument(mtn: String, subtype: ComplianceSubtype, title: String, description: String) : ComplianceDocDTO(
        mtn,
        ComplianceType.TAX_CODE_DOCUMENT,
        subtype,
        title,
        description,
    )

    class VerificationNeeded(mtn: String, subtype: ComplianceSubtype, title: String, description: String) : ComplianceDocDTO(
        mtn,
        ComplianceType.VERIFICATION_NEEDED,
        subtype,
        title,
        description,
    )

    class Watchlist(mtn: String, subtype: ComplianceSubtype, title: String, description: String) : ComplianceDocDTO(
        mtn,
        ComplianceType.WATCHLIST,
        subtype,
        title,
        description,
    )

    class WrongDeliveryMethod(mtn: String, subtype: ComplianceSubtype, title: String, description: String) : ComplianceDocDTO(
        mtn,
        ComplianceType.WRONG_DELIVERY_METHOD,
        subtype,
        title,
        description,
    )

    class EitherIdIsMissingOrExpiredOrDateOfBirthAndCityOfBirthAreMissing(mtn: String, subtype: ComplianceSubtype, title: String, description: String) : ComplianceDocDTO(
        mtn,
        ComplianceType.EITHER_ID_IS_MISSING_OR_EXPIRED_OR_DATE_OF_BIRTH_AND_CITY_OF_BIRTH_ARE_MISSING,
        subtype,
        title,
        description,
    )

    class SimilarClientOnWatchlist(mtn: String, subtype: ComplianceSubtype, title: String, description: String) : ComplianceDocDTO(
        mtn,
        ComplianceType.SIMILAR_CLIENT_ON_WATCHLIST,
        subtype,
        title,
        description,
    )

    object Other : ComplianceDocDTO()
}
