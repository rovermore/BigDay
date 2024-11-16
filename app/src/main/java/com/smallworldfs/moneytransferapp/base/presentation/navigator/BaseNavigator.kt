package com.smallworldfs.moneytransferapp.base.presentation.navigator

import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.base.presentation.ui.BaseStateAppCompatActivity
import com.smallworldfs.moneytransferapp.base.presentation.ui.NavigatorMap
import com.smallworldfs.moneytransferapp.base.presentation.viewmodel.State
import com.smallworldfs.moneytransferapp.base.presentation.viewmodel.Success
import com.smallworldfs.moneytransferapp.utils.Log
import java.lang.ref.WeakReference
import javax.inject.Inject

abstract class BaseNavigator {
    protected lateinit var activityRef: WeakReference<AppCompatActivity>

    @Inject
    lateinit var navigatorMap: NavigatorMap

    fun setActivity(activityRef: AppCompatActivity) {
        this.activityRef = WeakReference(activityRef)
    }

    /**
     * Register dynamic codes for results
     */
    open val dynamicCodes: List<Int>? = null

    fun registerDynamicCodes() {
        activityRef.get()?.let { activity ->
            dynamicCodes?.forEach { code ->
                (activity as BaseStateAppCompatActivity<*, *, *, *>).activityResultsLaunchers[code] = activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                    activity.whenActivityResult(code, it.resultCode, it.data)
                }
            }
        }
    }

    /**
     * Old method that only will be use for actions that does not hope a result, for intents with result use the method "launchForResult"
     */
    fun launch(intent: Intent, finish: Boolean = false, requestCode: Int? = null, animationMoving: AnimationType = AnimationType.FADE_IN_TO_FADE_OUT): State.Success<Success> {
        var destClass: Class<*>? = null
        activityRef.get()?.let { activity ->
            try {
                intent.resolveActivity(activity.packageManager).className
            } catch (e: Exception) {
                Log.e("ActivityNavigator", "Error: $e")
                return State.Success(Success.INTENT_ERROR)
            }

            try {
                destClass = Class.forName(intent.resolveActivity(activity.packageManager).className)
            } catch (e: Exception) {
                Log.e("ActivityNavigator", "Error: $e")
                // Name not found, this name is used for internal state assignment, not really
                // impact on application functionality
            }

            try {
                if (requestCode != null) {
                    activity.startActivityForResult(intent, requestCode)
                } else {
                    activity.startActivity(intent)
                }
                when (animationMoving) {
                    AnimationType.FADE_IN_TO_FADE_OUT -> activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                    AnimationType.MOVE_FROM_RIGHT_TO_LEFT -> activity.overridePendingTransition(R.anim.from_right_to_left, R.anim.from_position_to_left)
                }
                if (finish) {
                    activity.finish()
                }
            } catch (e: Exception) {
                Log.e("ActivityNavigator", "Error: $e")
                return State.Success(Success.INTENT_ERROR)
            }
            destClass?.let {
                if (navigatorMap.containsKey(it)) {
                    return State.Success(navigatorMap[it])
                }
            }
        }
        return State.Success(Success.UNKNOWN_ACTIVITY)
    }

    /**
     * This method follow the new Google recommendations and avoid deprecated instructions
     */
    fun launchForResult(intent: Intent, finish: Boolean = false, requestCode: Int, animationMoving: AnimationType = AnimationType.FADE_IN_TO_FADE_OUT) {
        activityRef.get()?.let { activity ->

            try {
                // Launch intent
                (activity as BaseStateAppCompatActivity<*, *, *, *>).activityResultsLaunchers[requestCode]?.launch(intent)

                // Set animation
                when (animationMoving) {
                    AnimationType.FADE_IN_TO_FADE_OUT -> activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                    AnimationType.MOVE_FROM_RIGHT_TO_LEFT -> activity.overridePendingTransition(R.anim.from_right_to_left, R.anim.from_position_to_left)
                }

                // If should finish current activity do it
                if (finish) {
                    activity.finish()
                }
            } catch (e: Exception) {
                Log.e("ActivityNavigator", "Error: $e")
            }
        }
    }

    enum class AnimationType {
        FADE_IN_TO_FADE_OUT,
        MOVE_FROM_RIGHT_TO_LEFT
    }
}

class AnyNavigator @Inject constructor() : BaseNavigator()
