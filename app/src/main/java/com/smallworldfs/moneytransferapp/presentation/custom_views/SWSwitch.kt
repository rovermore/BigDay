package com.smallworldfs.moneytransferapp.presentation.custom_views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.CompoundButton
import androidx.constraintlayout.widget.ConstraintLayout
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.databinding.SwswitchLayoutBinding

class SWSwitch @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var binding: SwswitchLayoutBinding? = SwswitchLayoutBinding.inflate(
        LayoutInflater.from(context),
        this,
        true,
    )

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.SWSwitch,
            0, 0,
        ).apply {
            try {
                binding?.switchButton?.isChecked = getBoolean(R.styleable.SWSwitch_checked, false)
            } finally {
                recycle()
            }
        }
    }

    private var listener: CompoundButton.OnCheckedChangeListener? = null

    fun setOnCheckedChangeListener(listener: CompoundButton.OnCheckedChangeListener?) {
        this.listener = listener
        binding?.switchButton?.setOnCheckedChangeListener(listener)
    }

    fun setChecked(checked: Boolean, alsoNotify: Boolean = true) {
        if (!alsoNotify) {
            binding?.switchButton?.setOnCheckedChangeListener(null)
            binding?.switchButton?.isChecked = checked
            binding?.switchButton?.setOnCheckedChangeListener(listener)
            return
        }
        binding?.switchButton?.isChecked = checked
    }

    fun toggle(alsoNotify: Boolean = true) {
        if (!alsoNotify) {
            binding?.switchButton?.setOnCheckedChangeListener(null)
            binding?.switchButton?.toggle()
            binding?.switchButton?.setOnCheckedChangeListener(listener)
            return
        }
        binding?.switchButton?.toggle()
    }
}
