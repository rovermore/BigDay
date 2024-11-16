package com.smallworldfs.moneytransferapp.presentation.form.adapter.holders

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field
import com.smallworldfs.moneytransferapp.presentation.form.adapter.HolderViewModel

abstract class BaseHolder<B : ViewBinding>(var binding: B, var viewModel: HolderViewModel) : RecyclerView.ViewHolder(binding.root) {

    protected lateinit var field: Field

    open fun initialize(field: Field, lastField: Boolean) {
        this.field = field
    }
}
