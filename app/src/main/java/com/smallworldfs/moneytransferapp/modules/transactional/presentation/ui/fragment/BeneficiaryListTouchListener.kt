package com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.fragment

import android.view.MotionEvent
import androidx.compose.runtime.mutableStateOf
import androidx.recyclerview.widget.RecyclerView

object BeneficiaryListTouchListener : RecyclerView.OnItemTouchListener {

    var userScrollEnabled = mutableStateOf(true)

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        when (e.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                userScrollEnabled.value = false
            }

            MotionEvent.ACTION_UP -> {
                userScrollEnabled.value = true
            }

            else -> {}
        }
        return false
    }

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
}
