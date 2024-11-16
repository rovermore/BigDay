package com.smallworldfs.moneytransferapp.compose.widgets

sealed class CountryListType {

    object SendMoneyFrom : CountryListType()
    object SendMoneyTo : CountryListType()
}
