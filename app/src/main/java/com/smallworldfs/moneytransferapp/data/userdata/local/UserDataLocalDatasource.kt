package com.smallworldfs.moneytransferapp.data.userdata.local

import com.google.gson.GsonBuilder
import com.smallworldfs.moneytransferapp.data.common.datastore.UserDataStore
import com.smallworldfs.moneytransferapp.data.common.encrypted.CryptoManager
import com.smallworldfs.moneytransferapp.data.login.mappers.UserDTOMapper
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.base.map
import com.smallworldfs.moneytransferapp.domain.migrated.base.mapFailure
import com.smallworldfs.moneytransferapp.domain.migrated.base.peekFailure
import com.smallworldfs.moneytransferapp.domain.migrated.base.recover
import com.smallworldfs.moneytransferapp.domain.migrated.login.model.PassCodeDTO
import com.smallworldfs.moneytransferapp.domain.migrated.login.model.PasswordDTO
import com.smallworldfs.moneytransferapp.domain.migrated.login.model.UserDTO
import com.smallworldfs.moneytransferapp.modules.login.domain.model.User
import com.smallworldfs.moneytransferapp.utils.parseJSON
import javax.inject.Inject

class UserDataLocalDatasource @Inject constructor(
    private val userDataStore: UserDataStore,
    private val cryptoManager: CryptoManager,
    private val userDTOMapper: UserDTOMapper
) {

    companion object {
        const val PASSCODE_KEY = "PASSCODE"
        const val PASSWORD_KEY = "PASSWORD"
        const val USER_KEY = "USER"
        const val CURRENT_USER_ALIAS = "CURRENT_USER"
        const val FINGERPRINT_KEY = "FINGERPRINT_SET"
        const val TEMP_PASSWORD_KEY = "temp_password_key"
        const val PASSWORD_TEMP_KEY = "password_temp_key"
        const val WELCOME_KEY = "WELCOME"
        const val UPLOAD_DIALOG_KEY = "UPLOAD_DIALOG_KEY"
        const val SMS_CONSENT_SHOWN = "SMS_CONSENT_SHOWN"
        const val LAST_PRIME_FOR_PUSH_EVENT_TIMESTAMP = "LAST_PRIME_FOR_PUSH_EVENT_TIMESTAMP"
    }

    /**
     * Pass Code
     */
    fun savePassCode(passCode: PassCodeDTO): OperationResult<Boolean, Error> {
        return userDataStore.save(
            PASSCODE_KEY,
            cryptoManager.encryptData(PASSCODE_KEY, passCode.code),
        )
    }

    fun retrievePassCode(): OperationResult<PassCodeDTO, Error> {
        return userDataStore.read<String>(PASSCODE_KEY)
            .map {
                PassCodeDTO(
                    cryptoManager.decryptData(
                        PASSCODE_KEY,
                        it.toCharArray(),
                    ),
                )
            }
    }

    fun removePassCode(): OperationResult<Boolean, Error> {
        return userDataStore.delete<String>(PASSCODE_KEY)
            .map {
                cryptoManager.deleteKey(PASSCODE_KEY)
                return Success(it)
            }
    }

    /**
     * Password
     */
    fun savePassword(passwordDTO: PasswordDTO): OperationResult<Boolean, Error> {
        val passwordObject = GsonBuilder().create().toJson(passwordDTO, PasswordDTO::class.java)
        return userDataStore.save(
            PASSWORD_KEY,
            cryptoManager.encryptData(PASSWORD_KEY, passwordObject.toCharArray()),
        )
    }

    fun retrievePassword(): OperationResult<PasswordDTO, Error> {
        return userDataStore.read<String>(PASSWORD_KEY)
            .map {
                val decryptedPassword = cryptoManager.decryptData(
                    PASSWORD_KEY,
                    it.toCharArray(),
                )
                val decryptedPasswordString = String(decryptedPassword)
                return decryptedPasswordString.parseJSON<PasswordDTO>()
            }
    }

    fun removePassword(): OperationResult<Boolean, Error> {
        return userDataStore.delete<String>(PASSWORD_KEY)
            .map {
                cryptoManager.deleteKey(PASSWORD_KEY)
                return Success(it)
            }
    }

    /**
     * User
     */
    fun saveUser(userDTO: UserDTO): OperationResult<Boolean, Error> {
        val userObject = GsonBuilder().create().toJson(userDTO, UserDTO::class.java)
        return userDataStore.save(
            USER_KEY,
            cryptoManager.encryptData(USER_KEY, userObject.toCharArray()),
        )
    }

    fun retrieveUser(): OperationResult<UserDTO, Error> {
        return userDataStore.read<String>(USER_KEY).map {
            val decryptedUser = cryptoManager.decryptData(USER_KEY, it.toCharArray())
            val decryptedUserString = String(decryptedUser)
            return decryptedUserString.parseJSON<UserDTO>().peekFailure {
                return decryptedUserString.parseJSON<User>().map { user ->
                    return userDTOMapper.map(user)
                }
            }
        }
    }

    fun removeUser(): OperationResult<Boolean, Error> {
        return userDataStore.delete<String>(USER_KEY)
            .map {
                cryptoManager.deleteKey(USER_KEY)
                return Success(it)
            }
    }

    fun isWelcomeShow(): Boolean {
        userDataStore.read<Boolean>(WELCOME_KEY)
            .map {
                return it
            }
            .mapFailure {
                return false
            }
        return false
    }

    fun setWelcomeShown(shown: Boolean) {
        userDataStore.save(WELCOME_KEY, shown)
    }

    fun isUploadDialogShown(): Boolean {
        userDataStore.read<Boolean>(UPLOAD_DIALOG_KEY)
            .map {
                return it
            }.mapFailure {
                return false
            }
        return false
    }

    fun setUploadDialogShown(shown: Boolean) {
        userDataStore.save(UPLOAD_DIALOG_KEY, shown)
    }

    // TODO remove when QuickLoginSettingsMigration finishes
    fun isFingerPrintEnabled(): OperationResult<Boolean, Error> {
        return userDataStore.read(FINGERPRINT_KEY)
    }

    fun setSMSConsentShown(consent: Boolean) {
        userDataStore.save(SMS_CONSENT_SHOWN, consent)
    }

    fun getSMSConsentShown(): Boolean = userDataStore.read<Boolean>(SMS_CONSENT_SHOWN).map { it }.recover { false }

    fun setLastPrimeForPushEventTimestamp(timestamp: Long) {
        userDataStore.save(LAST_PRIME_FOR_PUSH_EVENT_TIMESTAMP, timestamp)
    }

    fun getLastPrimeForPushEventTimestamp(): Long = userDataStore.read<Long>(LAST_PRIME_FOR_PUSH_EVENT_TIMESTAMP).map { it }.recover { 0L }
}
