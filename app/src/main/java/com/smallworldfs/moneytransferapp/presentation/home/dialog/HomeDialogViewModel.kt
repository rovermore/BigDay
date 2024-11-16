package com.smallworldfs.moneytransferapp.presentation.home.dialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smallworldfs.moneytransferapp.domain.migrated.base.map
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.usecase.ValidateEmailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeDialogViewModel @Inject constructor(
    private val validateEmailUseCase: ValidateEmailUseCase,
) : ViewModel() {

    private val _emailSent = MutableStateFlow<Boolean>(false)
    val emailSent: StateFlow<Boolean> get() = _emailSent.asStateFlow()

    fun sendEmail() {
        viewModelScope.launch(Dispatchers.IO) {
            validateEmailUseCase.sendEmail()
                .map {
                    _emailSent.value = true
                }
        }
    }
}
