package com.smallworldfs.moneytransferapp.presentation.form.adapter.holders

import android.text.InputType
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.loadCircularImage
import com.smallworldfs.moneytransferapp.databinding.FormComboCountryHolderBinding
import com.smallworldfs.moneytransferapp.modules.customization.presentation.ui.fragment.ChooseCountryFromFragment
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field
import com.smallworldfs.moneytransferapp.presentation.form.adapter.FormNavigator
import com.smallworldfs.moneytransferapp.presentation.form.adapter.HolderViewModel
import com.smallworldfs.moneytransferapp.utils.Constants.COUNTRY.FLAG_IMAGE_ASSETS
import com.smallworldfs.moneytransferapp.utils.Constants.COUNTRY.FLAG_IMAGE_EXTENSION
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.TreeMap

class ComboCountryHolder(
    binding: FormComboCountryHolderBinding,
    private val formNavigator: FormNavigator,
    holderViewModel: HolderViewModel
) :
    BaseHolder<FormComboCountryHolderBinding>(binding, holderViewModel) {

    override fun initialize(field: Field, lastField: Boolean) {
        super.initialize(field, lastField)
        binding.inputEdit.inputType = InputType.TYPE_NULL
        binding.inputEdit.contentDescription = field.name
        binding.input.hint = field.placeholder ?: STRING_EMPTY
        for (entry: TreeMap<String, String> in field.data) {
            if (entry.containsKey(field.value)) {
                binding.inputEdit.setText(entry[field.value])
                break
            }
        }

        binding.countryFlag.loadCircularImage(
            binding.root.context,
            R.drawable.placeholder_country_adapter,
            FLAG_IMAGE_ASSETS + field.value + FLAG_IMAGE_EXTENSION
        )

        binding.button.setOnClickListener {
            if (!EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().register(this)
            }
            formNavigator.showCountrySelector()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: ChooseCountryFromFragment.EventRefreshCountry) {
        field.value = event.country.first
        initialize(field, false)
        EventBus.getDefault().unregister(this)
    }
}
