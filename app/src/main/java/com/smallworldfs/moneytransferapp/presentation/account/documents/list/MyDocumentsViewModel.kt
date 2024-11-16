package com.smallworldfs.moneytransferapp.presentation.account.documents.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smallworldfs.moneytransferapp.base.presentation.viewmodel.Event
import com.smallworldfs.moneytransferapp.domain.migrated.account.documents.model.DocumentStatusDTO
import com.smallworldfs.moneytransferapp.domain.migrated.account.documents.usecase.ManageDocumentsUseCase
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.map
import com.smallworldfs.moneytransferapp.domain.migrated.base.mapFailure
import com.smallworldfs.moneytransferapp.domain.migrated.base.then
import com.smallworldfs.moneytransferapp.presentation.account.documents.list.model.DocumentUIModel
import com.smallworldfs.moneytransferapp.presentation.account.documents.list.model.MyDocumentsUIModel
import com.smallworldfs.moneytransferapp.presentation.account.documents.list.model.MyDocumentsUIModelMapper
import com.smallworldfs.moneytransferapp.presentation.base.ErrorType
import com.smallworldfs.moneytransferapp.presentation.base.ErrorTypeMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MyDocumentsViewModel @Inject constructor(
    private val manageDocumentsUseCase: ManageDocumentsUseCase,
    private val myDocumentsUIModelMapper: MyDocumentsUIModelMapper,
    private val errorTypeMapper: ErrorTypeMapper
) : ViewModel() {

    private val _myDocuments: MutableLiveData<Event<MyDocumentsUIModel>> = MutableLiveData()
    val myDocuments: LiveData<Event<MyDocumentsUIModel>> get() = _myDocuments

    private val _complianceFile: MutableLiveData<Event<File>> = MutableLiveData()
    val complianceFile: LiveData<Event<File>> get() = _complianceFile

    private val _isLoading: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val isLoading: LiveData<Event<Boolean>> get() = _isLoading

    private val _documentsError: MutableLiveData<Event<ErrorType>> = MutableLiveData()
    val documentsError: LiveData<Event<ErrorType>> get() = _documentsError

    private val _launchManualUpload: MutableLiveData<Event<DocumentUIModel>> = MutableLiveData()
    val launchManualUpload: LiveData<Event<DocumentUIModel>> get() = _launchManualUpload

    private val _documentValidated: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val documentValidated: LiveData<Event<Boolean>> get() = _documentValidated

    fun getDocuments() {
        _isLoading.value = Event(true)
        viewModelScope.launch(Dispatchers.IO) {
            manageDocumentsUseCase.getDocuments()
                .map {
                    _myDocuments.postValue(Event(myDocumentsUIModelMapper.map(it)))
                }.mapFailure {
                    _documentsError.postValue(Event(errorTypeMapper.map(it)))
                }.then {
                    _isLoading.postValue(Event(false))
                }
        }
    }

    fun getAttachmentById(id: String, subtype: String) {
        _isLoading.value = Event(true)
        viewModelScope.launch(Dispatchers.IO) {
            manageDocumentsUseCase.getAttachment(id, subtype)
                .map {
                    _complianceFile.postValue(Event(it))
                }.then {
                    _isLoading.postValue(Event(false))
                }
        }
    }

    fun verifyDocumentStatus(documentWithType: DocumentUIModel) {
        _isLoading.value = Event(true)
        viewModelScope.launch(Dispatchers.IO) {
            manageDocumentsUseCase.getDocumentById(documentWithType.uid)
                .map { document ->
                    if (document.status.id == DocumentStatusDTO.MISSING) {
                        _launchManualUpload.postValue(Event(documentWithType))
                    }
                }.mapFailure {
                    if (it is Error.DocumentNotFound) {
                        _documentValidated.postValue(Event(true))
                    } else {
                        _documentsError.postValue(Event(errorTypeMapper.map(it)))
                    }
                }.then {
                    _isLoading.postValue(Event(false))
                }
        }
    }
}
