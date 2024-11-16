package com.smallworldfs.moneytransferapp.di

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.google.android.play.core.integrity.IntegrityManagerFactory
import com.smallworldfs.moneytransferapp.data.account.account.network.AccountDataService
import com.smallworldfs.moneytransferapp.data.account.account.network.AccountNetworkDatasource
import com.smallworldfs.moneytransferapp.data.account.contactsupport.repository.network.ContactSupportService
import com.smallworldfs.moneytransferapp.data.account.documents.repository.network.DocumentService
import com.smallworldfs.moneytransferapp.data.account.documents.repository.network.DocumentsNetworkDatasource
import com.smallworldfs.moneytransferapp.data.account.documents.repository.network.UserAuthenticationService
import com.smallworldfs.moneytransferapp.data.account.profile.repository.network.EditProfileNetwork
import com.smallworldfs.moneytransferapp.data.account.profile.repository.network.EditProfileService
import com.smallworldfs.moneytransferapp.data.address.network.AddressNetworkDatasource
import com.smallworldfs.moneytransferapp.data.address.network.AddressService
import com.smallworldfs.moneytransferapp.data.autentix.local.AutentixDocumentsNetworkDatasource
import com.smallworldfs.moneytransferapp.data.base.Cache
import com.smallworldfs.moneytransferapp.data.base.network.RetrofitV3
import com.smallworldfs.moneytransferapp.data.base.network.RetrofitV4
import com.smallworldfs.moneytransferapp.data.calculator.network.CalculatorNetworkDatasource
import com.smallworldfs.moneytransferapp.data.calculator.network.CalculatorService
import com.smallworldfs.moneytransferapp.data.common.preferences.repository.local.PreferencesLocal
import com.smallworldfs.moneytransferapp.data.common.resources.country.repository.network.CountryNetworkDatasource
import com.smallworldfs.moneytransferapp.data.common.resources.country.repository.network.CountryService
import com.smallworldfs.moneytransferapp.data.forgotpassword.network.ForgotPasswordNetworkDatasource
import com.smallworldfs.moneytransferapp.data.forgotpassword.network.ForgotPasswordService
import com.smallworldfs.moneytransferapp.data.form.network.FormNetworkDatasource
import com.smallworldfs.moneytransferapp.data.integrity.network.IntegrityNetworkDatasource
import com.smallworldfs.moneytransferapp.data.integrity.network.IntegrityService
import com.smallworldfs.moneytransferapp.data.login.network.FormService
import com.smallworldfs.moneytransferapp.data.login.network.LoginService
import com.smallworldfs.moneytransferapp.data.login.network.LoginServiceV3
import com.smallworldfs.moneytransferapp.data.mtn.network.TransactionTrackingNetworkDatasource
import com.smallworldfs.moneytransferapp.data.mtn.network.TransactionTrackingService
import com.smallworldfs.moneytransferapp.data.oauth.repository.network.OAuthService
import com.smallworldfs.moneytransferapp.data.offices.network.OfficeNetworkDatasource
import com.smallworldfs.moneytransferapp.data.offices.network.OfficeService
import com.smallworldfs.moneytransferapp.data.operations.network.OperationsNetworkDatasource
import com.smallworldfs.moneytransferapp.data.operations.network.OperationsService
import com.smallworldfs.moneytransferapp.data.resetpassword.network.ResetPasswordNetworkDatasource
import com.smallworldfs.moneytransferapp.data.resetpassword.network.ResetPasswordService
import com.smallworldfs.moneytransferapp.data.settings.network.SettingsNetworkDataSource
import com.smallworldfs.moneytransferapp.data.settings.network.SettingsService
import com.smallworldfs.moneytransferapp.data.status.network.StatusNetworkDatasource
import com.smallworldfs.moneytransferapp.data.status.network.StatusService
import com.smallworldfs.moneytransferapp.data.transactional.cashpickup.repository.CashPickUpRepositoryImpl
import com.smallworldfs.moneytransferapp.data.transactional.cashpickup.repository.network.CashPickUpNetwork
import com.smallworldfs.moneytransferapp.data.transactional.cashpickup.repository.network.CashPickUpService
import com.smallworldfs.moneytransferapp.data.transactions.network.TransactionsNetworkDatasource
import com.smallworldfs.moneytransferapp.data.transactions.network.TransactionsService
import com.smallworldfs.moneytransferapp.data.userdata.network.UserDataNetworkDatasource
import com.smallworldfs.moneytransferapp.data.userdata.network.UserDataService
import com.smallworldfs.moneytransferapp.data.userdata.network.UserServiceV3
import com.smallworldfs.moneytransferapp.domain.migrated.base.Retryer
import com.smallworldfs.moneytransferapp.domain.migrated.capabilities.CapabilityChecker
import com.smallworldfs.moneytransferapp.domain.migrated.transactional.cashpickup.repository.CashPickUpRepository
import com.smallworldfs.moneytransferapp.domain.signup.repository.network.SignUpNetwork
import com.smallworldfs.moneytransferapp.domain.signup.repository.network.SignUpService
import com.smallworldfs.moneytransferapp.domain.token.repository.network.AppTokenNetwork
import com.smallworldfs.moneytransferapp.domain.token.repository.network.AppTokenService
import com.smallworldfs.moneytransferapp.modules.c2b.domain.service.C2BNetworkDatasource
import com.smallworldfs.moneytransferapp.modules.c2b.domain.service.C2BService
import com.smallworldfs.moneytransferapp.modules.calculator.domain.service.CalculatorNetworkDatasourceLegacy
import com.smallworldfs.moneytransferapp.modules.calculator.domain.service.CalculatorServiceLegacy
import com.smallworldfs.moneytransferapp.modules.checkout.domain.service.CheckoutService
import com.smallworldfs.moneytransferapp.modules.flinks.domain.service.FlinksNetworkDatasource
import com.smallworldfs.moneytransferapp.modules.flinks.domain.service.FlinksService
import com.smallworldfs.moneytransferapp.modules.login.domain.service.LoginNetworkDatasource
import com.smallworldfs.moneytransferapp.modules.oauth.domain.service.OAuthNetworkDatasource
import com.smallworldfs.moneytransferapp.data.promotions.network.PromotionsNetworkDatasource
import com.smallworldfs.moneytransferapp.data.promotions.network.PromotionsService
import com.smallworldfs.moneytransferapp.modules.register.domain.service.SoftRegisterNetworkDatasource
import com.smallworldfs.moneytransferapp.modules.register.domain.service.SoftRegisterService
import com.smallworldfs.moneytransferapp.modules.status.domain.service.StatusNetworkDatasourceLegacy
import com.smallworldfs.moneytransferapp.modules.status.domain.service.StatusServiceLegacy
import com.smallworldfs.moneytransferapp.modules.transactional.domain.service.GenericDropContentNetworkDatasource
import com.smallworldfs.moneytransferapp.modules.transactional.domain.service.GenericDropContentService
import com.smallworldfs.moneytransferapp.modules.transactional.domain.service.TransactionalNetworkDatasource
import com.smallworldfs.moneytransferapp.modules.transactional.domain.service.TransactionalService
import com.smallworldfs.moneytransferapp.presentation.base.SWRetry
import com.smallworldfs.moneytransferapp.presentation.base.Scheduler
import com.smallworldfs.moneytransferapp.presentation.capabilities.CapabilityCheckerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton
import com.smallworldfs.moneytransferapp.data.account.beneficiary.repository.network.BeneficiaryNetworkDatasource as NewBeneficiaryNetworkDatasource
import com.smallworldfs.moneytransferapp.data.account.beneficiary.repository.network.BeneficiaryService as NewBeneficiaryService
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.service.BeneficiaryNetworkDatasource as LegacyBeneficiaryNetworkDatasource
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.service.BeneficiaryService as BeneficiaryNetworkService
import com.smallworldfs.moneytransferapp.modules.country.domain.service.CountryNetworkDatasource as CountryNetworkServiceDatasource
import com.smallworldfs.moneytransferapp.modules.country.domain.service.CountryService as CountryNetworkService
import com.smallworldfs.moneytransferapp.modules.country.domain.service.CountryService as LegacyCountryService
import com.smallworldfs.moneytransferapp.modules.login.domain.service.LoginService as LoginNetworkService
import com.smallworldfs.moneytransferapp.modules.status.domain.service.ContactSupportService as LegacyContactSupportService

