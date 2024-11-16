package com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.smallworldfs.moneytransferapp.R;
import com.smallworldfs.moneytransferapp.utils.widget.StyledTextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by luismiguel on 19/6/17.
 */

public class HomePageFragmentAdapter extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    int tabCount;
    private Context mContext;
    private List<String> mListTabsTitles;
    private ArrayList<Fragment> mListFragments;

    //Constructor to the class
    public HomePageFragmentAdapter(FragmentManager fm, int tabCount, Context context, ArrayList<Fragment> data, String[] tabsTitles) {
        super(fm);
        //Initializing tab count
        this.tabCount = tabCount;
        this.mContext = context;
        this.mListTabsTitles = Arrays.asList(tabsTitles);
        this.mListFragments = data;

    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        return mListFragments.get(position);
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return tabCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return null;
    }

    public View getTabView(int position) {
        // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
        View v = LayoutInflater.from(mContext).inflate(R.layout.custom_tab_layout, null);
        StyledTextView tv = (StyledTextView) v.findViewById(R.id.page_title);
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tv.setText(mListTabsTitles.get(position));
        setupContentDescription(v, position);
        return v;
    }

    public void setupContentDescription(View view, int position) {
        switch (position) {
            case 0:
                view.setContentDescription("sendTo");
                break;
            case 1:
                view.setContentDescription("status");
                break;
            case 2:
                view.setContentDescription("account");
                break;
            default:
                break;
        }
    }

    public void setCount(int position, int count){
        View view = getTabView(position);
        view.findViewById(R.id.bubble_container).setVisibility(View.VISIBLE);
        ((StyledTextView) view.findViewById(R.id.count_bubble_indicator)).setText(String.valueOf(count));
    }
}
