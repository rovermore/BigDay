package com.smallworldfs.moneytransferapp.data.account.documents.mappers

import com.smallworldfs.moneytransferapp.data.account.documents.model.Document
import com.smallworldfs.moneytransferapp.data.account.documents.model.GetDocumentsResponse
import com.smallworldfs.moneytransferapp.data.account.documents.model.UploadDocumentRequest
import com.smallworldfs.moneytransferapp.domain.migrated.account.documents.model.ComplianceDocDTO
import com.smallworldfs.moneytransferapp.domain.migrated.account.documents.model.ComplianceSubtype
import com.smallworldfs.moneytransferapp.domain.migrated.account.documents.model.DocumentDTO
import com.smallworldfs.moneytransferapp.domain.migrated.account.documents.model.DocumentFileDTO
import com.smallworldfs.moneytransferapp.domain.migrated.account.documents.model.DocumentIdDTO
import com.smallworldfs.moneytransferapp.domain.migrated.account.documents.model.DocumentStatusDTO
import com.smallworldfs.moneytransferapp.domain.migrated.account.documents.model.Issuer
import com.smallworldfs.moneytransferapp.domain.migrated.account.documents.model.Status
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import java.util.Date
import javax.inject.Inject

class DocumentsDTOMapper @Inject constructor() {

    private companion object {
        const val COMPLIANCE = "transactionCompliance"
        const val PASSPORT = "PASSPORT"
        const val CEDULA = "CEDULA"
        const val ID = "ID"
        const val ALIENCARD = "ALIENCARD"
        const val CREW_MEMBER_ID = "CREW_MEMBER_ID"
        const val DNI = "DNI"
        const val DRIVERS_LICENSE = "DRIVERS_LICENSE"
        const val F_N_PERMIT = "F_N_PERMIT"
        const val GREEN_CARD_USCIS = "GREEN_CARD_USCIS"
        const val LMA_CARD = "LMA_CARD"
        const val NATIONAL_ID_CARD = "NATIONAL_ID_CARD"
        const val NATURALIZATION = "NATURALIZATION"
        const val ORANGE_CARD = "ORANGE_CARD"
        const val PERMANENT_REGISTRATION_CARD = "PERMANENT_REGISTRATION_CARD"
        const val REGISTRATION_CARD = "REGISTRATION_CARD"
        const val STATE_IDENTIFICATION = "STATE_IDENTIFICATION"
        const val STATE_ID_CARD = "STATE_ID_CARD"
        const val USA_MILITARY_ID_CARD = "USA_MILITARY_ID_CARD"

        const val EXPIRED = "EXPIRED"
        const val MISSING = "MISSING"
        const val REJECTED = "REJECTED"
        const val UNDER_REVIEW = "UNDER_REVIEW"
        const val APPROVED = "APPROVED"

        const val AGGREGATE_RULE = "AGGREGATE_RULE"
        const val BLOCKED_FOR_COMPLIANCE_REASON = "BLOCKED_FOR_COMPLIANCE_REASON"
        const val CERTIFIED_KYC_REQUIRED = "CERTIFIED_KYC_REQUIRED"
        const val CLEARANCE_NEEDED = "CLEARANCE_NEEDED"
        const val CLIENT_MATCHING_OVERFLOW = "CLIENT_MATCHING_OVERFLOW"
        const val CLIENT_RELATION_REQUIRED = "CLIENT_RELATION_REQUIRED"
        const val COMPLIANCE_FORM = "COMPLIANCE_FORM"
        const val DIGITAL_COPY_MISSING_FOR_PRIMARY_ID = "DIGITAL_COPY_MISSING_FOR_PRIMARY_ID"
        const val ELECTRONIC_ID = "ELECTRONIC_ID"
        const val ID_MISSING_OR_EXPIRED = "ID_MISSING_OR_EXPIRED"
        const val ID_NOT_VERIFIED = "ID_NOT_VERIFIED"
        const val KYC = "KYC"
        const val KYC_REQUIRED = "KYC_REQUIRED"
        const val LETTER = "LETTER"
        const val MINIMUM_AGE = "MINIMUM_AGE"
        const val MISSING_DATA = "MISSING_DATA"
        const val MNI = "MNI"
        const val NON_FACETOFACE = "NON_FACETOFACE"
        const val FACE_VERIFICATION = "FACE_VERIFICATION"
        const val POLITICALLY_EXPOSED_PERSON = "POLITICALLY_EXPOSED_PERSON"
        const val PROOF_OF_DOMICILE = "PROOF_OF_DOMICILE"
        const val PROOF_OF_FUNDS = "PROOF_OF_FUNDS"
        const val PROOF_OF_OCCUPATION = "PROOF_OF_OCCUPATION"
        const val PURPOSE_REQUIRED = "PURPOSE_REQUIRED"
        const val SOURCE_REQUIRED = "SOURCE_REQUIRED"
        const val TAX_CODE_DOCUMENT = "TAX_CODE_DOCUMENT"
        const val VERIFICATION_NEEDED = "VERIFICATION_NEEDED"
        const val WATCHLIST = "WATCHLIST"
        const val WRONG_DELIVERY_METHOD = "WRONG_DELIVERY_METHOD"
        const val EITHER_ID_IS_MISSING_OR_EXPIRED_OR_DATE_OF_BIRTH_AND_CITY_OF_BIRTH_ARE_MISSING = "EITHER_ID_IS_MISSING_OR_EXPIRED_OR_DATE_OF_BIRTH_AND_CITY_OF_BIRTH_ARE_MISSING"
        const val SIMILAR_CLIENT_ON_WATCHLIST = "SIMILAR_CLIENT_ON_WATCHLIST"
    }

