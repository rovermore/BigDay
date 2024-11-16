package com.smallworldfs.moneytransferapp.data.common.resources.country.repository.local

import android.content.Context
import android.telephony.TelephonyManager
import android.text.TextUtils
import com.smallworldfs.moneytransferapp.data.common.resources.country.model.CountryListEntity
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CountryLocalDataSource @Inject constructor(
    private val context: Context
) {
    private var countries: CountryListEntity = CountryListEntity()
    private var originCountries: CountryListEntity = CountryListEntity()

    fun setCountries(countries: CountryListEntity) {
        this.countries = countries
    }

    fun getCountries(): OperationResult<CountryListEntity, Error> =
        if (countries.countries.isNotEmpty()) {
            Success(countries)
        } else {
            Failure(Error.UncompletedOperation("Countries not found in cache"))
        }

    fun setOriginCountries(originCountries: CountryListEntity) {
        this.originCountries = originCountries
    }

    fun getOriginCountries(): OperationResult<CountryListEntity, Error> =
        if (originCountries.countries.isNotEmpty()) {
            Success(originCountries)
        } else {
            Failure(Error.UncompletedOperation("Origin countries not found in cache"))
        }

    fun getCountryFromPhoneNumber(): OperationResult<String, Error> {
        var countryStr = (context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).networkCountryIso
        if (TextUtils.isEmpty(countryStr)) {
            countryStr = Locale.getDefault().country
        }
        val countryISO3: String
        if (countryStr.isNotEmpty()) {
            val countryCode = countryStr.toUpperCase(Locale.getDefault())

            val locale = Locale(STRING_EMPTY, countryCode)
            countryISO3 = locale.isO3Country
        } else return Failure(Error.UncompletedOperation("Country list not found in cache"))

        return Success(countryISO3)
    }
}
