package com.smallworldfs.moneytransferapp.presentation.custom_views

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.databinding.SwPagerIndicatorBinding
import com.smallworldfs.moneytransferapp.utils.dpToPixels

class SWPagerIndicator @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    var binding: SwPagerIndicatorBinding? = null

    var currentPosition = 1
        private set
    private var steps = 1

    init {
        binding = SwPagerIndicatorBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.SWPagerIndicator,
            0, 0
        ).apply {

            try {
                steps = getInteger(R.styleable.SWPagerIndicator_steps, 1)
                currentPosition = getInteger(R.styleable.SWPagerIndicator_selected_step, 1)
                setupSteps(steps)
                setSelectedStep(currentPosition)
            } finally {
                recycle()
            }
        }
    }

    private fun setupSteps(steps: Int) {
        for (step in 1..steps) {
            with(binding!!) {
                val stepIndicator = TextView(context).apply {
                    tag = "step_$step"
                    layoutParams = LinearLayout.LayoutParams(30.dpToPixels(), 30.dpToPixels())
                    text = step.toString()
                    textSize = 14f
                    setTypeface(null, Typeface.BOLD)
                    gravity = Gravity.CENTER
                    background = ContextCompat.getDrawable(context, R.drawable.blue_dark_circle_indicator)
                    setTextColor(ContextCompat.getColor(context, R.color.colorBlueOceanDarkSemitransparent))
                }
                stepContainer.addView(stepIndicator)
                if (step < steps) {
                    val stepSeparator = View(context).apply {
                        tag = "separator_$step"
                        layoutParams = LinearLayout.LayoutParams(64.dpToPixels(), 2.dpToPixels()).apply {
                            gravity = Gravity.CENTER
                        }
                        background = ColorDrawable(ContextCompat.getColor(context, R.color.colorBlueOceanDarkSemitransparent))
                    }
                    stepContainer.addView(stepSeparator)
                }
            }
        }
    }

    fun setSelectedStep(step: Int) {
        for (i in 1..step) {
            val textView = binding!!.stepContainer.findViewWithTag<TextView>("step_$i")
            textView.background = ContextCompat.getDrawable(context, R.drawable.green_circle_indicator)
            textView.setTextColor(ContextCompat.getColor(context, R.color.colorBlueOceanDark))
        }
        for (i in 1 until step) {
            val separator = binding!!.stepContainer.findViewWithTag<View>("separator_$i")
            separator.background = ColorDrawable(ContextCompat.getColor(context, R.color.colorGreenMain))
        }
        for (i in step + 1 until steps + 1) {
            val textView = binding!!.stepContainer.findViewWithTag<TextView>("step_$i")
            textView.background = ContextCompat.getDrawable(context, R.drawable.blue_dark_circle_indicator)
            textView.setTextColor(ContextCompat.getColor(context, R.color.colorBlueOceanDarkSemitransparent))
            if (i > 1) {
                val separator = binding!!.stepContainer.findViewWithTag<View>("separator_${i - 1}")
                separator.background = ColorDrawable(
                    ContextCompat.getColor(
                        context,
                        R.color.colorBlueOceanDarkSemitransparent
                    )
                )
            }
        }
        currentPosition = step
    }
}
