package com.smallworldfs.moneytransferapp.presentation.form.selector

import androidx.lifecycle.viewModelScope
import com.smallworldfs.moneytransferapp.base.presentation.viewmodel.BaseStateViewModel
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.mapFailure
import com.smallworldfs.moneytransferapp.domain.migrated.base.peek
import com.smallworldfs.moneytransferapp.domain.migrated.form.FormSelectorUseCase
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

class FormSelectorViewModel @Inject constructor(
    private val formSelectorUseCase: FormSelectorUseCase
) : BaseStateViewModel<FormSelectorState, FormSelectorNavigator>() {

    override val initialViewState: FormSelectorState = FormSelectorState()

    /**
     * Request information
     */
    override fun onStartFirstTime(statePreloaded: Boolean) {
        checkDataState { state ->
            if (state.listToShow.isNullOrEmpty())
                state.source?.let {
                    viewModelScope.launch(Dispatchers.IO) {
                        formSelectorUseCase.getFieldContent(it)
                            .peek { result ->
                                updateToNormalState { copy(filteredList = result, listToShow = result) }
                            }
                            .mapFailure {
                                if (it is Error.EntityValidationError) {
                                    val stringFields: String = it.fields.joinToString(",") { it.field }
                                    updateToErrorState(
                                        FormSelectorFieldThrowable(
                                            stringFields,
                                            it.message
                                        )
                                    )
                                }
                            }
                    }
                }
            else
                filterList(STRING_EMPTY)
        }
    }

    /**
     * Actions of the user
     */
    fun onActionSelectItem(item: FormSelectorItem) {
        checkDataState { data ->
            navigator.finishActivityAndReturnResult(item, data.fieldName)
        }
    }

    fun onActionChangeSearchText(text: String) {
        filterList(text)
    }

    /**
     * Filter text
     */
    private fun filterList(text: String) {
        updateToNormalState {
            if (isVisibleSearchContainer && text.isNotEmpty() && searchText != text) {
                val filtered = listToShow.filter {
                    it.value.toLowerCase(Locale.getDefault()).contains(searchText.toLowerCase(Locale.getDefault()))
                }
                copy(filteredList = filtered, searchText = text)
            } else {
                copy(filteredList = listToShow, searchText = text)
            }
        }
    }
}
