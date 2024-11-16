package com.smallworldfs.moneytransferapp.presentation.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smallworldfs.moneytransferapp.base.migrations.QuickLoginSettingsMigration
import com.smallworldfs.moneytransferapp.base.presentation.viewmodel.Event
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.peek
import com.smallworldfs.moneytransferapp.domain.migrated.base.peekFailure
import com.smallworldfs.moneytransferapp.domain.migrated.base.then
import com.smallworldfs.moneytransferapp.domain.migrated.splash.usecase.SplashUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val splashUseCase: SplashUseCase, private val migration: QuickLoginSettingsMigration) : ViewModel() {

    private val _updateRequired: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val updateRequired: LiveData<Event<Boolean>> = _updateRequired

    private val _userLoggedIn: MutableLiveData<Event<String>> = MutableLiveData()
    val userLoggedIn: LiveData<Event<String>> = _userLoggedIn

    private val _notExistingUser: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val notExistingUser: LiveData<Event<Boolean>> = _notExistingUser

    private val _newInstallation: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val newInstallation: LiveData<Event<Boolean>> = _newInstallation

    private val _loading: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val loading: LiveData<Event<Boolean>> = _loading

    private val _error: MutableLiveData<Event<Error>> = MutableLiveData()
    val error: LiveData<Event<Error>> = _error

    fun loadData() {
        _loading.value = Event(true)
        viewModelScope.launch(Dispatchers.IO) {
            migration.execute()
            splashUseCase.loadAppConfig()
                .peekFailure { error ->
                    _loading.postValue(Event(false))
                    if (error is Error.UpdateRequired) {
                        _updateRequired.postValue(Event(true))
                    } else _error.postValue(Event(error))
                }
                .peek { appToken ->
                    splashUseCase.loadInitialCountryData()
                    splashUseCase.isOnBoardShown().peek { shown ->
                        if (!shown) {
                            _newInstallation.postValue(Event(true))
                        } else {
                            splashUseCase.checkUserStatus()
                                .peek { user ->
                                    if (user.isFullyRegistered()) {
                                        _userLoggedIn.postValue(Event(appToken))
                                    } else
                                        _notExistingUser.postValue(Event(true))
                                }.peekFailure {
                                    _notExistingUser.postValue(Event(true))
                                }.then {
                                    _loading.postValue(Event(false))
                                }
                        }
                    }
                }
        }
    }
}
