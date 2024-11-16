package com.smallworldfs.moneytransferapp.domain.migrated.form

import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.form.repository.FormRepository
import com.smallworldfs.moneytransferapp.domain.migrated.form.repository.Source
import com.smallworldfs.moneytransferapp.presentation.form.selector.FormSelectorItem
import com.smallworldfs.moneytransferapp.utils.UrlUtils
import java.util.ArrayList
import javax.inject.Inject

class FormSelectorUseCase @Inject constructor(
    private val formRepository: FormRepository,
    private val urlUtils: UrlUtils
) {
    fun getFieldContent(source: Source): OperationResult<List<FormSelectorItem>, Error> {
        return when (source) {
            is Source.API -> {
                val url = urlUtils.formUrlFromApiRef(
                    source.url,
                    source.queryParams as ArrayList<String>,
                    source.auxParam
                )
                formRepository.getFieldContentFromNetwork(url)
            }
            else -> Failure(Error.UncompletedOperation("Uncompleted source cast"))
        }
    }
}
