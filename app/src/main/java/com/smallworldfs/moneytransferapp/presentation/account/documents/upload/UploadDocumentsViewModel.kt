package com.smallworldfs.moneytransferapp.presentation.account.documents.upload

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smallworldfs.moneytransferapp.base.presentation.viewmodel.Event
import com.smallworldfs.moneytransferapp.domain.migrated.account.documents.model.DocumentFileDTO
import com.smallworldfs.moneytransferapp.domain.migrated.account.documents.usecase.UploadDocumentsUseCase
import com.smallworldfs.moneytransferapp.domain.migrated.base.map
import com.smallworldfs.moneytransferapp.domain.migrated.base.mapFailure
import com.smallworldfs.moneytransferapp.domain.migrated.base.then
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field
import com.smallworldfs.moneytransferapp.presentation.account.documents.list.model.ComplianceDocUIModel
import com.smallworldfs.moneytransferapp.presentation.account.documents.list.model.DocumentUIModel
import com.smallworldfs.moneytransferapp.presentation.account.profile.edit.model.FormUIModel
import com.smallworldfs.moneytransferapp.presentation.base.ErrorType
import com.smallworldfs.moneytransferapp.presentation.base.ErrorTypeMapper
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class UploadDocumentsViewModel @Inject constructor(
    private val uploadDocumentsUseCase: UploadDocumentsUseCase,
    private val errorTypeMapper: ErrorTypeMapper
) : ViewModel() {

    private val _permissionsGranted: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val permissionsGranted: LiveData<Event<Boolean>> get() = _permissionsGranted

    private val _form: MutableLiveData<Event<FormUIModel>> = MutableLiveData()
    val form: LiveData<Event<FormUIModel>> get() = _form

    private val _documentSaved: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val documentSaved: LiveData<Event<Boolean>> get() = _documentSaved

    private val _showProgressBar: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val showProgressBar: LiveData<Event<Boolean>> get() = _showProgressBar

    private val _onPermissionsError: MutableLiveData<Event<ErrorType>> = MutableLiveData()
    val onPermissionsError: LiveData<Event<ErrorType>> get() = _onPermissionsError

    private val _documentFormError: MutableLiveData<Event<ErrorType>> = MutableLiveData()
    val documentFormError: LiveData<Event<ErrorType>> get() = _documentFormError

    private val _saveDocumentError: MutableLiveData<Event<ErrorType>> = MutableLiveData()
    val saveDocumentError: LiveData<Event<ErrorType>> get() = _saveDocumentError

    fun checkReadWritePermissions() {
        _showProgressBar.value = Event(true)
        viewModelScope.launch(Dispatchers.IO) {
            uploadDocumentsUseCase.checkReadWritePermissions()
                .map {
                    _permissionsGranted.postValue(Event(true))
                }.mapFailure {
                    _onPermissionsError.postValue(Event(ErrorType.PermissionsNotGranted))
                }
        }
    }

    fun getDocumentForm(documentID: String, documentType: String) {
        _showProgressBar.value = Event(true)
        viewModelScope.launch(Dispatchers.IO) {
            uploadDocumentsUseCase.getDocumentForm(documentID, documentType)
                .map {
                    _form.postValue(Event(FormUIModel(it)))
                }.mapFailure {
                    _documentFormError.postValue(Event(errorTypeMapper.map(it)))
                }.then {
                    _showProgressBar.postValue(Event(false))
                }
        }
    }

    fun saveDocument(document: DocumentUIModel, formData: List<Field>) {
        _showProgressBar.value = Event(true)
        var expirationDate: Date? = null
        var issueDate: Date? = null
        try {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy")
            expirationDate = dateFormat.parse(formData.firstOrNull { it.name == UploadDocumentsActivity.EXPIRATION_DATE }?.value ?: STRING_EMPTY)
            issueDate = dateFormat.parse(formData.firstOrNull { it.name == UploadDocumentsActivity.ISSUE_DATE }?.value ?: STRING_EMPTY)
        } catch (_: ParseException) {}

        val countrySelected = formData.firstOrNull { it.name == UploadDocumentsActivity.ISSUE_COUNTRY }?.value ?: STRING_EMPTY

        val iso3Selected = formData
            .firstOrNull {
                it.name == UploadDocumentsActivity.ISSUE_COUNTRY
            }?.data?.firstOrNull {
                (it.firstEntry()?.value ?: STRING_EMPTY) == countrySelected
            }?.firstKey() ?: STRING_EMPTY

        val documentFileDTO = DocumentFileDTO(
            numberDocument = formData.firstOrNull { it.name == UploadDocumentsActivity.DOCUMENT_NUMBER }?.value ?: STRING_EMPTY,
            expirationDate = expirationDate?.time ?: 0L,
            issueDate = issueDate?.time ?: 0L,
            idIssueCountry = iso3Selected,
            front = formData.firstOrNull { it.name == UploadDocumentsActivity.FRONT }?.value ?: STRING_EMPTY,
            back = formData.firstOrNull { it.name == UploadDocumentsActivity.BACK }?.value ?: STRING_EMPTY,
            document = when (document) {
                is ComplianceDocUIModel -> document.type.toString()
                else -> STRING_EMPTY
            }
        )
        viewModelScope.launch(Dispatchers.IO) {
            uploadDocumentsUseCase.saveDocument(
                when (document) {
                    is ComplianceDocUIModel -> documentFileDTO.copy(
                        documentType = "transactionCompliance",
                        complianceType = document.subtype,
                        mtn = document.mtn,
                        uid = document.uid,
                        taxCode = formData.firstOrNull { it.name == UploadDocumentsActivity.TAX_CODE }?.value ?: STRING_EMPTY,
                        userIdType = if (document.type == ComplianceDocUIModel.ComplianceType.ID_MISSING_OR_EXPIRED) document.documentTypeSelected else STRING_EMPTY
                    )
                    else -> documentFileDTO.copy(documentType = "userId")
                }
            ).map {
                _documentSaved.postValue(Event(true))
            }.mapFailure {
                _saveDocumentError.postValue(Event(errorTypeMapper.map(it)))
            }.then {
                _showProgressBar.postValue(Event(false))
            }
        }
    }
}
