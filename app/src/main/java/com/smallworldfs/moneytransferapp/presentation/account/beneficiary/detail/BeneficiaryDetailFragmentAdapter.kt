package com.smallworldfs.moneytransferapp.presentation.account.beneficiary.detail

import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.presentation.account.beneficiary.detail.fragments.BeneficiaryDetailActivityFragment
import com.smallworldfs.moneytransferapp.presentation.account.beneficiary.detail.fragments.BeneficiaryDetailProfileFragment
import com.smallworldfs.moneytransferapp.utils.INT_ONE
import com.smallworldfs.moneytransferapp.utils.INT_ZERO
import com.smallworldfs.moneytransferapp.utils.widget.StyledTextView

class BeneficiaryDetailFragmentAdapter(private val activity: FragmentActivity, private val tabCount: Int = INT_ZERO) : FragmentStateAdapter(activity) {

    var mListTabsTitles = mutableListOf<String>()

    init {
        mListTabsTitles.add(activity.getString(R.string.tab_beneficiary_detail_profile_title))
        mListTabsTitles.add(activity.getString(R.string.tab_beneficiary_detail_activity_title))
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            INT_ZERO -> {
                return BeneficiaryDetailProfileFragment()
            }
            INT_ONE -> {
                return BeneficiaryDetailActivityFragment()
            }
            else -> Fragment()
        }
    }

    override fun getItemCount(): Int = tabCount

    fun getTabView(position: Int): View? {
        val view: View = LayoutInflater.from(activity).inflate(R.layout.custom_tab_layout, null)
        val textView = view.findViewById<View>(R.id.page_title) as StyledTextView
        if (mListTabsTitles.size > position) {
            textView.text = mListTabsTitles[position]
        }
        return view
    }
}