/**
 * AppModule
 *
 * All the data layer dependencies that will be necessary along the app life
 */

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    /**-------------------------------------------------------------------------------------------*/
    /**                                                                                           */
    /**                               REPOSITORIES AND SERVICES                                   */
    /**                                                                                           */
    /**-------------------------------------------------------------------------------------------*/

    @Provides
    fun provideCapabilityChecker(context: Context): CapabilityChecker = CapabilityCheckerImpl(context)

    @Provides
    fun provideCashPickUpRepository(@RetrofitV3 retrofit: Retrofit): CashPickUpRepository =
        CashPickUpRepositoryImpl(
            CashPickUpNetwork(
                retrofit.create(CashPickUpService::class.java),
            ),
        )

    // PREVIOUS MIGRATION

    private const val PREFERENCES_NAME = "SMALLWORLD"
    private const val ENCRYPTED_PREFERENCES_NAME = "SMALLWORLD_ENCRYPTED"

    @Provides
    fun provideSharedPreferences(context: Context): SharedPreferences =
        context.getSharedPreferences(PREFERENCES_NAME, Activity.MODE_PRIVATE)

    @Provides
    fun providePreferences(sharedPreferences: SharedPreferences): PreferencesLocal =
        PreferencesLocal(sharedPreferences)

    @Provides
    fun provideRetryer(scheduler: Scheduler): Retryer = SWRetry(scheduler)

    @Provides
    fun provideAppTokenNetwork(@RetrofitV3 retrofit: Retrofit): AppTokenNetwork =
        AppTokenNetwork(
            retrofit.create(AppTokenService::class.java),
        )

    @Provides
    fun provideSignUpNetwork(@RetrofitV3 retrofit: Retrofit): SignUpNetwork =
        SignUpNetwork(
            retrofit.create(SignUpService::class.java),
        )

    @Provides
    fun provideEditProfileNetwork(@RetrofitV3 retrofit: Retrofit): EditProfileNetwork =
        EditProfileNetwork(retrofit.create(EditProfileService::class.java))

    @Provides
    fun provideDocumentsNetwork(@RetrofitV4 retrofit: Retrofit): DocumentsNetworkDatasource =
        DocumentsNetworkDatasource(retrofit.create(DocumentService::class.java))

    @Provides
    fun provideAutentixDocumentsNetworkDatasource(@RetrofitV4 retrofit: Retrofit): AutentixDocumentsNetworkDatasource =
        AutentixDocumentsNetworkDatasource(
            retrofit.create(UserAuthenticationService::class.java),
        )

    @Provides
    fun provideSettingsNetworkDatasource(@RetrofitV3 retrofit: Retrofit): SettingsNetworkDataSource =
        SettingsNetworkDataSource(
            retrofit.create(SettingsService::class.java),
        )

    @Provides
    fun provideTransactionsNetworkDatasource(@RetrofitV3 retrofit: Retrofit): TransactionsNetworkDatasource =
        TransactionsNetworkDatasource(
            retrofit.create(TransactionsService::class.java),
        )

    @Provides
    fun provideCountryNetworkDatasource(@RetrofitV4 retrofitV4: Retrofit, @RetrofitV3 retrofitV3: Retrofit): CountryNetworkDatasource =
        CountryNetworkDatasource(
            retrofitV4.create(CountryService::class.java),
            retrofitV3.create(LegacyCountryService::class.java),
        )

    @Provides
    fun provideOperationsNetworkDatasource(@RetrofitV3 retrofit: Retrofit): OperationsNetworkDatasource =
        OperationsNetworkDatasource(
            retrofit.create(OperationsService::class.java),
        )

    @Provides
    fun provideUserDataNetworkDataSource(@RetrofitV4 retrofitV4: Retrofit, @RetrofitV3 retrofitV3: Retrofit): UserDataNetworkDatasource =
        UserDataNetworkDatasource(
            retrofitV4.create(UserDataService::class.java),
            retrofitV4.create(LoginService::class.java),
            retrofitV3.create(LoginServiceV3::class.java),
            retrofitV3.create(UserServiceV3::class.java),
        )

    @Provides
    fun providesAddressNetworkDatasource(@RetrofitV4 retrofit: Retrofit): AddressNetworkDatasource =
        AddressNetworkDatasource(
            retrofit.create(
                AddressService::class.java,
            ),
        )

    @Provides
    fun providesFormNetworkDatasource(@RetrofitV4 retrofit: Retrofit): FormNetworkDatasource =
        FormNetworkDatasource(
            retrofit.create(FormService::class.java),
        )

    @Provides
    fun providesIntegrityNetworkDatasource(@RetrofitV4 retrofit: Retrofit, context: Context): IntegrityNetworkDatasource =
        IntegrityNetworkDatasource(
            retrofit.create(IntegrityService::class.java),
            IntegrityManagerFactory.create(context),
        )

    @Provides
    fun providesOfficeNetworkDatasource(@RetrofitV3 retrofit: Retrofit): OfficeNetworkDatasource =
        OfficeNetworkDatasource(
            retrofit.create(OfficeService::class.java),
        )

    @Provides
    fun providesAccountNetworkDatasource(@RetrofitV3 retrofit: Retrofit): AccountNetworkDatasource =
        AccountNetworkDatasource(
            retrofit.create(
                AccountDataService::class.java,
            ),
        )

    @Provides
    fun providesSoftRegisterNetworkDatasource(@RetrofitV3 retrofit: Retrofit, context: Context): SoftRegisterNetworkDatasource =
        SoftRegisterNetworkDatasource(
            retrofit.create(SoftRegisterService::class.java),
            IntegrityManagerFactory.create(context),
        )

    @Provides
    fun providesPromotionsNetworkDatasource(@RetrofitV3 retrofit: Retrofit): PromotionsNetworkDatasource =
        PromotionsNetworkDatasource(
            retrofit.create(PromotionsService::class.java),
        )

    @Provides
    fun providesBeneficiaryNetworkDatasource(@RetrofitV3 retrofit: Retrofit): LegacyBeneficiaryNetworkDatasource =
        LegacyBeneficiaryNetworkDatasource(
            retrofit.create(BeneficiaryNetworkService::class.java),
        )

    @Provides
    fun providesTransactionalNetworkDatasource(@RetrofitV3 retrofit: Retrofit): TransactionalNetworkDatasource =
        TransactionalNetworkDatasource(
            retrofit.create(TransactionalService::class.java),
        )

    @Provides
    fun providesCountryNetworkDatasource(@RetrofitV3 retrofit: Retrofit): CountryNetworkServiceDatasource =
        CountryNetworkServiceDatasource(
            retrofit.create(CountryNetworkService::class.java),
        )

    @Provides
    fun providesLoginNetworkDatasource(@RetrofitV3 retrofit: Retrofit): LoginNetworkDatasource =
        LoginNetworkDatasource(
            retrofit.create(LoginNetworkService::class.java),
        )

    @Provides
    fun providesGenericDropContentNetworkDatasource(@RetrofitV3 retrofit: Retrofit): GenericDropContentNetworkDatasource =
        GenericDropContentNetworkDatasource(
            retrofit.create(GenericDropContentService::class.java),
        )

    @Provides
    fun providesTransactionTrackingNetworkDatasource(@RetrofitV3 retrofit: Retrofit): TransactionTrackingNetworkDatasource =
        TransactionTrackingNetworkDatasource(
            retrofit.create(TransactionTrackingService::class.java),
        )

    @Provides
    fun providesFlinksNetworkDatasource(@RetrofitV3 retrofit: Retrofit): FlinksNetworkDatasource =
        FlinksNetworkDatasource(
            retrofit.create(FlinksService::class.java),
        )

    @Provides
    fun providesOAuthAccessTokenNetworkDatasource(@RetrofitV3 retrofit: Retrofit): OAuthNetworkDatasource =
        OAuthNetworkDatasource(
            retrofit.create(OAuthService::class.java),
        )

    @Provides
    fun providesCalculatorNetworkDatasourceLegacy(@RetrofitV3 retrofit: Retrofit): CalculatorNetworkDatasourceLegacy =
        CalculatorNetworkDatasourceLegacy(
            retrofit.create(CalculatorServiceLegacy::class.java),
        )

    @Provides
    fun providesC2BNetworkDatasource(@RetrofitV3 retrofit: Retrofit): C2BNetworkDatasource =
        C2BNetworkDatasource(
            retrofit.create(C2BService::class.java),
        )

    @Provides
    fun providesStatusNetworkDatasourceLegacy(@RetrofitV3 retrofit: Retrofit): StatusNetworkDatasourceLegacy =
        StatusNetworkDatasourceLegacy(
            retrofit.create(StatusServiceLegacy::class.java),
        )

    @Provides
    fun providesStatusNetworkDataSource(@RetrofitV3 retrofit: Retrofit): StatusNetworkDatasource =
        StatusNetworkDatasource(
            retrofit.create(StatusService::class.java),
        )

    @Provides
    fun providesCheckoutNetworkDatasource(@RetrofitV3 retrofit: Retrofit): CheckoutService =
        retrofit.create(CheckoutService::class.java)

    @Provides
    fun providesContactSupportNetworkDatasource(@RetrofitV3 retrofit: Retrofit): ContactSupportService =
        retrofit.create(ContactSupportService::class.java)

    @Provides
    fun providesLegacyContactSupportNetworkDatasource(@RetrofitV3 retrofit: Retrofit): LegacyContactSupportService =
        retrofit.create(LegacyContactSupportService::class.java)

    @Provides
    fun providesBeneficiaryNetDatasource(@RetrofitV3 retrofit: Retrofit): NewBeneficiaryNetworkDatasource =
        NewBeneficiaryNetworkDatasource(
            retrofit.create(NewBeneficiaryService::class.java),
        )

    @Provides
    fun providesCalculatorNetworkDatasource(@RetrofitV3 retrofit: Retrofit): CalculatorNetworkDatasource =
        CalculatorNetworkDatasource(
            retrofit.create(CalculatorService::class.java),
        )

    @Provides
    fun providesForgotPasswordNetworkDataSource(@RetrofitV3 retrofit: Retrofit): ForgotPasswordNetworkDatasource =
        ForgotPasswordNetworkDatasource(
            retrofit.create(ForgotPasswordService::class.java),
        )

    @Provides
    fun providesResetPasswordDatasource(@RetrofitV3 retrofit: Retrofit): ResetPasswordNetworkDatasource =
        ResetPasswordNetworkDatasource(
            retrofit.create(ResetPasswordService::class.java),
        )

    @Provides
    @Singleton
    fun providesCache(): Cache = Cache()
}
