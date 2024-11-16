package com.smallworldfs.moneytransferapp.presentation.account.profile.edit.model

import com.smallworldfs.moneytransferapp.domain.model.FormModel
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field
import com.smallworldfs.moneytransferapp.presentation.account.profile.edit.EditProfileViewModel
import com.smallworldfs.moneytransferapp.utils.INT_ONE
import com.smallworldfs.moneytransferapp.utils.INT_ZERO
import com.smallworldfs.moneytransferapp.utils.forms.SubType
import com.smallworldfs.moneytransferapp.utils.forms.Type
import javax.inject.Inject

class FormUIModelMapper @Inject constructor() {

    fun map(formModel: FormModel): FormUIModel {
        return try {
            val groupOne = mutableListOf(Field(Type.SECTION_HEADER, SubType.SECTION_HEADER_PROFILE, formModel.groups[EditProfileViewModel.HEADER_PROFILE].title))
            val groupTwo = mutableListOf(Field(Type.SECTION_HEADER, SubType.SECTION_HEADER_ADDRESS, formModel.groups[EditProfileViewModel.HEADER_ADDRESS].title))

            formModel.fields?.forEach {
                if (it.attributes.group.toString() == EditProfileViewModel.GROUP_TWO_ADDRESS_FIELDS) {
                    groupTwo.add(it)
                } else {
                    groupOne.add(it)
                }
            }

            val fieldsFormatted = mutableListOf<Field>()
            fieldsFormatted.addAll(groupOne)
            fieldsFormatted.addAll(groupTwo)

            val formFormatted = FormUIModel(
                fieldsFormatted as ArrayList<Field>
            )

            // Set address key value
            var field = formFormatted.fields.firstOrNull { it.type == Type.GROUP && it.subtype == SubType.GROUP_TEXT }
            field?.let {
                if (it.childs.size > INT_ONE) {
                    it.childs[INT_ZERO].keyValue = it.childs[INT_ZERO].value
                }
            }

            // Set state key value
            field = formFormatted.fields.firstOrNull { it.name == "state" }
            field?.let {
                it.keyValue = it.value
            }

            formFormatted
        } catch (e: Exception) {
            FormUIModel(
                formModel.fields ?: arrayListOf()
            )
        }
    }
}