    fun mapGetDocumentsResponse(response: GetDocumentsResponse): List<DocumentDTO> {
        return mapDocuments(response.documents)
    }

    private fun mapDocuments(list: List<Document?>?): List<DocumentDTO> {
        val documentDTOList = mutableListOf<DocumentDTO>()
        list?.map { document ->
            document?.let { documentDTOList.add(mapDocument(it)) }
        } ?: return emptyList()
        return documentDTOList
    }

    fun mapDocument(it: Document): DocumentDTO {
        val documentDTO: DocumentDTO = when (it.documentType) {
            COMPLIANCE -> mapComplianceDocument(it)
            else -> {
                when (it.type) {
                    ID -> DocumentIdDTO.Id
                    PASSPORT -> DocumentIdDTO.Passport
                    CEDULA -> DocumentIdDTO.Cedula
                    ALIENCARD -> DocumentIdDTO.Aliencard
                    CREW_MEMBER_ID -> DocumentIdDTO.CrewMemberID
                    DNI -> DocumentIdDTO.Dni
                    DRIVERS_LICENSE -> DocumentIdDTO.DriversLicense
                    F_N_PERMIT -> DocumentIdDTO.FNPermit
                    GREEN_CARD_USCIS -> DocumentIdDTO.GreenCardUSCis
                    LMA_CARD -> DocumentIdDTO.LMACard
                    NATIONAL_ID_CARD -> DocumentIdDTO.NationalIdCard
                    NATURALIZATION -> DocumentIdDTO.Naturalization
                    ORANGE_CARD -> DocumentIdDTO.OrangeCard
                    PERMANENT_REGISTRATION_CARD -> DocumentIdDTO.PermanentRegistrationCard
                    REGISTRATION_CARD -> DocumentIdDTO.RegistrationCard
                    STATE_IDENTIFICATION -> DocumentIdDTO.StateIdentification
                    STATE_ID_CARD -> DocumentIdDTO.StateIdCard
                    USA_MILITARY_ID_CARD -> DocumentIdDTO.UsaMilitaryIdCard
                    else -> DocumentIdDTO.Other
                }
            }
        }

        with(documentDTO) {
            changeDate = it.changeDate ?: STRING_EMPTY
            clientId = it.client_id.toString() ?: STRING_EMPTY
            expirationDate = Date().time
            uid = it.uid ?: STRING_EMPTY
            status = when (it.status) {
                EXPIRED -> Status(DocumentStatusDTO.EXPIRED, it.locale?.status ?: STRING_EMPTY)
                MISSING -> Status(DocumentStatusDTO.MISSING, it.locale?.status ?: STRING_EMPTY)
                REJECTED -> Status(DocumentStatusDTO.REJECTED, it.locale?.status ?: STRING_EMPTY)
                UNDER_REVIEW -> Status(DocumentStatusDTO.UNDER_REVIEW, it.locale?.status ?: STRING_EMPTY)
                APPROVED -> Status(DocumentStatusDTO.APPROVED, it.locale?.status ?: STRING_EMPTY)
                else -> Status(DocumentStatusDTO.UNDEFINED, it.locale?.status ?: STRING_EMPTY)
            }
            block = it.block ?: false
            upload = it.upload ?: false
            updatedAt = it.updatedAt ?: STRING_EMPTY
            current = it.current ?: STRING_EMPTY
            doc = it.doc ?: STRING_EMPTY
            number = it.number ?: STRING_EMPTY
            issuer = Issuer(
                "iso3",
                it.locale?.issueCountry ?: STRING_EMPTY,
                "stateName",
                it.issueCountry ?: STRING_EMPTY,
                100L,
            )
            name = it.locale?.type ?: STRING_EMPTY
        }
        return documentDTO
    }

