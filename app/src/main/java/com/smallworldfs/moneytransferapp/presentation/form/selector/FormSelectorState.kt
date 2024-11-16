package com.smallworldfs.moneytransferapp.presentation.form.selector

import com.smallworldfs.moneytransferapp.domain.migrated.form.repository.Source
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import java.io.Serializable

data class FormSelectorState(
    val toolbarTitle: String = STRING_EMPTY,
    val listToShow: List<FormSelectorItem> = listOf(),
    val isVisibleSearchContainer: Boolean = true,
    val source: Source.API? = null,
    val searchText: String = STRING_EMPTY,
    val filteredList: List<FormSelectorItem> = listOf(),
    val screenName: String = STRING_EMPTY,
    val fieldName: String = STRING_EMPTY
) : Serializable
