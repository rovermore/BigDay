package com.smallworldfs.moneytransferapp.utils.widget

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.loadCircularImage
import com.smallworldfs.moneytransferapp.databinding.FormComboCountryHolderBinding
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import com.smallworldfs.moneytransferapp.utils.gone
import com.smallworldfs.moneytransferapp.utils.visible

class SWRegionPicker @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var binding: FormComboCountryHolderBinding? = null
    private var uiModel: RegionPickerUIModel? = RegionPickerUIModel()

    init {
        binding = FormComboCountryHolderBinding.inflate(
            LayoutInflater.from(context),
            this,
            true,
        )
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.SWCountryPicker,
            0, 0,
        ).apply {
            try {
                showProgressBar(getBoolean(R.styleable.SWCountryPicker_showProgressBar, false))
            } finally {
                recycle()
            }
        }
        binding!!.inputEdit.inputType = InputType.TYPE_NULL
    }

    fun setRegion(uiModel: RegionPickerUIModel) {
        this.uiModel = uiModel
        loadImage(uiModel.image)
        binding?.inputEdit?.setText(uiModel.title)
    }

    fun getCountry() = uiModel

    fun showProgressBar(show: Boolean) {
        if (show)
            binding!!.loadingLayout.visible()
        else
            binding!!.loadingLayout.gone()
    }

    fun setButtonClickListener(action: () -> Unit) {
        binding?.button?.setOnClickListener {
            action()
        }
    }

    private fun loadImage(path: String) {
        binding?.countryFlag?.loadCircularImage(
            context,
            R.drawable.placeholder_country_adapter,
            path,
        )
    }

    fun setHint(hint: String) {
        binding?.inputEdit?.hint = hint
    }

    fun getText(): String {
        return binding?.inputEdit?.text.toString()
    }

    fun setContentDescription(contentDescription: String) {
        binding?.inputEdit?.contentDescription = contentDescription
    }
}

data class RegionPickerUIModel(val id: String = STRING_EMPTY, val title: String = STRING_EMPTY, val image: String = STRING_EMPTY)
