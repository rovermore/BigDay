package com.smallworldfs.moneytransferapp.di

import com.smallworldfs.moneytransferapp.data.account.account.AccountRepositoryImpl
import com.smallworldfs.moneytransferapp.data.account.beneficiary.repository.BeneficiaryRepositoryImpl
import com.smallworldfs.moneytransferapp.data.account.contactsupport.repository.ContactSupportRepositoryImpl
import com.smallworldfs.moneytransferapp.data.account.documents.repository.DocumentsRepositoryImpl
import com.smallworldfs.moneytransferapp.data.account.profile.repository.EditProfileRepositoryImpl
import com.smallworldfs.moneytransferapp.data.address.repository.AddressRepositoryImpl
import com.smallworldfs.moneytransferapp.data.autentix.repository.AutentixRepositoryImpl
import com.smallworldfs.moneytransferapp.data.braze.BrazeRepository
import com.smallworldfs.moneytransferapp.data.braze.BrazeRepositoryImpl
import com.smallworldfs.moneytransferapp.data.calculator.repository.CalculatorRepositoryImpl
import com.smallworldfs.moneytransferapp.data.common.resources.country.repository.CountryRepositoryImpl
import com.smallworldfs.moneytransferapp.data.forgotpassword.repository.ForgotPasswordRepositoryImpl
import com.smallworldfs.moneytransferapp.data.form.FormRepositoryImpl
import com.smallworldfs.moneytransferapp.data.mtn.MTNRepositoryImpl
import com.smallworldfs.moneytransferapp.data.oauth.repository.OAuthRepositoryImpl
import com.smallworldfs.moneytransferapp.data.offices.repository.OfficesRepositoryImpl
import com.smallworldfs.moneytransferapp.data.operations.repository.OperationsRepositoryImpl
import com.smallworldfs.moneytransferapp.data.promotions.PromotionsRepositoryImpl
import com.smallworldfs.moneytransferapp.data.resetpassword.repository.ResetPasswordRepositoryImpl
import com.smallworldfs.moneytransferapp.data.settings.repository.SettingsRepositoryImpl
import com.smallworldfs.moneytransferapp.data.status.repository.StatusRepositoryImpl
import com.smallworldfs.moneytransferapp.data.transactions.repository.TransactionsRepositoryImpl
import com.smallworldfs.moneytransferapp.data.userdata.repository.UserDataRepositoryImpl
import com.smallworldfs.moneytransferapp.domain.migrated.account.AccountRepository
import com.smallworldfs.moneytransferapp.domain.migrated.account.beneficiary.repository.BeneficiaryRepository
import com.smallworldfs.moneytransferapp.domain.migrated.account.contactsupport.repository.ContactSupportRepository
import com.smallworldfs.moneytransferapp.domain.migrated.account.documents.repository.DocumentsRepository
import com.smallworldfs.moneytransferapp.domain.migrated.account.profile.edit.repository.EditProfileRepository
import com.smallworldfs.moneytransferapp.domain.migrated.addess.repository.AddressRepository
import com.smallworldfs.moneytransferapp.domain.migrated.autentix.repository.AutentixRepository
import com.smallworldfs.moneytransferapp.domain.migrated.calculator.repository.CalculatorRepository
import com.smallworldfs.moneytransferapp.domain.migrated.capabilities.LocationRepository
import com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.repository.CountryRepository
import com.smallworldfs.moneytransferapp.domain.migrated.forgotpassword.repository.ForgotPasswordRepository
import com.smallworldfs.moneytransferapp.domain.migrated.form.repository.FormRepository
import com.smallworldfs.moneytransferapp.domain.migrated.mtn.repository.MTNRepository
import com.smallworldfs.moneytransferapp.domain.migrated.oauth.repository.OAuthRepository
import com.smallworldfs.moneytransferapp.domain.migrated.offices.repository.OfficesRepository
import com.smallworldfs.moneytransferapp.domain.migrated.operations.repository.OperationsRepository
import com.smallworldfs.moneytransferapp.domain.migrated.promotions.repository.PromotionsRepository
import com.smallworldfs.moneytransferapp.domain.migrated.resetpassword.repository.ResetPasswordRepository
import com.smallworldfs.moneytransferapp.domain.migrated.settings.repository.SettingsRepository
import com.smallworldfs.moneytransferapp.domain.migrated.status.repository.StatusRepository
import com.smallworldfs.moneytransferapp.domain.migrated.transactions.repository.TransactionsRepository
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository
import com.smallworldfs.moneytransferapp.presentation.capabilities.LocationRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {

    @Binds
    abstract fun bindSettingsRepository(impl: SettingsRepositoryImpl): SettingsRepository

    @Binds
    abstract fun bindTransactionsRepository(impl: TransactionsRepositoryImpl): TransactionsRepository

    @Binds
    abstract fun bindUserDataRepository(impl: UserDataRepositoryImpl): UserDataRepository

    @Binds
    abstract fun bindCountryRepository(impl: CountryRepositoryImpl): CountryRepository

    @Binds
    abstract fun bindOperationsRepository(impl: OperationsRepositoryImpl): OperationsRepository

    @Binds
    abstract fun bindAddressRepository(impl: AddressRepositoryImpl): AddressRepository

    @Binds
    abstract fun bindFormRepository(impl: FormRepositoryImpl): FormRepository

    @Binds
    abstract fun bindOfficesRepository(impl: OfficesRepositoryImpl): OfficesRepository

    @Binds
    abstract fun bindAccountRepository(impl: AccountRepositoryImpl): AccountRepository

    @Binds
    abstract fun bindBeneficiaryRepository(impl: BeneficiaryRepositoryImpl): BeneficiaryRepository

    @Binds
    abstract fun bindDocumentsRepository(impl: DocumentsRepositoryImpl): DocumentsRepository

    @Binds
    abstract fun bindEditProfileRepository(impl: EditProfileRepositoryImpl): EditProfileRepository

    @Binds
    abstract fun bindContactSupportRepository(impl: ContactSupportRepositoryImpl): ContactSupportRepository

    @Binds
    abstract fun bindStatusRepository(impl: StatusRepositoryImpl): StatusRepository

    @Binds
    abstract fun bindCalculatorRepository(impl: CalculatorRepositoryImpl): CalculatorRepository

    @Binds
    abstract fun bindLocationRepository(impl: LocationRepositoryImpl): LocationRepository

    @Binds
    abstract fun bindOAuthRepository(impl: OAuthRepositoryImpl): OAuthRepository

    @Binds
    abstract fun bindMtnRepository(impl: MTNRepositoryImpl): MTNRepository

    @Binds
    abstract fun bindAutentixRepository(impl: AutentixRepositoryImpl): AutentixRepository

    @Binds
    abstract fun bindBrazeRepository(impl: BrazeRepositoryImpl): BrazeRepository

    @Binds
    abstract fun bindForgotPasswordRepository(impl: ForgotPasswordRepositoryImpl): ForgotPasswordRepository

    @Binds
    abstract fun bindResetPasswordRepository(impl: ResetPasswordRepositoryImpl): ResetPasswordRepository

    @Binds
    abstract fun bindPromotionsRepository(impl: PromotionsRepositoryImpl): PromotionsRepository
}
