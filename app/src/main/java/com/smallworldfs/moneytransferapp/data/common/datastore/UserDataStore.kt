package com.smallworldfs.moneytransferapp.data.common.datastore

import android.content.Context
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

private val smallWorldEncryptedKeys: Set<String> = setOf(
    "PASSCODE",
    "PASSWORD",
    "USER",
    "LIMITED_USER",
    "CURRENT_USER",
    "FINGERPRINT_SET",
    "FINGERPRINT_ENABLED_BY_USER",
    "passcode",
    "password",
    "user",
    "limited_user",
    "current_user"
)

private val Context.dataStore by preferencesDataStore(
    name = "SMALLWORLD_DATA_STORE",
    produceMigrations = { context ->
        listOf(
            SharedPreferencesMigration(context, "SMALLWORLD", smallWorldEncryptedKeys),
            SharedPreferencesMigration(context, "SMALLWORLD_ENCRYPTED", smallWorldEncryptedKeys)
        )
    }
)

@Singleton
class UserDataStore @Inject constructor(context: Context) {

    private val userDataStore = context.dataStore

    @Suppress("NON_PUBLIC_CALL_FROM_PUBLIC_INLINE")
    inline fun <reified T> save(key: String, value: T): OperationResult<Boolean, Error> {
        return try {
            val keyString = getKey<T>(key)
            runBlocking {
                userDataStore.edit { userData ->
                    userData[keyString] = value
                }
            }
            Success(true)
        } catch (e: InterruptedException) {
            Failure(Error.UnsupportedOperation(e.message ?: "Error saving data"))
        } catch (e: ClassNotFoundException) {
            Failure(Error.UnsupportedOperation(e.message ?: "Error saving data"))
        }
    }

    @Suppress("NON_PUBLIC_CALL_FROM_PUBLIC_INLINE")
    inline fun <reified T> read(key: String): OperationResult<T, Error> {
        return try {
            val keyString = getKey<T>(key)
            val value = runBlocking {
                userDataStore.data.map { preferences ->
                    preferences[keyString]
                }.firstOrNull()
            }
            if (value == null)
                Failure(Error.UncompletedOperation("Data couldn't be read"))
            else
                Success(value)
        } catch (e: InterruptedException) {
            Failure(Error.UnsupportedOperation(e.message ?: "Error reading data"))
        } catch (e: ClassNotFoundException) {
            Failure(Error.UnsupportedOperation(e.message ?: "Error reading data"))
        }
    }

    @Suppress("NON_PUBLIC_CALL_FROM_PUBLIC_INLINE")
    inline fun <reified T> update(key: String, value: T): OperationResult<Boolean, Error> {
        return try {
            val keyString = getKey<T>(key)
            runBlocking {
                userDataStore.edit { userData ->
                    userData[keyString] = value
                }
            }
            Success(true)
        } catch (e: InterruptedException) {
            Failure(Error.UnsupportedOperation(e.message ?: "Error saving data"))
        } catch (e: ClassNotFoundException) {
            Failure(Error.UnsupportedOperation(e.message ?: "Error saving data"))
        }
    }

    @Suppress("NON_PUBLIC_CALL_FROM_PUBLIC_INLINE")
    inline fun <reified T> delete(key: String): OperationResult<Boolean, Error> {
        return try {
            val keyString = getKey<T>(key)
            runBlocking {
                userDataStore.edit { userData ->
                    userData.remove(keyString)
                }
            }
            Success(true)
        } catch (e: InterruptedException) {
            Failure(Error.UnsupportedOperation(e.message ?: "Error deleting data"))
        } catch (e: ClassNotFoundException) {
            Failure(Error.UnsupportedOperation(e.message ?: "Error deleting data"))
        }
    }

    @Throws(ClassNotFoundException::class)
    inline fun <reified T> getKey(key: String): Preferences.Key<T> {
        return when (T::class) {
            String::class -> stringPreferencesKey(key) as Preferences.Key<T>
            Boolean::class -> booleanPreferencesKey(key) as Preferences.Key<T>
            Float::class -> floatPreferencesKey(key) as Preferences.Key<T>
            Long::class -> longPreferencesKey(key) as Preferences.Key<T>
            else -> throw ClassNotFoundException()
        }
    }
}
