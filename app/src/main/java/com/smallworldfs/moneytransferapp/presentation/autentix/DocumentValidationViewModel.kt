package com.smallworldfs.moneytransferapp.presentation.autentix

import android.webkit.JavascriptInterface
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smallworldfs.moneytransferapp.base.presentation.viewmodel.Event
import com.smallworldfs.moneytransferapp.domain.migrated.autentix.usecase.DocumentValidationUseCase
import com.smallworldfs.moneytransferapp.domain.migrated.base.map
import com.smallworldfs.moneytransferapp.domain.migrated.base.mapFailure
import com.smallworldfs.moneytransferapp.presentation.autentix.model.AutentixMessageUIModel
import com.smallworldfs.moneytransferapp.presentation.autentix.model.AutentixMessageUIModelMapper
import com.smallworldfs.moneytransferapp.presentation.autentix.model.AutentixViewConfig
import com.smallworldfs.moneytransferapp.presentation.autentix.model.JSInterface
import com.smallworldfs.moneytransferapp.presentation.autentix.model.LocalStorageJavaScriptInterface
import com.smallworldfs.moneytransferapp.presentation.autentix.model.MessageMapper
import com.smallworldfs.moneytransferapp.presentation.base.ErrorType
import com.smallworldfs.moneytransferapp.presentation.base.ErrorTypeMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DocumentValidationViewModel @Inject constructor(
    private val documentValidationUseCase: DocumentValidationUseCase,
    private val errorTypeMapper: ErrorTypeMapper,
    private val localStorageJavaScriptInterface: LocalStorageJavaScriptInterface,
    private val messageMapper: MessageMapper,
    private val autentixMessageUIModelMapper: AutentixMessageUIModelMapper,
) : ViewModel() {

    private val autentixJSInterface = object : JSInterface {

        @JavascriptInterface
        override fun toString(): String = "webview"

        @JavascriptInterface
        override fun postMessage(json: String, origin: String) {
            messageMapper.handleEvent(json)
                .map {
                    _onAutentixEvent.postValue(Event(autentixMessageUIModelMapper.map(it)))
                }.mapFailure {
                    _onError.postValue(Event(errorTypeMapper.map(it)))
                }
        }
    }

    private val _autentixViewConfig: MutableLiveData<Event<AutentixViewConfig>> = MutableLiveData()
    var autentixViewConfig: LiveData<Event<AutentixViewConfig>> = _autentixViewConfig

    private val _onError: MutableLiveData<Event<ErrorType>> = MutableLiveData()
    var onError: LiveData<Event<ErrorType>> = _onError

    private val _onAutentixEvent: MutableLiveData<Event<AutentixMessageUIModel>> = MutableLiveData()
    var onAutentixEvent: LiveData<Event<AutentixMessageUIModel>> = _onAutentixEvent

    private val _autentixSessionStatus: MutableLiveData<Event<Unit>> = MutableLiveData()
    var autentixSessionStatus: LiveData<Event<Unit>> = _autentixSessionStatus

    fun startSession(faceCompare: Boolean, documentType: String) {
        viewModelScope.launch(Dispatchers.IO) {
            documentValidationUseCase.getAutentixSessionURL(faceCompare, documentType)
                .map {
                    _autentixViewConfig.postValue(
                        Event(
                            AutentixViewConfig(it.url, it.timeout, it.externalId, localStorageJavaScriptInterface, autentixJSInterface)
                        )
                    )
                }.mapFailure {
                    _onError.postValue(Event(errorTypeMapper.map(it)))
                }
        }
    }

    fun checkAutentixSessionStatus(timeout: Long, externalId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            documentValidationUseCase.checkAutentixSessionStatus(externalId, timeout)
                .map {
                    _autentixSessionStatus.postValue(Event(Unit))
                }.mapFailure {
                    _onError.postValue(Event(errorTypeMapper.map(it)))
                }
        }
    }
}