    private fun mapComplianceDocument(document: Document): ComplianceDocDTO {
        val mtn = document.mtn?.toString() ?: STRING_EMPTY
        val subtype = ComplianceSubtype.toEnum(document.subtype)
        val title = document.locale?.title ?: STRING_EMPTY
        val description = document.locale?.description ?: STRING_EMPTY
        return when (document.type) {
            AGGREGATE_RULE -> ComplianceDocDTO.AggregateRule(mtn, subtype, title, description)
            BLOCKED_FOR_COMPLIANCE_REASON -> ComplianceDocDTO.BlockedForComplianceReason(mtn, subtype, title, description)
            CERTIFIED_KYC_REQUIRED -> ComplianceDocDTO.CertifiedKycRequired(mtn, subtype, title, description)
            CLEARANCE_NEEDED -> ComplianceDocDTO.ClearanceNeeded(mtn, subtype, title, description)
            CLIENT_MATCHING_OVERFLOW -> ComplianceDocDTO.ClientMatchingOverflow(mtn, subtype, title, description)
            CLIENT_RELATION_REQUIRED -> ComplianceDocDTO.ClientRelationRequired(mtn, subtype, title, description)
            COMPLIANCE_FORM -> ComplianceDocDTO.ComplianceForm(mtn, subtype, title, description)
            DIGITAL_COPY_MISSING_FOR_PRIMARY_ID -> ComplianceDocDTO.DigitalCopyMissingForPrimaryId(mtn, subtype, title, description)
            ELECTRONIC_ID -> ComplianceDocDTO.ElectronicId(mtn, subtype, title, description)
            ID_MISSING_OR_EXPIRED -> ComplianceDocDTO.IdMissingOrExpired(mtn, subtype, title, description)
            ID_NOT_VERIFIED -> ComplianceDocDTO.IdNotVerified(mtn, subtype, title, description)
            KYC -> ComplianceDocDTO.Kyc(mtn, subtype, title, description)
            KYC_REQUIRED -> ComplianceDocDTO.KycRequired(mtn, subtype, title, description)
            LETTER -> ComplianceDocDTO.Letter(mtn, subtype, title, description)
            MINIMUM_AGE -> ComplianceDocDTO.MinimumAge(mtn, subtype, title, description)
            MISSING_DATA -> ComplianceDocDTO.MissingData(mtn, subtype, title, description)
            MNI -> ComplianceDocDTO.Mni(mtn, subtype, title, description)
            NON_FACETOFACE -> ComplianceDocDTO.NonFacetoface(mtn, subtype, title, description)
            FACE_VERIFICATION -> ComplianceDocDTO.FaceVerification(mtn, subtype, title, description)
            POLITICALLY_EXPOSED_PERSON -> ComplianceDocDTO.PoliticallyExposedPerson(mtn, subtype, title, description)
            PROOF_OF_DOMICILE -> ComplianceDocDTO.ProofOfDomicile(mtn, subtype, title, description)
            PROOF_OF_FUNDS -> ComplianceDocDTO.ProofOfFunds(mtn, subtype, title, description)
            PROOF_OF_OCCUPATION -> ComplianceDocDTO.ProofOfOccupation(mtn, subtype, title, description)
            PURPOSE_REQUIRED -> ComplianceDocDTO.PurposeRequired(mtn, subtype, title, description)
            SOURCE_REQUIRED -> ComplianceDocDTO.SourceRequired(mtn, subtype, title, description)
            TAX_CODE_DOCUMENT -> ComplianceDocDTO.TaxCodeDocument(mtn, subtype, title, description)
            VERIFICATION_NEEDED -> ComplianceDocDTO.VerificationNeeded(mtn, subtype, title, description)
            WATCHLIST -> ComplianceDocDTO.Watchlist(mtn, subtype, title, description)
            WRONG_DELIVERY_METHOD -> ComplianceDocDTO.WrongDeliveryMethod(mtn, subtype, title, description)
            EITHER_ID_IS_MISSING_OR_EXPIRED_OR_DATE_OF_BIRTH_AND_CITY_OF_BIRTH_ARE_MISSING -> ComplianceDocDTO.EitherIdIsMissingOrExpiredOrDateOfBirthAndCityOfBirthAreMissing(mtn, subtype, title, description)
            else -> ComplianceDocDTO.SimilarClientOnWatchlist(mtn, subtype, title, description)
        }
    }

    fun mapUploadDocumentsRequest(uuid: String, userToken: String, documentFileDTO: DocumentFileDTO) =
        UploadDocumentRequest(
            uuid,
            userToken,
            documentFileDTO.document,
            documentFileDTO.numberDocument,
            documentFileDTO.country,
            documentFileDTO.expirationDate.toString(),
            documentFileDTO.type,
            documentFileDTO.idIssueCountry,
            documentFileDTO.issueDate.toString(),
            documentFileDTO.complianceType,
            documentFileDTO.mtn,
            documentFileDTO.documentType,
            documentFileDTO.front,
            documentFileDTO.back,
            documentFileDTO.uid,
            documentFileDTO.taxCode,
            documentFileDTO.userIdType,
        )
}
