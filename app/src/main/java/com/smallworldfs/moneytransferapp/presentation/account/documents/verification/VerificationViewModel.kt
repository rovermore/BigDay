package com.smallworldfs.moneytransferapp.presentation.account.documents.verification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smallworldfs.moneytransferapp.base.presentation.viewmodel.Event
import com.smallworldfs.moneytransferapp.domain.migrated.account.documents.model.DocumentStatusDTO
import com.smallworldfs.moneytransferapp.domain.migrated.account.documents.usecase.ManageDocumentsUseCase
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error.DocumentNotFound
import com.smallworldfs.moneytransferapp.domain.migrated.base.map
import com.smallworldfs.moneytransferapp.domain.migrated.base.mapFailure
import com.smallworldfs.moneytransferapp.domain.migrated.base.then
import com.smallworldfs.moneytransferapp.presentation.account.documents.list.model.ComplianceDocUIModel
import com.smallworldfs.moneytransferapp.presentation.account.documents.list.model.DocumentUIModel
import com.smallworldfs.moneytransferapp.presentation.account.documents.list.model.MyDocumentsUIModelMapper
import com.smallworldfs.moneytransferapp.presentation.base.ErrorType
import com.smallworldfs.moneytransferapp.presentation.base.ErrorTypeMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class VerificationViewModel @Inject constructor(
    private val manageDocumentsUseCase: ManageDocumentsUseCase,
    private val verificationUIModelMapper: VerificationUIModelMapper,
    private val myDocumentsUIModelMapper: MyDocumentsUIModelMapper,
    private val errorTypeMapper: ErrorTypeMapper
) : ViewModel() {

    private val _loading: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val loading: LiveData<Event<Boolean>> = _loading

    private val _documents: MutableLiveData<Event<List<DocumentUIModel>>> = MutableLiveData()
    val documents: LiveData<Event<List<DocumentUIModel>>> get() = _documents

    private val _documentsError: MutableLiveData<Event<ErrorType>> = MutableLiveData()
    val documentsError: LiveData<Event<ErrorType>> get() = _documentsError

    private val _download: MutableLiveData<Event<File>> = MutableLiveData()
    val download: LiveData<Event<File>> = _download

    private val _launchManualUpload: MutableLiveData<Event<DocumentUIModel>> = MutableLiveData()
    val launchManualUpload: LiveData<Event<DocumentUIModel>> get() = _launchManualUpload

    private val _documentValidated: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val documentValidated: LiveData<Event<Boolean>> get() = _documentValidated

    fun requestDocuments() {
        _loading.value = Event(true)
        viewModelScope.launch(Dispatchers.IO) {
            manageDocumentsUseCase.getMandatoryDocuments()
                .map { documentList ->
                    _documents.postValue(
                        Event(
                            verificationUIModelMapper.map(documentList)
                                .sortedBy { it.status.ordinal },
                        ),
                    )
                }.mapFailure {
                    _documentsError.postValue(Event(errorTypeMapper.map(it)))
                }.then {
                    _loading.postValue(Event(false))
                }
        }
    }

    fun onDownloadDocument(item: DocumentUIModel) {
        viewModelScope.launch(Dispatchers.IO) {
            manageDocumentsUseCase.getAttachment(item.uid, if (item is ComplianceDocUIModel) item.subtype else "document")
                .map {
                    _download.postValue(Event(it))
                }
        }
    }

    fun verifyDocumentStatus(documentWithType: DocumentUIModel) {
        _loading.value = Event(true)
        viewModelScope.launch(Dispatchers.IO) {
            manageDocumentsUseCase.getDocumentById(documentWithType.uid)
                .map { document ->
                    if (document.status.id == DocumentStatusDTO.MISSING) {
                        _launchManualUpload.postValue(Event(documentWithType))
                    }
                }.mapFailure {
                    if (it is DocumentNotFound) {
                        _documentValidated.postValue(Event(true))
                    } else {
                        _documentsError.postValue(Event(errorTypeMapper.map(it)))
                    }
                }.then {
                    _loading.postValue(Event(false))
                }
        }
    }
}
