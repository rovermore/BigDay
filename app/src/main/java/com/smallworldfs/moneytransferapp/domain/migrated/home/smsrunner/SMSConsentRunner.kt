package com.smallworldfs.moneytransferapp.domain.migrated.home.smsrunner

import android.os.Handler
import android.os.Looper
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository
import com.smallworldfs.moneytransferapp.modules.login.domain.model.User
import com.smallworldfs.moneytransferapp.modules.register.domain.model.Form
import com.smallworldfs.moneytransferapp.utils.Constants
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Inject

class SMSConsentRunner @Inject constructor(
    private val userDataRepository: UserDataRepository
) {

    private val executor: Executor = Executors.newSingleThreadExecutor()
    private val handler: Handler = Handler(Looper.getMainLooper())

    fun checkSMSConsent(user: User, callback: (Boolean) -> Unit) {
        executor.execute {
            val result =
                try {
                    if (user.country.firstEntry() != null && user.status != Constants.UserType.LIMITED) {
                        val country = user.country.firstEntry()?.key
                        var smsConsented = false
                        val marketingPreferences: OperationResult<Form, Error> = userDataRepository.requestMarketingPreferences("")
                        if (marketingPreferences is Success<*>) {
                            val fields = (marketingPreferences as Success<Form>).value.fields
                            for (field in fields) {
                                if (field.name == "accept_sms" && field.value == "1") {
                                    smsConsented = true
                                }
                            }
                        }
                        if (country != null && country == Constants.COUNTRY.US_COUNTRY_VALUE && !userDataRepository.getSMSConsentShown() && !smsConsented) {
                            true
                        } else {
                            false
                        }
                    } else {
                        false
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    false
                }
            handler.post {
                callback(result)
            }
        }
    }
}
