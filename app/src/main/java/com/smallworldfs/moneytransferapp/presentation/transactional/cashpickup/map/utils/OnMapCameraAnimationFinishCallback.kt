package com.smallworldfs.moneytransferapp.presentation.transactional.cashpickup.map.utils

import com.google.android.gms.maps.GoogleMap

abstract class OnMapCameraAnimationFinishCallback : GoogleMap.CancelableCallback {
    override fun onFinish() {
        onAnimationFinish()
    }

    override fun onCancel() {
        onAnimationFinish()
    }

    abstract fun onAnimationFinish()
}
