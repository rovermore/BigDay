package com.smallworldfs.moneytransferapp.base.presentation.ui

import android.app.Dialog
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.smallworldfs.moneytransferapp.R
import javax.inject.Inject

class Loader @Inject constructor() {
    private lateinit var dialog: Dialog

    fun show(
        activity: AppCompatActivity,
        backgroundColor: Int? = null,
        backgroundDrawable: Int? = null,
        filterColor: Int? = null,
        filterDrawable: Int? = null,
        fullScreen: Boolean = true
    ) {
        if (!activity.isDestroyed && !activity.isFinishing) {
            dialog = if (fullScreen) {
                Dialog(activity, R.style.LoadingDialogFullScreen)
            } else {
                Dialog(activity, R.style.LoadingDialog)
            }
            dialog.setContentView(R.layout.dialog_loading)
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window?.setLayout(width, height)
            dialog.setCancelable(false)

            when {
                backgroundColor != null -> dialog.findViewById<ConstraintLayout>(R.id.root_layout)
                    .setBackgroundColor(ContextCompat.getColor(activity, backgroundColor))
                backgroundDrawable != null -> dialog.findViewById<ConstraintLayout>(R.id.root_layout)
                    .background = ContextCompat.getDrawable(activity, backgroundDrawable)
                else -> dialog.findViewById<ConstraintLayout>(R.id.root_layout)
                    .setBackgroundColor(ContextCompat.getColor(activity, R.color.main_blue))
            }
            when {
                filterColor != null -> dialog.findViewById<ConstraintLayout>(R.id.filter_layout)
                    .setBackgroundColor(ContextCompat.getColor(activity, filterColor))
                filterDrawable != null -> dialog.findViewById<ConstraintLayout>(R.id.filter_layout)
                    .background = ContextCompat.getDrawable(activity, filterDrawable)
                else -> dialog.findViewById<ConstraintLayout>(R.id.filter_layout)
                    .background = null
            }

            dialog.show()
        }
    }

    fun hide(activity: AppCompatActivity) {
        if (::dialog.isInitialized && !activity.isDestroyed && !activity.isFinishing && dialog.isShowing) {
            dialog.dismiss()
        }
    }
}
