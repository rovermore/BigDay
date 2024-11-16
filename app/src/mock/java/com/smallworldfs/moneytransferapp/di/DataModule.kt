package com.smallworldfs.moneytransferapp.di

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.smallworldfs.moneytransferapp.data.auth.oauth.OAuthRepositoryMockImpl
import com.smallworldfs.moneytransferapp.data.auth.oauth.repository.local.OAuthLocal
import com.smallworldfs.moneytransferapp.data.common.encrypted.local.EncryptedPreferences
import com.smallworldfs.moneytransferapp.data.common.preferences.repository.local.PreferencesLocal
import com.smallworldfs.moneytransferapp.data.common.resources.country.CountryRepositoryMockImpl
import com.smallworldfs.moneytransferapp.data.splash.SplashRepositoryMockImpl
import com.smallworldfs.moneytransferapp.data.transactional.cashpickup.CashPickUpRepositoryMockImpl
import com.smallworldfs.moneytransferapp.domain.activity.repository.network.MyActivityNetwork
import com.smallworldfs.moneytransferapp.domain.activity.repository.network.MyActivityService
import com.smallworldfs.moneytransferapp.domain.beneficiary.repository.network.BeneficiaryNetwork
import com.smallworldfs.moneytransferapp.domain.beneficiary.repository.network.BeneficiaryService
import com.smallworldfs.moneytransferapp.domain.documents.repository.network.DocumentsNetwork
import com.smallworldfs.moneytransferapp.domain.documents.repository.network.DocumentsService
import com.smallworldfs.moneytransferapp.domain.login.repository.network.LoginNetwork
import com.smallworldfs.moneytransferapp.domain.login.repository.network.LoginService
import com.smallworldfs.moneytransferapp.domain.marketing.repository.network.MarketingPreferencesNetwork
import com.smallworldfs.moneytransferapp.domain.marketing.repository.network.MarketingPreferencesService
import com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.repository.CountryRepository
import com.smallworldfs.moneytransferapp.domain.migrated.oauth.repository.OAuthRepository
import com.smallworldfs.moneytransferapp.domain.migrated.splash.repository.SplashRepository
import com.smallworldfs.moneytransferapp.domain.migrated.transactional.cashpickup.repository.CashPickUpRepository
import com.smallworldfs.moneytransferapp.domain.offices.repository.network.OfficesNetwork
import com.smallworldfs.moneytransferapp.domain.offices.repository.network.OfficesService
import com.smallworldfs.moneytransferapp.domain.profile.repository.network.ProfileNetwork
import com.smallworldfs.moneytransferapp.domain.profile.repository.network.ProfileService
import com.smallworldfs.moneytransferapp.domain.settings.repository.network.SettingsNetwork
import com.smallworldfs.moneytransferapp.domain.settings.repository.network.SettingsService
import com.smallworldfs.moneytransferapp.domain.signup.repository.network.SignUpNetwork
import com.smallworldfs.moneytransferapp.domain.signup.repository.network.SignUpService
import com.smallworldfs.moneytransferapp.domain.support.repository.network.SupportNetwork
import com.smallworldfs.moneytransferapp.domain.support.repository.network.SupportService
import com.smallworldfs.moneytransferapp.domain.token.repository.network.AppTokenNetwork
import com.smallworldfs.moneytransferapp.domain.token.repository.network.AppTokenService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

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
    fun provideOAuthRepository(): OAuthRepository = OAuthRepositoryMockImpl()

    @Provides
    fun provideSplashRepository(): SplashRepository = SplashRepositoryMockImpl()

    @Provides
    fun provideCashPickUpRepository(): CashPickUpRepository = CashPickUpRepositoryMockImpl()

    @Provides
    fun provideCountryRepository(): CountryRepository = CountryRepositoryMockImpl()


    // PREVIOUS MIGRATION

    private const val PREFERENCES_NAME = "SMALLWORLD"
    private const val ENCRYPTED_PREFERENCES_NAME = "SMALLWORLD_ENCRYPTED"

    @Provides
    fun provideSharedPreferences(context: Context): SharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Activity.MODE_PRIVATE)

    @Provides
    fun provideOAuthLocal(sharedPreferences: SharedPreferences): OAuthLocal = OAuthLocal(sharedPreferences)

    @Provides
    fun providePreferences(sharedPreferences: SharedPreferences): PreferencesLocal = PreferencesLocal(sharedPreferences)

    @Provides
    fun provideEncryptedPreferences(context: Context): EncryptedPreferences = EncryptedPreferences(context.getSharedPreferences(ENCRYPTED_PREFERENCES_NAME, Activity.MODE_PRIVATE))

    @Provides
    fun provideLoginNetwork(retrofit: Retrofit): LoginNetwork = LoginNetwork(retrofit.create(LoginService::class.java))

    @Provides
    fun provideAppTokenNetwork(retrofit: Retrofit): AppTokenNetwork = AppTokenNetwork(retrofit.create(AppTokenService::class.java))

    @Provides
    fun provideSettingsNetwork(retrofit: Retrofit): SettingsNetwork = SettingsNetwork(retrofit.create(SettingsService::class.java))

    @Provides
    fun provideSignUpNetwork(retrofit: Retrofit): SignUpNetwork = SignUpNetwork(retrofit.create(SignUpService::class.java))

    @Provides
    fun provideSupportNetwork(retrofit: Retrofit): SupportNetwork = SupportNetwork(retrofit.create(SupportService::class.java))

    @Provides
    fun provideMarketingPreferencesNetwork(retrofit: Retrofit): MarketingPreferencesNetwork = MarketingPreferencesNetwork(retrofit.create(MarketingPreferencesService::class.java))

    @Provides
    fun provideProfileNetwork(@RetrofitForCoroutinesDependency retrofit: Retrofit): ProfileNetwork = ProfileNetwork(retrofit.create(ProfileService::class.java))

    @Provides
    fun provideOfficesNetwork(@RetrofitForCoroutinesDependency retrofit: Retrofit): OfficesNetwork = OfficesNetwork(retrofit.create(OfficesService::class.java))

    @Provides
    fun provideMyActivityNetwork(@RetrofitForCoroutinesDependency retrofit: Retrofit): MyActivityNetwork = MyActivityNetwork(retrofit.create(MyActivityService::class.java))

    @Provides
    fun provideBeneficiaryNetwork(@RetrofitForCoroutinesDependency retrofit: Retrofit): BeneficiaryNetwork = BeneficiaryNetwork(retrofit.create(BeneficiaryService::class.java))

    @Provides
    fun provideDocumentsNetwork(@RetrofitForCoroutinesDependency retrofit: Retrofit): DocumentsNetwork = DocumentsNetwork(retrofit.create(DocumentsService::class.java))
}