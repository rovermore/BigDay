package com.smallworldfs.moneytransferapp.utils.widget.timer.sms

import android.os.CountDownTimer
import javax.inject.Inject

class SWTimer @Inject constructor() {
    private var timer: CountDownTimer? = null

    fun launch(
        timeRemaining: Long,
        interval: Long = 1000,
        onTickCallback: (millisUntilFinished: Long) -> Unit = {},
        onFinishCallback: () -> Unit = {}
    ) {
        timer?.cancel()
        timer = object : CountDownTimer(timeRemaining, interval) {
            override fun onTick(millisUntilFinished: Long) {
                onTickCallback(millisUntilFinished)
            }

            override fun onFinish() {
                onFinishCallback()
            }
        }
        timer?.start()
    }

    fun cancel() {
        timer?.cancel()
    }
}
