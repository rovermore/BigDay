package com.smallworldfs.moneytransferapp.presentation.account.documents.list.model

import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.domain.migrated.account.documents.model.ComplianceDocDTO
import com.smallworldfs.moneytransferapp.domain.migrated.account.documents.model.ComplianceType
import com.smallworldfs.moneytransferapp.domain.migrated.account.documents.model.DocumentDTO
import com.smallworldfs.moneytransferapp.domain.migrated.account.documents.model.DocumentIdDTO
import com.smallworldfs.moneytransferapp.domain.migrated.account.documents.model.DocumentStatusDTO
import com.smallworldfs.moneytransferapp.utils.DateFormatter
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import com.smallworldfs.moneytransferapp.utils.StringResolver
import javax.inject.Inject

class DocumentUIModelMapper @Inject constructor(
    private val stringResolver: StringResolver,
    private val dateFormatter: DateFormatter
) {

    fun map(documents: List<DocumentDTO>): List<DocumentUIModel> {
        val result = mutableListOf<DocumentUIModel>()

        val ids = (documents.filterIsInstance<DocumentIdDTO>()).map { mapIdDocument(it) }
        val compliance = (documents.filterIsInstance<ComplianceDocDTO>()).map { mapComplianceDocument(it) }.toMutableList()

        val idMissingOrExpiredDoc = compliance.firstOrNull { it.type == ComplianceDocUIModel.ComplianceType.ID_MISSING_OR_EXPIRED }
        val faceVerification = compliance.firstOrNull { it.type == ComplianceDocUIModel.ComplianceType.FACE_VERIFICATION }

        if (idMissingOrExpiredDoc != null && faceVerification != null) {
            val fullValidationCompliance = mapFullValidationCompliance(idMissingOrExpiredDoc)
            compliance.remove(idMissingOrExpiredDoc)
            compliance.remove(faceVerification)
            compliance.add(fullValidationCompliance)
        }

        result.addAll(ids)
        result.addAll(compliance)

        return result
    }

    private fun mapIdDocument(documentIdDTO: DocumentIdDTO): DocumentIdUIModel {
        val document = when (documentIdDTO) {
            is DocumentIdDTO.Passport -> DocumentIdUIModel.Passport
            is DocumentIdDTO.Id -> DocumentIdUIModel.Id
            is DocumentIdDTO.Cedula -> DocumentIdUIModel.Cedula
            is DocumentIdDTO.Aliencard -> DocumentIdUIModel.Aliencard
            is DocumentIdDTO.CrewMemberID -> DocumentIdUIModel.CrewMemberID
            is DocumentIdDTO.Dni -> DocumentIdUIModel.Dni
            is DocumentIdDTO.DriversLicense -> DocumentIdUIModel.DriversLicense
            is DocumentIdDTO.FNPermit -> DocumentIdUIModel.FNPermit
            is DocumentIdDTO.GreenCardUSCis -> DocumentIdUIModel.GreenCardUSCis
            is DocumentIdDTO.LMACard -> DocumentIdUIModel.LMACard
            is DocumentIdDTO.NationalIdCard -> DocumentIdUIModel.NationalIdCard
            is DocumentIdDTO.Naturalization -> DocumentIdUIModel.Naturalization
            is DocumentIdDTO.OrangeCard -> DocumentIdUIModel.OrangeCard
            is DocumentIdDTO.PermanentRegistrationCard -> DocumentIdUIModel.PermanentRegistrationCard
            is DocumentIdDTO.RegistrationCard -> DocumentIdUIModel.RegistrationCard
            is DocumentIdDTO.StateIdentification -> DocumentIdUIModel.StateIdentification
            is DocumentIdDTO.StateIdCard -> DocumentIdUIModel.StateIdCard
            is DocumentIdDTO.UsaMilitaryIdCard -> DocumentIdUIModel.UsaMilitaryIdCard
            else -> DocumentIdUIModel.Other
        }

        document.apply {
            uid = documentIdDTO.uid
            status = mapStatus(documentIdDTO.status.id)
            statusText = documentIdDTO.status.name
            block = documentIdDTO.block
            upload = documentIdDTO.upload
            buttons = mapDocumentButtonsUIModel(documentIdDTO)
            number = documentIdDTO.number
            name = documentIdDTO.name
            expirationDate = dateFormatter.getSimpleDateString(documentIdDTO.expirationDate)
        }

        return document
    }

    private fun mapComplianceDocument(complianceDocDTO: ComplianceDocDTO) =
        ComplianceDocUIModel().apply {
            subtype = complianceDocDTO.subtype.toString()
            doc = complianceDocDTO.doc
            uid = complianceDocDTO.uid
            mtn = complianceDocDTO.mtn
            status = mapStatus(complianceDocDTO.status.id)
            description = complianceDocDTO.description
            title = complianceDocDTO.title
            type = mapDocumentType(complianceDocDTO.type)
            block = complianceDocDTO.block
            upload = complianceDocDTO.upload
            buttons = mapComplianceButtons(complianceDocDTO)
            name = complianceDocDTO.name
            expirationDate = dateFormatter.getSimpleDateString(complianceDocDTO.expirationDate)
        }

    private fun mapDocumentType(type: ComplianceType): ComplianceDocUIModel.ComplianceType = when (type) {
        ComplianceType.AGGREGATE_RULE -> ComplianceDocUIModel.ComplianceType.AGGREGATE_RULE
        ComplianceType.BLOCKED_FOR_COMPLIANCE_REASON -> ComplianceDocUIModel.ComplianceType.BLOCKED_FOR_COMPLIANCE_REASON
        ComplianceType.CERTIFIED_KYC_REQUIRED -> ComplianceDocUIModel.ComplianceType.CERTIFIED_KYC_REQUIRED
        ComplianceType.CLEARANCE_NEEDED -> ComplianceDocUIModel.ComplianceType.CLEARANCE_NEEDED
        ComplianceType.CLIENT_MATCHING_OVERFLOW -> ComplianceDocUIModel.ComplianceType.CLIENT_MATCHING_OVERFLOW
        ComplianceType.CLIENT_RELATION_REQUIRED -> ComplianceDocUIModel.ComplianceType.CLIENT_RELATION_REQUIRED
        ComplianceType.COMPLIANCE_FORM -> ComplianceDocUIModel.ComplianceType.COMPLIANCE_FORM
        ComplianceType.DIGITAL_COPY_MISSING_FOR_PRIMARY_ID -> ComplianceDocUIModel.ComplianceType.DIGITAL_COPY_MISSING_FOR_PRIMARY_ID
        ComplianceType.ELECTRONIC_ID -> ComplianceDocUIModel.ComplianceType.ELECTRONIC_ID
        ComplianceType.ID_MISSING_OR_EXPIRED -> ComplianceDocUIModel.ComplianceType.ID_MISSING_OR_EXPIRED
        ComplianceType.ID_NOT_VERIFIED -> ComplianceDocUIModel.ComplianceType.ID_NOT_VERIFIED
        ComplianceType.KYC -> ComplianceDocUIModel.ComplianceType.KYC
        ComplianceType.KYC_REQUIRED -> ComplianceDocUIModel.ComplianceType.KYC_REQUIRED
        ComplianceType.LETTER -> ComplianceDocUIModel.ComplianceType.LETTER
        ComplianceType.MINIMUM_AGE -> ComplianceDocUIModel.ComplianceType.MINIMUM_AGE
        ComplianceType.MISSING_DATA -> ComplianceDocUIModel.ComplianceType.MISSING_DATA
        ComplianceType.MNI -> ComplianceDocUIModel.ComplianceType.MNI
        ComplianceType.NON_FACETOFACE -> ComplianceDocUIModel.ComplianceType.NON_FACETOFACE
        ComplianceType.FACE_VERIFICATION -> ComplianceDocUIModel.ComplianceType.FACE_VERIFICATION
        ComplianceType.POLITICALLY_EXPOSED_PERSON -> ComplianceDocUIModel.ComplianceType.POLITICALLY_EXPOSED_PERSON
        ComplianceType.PROOF_OF_DOMICILE -> ComplianceDocUIModel.ComplianceType.PROOF_OF_DOMICILE
        ComplianceType.PROOF_OF_FUNDS -> ComplianceDocUIModel.ComplianceType.PROOF_OF_FUNDS
        ComplianceType.PROOF_OF_OCCUPATION -> ComplianceDocUIModel.ComplianceType.PROOF_OF_OCCUPATION
        ComplianceType.PURPOSE_REQUIRED -> ComplianceDocUIModel.ComplianceType.PURPOSE_REQUIRED
        ComplianceType.SOURCE_REQUIRED -> ComplianceDocUIModel.ComplianceType.SOURCE_REQUIRED
        ComplianceType.TAX_CODE_DOCUMENT -> ComplianceDocUIModel.ComplianceType.TAX_CODE_DOCUMENT
        ComplianceType.VERIFICATION_NEEDED -> ComplianceDocUIModel.ComplianceType.VERIFICATION_NEEDED
        ComplianceType.WATCHLIST -> ComplianceDocUIModel.ComplianceType.WATCHLIST
        ComplianceType.WRONG_DELIVERY_METHOD -> ComplianceDocUIModel.ComplianceType.WRONG_DELIVERY_METHOD
        ComplianceType.EITHER_ID_IS_MISSING_OR_EXPIRED_OR_DATE_OF_BIRTH_AND_CITY_OF_BIRTH_ARE_MISSING -> ComplianceDocUIModel.ComplianceType.EITHER_ID_IS_MISSING_OR_EXPIRED_OR_DATE_OF_BIRTH_AND_CITY_OF_BIRTH_ARE_MISSING
        ComplianceType.SIMILAR_CLIENT_ON_WATCHLIST -> ComplianceDocUIModel.ComplianceType.SIMILAR_CLIENT_ON_WATCHLIST
        ComplianceType.OTHER -> ComplianceDocUIModel.ComplianceType.OTHER
    }

    private fun mapFullValidationCompliance(complianceDocUIModel: ComplianceDocUIModel) =
        ComplianceDocUIModel.FullValidationUIModel().apply {
            subtype = complianceDocUIModel.subtype
            doc = complianceDocUIModel.doc
            uid = complianceDocUIModel.uid
            mtn = complianceDocUIModel.mtn
            status = complianceDocUIModel.status
            description = complianceDocUIModel.description
            title = complianceDocUIModel.title
            type = complianceDocUIModel.type
            block = complianceDocUIModel.block
            upload = complianceDocUIModel.upload
            buttons = complianceDocUIModel.buttons
            name = complianceDocUIModel.name
            expirationDate = complianceDocUIModel.expirationDate
        }

    private fun mapStatus(statusDTO: DocumentStatusDTO): DocumentUIModel.DocumentStatus {
        return when (statusDTO) {
            DocumentStatusDTO.MISSING -> DocumentUIModel.DocumentStatus.MISSING
            DocumentStatusDTO.EXPIRED -> DocumentUIModel.DocumentStatus.EXPIRED
            DocumentStatusDTO.REJECTED -> DocumentUIModel.DocumentStatus.REJECTED
            DocumentStatusDTO.UNDER_REVIEW -> DocumentUIModel.DocumentStatus.UNDER_REVIEW
            DocumentStatusDTO.APPROVED -> DocumentUIModel.DocumentStatus.APPROVED
            else -> DocumentUIModel.DocumentStatus.UNDEFINED
        }
    }

    private fun mapDocumentButtonsUIModel(documentIdDTO: DocumentIdDTO): List<DocumentButtonUIModel> {
        val documentList = mutableListOf<DocumentButtonUIModel>()
        val documentButtonUIModel = DocumentButtonUIModel(text = documentIdDTO.status.name, url = documentIdDTO.buttons.getOrNull(0)?.url ?: STRING_EMPTY)
        when (documentIdDTO.status.id) {
            DocumentStatusDTO.MISSING,
            DocumentStatusDTO.UNDEFINED -> documentButtonUIModel.apply { color = R.color.document_pink }
            DocumentStatusDTO.EXPIRED,
            DocumentStatusDTO.REJECTED -> documentButtonUIModel.apply { color = R.color.document_red }
            DocumentStatusDTO.UNDER_REVIEW -> documentButtonUIModel.apply { color = R.color.document_blue_dark }
            DocumentStatusDTO.APPROVED -> documentButtonUIModel.apply { color = R.color.document_green }
        }
        documentList.add(documentButtonUIModel)
        return documentList
    }

    private fun mapComplianceButtons(complianceDocDTO: ComplianceDocDTO): List<DocumentButtonUIModel> {
        val complianceButtonsUIModelList = mutableListOf<DocumentButtonUIModel>()
        val buttons = complianceDocDTO.buttons
        val doc = complianceDocDTO.doc
        val statusName = complianceDocDTO.status.name
        val status = complianceDocDTO.status.id
        val block = complianceDocDTO.block
        val upload = complianceDocDTO.upload
        when (status) {
            DocumentStatusDTO.MISSING -> {
                if (doc.isEmpty()) {
                    if (block && !upload) {
                        complianceButtonsUIModelList.add(
                            DocumentButtonUIModel(
                                text = buttons.getOrNull(0)?.text ?: STRING_EMPTY,
                                color = R.color.document_blue
                            )
                        )
                        complianceButtonsUIModelList.add(
                            DocumentButtonUIModel(
                                text = buttons.getOrNull(1)?.text ?: STRING_EMPTY,
                                color = R.color.document_blue
                            )
                        )
                    } else {
                        complianceButtonsUIModelList.add(
                            DocumentButtonUIModel(
                                text = stringResolver.getStringFromId(R.string.documentsListAdapterUploadText),
                                color = R.color.document_blue
                            )
                        )
                    }
                } else {
                    complianceButtonsUIModelList.add(
                        DocumentButtonUIModel(
                            text = stringResolver.getStringFromId(R.string.documentsListAdapterDownloadText),
                            color = R.color.document_purple
                        )
                    )
                    complianceButtonsUIModelList.add(
                        DocumentButtonUIModel(
                            text = stringResolver.getStringFromId(R.string.documentsListAdapterUploadText),
                            color = R.color.document_blue
                        )
                    )
                }
            }
            DocumentStatusDTO.REJECTED -> {
                complianceButtonsUIModelList.add(
                    DocumentButtonUIModel(
                        text = statusName,
                        color = R.color.document_red
                    )
                )
            }
            DocumentStatusDTO.UNDER_REVIEW -> {
                complianceButtonsUIModelList.add(
                    DocumentButtonUIModel(
                        text = statusName,
                        color = R.color.document_blue_dark
                    )
                )
            }
            DocumentStatusDTO.APPROVED -> {
                complianceButtonsUIModelList.add(
                    DocumentButtonUIModel(
                        text = statusName,
                        color = R.color.document_green
                    )
                )
            }
            else -> {}
        }
        return complianceButtonsUIModelList
    }
}
