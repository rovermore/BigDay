package com.smallworldfs.moneytransferapp.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.braze.Braze
import com.smallworldfs.moneytransferapp.SmallWorldApplication.Companion.app
import com.smallworldfs.moneytransferapp.base.domain.utils.Base64Tool
import com.smallworldfs.moneytransferapp.data.base.network.LangInterceptor
import com.smallworldfs.moneytransferapp.data.base.network.RetrofitInstance
import com.smallworldfs.moneytransferapp.data.base.network.RetrofitV3
import com.smallworldfs.moneytransferapp.data.base.network.RetrofitV4
import com.smallworldfs.moneytransferapp.data.base.network.interceptors.DeviceInfoInterceptor
import com.smallworldfs.moneytransferapp.data.base.network.interceptors.RequestLogInterceptor
import com.smallworldfs.moneytransferapp.data.base.network.interceptors.TokenInterceptor
import com.smallworldfs.moneytransferapp.data.base.network.interceptors.UserIdInterceptor
import com.smallworldfs.moneytransferapp.data.common.form.FormRepository
import com.smallworldfs.moneytransferapp.data.common.resources.country.repository.local.CountryLocalDataSource
import com.smallworldfs.moneytransferapp.data.login.mappers.UserMapperFromDTO
import com.smallworldfs.moneytransferapp.data.oauth.repository.local.OAuthLocal
import com.smallworldfs.moneytransferapp.data.userdata.repository.UserDataRepositoryImpl
import com.smallworldfs.moneytransferapp.domain.migrated.account.contactsupport.repository.ContactSupportRepository
import com.smallworldfs.moneytransferapp.domain.migrated.account.contactsupport.usecase.ContactSupportUseCase
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository
import com.smallworldfs.moneytransferapp.domain.signup.repository.SignUpRepository
import com.smallworldfs.moneytransferapp.modules.c2b.C2BContract
import com.smallworldfs.moneytransferapp.modules.c2b.presentation.presenter.implementation.C2BPresenterImpl
import com.smallworldfs.moneytransferapp.modules.changepassword.usecase.ChangePasswordChangePasswordUseCase
import com.smallworldfs.moneytransferapp.modules.changepassword.usecase.ChangePasswordChangePasswordUseCaseContract
import com.smallworldfs.moneytransferapp.modules.changepassword.usecase.ChangePasswordClearPasswordFieldsUseCase
import com.smallworldfs.moneytransferapp.modules.changepassword.usecase.ChangePasswordClearPasswordFieldsUseCaseContract
import com.smallworldfs.moneytransferapp.modules.changepassword.usecase.ChangePasswordCreateFormUseCase
import com.smallworldfs.moneytransferapp.modules.changepassword.usecase.ChangePasswordCreateFormUseCaseContract
import com.smallworldfs.moneytransferapp.modules.changepassword.usecase.ChangePasswordGetUserUseCase
import com.smallworldfs.moneytransferapp.modules.changepassword.usecase.ChangePasswordGetUserUseCaseContract
import com.smallworldfs.moneytransferapp.modules.changepassword.usecase.ChangePasswordRequestPasswordAttributesUseCase
import com.smallworldfs.moneytransferapp.modules.changepassword.usecase.ChangePasswordRequestPasswordAttributesUseCaseContract
import com.smallworldfs.moneytransferapp.modules.changepassword.usecase.ChangePasswordSaveNewPasswordUseCase
import com.smallworldfs.moneytransferapp.modules.changepassword.usecase.ChangePasswordSaveNewPasswordUseCaseContract
import com.smallworldfs.moneytransferapp.modules.qr.presentation.presenter.QrPresenter
import com.smallworldfs.moneytransferapp.modules.qr.presentation.presenter.implementation.QrPresenterImpl
import com.smallworldfs.moneytransferapp.presentation.common.session.SessionHandler
import com.yakivmospan.scytale.Crypto
import com.yakivmospan.scytale.Options
import com.yakivmospan.scytale.Store
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import java.text.NumberFormat
import javax.inject.Named
import javax.inject.Singleton

