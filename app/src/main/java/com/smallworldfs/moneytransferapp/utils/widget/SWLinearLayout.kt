package com.smallworldfs.moneytransferapp.utils.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.LinearLayout
import com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.fragment.BeneficiaryListTouchListener

class SWLinearLayout : LinearLayout {

    constructor (context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return true
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }

    override fun onInterceptTouchEvent(e: MotionEvent): Boolean {
        when (e.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                BeneficiaryListTouchListener.userScrollEnabled.value = true
            }

            MotionEvent.ACTION_UP -> {
                BeneficiaryListTouchListener.userScrollEnabled.value = false
            }

            else -> {}
        }
        return false
    }
}
