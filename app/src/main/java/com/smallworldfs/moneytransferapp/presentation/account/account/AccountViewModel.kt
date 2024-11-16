package com.smallworldfs.moneytransferapp.presentation.account.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smallworldfs.moneytransferapp.domain.migrated.account.AccountUseCase
import com.smallworldfs.moneytransferapp.domain.migrated.base.peek
import com.smallworldfs.moneytransferapp.domain.migrated.base.peekFailure
import com.smallworldfs.moneytransferapp.domain.migrated.base.then
import com.smallworldfs.moneytransferapp.presentation.account.account.model.AccountMenuUIModel
import com.smallworldfs.moneytransferapp.presentation.account.account.model.AccountMenuUIModelMapper
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
class AccountViewModel @Inject constructor(
    private val accountUseCase: AccountUseCase,
    private val accountMenuUIModelMapper: AccountMenuUIModelMapper,
    private val errorTypeMapper: ErrorTypeMapper,
    private val userUIModelMapper: UserUIModelMapper,
) : ViewModel() {

    private var _accountMenu = MutableStateFlow<AccountMenuUIModel>(AccountMenuUIModel())
    val accountMenu: StateFlow<AccountMenuUIModel> get() = _accountMenu.asStateFlow()

    private var _accountMenuError = MutableStateFlow<ErrorType>(ErrorType.None)
    val accountMenuError: StateFlow<ErrorType> get() = _accountMenuError.asStateFlow()

    private var _user = MutableStateFlow<UserUIModel>(UserUIModel())
    val user: StateFlow<UserUIModel> get() = _user.asStateFlow()

    private val _loading = MutableStateFlow<Boolean>(true)
    val loading: StateFlow<Boolean> get() = _loading.asStateFlow()

    private val _logout = MutableStateFlow<Boolean>(false)
    val logout: StateFlow<Boolean> get() = _logout.asStateFlow()

    init {
        getUserInfo()
    }

    fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            accountUseCase.logout()
                .peek {
                    _logout.value = true
                }.peekFailure {
                    _accountMenuError.value = errorTypeMapper.map(it)
                }
        }
    }

    fun getAccountMenu() {
        viewModelScope.launch(Dispatchers.IO) {
            _loading.value = true
            accountUseCase.getAccountMenu()
                .peek {
                    _accountMenu.value = accountMenuUIModelMapper.map(it)
                }.peekFailure {
                    _accountMenuError.value = errorTypeMapper.map(it)
                }.then {
                    _loading.value = false
                }
        }
    }

    private fun getUserInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            accountUseCase.getExistingUser()
                .peek {
                    _user.value = userUIModelMapper.map(it, false)
                }.peekFailure {
                    _accountMenuError.value = errorTypeMapper.map(it)
                }.then {
                    getAccountMenu()
                }
        }
    }

    fun hideErrorView() {
        _accountMenuError.value = ErrorType.None
    }
}
