# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/luismiguel/android-sdks/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Explicitly preserve all serialization members. The Serializable interface
# is only a marker interface, so it wouldn't save them.


# Preserve static fields of inner classes of R classes that might be accessed
# through introspection.
-keepclassmembers class **.R$* {
  public static <fields>;
}

# Preserve the special static methods that are required in all enumeration classes.
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep interface **DaggerHiltEntryPoint { *; }

-keep class rx.schedulers.Schedulers {
    public static <methods>;
}
-keep class rx.schedulers.ImmediateScheduler {
    public <methods>;
}
-keep class rx.schedulers.TestScheduler {
    public <methods>;
}
-keep class rx.schedulers.Schedulers {
    public static ** test();
}

-keep class com.au10tix.** { *; }

-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

#Freshchat

-keep class com.freshchat.consumer.sdk.** { *; }

-keepattributes Signature
-keepattributes Annotation
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn retrofit2.Platform$Java8
-dontwarn com.squareup.picasso.**
-dontwarn javax.annotation.**
-dontwarn sun.misc.Unsafe

# --------------- Begin: proguard configuration for support library  ----------
-keep class android.support.v4.app.** { *; }
-keep interface android.support.v4.app.** { *; }

# The support library contains references to newer platform versions.
# Don't warn about those in case this app is linking against an older
# platform version. We know about them, and they are safe.
-dontwarn android.support.**

# ---------------- Event Bus
-keepattributes *Annotation*
-keepclassmembers class * {
    @org.greenrobot.eventbus.Subscribe <methods>;
}

-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

# Base module
-keep class com.smallworldfs.moneytransferapp.base.data.net.api.** { *; }
-keep class com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.AnalyticsConstantsKt { *; }
-keep class com.smallworldfs.moneytransferapp.base.presentation.viewmodel.Error { *; }
-keep class com.smallworldfs.moneytransferapp.base.presentation.viewmodel.State { *; }
-keep class com.smallworldfs.moneytransferapp.base.presentation.viewmodel.Success { *; }

# Autentix module
-keep class com.smallworldfs.moneytransferapp.presentation.autentix.model.** { *; }

# Data module
-keep class com.smallworldfs.moneytransferapp.data.account.account.model.** { *; }
-keep class com.smallworldfs.moneytransferapp.data.account.beneficiary.model.** { *; }
-keep class com.smallworldfs.moneytransferapp.data.account.documents.model.** { *; }
-keep class com.smallworldfs.moneytransferapp.data.account.offices.model.** { *; }
-keep class com.smallworldfs.moneytransferapp.data.account.profile.model.** { *; }
-keep class com.smallworldfs.moneytransferapp.data.address.model.** { *; }
-keep class com.smallworldfs.moneytransferapp.data.autentix.network.model.** { *; }
-keep class com.smallworldfs.moneytransferapp.data.auth.oauth.model.** { *; }
-keep class com.smallworldfs.moneytransferapp.data.base.network.model.** { *; }
-keep class com.smallworldfs.moneytransferapp.data.base.network.models.** { *; }
-keep class com.smallworldfs.moneytransferapp.data.base.network.NetworkException { *; }
-keep class com.smallworldfs.moneytransferapp.data.common.datastore.** { *; }
-keep class com.smallworldfs.moneytransferapp.data.common.encrypted.** { *; }
-keep class com.smallworldfs.moneytransferapp.data.common.form.** { *; }
-keep class com.smallworldfs.moneytransferapp.data.common.locale.** { *; }
-keep class com.smallworldfs.moneytransferapp.data.common.preferences.repository.local.** { *; }
-keep class com.smallworldfs.moneytransferapp.data.common.resources.country.model.** { *; }
-keep class com.smallworldfs.moneytransferapp.data.forgotpassword.model.** { *; }
-keep class com.smallworldfs.moneytransferapp.data.countries.model.** { *; }
-keep class com.smallworldfs.moneytransferapp.data.form.model.** { *; }
-keep class com.smallworldfs.moneytransferapp.data.integrity.model.** { *; }
-keep class com.smallworldfs.moneytransferapp.data.login.model.** { *; }
-keep class com.smallworldfs.moneytransferapp.data.marketing.model.** { *; }
-keep class com.smallworldfs.moneytransferapp.data.operations.model.** { *; }
-keep class com.smallworldfs.moneytransferapp.data.settings.model.** { *; }
-keep class com.smallworldfs.moneytransferapp.data.splash.model.** { *; }
-keep class com.smallworldfs.moneytransferapp.data.transactional.cashpickup.model.** { *; }
-keep class com.smallworldfs.moneytransferapp.data.transactions.model.** { *; }
-keep class com.smallworldfs.moneytransferapp.data.userdata.local.** { *; }
-keep class com.smallworldfs.moneytransferapp.data.userdata.model.** { *; }
-keep class com.smallworldfs.moneytransferapp.data.offices.model.** { *; }

