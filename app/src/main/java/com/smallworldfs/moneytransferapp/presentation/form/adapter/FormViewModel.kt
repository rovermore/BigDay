package com.smallworldfs.moneytransferapp.presentation.form.adapter

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field
import com.smallworldfs.moneytransferapp.utils.Constants
import com.smallworldfs.moneytransferapp.utils.INT_ZERO
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import com.smallworldfs.moneytransferapp.utils.forms.SubType
import com.smallworldfs.moneytransferapp.utils.forms.Type
import com.smallworldfs.moneytransferapp.utils.forms.ViewType
import java.lang.Boolean.parseBoolean
import javax.inject.Inject
import kotlin.collections.set

class FormViewModel @Inject constructor() : ViewModel() {

    val list: MutableList<Field> = mutableListOf()

    private val action = MutableLiveData<String>()
    fun getAction(): LiveData<String> = action

    @Inject
    lateinit var navigator: FormNavigator

    fun getFieldsCount(): Int = list.size

    fun getItem(position: Int): Field = if (position >= INT_ZERO && position < list.size) {
        list[position]
    } else {
        Field(STRING_EMPTY, STRING_EMPTY, STRING_EMPTY)
    }

    fun getItemType(position: Int): Int = if (position >= INT_ZERO && position < list.size) {
        val item = list[position]
        if (item.isHidden) {
            ViewType.UNKNOWN_VIEW
        } else {
            when (item.type) {
                Type.GROUP -> {
                    when (item.subtype) {
                        SubType.GROUP_DATE -> ViewType.GROUP_DATE_VIEW
                        SubType.GROUP_PHONE -> ViewType.GROUP_PHONE_VIEW
                        SubType.GROUP_TEXT -> ViewType.GROUP_TEXT_VIEW
                        else -> ViewType.UNKNOWN_VIEW
                    }
                }
                Type.SECTION_HEADER -> ViewType.SECTION_HEADER_VIEW
                Type.TEXT -> ViewType.TEXT_VIEW
                Type.PASSWORD -> ViewType.PASSWORD_VIEW
                Type.EMAIL -> ViewType.EMAIL_VIEW
                Type.COMBO -> {
                    when (item.subtype) {
                        SubType.OWN -> ViewType.COMBO_OWN_VIEW
                        SubType.API -> ViewType.COMBO_OWN_VIEW
                        else -> ViewType.UNKNOWN_VIEW
                    }
                }
                Type.COMBO_COUNTRY -> ViewType.COMBO_COUNTRY_VIEW
                Type.CHECK_BOX -> ViewType.CHECK_BOX
                Type.WHITEBOX -> ViewType.WHITEBOX
                Type.SWITCH -> ViewType.SWITCH_VIEW
                Type.BUTTON -> {
                    if (item.subtype == SubType.ALPHA_NUM) {
                        ViewType.TEXT_BUTTON_VIEW
                    } else {
                        ViewType.BUTTON_VIEW
                    }
                }
                Type.FILE -> {
                    when (item.subtype) {
                        SubType.DOCUMENT -> ViewType.FILE_DOCUMENT
                        SubType.DOCUMENT_FIELDS_FROM_IMAGE -> ViewType.FILE_DOCUMENT
                        SubType.USER -> ViewType.FILE_DOCUMENT
                        else -> ViewType.UNKNOWN_VIEW
                    }
                }
                else -> ViewType.UNKNOWN_VIEW
            }
        }
    } else {
        INT_ZERO
    }

    fun setList(fields: List<Field>) {
        list.clear()
        list.addAll(fields)
    }

    fun updateItem(field: Field, position: Int) {
        list[position] = field
    }

    fun addItem(field: Field) {
        list.add(field)
    }

    /**
     * Send when there is a click action on an item the name of the field or the keyValue set by the developer if it is not empty
     * Use the second param when the field.name param is null or empty
     */
    fun onClick(position: Int, keyValue: String = STRING_EMPTY) {
        if (position >= INT_ZERO && position < list.size) {
            if (keyValue.isNotEmpty()) {
                action.postValue(keyValue)
            } else {
                action.postValue(list[position].name)
            }
        }
    }

    fun checkForm(emptyText: String): Boolean {
        var errorsFound = false
        for (field: Field in list) {
            if (field.subtype == SubType.STRONG_PASSWORD && field.keyValue == "invalid") {
                field.errorMessage = " "
                errorsFound = true
                continue
            }
            if (field.isRequired && field.value != null && field.value.isEmpty()) {
                field.errorMessage = emptyText
                errorsFound = true
            }
        }
        return errorsFound
    }

    fun sendForm(): HashMap<String, String> {
        var map = HashMap<String, String>()
        for (field: Field in list) {
            map = prepareForm()
        }
        return map
    }

    private fun prepareForm(): HashMap<String, String> {
        val tempList = HashMap<String, String>()

        for (field: Field in list) {
            if (!field.isHidden || field.name == "fallback" || field.name == "documentType") {
                when (field.type) {
                    Constants.FIELD_TYPE.TEXT, Constants.FIELD_TYPE.TEXT_AREA, Type.FILE, Type.EMAIL, Type.PASSWORD ->
                        tempList[field.name ?: STRING_EMPTY] = field.value ?: STRING_EMPTY

                    Type.COMBO, Type.COMBO_COUNTRY ->
                        if (field.subtype.equals(Constants.FIELD_SUBTYPES.COMBO_API, ignoreCase = true)) {
                            // Try to get keyValue data, and if is not present (Created beneficiary), try to get value from value attr
                            if (!TextUtils.isEmpty(field.name)) {
                                var keyValue = field.keyValue
                                if (TextUtils.isEmpty(keyValue)) {
                                    keyValue = field.value
                                }
                                tempList[field.name] = keyValue ?: STRING_EMPTY
                            }
                        } else {
                            val value = if (field.keyValue == null) if (field.value == null) STRING_EMPTY else
                                field.value else field.keyValue
                            tempList[field.name ?: STRING_EMPTY] = value
                        }

                    Constants.FIELD_TYPE.GROUP ->
                        if (field.subtype.equals(Constants.FIELD_SUBTYPES.TEXT_GROUP, ignoreCase = true)) {
                            for ((position, fieldAux) in field.childs.withIndex()) {
                                tempList[fieldAux.name ?: STRING_EMPTY] =
                                    if (position == INT_ZERO) if (fieldAux.keyValue == null) STRING_EMPTY else
                                        fieldAux.keyValue else if (fieldAux.value == null) STRING_EMPTY else
                                        fieldAux.value
                            }
                        } else {
                            for (fieldAux in field.childs) {
                                tempList[fieldAux.name ?: STRING_EMPTY] = fieldAux.value ?: STRING_EMPTY
                            }
                        }

                    Type.CHECK_BOX, Type.SWITCH, Constants.FIELD_TYPE.RADIO_BUTTON ->
                        // Convert String (true, false) to String (0, 1)
                        if (!TextUtils.isEmpty(field.name) && !TextUtils.isEmpty(field.value)) {
                            val value = if (parseBoolean(field.value)) "1" else "0"
                            tempList[field.name] = value
                        }
                }
            }
        }

        return tempList
    }

    fun getLastEditablePosition(): Int {
        var pos = INT_ZERO
        for ((idx, field: Field) in list.withIndex()) {
            if (field.type == Type.PASSWORD || field.type == Type.EMAIL) {
                pos = idx
            }
        }
        return pos
    }

    fun getFormList(): List<Field> {
        return list
    }
}
