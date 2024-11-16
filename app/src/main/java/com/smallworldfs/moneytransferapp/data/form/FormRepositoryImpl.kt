package com.smallworldfs.moneytransferapp.data.form

import com.smallworldfs.moneytransferapp.data.base.network.models.APIErrorMapper
import com.smallworldfs.moneytransferapp.data.form.network.FormNetworkDatasource
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.base.map
import com.smallworldfs.moneytransferapp.domain.migrated.base.mapFailure
import com.smallworldfs.moneytransferapp.domain.migrated.form.repository.FormRepository
import com.smallworldfs.moneytransferapp.presentation.form.selector.FormSelectorItem
import javax.inject.Inject

class FormRepositoryImpl @Inject constructor(
    private val formNetworkDatasource: FormNetworkDatasource,
    private val apiErrorMapper: APIErrorMapper
) : FormRepository {
    override fun getFieldContentFromNetwork(url: String): OperationResult<List<FormSelectorItem>, Error> {
        return formNetworkDatasource.getFieldContent(url)
            .map {
                val formItemSelectorList = mutableListOf<FormSelectorItem>()
                it.data.forEach {
                    formItemSelectorList.add(
                        FormSelectorItem(
                            it.keys.first(),
                            it.values.first()
                        )
                    )
                }
                return Success(formItemSelectorList.toList())
            }
            .mapFailure {
                apiErrorMapper.map(it)
            }
    }
}
