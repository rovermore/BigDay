package com.smallworldfs.moneytransferapp.presentation.softregister

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericActivity
import com.smallworldfs.moneytransferapp.presentation.softregister.credentials.RegisterCredentialsFragment
import com.smallworldfs.moneytransferapp.presentation.softregister.phone.RegisterPhoneFragment
import com.smallworldfs.moneytransferapp.presentation.softregister.profile.RegisterProfileFragment
import com.smallworldfs.moneytransferapp.utils.INT_THREE

class SignupPagerAdapter(activity: GenericActivity) : FragmentStateAdapter(activity) {

    companion object {
        const val REGISTER_USER_CREDENTIALS_PAGE = 0
        const val REGISTER_PHONE_PAGE = 1
        const val REGISTER_DATA_PAGE = 2
    }

    override fun getItemCount(): Int = INT_THREE

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            REGISTER_USER_CREDENTIALS_PAGE -> {
                return RegisterCredentialsFragment()
            }
            REGISTER_PHONE_PAGE -> {
                return RegisterPhoneFragment()
            }
            REGISTER_DATA_PAGE -> {
                return RegisterProfileFragment()
            }
            else -> Fragment()
        }
    }
}
