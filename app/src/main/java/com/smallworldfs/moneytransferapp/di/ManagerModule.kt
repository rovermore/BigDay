package com.smallworldfs.moneytransferapp.di

import com.smallworldfs.moneytransferapp.data.common.encrypted.CryptoManager
import com.smallworldfs.moneytransferapp.data.common.encrypted.LegacyCryptoManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class ManagerModule {

    @Binds
    abstract fun bindCryptoManager(impl: LegacyCryptoManager): CryptoManager
}
