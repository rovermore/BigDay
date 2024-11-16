package com.smallworldfs.moneytransferapp.presentation.form.adapter

import com.smallworldfs.moneytransferapp.base.presentation.navigator.BaseNavigator
import com.smallworldfs.moneytransferapp.modules.customization.presentation.ui.fragment.ChooseCountryFromFragment
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FormNavigator @Inject constructor() : BaseNavigator() {

    fun showCountrySelector() {
        val fragment = ChooseCountryFromFragment()
        val transaction = activityRef.get()?.supportFragmentManager?.beginTransaction()
        val fragment1 = activityRef.get()?.supportFragmentManager?.findFragmentByTag(fragment.javaClass.simpleName)
        if (fragment1 == null) {
            transaction?.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
            transaction?.add(android.R.id.content, fragment, fragment.javaClass.simpleName)
            transaction?.addToBackStack(fragment.javaClass.simpleName)
            transaction?.commit()
        }
    }
}
