package com.smallworldfs.moneytransferapp.presentation.deeplinking

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smallworldfs.moneytransferapp.base.presentation.viewmodel.Event
import com.smallworldfs.moneytransferapp.domain.migrated.base.peek
import com.smallworldfs.moneytransferapp.domain.migrated.base.peekFailure
import com.smallworldfs.moneytransferapp.domain.migrated.base.then
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltViewModel
class DeepLinkingViewModel @Inject
constructor(
    private val userDataRepository: UserDataRepository
) : ViewModel() {

    private val _userLogged: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val userLogged: LiveData<Event<Boolean>> get() = _userLogged

    private val _userNotLogged: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val userNotLogged: LiveData<Event<Boolean>> get() = _userNotLogged

    fun checkUserIsLoggedIn() {
        viewModelScope.launch(Dispatchers.IO) {
            userDataRepository.getLoggedUser().peek {
                userDataRepository.setLoggedUser(it.copy(showEmailValidated = true))
                    .then {
                        _userLogged.postValue(Event(true))
                    }
            }.peekFailure {
                _userNotLogged.postValue(Event(true))
            }
        }
    }
}
