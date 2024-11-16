package com.smallworldfs.moneytransferapp.presentation.account.beneficiary.list

import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.presentation.account.beneficiary.list.fragments.all.BeneficiaryListAllFragment
import com.smallworldfs.moneytransferapp.presentation.account.beneficiary.list.fragments.filtered.BeneficiaryListFilteredFragment
import com.smallworldfs.moneytransferapp.utils.INT_ONE
import com.smallworldfs.moneytransferapp.utils.INT_ZERO
import com.smallworldfs.moneytransferapp.utils.widget.StyledTextView

class BeneficiaryListFragmentAdapter(private val activity: FragmentActivity, private val tabCount: Int = INT_ZERO) : FragmentStateAdapter(activity) {

    var mListTabsTitles = mutableListOf<String>()

    init {
        mListTabsTitles.add(activity.getString(R.string.tab_my_beneficiaries_all_title))
        mListTabsTitles.add(activity.getString(R.string.tab_my_beneficiaries_delivery_title))
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            INT_ZERO -> BeneficiaryListAllFragment()
            INT_ONE -> BeneficiaryListFilteredFragment()
            else -> Fragment()
        }
    }

    override fun getItemCount(): Int = tabCount

    fun getTabView(position: Int): View {
        val view: View = LayoutInflater.from(activity).inflate(R.layout.custom_tab_layout, null)
        val textView = view.findViewById<View>(R.id.page_title) as StyledTextView
        if (mListTabsTitles.size > position) {
            textView.text = mListTabsTitles[position]
        }
        return view
    }
}
