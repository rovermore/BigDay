package com.smallworldfs.moneytransferapp.presentation.account.profile.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smallworldfs.moneytransferapp.base.presentation.viewmodel.Event
import com.smallworldfs.moneytransferapp.data.account.profile.model.EditProfileFormRequest
import com.smallworldfs.moneytransferapp.data.account.profile.model.SaveProfileFormRequest
import com.smallworldfs.moneytransferapp.data.account.profile.model.StateRequest
import com.smallworldfs.moneytransferapp.domain.migrated.account.profile.edit.usecase.EditProfileUseCase
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.peek
import com.smallworldfs.moneytransferapp.domain.migrated.base.peekFailure
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field
import com.smallworldfs.moneytransferapp.presentation.account.profile.edit.model.FormUIModel
import com.smallworldfs.moneytransferapp.presentation.account.profile.edit.model.FormUIModelMapper
import com.smallworldfs.moneytransferapp.presentation.base.ErrorType
import com.smallworldfs.moneytransferapp.presentation.base.ErrorTypeMapper
import com.smallworldfs.moneytransferapp.presentation.form.selector.FormSelectorItem
import com.smallworldfs.moneytransferapp.presentation.form.selector.FormSelectorState
import com.smallworldfs.moneytransferapp.utils.Constants
import com.smallworldfs.moneytransferapp.utils.INT_ONE
import com.smallworldfs.moneytransferapp.utils.INT_ZERO
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val editProfileUseCase: EditProfileUseCase,
    private val formUIModelMapper: FormUIModelMapper,
    private val errorTypeMapper: ErrorTypeMapper
) : ViewModel() {

    private val _editProfileFormLiveData: MutableLiveData<Event<FormUIModel>> = MutableLiveData()
    var editProfileFormLiveData: LiveData<Event<FormUIModel>> = _editProfileFormLiveData

    private val _errorEditProfileFormLiveData: MutableLiveData<ErrorType> = MutableLiveData()
    var errorEditProfileFormLiveData: LiveData<ErrorType> = _errorEditProfileFormLiveData

    private val _saveProfileFormLiveData: MutableLiveData<Event<Boolean>> = MutableLiveData()
    var saveProfileFormLiveData: LiveData<Event<Boolean>> = _saveProfileFormLiveData

    private val _errorSaveProfileFormLiveData: MutableLiveData<Event<String>> = MutableLiveData()
    var errorSaveProfileFormLiveData: LiveData<Event<String>> = _errorSaveProfileFormLiveData

    companion object {
        const val HEADER_PROFILE = 0
        const val HEADER_ADDRESS = 1
        const val GROUP_TWO_ADDRESS_FIELDS = "2"
    }

    /**
     * Request information
     */
    fun requestEditForm() {
        viewModelScope.launch(Dispatchers.IO) {
            editProfileUseCase.getUser()
                .peek { user ->
                    editProfileUseCase.requestEditProfile(
                        EditProfileFormRequest(
                            userToken = user.userToken,
                            userId = user.id
                        )
                    )
                        .peek { response ->
                            _editProfileFormLiveData.postValue(
                                Event(
                                    formUIModelMapper.map(response.form)
                                )
                            )
                        }.peekFailure {
                            _errorEditProfileFormLiveData.postValue(errorTypeMapper.map(Error.OperationCompletedWithError()))
                        }
                }.peekFailure {
                    _errorEditProfileFormLiveData.postValue(errorTypeMapper.map(Error.OperationCompletedWithError()))
                }
        }
    }

    fun onActionSaveData(formData: HashMap<String, String>) {
        viewModelScope.launch(Dispatchers.IO) {
            editProfileUseCase.getUser()
                .peek { user ->
                    val request = SaveProfileFormRequest(user.userToken, user.id)

                    for (keyValueField in formData) {
                        request[keyValueField.key] = keyValueField.value.ifEmpty { STRING_EMPTY }
                    }

                    request[Constants.USER_PARAMS.TEST_VAR] = INT_ZERO.toString()

                    editProfileUseCase.saveProfileForm(request).peek {
                        _saveProfileFormLiveData.postValue(Event(true))
                    }.peekFailure { error ->
                        try {
                            val jsonObj = JSONObject(error.message)
                            val errorField = ((jsonObj.get("msg") as JSONObject).keys()).asSequence().toList().firstOrNull() ?: ""
                            _errorSaveProfileFormLiveData.postValue(Event(errorField))
                        } catch (e: Exception) {
                            _errorSaveProfileFormLiveData.postValue(Event(STRING_EMPTY))
                        }
                    }
                }
        }
    }

    fun onActionNavigateToFormSelectItemFromCity(form: List<Field>, field: Field, navigator: EditProfileNavigator) {
        // Get the country selected
        val paramName = "country"
        val country = field.refApi.params.firstOrNull { it.contains(paramName) }
        var countryString = STRING_EMPTY
        country?.let {
            countryString = country.substring(country.indexOf(paramName) + paramName.length + INT_ONE)
        }

        // Get the state selected
        val state = form.firstOrNull { it.name == "state" }
        var stateString = STRING_EMPTY
        state?.let {
            stateString = it.keyValue
        }
        val type = "sender"
        if (type.isNotEmpty() && countryString.isNotEmpty() && stateString.isNotEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                editProfileUseCase.getCitiesFromCountries(
                    StateRequest(
                        type,
                        stateString,
                        countryString
                    )
                ).peek { cities ->
                    val listToShow: MutableList<FormSelectorItem> = mutableListOf()
                    if ((cities.data.size) > INT_ZERO) {
                        field.data = cities.data
                        cities.data.forEach {
                            listToShow.add(FormSelectorItem(it.firstKey(), it.getValue(it.firstKey())))
                        }
                        navigator.navigateToFormSelectItemFromCity(FormSelectorState(field.title, listToShow, true))
                    }
                }.peekFailure {
                    _errorEditProfileFormLiveData.postValue(errorTypeMapper.map(Error.OperationCompletedWithError()))
                }
            }
        }
    }

    fun onActionNavigateToFormSelectItemFromBirthCountry(field: Field, navigator: EditProfileNavigator) {
        val listToShow: MutableList<FormSelectorItem> = mutableListOf()
        field.data.forEach {
            listToShow.add(FormSelectorItem(it.firstKey(), it.getValue(it.firstKey())))
        }
        navigator.navigateToFormSelectItemFromBirthCountry(FormSelectorState(field.title, listToShow, true))
    }

    fun onActionNavigateToFormSelectItemFromState(field: Field, navigator: EditProfileNavigator) {
        val listToShow: MutableList<FormSelectorItem> = mutableListOf()
        field.data.forEach {
            listToShow.add(FormSelectorItem(it.firstKey(), it.getValue(it.firstKey())))
        }
        navigator.navigateToFormSelectItemFromState(FormSelectorState(field.title, listToShow, true))
    }

    fun onActionNavigateToFormSelectItemFromPhonePrefix(field: Field, navigator: EditProfileNavigator) {
        val listToShow: MutableList<FormSelectorItem> = mutableListOf()
        field.childs[INT_ZERO].data.forEach {
            listToShow.add(FormSelectorItem(it.firstKey(), it.getValue(it.firstKey()), urlDrawable = Constants.COUNTRY.FLAG_IMAGE_ASSETS + it.firstKey() + Constants.COUNTRY.FLAG_IMAGE_EXTENSION))
        }
        navigator.navigateToFormSelectItemFromPhonePrefix(FormSelectorState(field.title, listToShow, true))
    }

    fun onActionNavigateToFormSelectItemFromStreetType(field: Field, navigator: EditProfileNavigator) {
        val listToShow: MutableList<FormSelectorItem> = mutableListOf()
        field.childs[INT_ZERO].data.forEach {
            listToShow.add(FormSelectorItem(it.firstKey(), it.getValue(it.firstKey())))
        }
        navigator.navigateToFormSelectItemFromStreetType(FormSelectorState(field.title, listToShow, true))
    }
}
