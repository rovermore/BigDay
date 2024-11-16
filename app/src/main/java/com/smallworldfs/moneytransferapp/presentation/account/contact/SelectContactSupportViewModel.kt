package com.smallworldfs.moneytransferapp.presentation.account.contact

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smallworldfs.moneytransferapp.domain.migrated.account.contactsupport.usecase.ContactSupportUseCase
import com.smallworldfs.moneytransferapp.domain.migrated.base.map
import com.smallworldfs.moneytransferapp.domain.migrated.base.mapFailure
import com.smallworldfs.moneytransferapp.modules.support.model.ContactSupportInfoUIModel
import com.smallworldfs.moneytransferapp.modules.support.model.ContactSupportInfoUIModelMapper
import com.smallworldfs.moneytransferapp.presentation.base.ErrorType
import com.smallworldfs.moneytransferapp.presentation.base.ErrorTypeMapper
import com.smallworldfs.moneytransferapp.presentation.login.model.UserUIModel
import com.smallworldfs.moneytransferapp.presentation.login.model.UserUIModelMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectContactSupportViewModel @Inject constructor(
    private val contactSupportUseCase: ContactSupportUseCase,
    private val contactSupportInfoUIModelMapper: ContactSupportInfoUIModelMapper,
    private val userUIModelMapper: UserUIModelMapper,
    private val errorTypeMapper: ErrorTypeMapper
) : ViewModel() {

    private val _contactSupportInfo: MutableStateFlow<ContactSupportInfoUIModel> = MutableStateFlow(ContactSupportInfoUIModel())
    val contactSupportInfo: StateFlow<ContactSupportInfoUIModel> get() = _contactSupportInfo.asStateFlow()

    private val _currentUser: MutableStateFlow<UserUIModel> = MutableStateFlow(UserUIModel())
    val currentUser: StateFlow<UserUIModel> get() = _currentUser.asStateFlow()

    private val _error: MutableStateFlow<ErrorType> = MutableStateFlow(ErrorType.None)
    val error: StateFlow<ErrorType> get() = _error.asStateFlow()

    init {
        retrieveContactSupportInfo()
        retrieveCurrentUser()
    }

    private fun retrieveContactSupportInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            contactSupportUseCase.getContactSupport()
                .map {
                    _contactSupportInfo.value = contactSupportInfoUIModelMapper.map(it)
                }.mapFailure {
                    _error.value = errorTypeMapper.map(it)
                }
        }
    }

    private fun retrieveCurrentUser() {
        viewModelScope.launch(Dispatchers.IO) {
            contactSupportUseCase.getCurrentUser()
                .map {
                    _currentUser.value = userUIModelMapper.map(it, false)
                }.mapFailure {
                    _error.value = errorTypeMapper.map(it)
                }
        }
    }

    fun hideErrorView() {
        _error.value = ErrorType.None
    }
}