/**
 * AppModule
 *
 * All the dependencies that will be necessary along the app life
 */

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /**-------------------------------------------------------------------------------------------*/
    /**                                                                                           */
    /**                                     GLOBAL DEPENDENCIES                                   */
    /**                                                                                           */
    /**-------------------------------------------------------------------------------------------*/

    @Provides
    @Singleton
    fun provideAppContext(): Context = app

    @Provides
    @Named("ExternalFilesPath")
    fun provideExternalFilesPath(context: Context): String = context.getExternalFilesDir("application/pdf").toString()

    @Provides
    fun provideNumberFormatter(): NumberFormat = NumberFormat.getNumberInstance()

    @Provides
    fun provideStore(app: Application): Store = Store(app)

    /**-------------------------------------------------------------------------------------------*/
    /**                                                                                           */
    /**                                     NETWORK DEPENDENCIES                                  */
    /**                                                                                           */
    /**-------------------------------------------------------------------------------------------*/

    @Provides
    fun provideOAuthLocal(sharedPreferences: SharedPreferences): OAuthLocal = OAuthLocal(sharedPreferences)

    @Provides
    @Singleton
    fun provideSessionHandler(context: Context): SessionHandler = SessionHandler(context)

    @Provides
    fun provideTokenInterceptor(context: Context, oAuthLocal: OAuthLocal) = TokenInterceptor(context, oAuthLocal)

    @Provides
    fun provideDeviceInterceptor() = DeviceInfoInterceptor(app)

    @Provides
    @Singleton
    fun provideRequestLogInterceptor(context: Context): RequestLogInterceptor = RequestLogInterceptor(context)

    @Provides
    @RetrofitV3
    fun provideRetrofitV3(retrofitInstance: RetrofitInstance, langInterceptor: LangInterceptor): Retrofit = retrofitInstance.get(langInterceptor)

    @Provides
    @RetrofitV4
    fun provideRetrofitV4(retrofitInstance: RetrofitInstance, userIdInterceptor: UserIdInterceptor): Retrofit = retrofitInstance.get(userIdInterceptor)

    /**-------------------------------------------------------------------------------------------*/
    /**                                                                                           */
    /**                                          USE CASES                                        */
    /**                                                                                           */
    /**-------------------------------------------------------------------------------------------*/

    @Provides
    fun provideChangePasswordChangePasswordUseCase(
        changePasswordGetUserUseCaseContract: ChangePasswordGetUserUseCaseContract,
        base64Tool: Base64Tool,
        signUpRepository: SignUpRepository,
        changePasswordSaveNewPasswordUseCaseContract: ChangePasswordSaveNewPasswordUseCaseContract
    ): ChangePasswordChangePasswordUseCaseContract = ChangePasswordChangePasswordUseCase(changePasswordGetUserUseCaseContract, base64Tool, signUpRepository, changePasswordSaveNewPasswordUseCaseContract)

    @Provides
    fun provideChangePasswordClearPasswordFieldsUseCase(formRepository: FormRepository): ChangePasswordClearPasswordFieldsUseCaseContract = ChangePasswordClearPasswordFieldsUseCase(formRepository)

    @Provides
    fun provideChangePasswordCreateFormUseCase(
        changePasswordRequestPasswordAttributesUseCaseContract: ChangePasswordRequestPasswordAttributesUseCaseContract,
        formRepository: FormRepository
    ): ChangePasswordCreateFormUseCaseContract = ChangePasswordCreateFormUseCase(changePasswordRequestPasswordAttributesUseCaseContract, formRepository)

    @Provides
    fun provideChangePasswordGetUserUseCase(
        userDataRepository: UserDataRepository,
        userMapperFromDTO: UserMapperFromDTO
    ): ChangePasswordGetUserUseCaseContract = ChangePasswordGetUserUseCase(userDataRepository, userMapperFromDTO)

    @Provides
    fun provideChangePasswordRequestPasswordAttributesUseCase(
        signUpRepository: SignUpRepository,
        changePasswordGetUserUseCaseContract: ChangePasswordGetUserUseCaseContract
    ): ChangePasswordRequestPasswordAttributesUseCaseContract = ChangePasswordRequestPasswordAttributesUseCase(signUpRepository, changePasswordGetUserUseCaseContract)

    @Provides
    fun provideChangePasswordSaveNewPasswordUseCase(encryptedRepository: UserDataRepositoryImpl): ChangePasswordSaveNewPasswordUseCaseContract = ChangePasswordSaveNewPasswordUseCase(encryptedRepository)

    @Provides
    fun provideContactSupportUseCase(
        contactSupportRepository: ContactSupportRepository,
        userDataRepository: UserDataRepository
    ): ContactSupportUseCase = ContactSupportUseCase(contactSupportRepository, userDataRepository)

    /**-------------------------------------------------------------------------------------------*/
    /**                                                                                           */
    /**      OLD DEPENDENCIES (PREVIOUS MIGRATION). DELETE AFTER MIGRATE THESE CLASSES            */
    /**                                                                                           */
    /**-------------------------------------------------------------------------------------------*/

    @Provides
    fun provideC2BPresenter(context: Context?): C2BContract.Presenter = C2BPresenterImpl(context)

    @Provides
    fun providesQrPresenter(context: Context?): QrPresenter.Presenter = QrPresenterImpl(context)

    @Provides
    fun providesCrypto(): Crypto = Crypto(Options.TRANSFORMATION_SYMMETRIC)

    @Provides
    @Singleton
    fun provideCountriesLocalDataSource(): CountryLocalDataSource = CountryLocalDataSource(app)

    @Provides
    @Singleton
    fun provideBraze(context: Context) = Braze.getInstance(context)
}
