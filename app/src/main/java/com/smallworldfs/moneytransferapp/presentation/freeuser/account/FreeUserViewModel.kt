package com.smallworldfs.moneytransferapp.presentation.freeuser.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smallworldfs.moneytransferapp.data.login.mappers.UserMapperFromDTO
import com.smallworldfs.moneytransferapp.domain.migrated.account.AccountUseCase
import com.smallworldfs.moneytransferapp.domain.migrated.base.map
import com.smallworldfs.moneytransferapp.domain.migrated.base.mapFailure
import com.smallworldfs.moneytransferapp.domain.migrated.base.then
import com.smallworldfs.moneytransferapp.modules.login.domain.model.User
import com.smallworldfs.moneytransferapp.presentation.account.account.model.AccountMenuUIModel
import com.smallworldfs.moneytransferapp.presentation.account.account.model.AccountMenuUIModelMapper
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
class FreeUserViewModel @Inject constructor(
    private val accountUseCase: AccountUseCase,
    private val accountMenuUIModelMapper: AccountMenuUIModelMapper,
    private val userMapperFromDTO: UserMapperFromDTO,
    private val errorTypeMapper: ErrorTypeMapper
) : ViewModel() {

    private val _accountMenu: MutableStateFlow<AccountMenuUIModel> = MutableStateFlow(AccountMenuUIModel())
    val accountMenu: StateFlow<AccountMenuUIModel> get() = _accountMenu.asStateFlow()

    private val _user: MutableStateFlow<User> = MutableStateFlow(User())
    val user: StateFlow<User> get() = _user.asStateFlow()

    private val _accountError: MutableStateFlow<ErrorType> = MutableStateFlow(ErrorType.None)
    val accountError: StateFlow<ErrorType> get() = _accountError.asStateFlow()

    init {
        getFreeUserData()
    }

    private fun getFreeUserData() {
        viewModelScope.launch(Dispatchers.IO) {
            accountUseCase.getExistingUser()
                .map {
                    _user.value = userMapperFromDTO.mapFromUserDTO(it)
                }.mapFailure {
                    _accountError.value = errorTypeMapper.map(it)
                }.then {
                    getFreeUserMenu()
                }
        }
    }

    private fun getFreeUserMenu() {
        viewModelScope.launch(Dispatchers.IO) {
            accountUseCase.getAccountMenu()
                .map {
                    _accountMenu.value = accountMenuUIModelMapper.map(it)
                }.mapFailure {
                    _accountError.value = errorTypeMapper.map(it)
                }
        }
    }

    fun clearUser() {
        viewModelScope.launch(Dispatchers.IO) {
            accountUseCase.clearUser()
        }
    }

    fun hideErrorView() {
        _accountError.value = ErrorType.None
    }
}
