package com.smallworldfs.moneytransferapp.presentation.account.profile.show

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smallworldfs.moneytransferapp.domain.migrated.account.profile.show.usecase.ProfileGetUserDataUseCase
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.peek
import com.smallworldfs.moneytransferapp.domain.migrated.base.peekFailure
import com.smallworldfs.moneytransferapp.presentation.account.profile.show.model.ProfileUIModel
import com.smallworldfs.moneytransferapp.presentation.account.profile.show.model.ProfileUIModelMapper
import com.smallworldfs.moneytransferapp.presentation.base.ErrorType
import com.smallworldfs.moneytransferapp.presentation.base.ErrorTypeMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileGetUserDataUseCase: ProfileGetUserDataUseCase,
    private val profileUIModelMapper: ProfileUIModelMapper,
    private val errorTypeMapper: ErrorTypeMapper
) : ViewModel() {

    private val _requestInformationLiveData: MutableStateFlow<ProfileUIModel> = MutableStateFlow(ProfileUIModel())
    val requestInformationLiveData: StateFlow<ProfileUIModel> get() = _requestInformationLiveData.asStateFlow()

    private val _errorRequestInformationLiveData: MutableStateFlow<ErrorType> = MutableStateFlow(ErrorType.None)
    val errorRequestInformationLiveData: StateFlow<ErrorType> get() = _errorRequestInformationLiveData.asStateFlow()

    private val _showDialog: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> get() = _showDialog.asStateFlow()

    init {
        requestInformation()
    }

    fun requestInformation() {
        viewModelScope.launch(Dispatchers.IO) {
            profileGetUserDataUseCase.getUserData()
                .peek { profile ->
                    _requestInformationLiveData.value = profileUIModelMapper.map(profile)
                }.peekFailure {
                    _errorRequestInformationLiveData.value = errorTypeMapper.map(Error.OperationCompletedWithError())
                }
        }
    }

    fun hideErrorView() {
        _errorRequestInformationLiveData.value = ErrorType.None
    }

    fun hideDialog() {
        _showDialog.value = false
    }

    fun showDialog() {
        _showDialog.value = true
    }
}
