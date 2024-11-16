package com.smallworldfs.moneytransferapp.presentation.account.documents.selector

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smallworldfs.moneytransferapp.base.presentation.viewmodel.Event
import com.smallworldfs.moneytransferapp.domain.migrated.account.documents.usecase.ManageDocumentsUseCase
import com.smallworldfs.moneytransferapp.domain.migrated.base.map
import com.smallworldfs.moneytransferapp.presentation.account.documents.list.model.TypesOfDocumentUIModel
import com.smallworldfs.moneytransferapp.presentation.account.documents.list.model.TypesOfDocumentUIModelMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DocumentsSelectorViewModel @Inject constructor(
    private val manageDocumentsUseCase: ManageDocumentsUseCase,
    private val typesOfDocumentUIModelMapper: TypesOfDocumentUIModelMapper
) : ViewModel() {

    private val _documentTypes: MutableLiveData<Event<List<TypesOfDocumentUIModel>>> = MutableLiveData()
    val documentTypes: LiveData<Event<List<TypesOfDocumentUIModel>>> = _documentTypes

    fun getDocumentTypes() {
        viewModelScope.launch(Dispatchers.IO) {
            manageDocumentsUseCase.getDocumentTypes()
                .map {
                    _documentTypes.postValue(
                        Event(
                            typesOfDocumentUIModelMapper.map(it)
                        )
                    )
                }
        }
    }
}
