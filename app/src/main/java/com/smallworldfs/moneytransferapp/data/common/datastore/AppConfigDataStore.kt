package com.smallworldfs.moneytransferapp.data.common.datastore

import android.content.Context
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

private val Context.dataStore by preferencesDataStore(
    name = "SMALLWORLD_APP_CONFIG_DATA_STORE"
)

@Singleton
class AppConfigDataStore @Inject constructor(context: Context) {

    private val appConfigDataStore = context.dataStore

    @Suppress("NON_PUBLIC_CALL_FROM_PUBLIC_INLINE")
    inline fun <reified T> save(key: String, value: T): OperationResult<Boolean, Error> {
        return try {
            val keyString = getKey<T>(key)
            runBlocking {
                appConfigDataStore.edit { userData ->
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
                appConfigDataStore.data.map { preferences ->
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
                appConfigDataStore.edit { userData ->
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
                appConfigDataStore.edit { userData ->
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
