package com.smallworldfs.moneytransferapp.di

import android.content.Context
import com.smallworldfs.moneytransferapp.domain.migrated.capabilities.CapabilityChecker
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericActivity
import com.smallworldfs.moneytransferapp.modules.support.navigator.SelectContactSupportNavigator
import com.smallworldfs.moneytransferapp.presentation.account.beneficiary.detail.BeneficiaryDetailNavigator
import com.smallworldfs.moneytransferapp.presentation.account.beneficiary.list.BeneficiaryListNavigator
import com.smallworldfs.moneytransferapp.presentation.account.beneficiary.list.fragments.common.MyBeneficiariesNavigator
import com.smallworldfs.moneytransferapp.presentation.account.documents.list.MyDocumentsNavigator
import com.smallworldfs.moneytransferapp.presentation.account.documents.upload.UploadDocumentsNavigator
import com.smallworldfs.moneytransferapp.presentation.account.documents.verification.VerificationNavigator
import com.smallworldfs.moneytransferapp.presentation.account.offices.detail.OfficeDetailNavigator
import com.smallworldfs.moneytransferapp.presentation.account.offices.list.OfficesNavigator
import com.smallworldfs.moneytransferapp.presentation.account.profile.edit.EditProfileNavigator
import com.smallworldfs.moneytransferapp.presentation.autentix.model.CapabilityBridge
import com.smallworldfs.moneytransferapp.presentation.capabilities.CapabilityCheckerImpl
import com.smallworldfs.moneytransferapp.presentation.deeplinking.DeepLinkingNavigator
import com.smallworldfs.moneytransferapp.presentation.freeuser.account.FreeUserNavigator
import com.smallworldfs.moneytransferapp.presentation.login.LoginNavigator
import com.smallworldfs.moneytransferapp.presentation.myactivity.MyActivityNavigator
import com.smallworldfs.moneytransferapp.presentation.passwordconfirm.PasswordConfirmNavigator
import com.smallworldfs.moneytransferapp.presentation.quicklogin.BiometricAuthenticator
import com.smallworldfs.moneytransferapp.presentation.quicklogin.BiometricAuthenticatorImpl
import com.smallworldfs.moneytransferapp.presentation.quicklogin.PassCodeNavigator
import com.smallworldfs.moneytransferapp.presentation.quicklogin.QuickLoginNavigator
import com.smallworldfs.moneytransferapp.presentation.settings.SettingsNavigator
import com.smallworldfs.moneytransferapp.presentation.softregister.SignupNavigator
import com.smallworldfs.moneytransferapp.presentation.splash.SplashNavigator
import com.smallworldfs.moneytransferapp.presentation.status.StatusNavigatorNew
import com.smallworldfs.moneytransferapp.presentation.welcome.WelcomeNavigator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped

/**
 * ActivityModule
 *
 * All the activities dependencies
 */

@Module
@InstallIn(ActivityComponent::class)
object ActivityModule {

    @ActivityScoped
    @Provides
    fun providesMyActivityNavigator(@ActivityContext context: Context): MyActivityNavigator = MyActivityNavigator(context as GenericActivity)

    @ActivityScoped
    @Provides
    fun providesLoginNavigator(@ActivityContext context: Context): LoginNavigator = LoginNavigator(context as GenericActivity)
    /**-------------------------------------------------------------------------------------------*/
    /**                                                                                           */
    /**                                        DIALOGS                                            */
    /**                                                                                           */
    /**-------------------------------------------------------------------------------------------*/

    @ActivityScoped
    @Provides
    fun providesWelcomeNavigator(@ActivityContext context: Context): WelcomeNavigator = WelcomeNavigator(context as GenericActivity)

    @ActivityScoped
    @Provides
    fun provideQuickLoginNavigator(@ActivityContext context: Context): QuickLoginNavigator = QuickLoginNavigator(context as GenericActivity)

