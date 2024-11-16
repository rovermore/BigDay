package com.smallworldfs.moneytransferapp

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.widget.ImageView
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.multidex.MultiDex
import com.braze.BrazeActivityLifecycleCallbackListener
import com.freshchat.consumer.sdk.Freshchat
import com.freshchat.consumer.sdk.FreshchatCallback
import com.freshchat.consumer.sdk.FreshchatConfig
import com.freshchat.consumer.sdk.FreshchatImageLoader
import com.freshchat.consumer.sdk.FreshchatImageLoaderRequest
import com.freshchat.consumer.sdk.FreshchatNotificationConfig
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.AnalyticsActivityLifecycleCallback
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.loadImage
import com.smallworldfs.moneytransferapp.data.login.mappers.UserMapperFromDTO
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository
import com.smallworldfs.moneytransferapp.modules.login.domain.repository.LoginRepository
import com.smallworldfs.moneytransferapp.modules.notifications.domain.listener.CustomInAppMessageManagerListener
import com.smallworldfs.moneytransferapp.presentation.splash.SplashActivity
import com.smallworldfs.moneytransferapp.utils.SamsungMemLeak
import com.smallworldfs.moneytransferapp.utils.widget.SWMapActivityLifecycleCallback
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent

@HiltAndroidApp
open class SmallWorldApplication : Application(), ActivityLifecycleCallbacks {

    /**
     * Constants
     */
    companion object {
        @JvmStatic
        lateinit var app: SmallWorldApplication

        @JvmStatic
        fun getStr(id: Int): String {
            return app.getString(id)
        }

        @JvmStatic
        fun getStr(id: Int, s: String?): String {
            return app.getString(id, s)
        }
    }

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    internal interface DaggerHiltEntryPoint {
        fun bindUserDataRepository(): UserDataRepository
        fun provideUserMapper(): UserMapperFromDTO
        fun provideLoginRepository(): LoginRepository
        fun provideAnalyticsLifeCycleCallback(): AnalyticsActivityLifecycleCallback
        fun provideInAppMessageManagerListener(): CustomInAppMessageManagerListener
    }

    lateinit var userDataRepository: UserDataRepository
    lateinit var userMapperFromDTO: UserMapperFromDTO
    lateinit var loginRepository: LoginRepository
    private lateinit var analyticsActivityLifecycleCallback: AnalyticsActivityLifecycleCallback

    /**
     * Application methods
     */
    override fun onCreate() {
        super.onCreate()

        // Application variable
        app = this

        // Fresh chat configuration
        initFreshChat()

        initLifecycleCallbacks()

        // Crashlytics
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)

        // FileProvider
        StrictMode.setVmPolicy(VmPolicy.Builder().build())

        registerActivityLifecycleCallbacks(
            BrazeActivityLifecycleCallbackListener(
                sessionHandlingEnabled = true,
                registerInAppMessageManager = true,
                hashSetOf(SplashActivity::class.java),
            ),
        )
    }

    private fun initFreshChat() {
        // Local broadcast manager
        LocalBroadcastManager.getInstance(applicationContext).registerReceiver(broadcastReceiver, IntentFilter(Freshchat.FRESHCHAT_USER_RESTORE_ID_GENERATED))

        Freshchat.setImageLoader(
            object : FreshchatImageLoader {
                override fun get(freshchatImageLoaderRequest: FreshchatImageLoaderRequest): Bitmap? = null
                override fun load(freshchatImageLoaderRequest: FreshchatImageLoaderRequest, imageView: ImageView) {
                    imageView.loadImage(
                        freshchatImageLoaderRequest.uri,
                    )
                }

                override fun loadInto(freshchatImageLoaderRequest: FreshchatImageLoaderRequest, imageView: ImageView, callback: FreshchatCallback) {
                    imageView.loadImage(
                        freshchatImageLoaderRequest.uri,
                    )
                }
                override fun fetch(freshchatImageLoaderRequest: FreshchatImageLoaderRequest) = Unit
            },
        )
        Freshchat.getInstance(applicationContext).setNotificationConfig(FreshchatNotificationConfig().setSmallIcon(R.drawable.ic_sw_freshchat_notification).setLargeIcon(if (BuildConfig.DEBUG) R.mipmap.ic_launcher_test else R.mipmap.ic_launcher))
        Freshchat.getInstance(applicationContext).init(
            FreshchatConfig(getString(R.string.freshchat_app_id), getString(R.string.freshchat_app_key)).apply {
                isCameraCaptureEnabled = true
                isGallerySelectionEnabled = true
                isResponseExpectationEnabled = true
                isUserEventsTrackingEnabled = true
                domain = getString(R.string.freshchat_domain)
            },
        )
    }

    private fun initLifecycleCallbacks() {
        // Crashlytics
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)

        // FileProvider
        StrictMode.setVmPolicy(VmPolicy.Builder().build())

        val hiltEntryPoint = EntryPointAccessors.fromApplication(app, DaggerHiltEntryPoint::class.java)
        analyticsActivityLifecycleCallback = hiltEntryPoint.provideAnalyticsLifeCycleCallback()

        registerActivityLifecycleCallbacks(analyticsActivityLifecycleCallback)
        registerActivityLifecycleCallbacks(SWMapActivityLifecycleCallback)
        registerActivityLifecycleCallbacks(this)
    }

    /**
     * ActivityLifecycleCallbacks methods
     */
    override fun onActivityStarted(activity: Activity) {}
    override fun onActivityResumed(activity: Activity) {}
    override fun onActivityPaused(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
    override fun onActivityDestroyed(activity: Activity) {
        SamsungMemLeak.onDestroy(applicationContext)
    }

    /**
     * Fresh chat broadcast receiver
     */
    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val restoreId = Freshchat.getInstance(applicationContext).user.restoreId
            val hiltEntryPoint = EntryPointAccessors.fromApplication(app, SmallWorldApplication.DaggerHiltEntryPoint::class.java)
            userDataRepository = hiltEntryPoint.bindUserDataRepository()
            userMapperFromDTO = hiltEntryPoint.provideUserMapper()
            loginRepository = hiltEntryPoint.provideLoginRepository()
            val user = userDataRepository.retrieveUser()
            if (user != null) {
                user.freshchatId = restoreId
                userDataRepository.putUser(user)
            }
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        LocalBroadcastManager.getInstance(applicationContext).unregisterReceiver(broadcastReceiver)
    }

    /**
     * Allow more the 64k methods of code
     */
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}