# Domain module
-keep class com.smallworldfs.moneytransferapp.domain.migrated.account.documents.model.** { *; }
-keep class com.smallworldfs.moneytransferapp.domain.migrated.base.Error { *; }
-keep class com.smallworldfs.moneytransferapp.domain.migrated.base.TypeAliasesKt { *; }
-keep class com.smallworldfs.moneytransferapp.domain.migrated.login.model.** { *; }
-keep class com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.model.** { *; }
-keep class com.smallworldfs.moneytransferapp.domain.model.** { *; }
-keep class com.smallworldfs.moneytransferapp.domain.signup.model.** { *; }
-keep class com.smallworldfs.moneytransferapp.domain.support.model.** { *; }
-keep class com.smallworldfs.moneytransferapp.domain.token.model.** { *; }

# Modules module
-keep class com.smallworldfs.moneytransferapp.modules.account.domain.model.** { *; }
-keep class com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.** { *; }
-keep class com.smallworldfs.moneytransferapp.modules.c2b.domain.model.** { *; }
-keep class com.smallworldfs.moneytransferapp.modules.calculator.domain.model.** { *; }
-keep class com.smallworldfs.moneytransferapp.modules.changepassword.viewmodel.ChangePasswordError { *; }
-keep class com.smallworldfs.moneytransferapp.modules.changepassword.viewmodel.ChangePasswordForm { *; }
-keep class com.smallworldfs.moneytransferapp.modules.changepassword.viewmodel.ChangePasswordLoaded { *; }
-keep class com.smallworldfs.moneytransferapp.modules.changepassword.viewmodel.ChangePasswordState { *; }
-keep class com.smallworldfs.moneytransferapp.modules.checkout.domain.model.** { *; }
-keep class com.smallworldfs.moneytransferapp.modules.checkout.common.model.** { *; }
-keep class com.smallworldfs.moneytransferapp.modules.country.domain.model.** { *; }
-keep class com.smallworldfs.moneytransferapp.modules.deeplinking.domain.model.** { *; }
-keep class com.smallworldfs.moneytransferapp.modules.documents.model.** { *; }
-keep class com.smallworldfs.moneytransferapp.modules.flinks.domain.model.** { *; }
-keep class com.smallworldfs.moneytransferapp.modules.home.domain.model.** { *; }
-keep class com.smallworldfs.moneytransferapp.modules.login.domain.model.** { *; }
-keep class com.smallworldfs.moneytransferapp.modules.mtn.domain.model.** { *; }
-keep class com.smallworldfs.moneytransferapp.modules.notifications.domain.model.** { *; }
-keep class com.smallworldfs.moneytransferapp.modules.oauth.domain.model.** { *; }
-keep class com.smallworldfs.moneytransferapp.modules.profile.domain.model.** { *; }
-keep class com.smallworldfs.moneytransferapp.modules.promotions.domain.model.** { *; }
-keep class com.smallworldfs.moneytransferapp.modules.quickaccess.domain.model.** { *; }
-keep class com.smallworldfs.moneytransferapp.modules.register.domain.model.** { *; }
-keep class com.smallworldfs.moneytransferapp.modules.settings.domain.model.** { *; }
-keep class com.smallworldfs.moneytransferapp.modules.status.domain.model.** { *; }
-keep class com.smallworldfs.moneytransferapp.modules.transaction.domain.model.** { *; }
-keep class com.smallworldfs.moneytransferapp.modules.transactional.domain.model.** { *; }

-dontwarn com.google.protobuf.java_com_google_android_gmscore_sdk_target_granule__proguard_group_gtm_N1281923064GeneratedExtensionRegistryLite$Loader