    @ActivityScoped
    @Provides
    fun provideBiometricAuthenticator(@ActivityContext context: Context, capabilityChecker: CapabilityChecker): BiometricAuthenticator = BiometricAuthenticatorImpl(context as GenericActivity, capabilityChecker)

    @ActivityScoped
    @Provides
    fun providesPassCodeNavigator(@ActivityContext context: Context): PassCodeNavigator = PassCodeNavigator(context as GenericActivity)

    @ActivityScoped
    @Provides
    fun providesSplashNavigator(@ActivityContext context: Context): SplashNavigator = SplashNavigator(context as GenericActivity)

    @ActivityScoped
    @Provides
    fun providesSettingsNavigator(@ActivityContext context: Context): SettingsNavigator = SettingsNavigator(context as GenericActivity)

    @ActivityScoped
    @Provides
    fun providesPasswordConfirmNavigator(@ActivityContext context: Context): PasswordConfirmNavigator = PasswordConfirmNavigator(context as GenericActivity)

    @ActivityScoped
    @Provides
    fun providesFreeUserNavigator(@ActivityContext context: Context): FreeUserNavigator = FreeUserNavigator(context as GenericActivity)

    @ActivityScoped
    @Provides
    fun providesSignupNavigator(@ActivityContext context: Context): SignupNavigator = SignupNavigator(context as GenericActivity)

    @ActivityScoped
    @Provides
    fun providesDeepLinkingNavigator(@ActivityContext context: Context): DeepLinkingNavigator = DeepLinkingNavigator(context as GenericActivity)

    @ActivityScoped
    @Provides
    fun providesOfficesNavigator(@ActivityContext context: Context): OfficesNavigator = OfficesNavigator(context as GenericActivity)

    @ActivityScoped
    @Provides
    fun providesOfficeDetailNavigator(@ActivityContext context: Context): OfficeDetailNavigator = OfficeDetailNavigator(context as GenericActivity)

    @ActivityScoped
    @Provides
    fun providesBeneficiaryListNavigator(@ActivityContext context: Context): BeneficiaryListNavigator = BeneficiaryListNavigator(context as GenericActivity)

    @ActivityScoped
    @Provides
    fun providesMyBeneficiariesNavigator(@ActivityContext context: Context): MyBeneficiariesNavigator = MyBeneficiariesNavigator(context as GenericActivity)

    @ActivityScoped
    @Provides
    fun providesBeneficiaryDetailNewNavigator(@ActivityContext context: Context): BeneficiaryDetailNavigator = BeneficiaryDetailNavigator(context as GenericActivity)

    @ActivityScoped
    @Provides
    fun providesDocumentsListNavigator(@ActivityContext context: Context): MyDocumentsNavigator = MyDocumentsNavigator(context as GenericActivity)

    @ActivityScoped
    @Provides
    fun providesVerificationNavigator(@ActivityContext context: Context): VerificationNavigator = VerificationNavigator(context as GenericActivity)

    @ActivityScoped
    @Provides
    fun providesEditProfileNavigator(@ActivityContext context: Context): EditProfileNavigator = EditProfileNavigator(context as GenericActivity)

    @ActivityScoped
    @Provides
    fun providesSelectContactSupportNavigator(@ActivityContext context: Context): SelectContactSupportNavigator = SelectContactSupportNavigator(context as GenericActivity)

    @ActivityScoped
    @Provides
    fun providesCapabilityBridge(@ActivityContext context: Context, capabilityChecker: CapabilityCheckerImpl): CapabilityBridge = CapabilityBridge(context as GenericActivity, capabilityChecker)

    @ActivityScoped
    @Provides
    fun providesStatusNavigator(@ActivityContext context: Context): StatusNavigatorNew = StatusNavigatorNew(context as GenericActivity)

    @ActivityScoped
    @Provides
    fun providesUploadDocumentsNavigator(@ActivityContext context: Context): UploadDocumentsNavigator = UploadDocumentsNavigator(context as GenericActivity)
}
