package com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.smallworldfs.moneytransferapp.R;
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ScreenCategory;
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ScreenName;
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.UserActionEvent;
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericFragment;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.QuickReminderMessage;
import com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.adapter.QuickReminderAdapter;
import com.smallworldfs.moneytransferapp.utils.Constants;
import com.smallworldfs.moneytransferapp.utils.widget.StyledTextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by luismiguel on 17/11/17.
 */

public class QuickReminderPopupFragment extends GenericFragment {

    public static final String MESSAGES_LIST_EXTRA = "MESSAGES_LIST_EXTRA";
    public static final String TITLE_EXTRA = "TITLE_EXTRA";
    public static final String STYLE_EXTRA = "STYLE_EXTRA";


    Unbinder mUnbinder;

    private ContinueListener mContinueListener;
    private ArrayList<QuickReminderMessage> mMessages;
    private QuickReminderAdapter mAdapter;
    private String mTextTitle;
    private int mStyle = Constants.QUICK_REMINDER_STYLES.DEFAULT;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.quick_reminder_subtitle)
    StyledTextView mTitle;
    @BindView(R.id.root_view)
    RelativeLayout mRootView;
    @BindView(R.id.ll_content_scrollable)
    View mLlContentScrollable;
    @BindView(R.id.pre_title_textview)
    StyledTextView mPretitleTextView;
    @BindView(R.id.action_button)
    StyledTextView mDoneButton;

    public interface ContinueListener {
        void onContinueClick();
    }

    public static QuickReminderPopupFragment getInstance(ArrayList<QuickReminderMessage> messages, String title) {
        QuickReminderPopupFragment fragment = new QuickReminderPopupFragment();
        Bundle args = new Bundle();

        args.putParcelableArrayList(MESSAGES_LIST_EXTRA, messages);
        args.putString(TITLE_EXTRA, title);
        fragment.setArguments(args);
        return fragment;
    }

    public static QuickReminderPopupFragment getInstance(ArrayList<QuickReminderMessage> messages, String title, int style) {
        QuickReminderPopupFragment fragment = new QuickReminderPopupFragment();
        Bundle args = new Bundle();

        args.putParcelableArrayList(MESSAGES_LIST_EXTRA, messages);
        args.putString(TITLE_EXTRA, title);
        args.putInt(STYLE_EXTRA, style);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quick_reminder_popup, null, false);
        mUnbinder = ButterKnife.bind(this, view);

        mRootView.setContentDescription("dialog_area");
        mDoneButton.setContentDescription("quick_reminder_continue_button");

        mMessages = getArguments().getParcelableArrayList(MESSAGES_LIST_EXTRA);
        mTextTitle = getArguments().getString(TITLE_EXTRA);
        mStyle = getArguments().getInt(STYLE_EXTRA);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mMessages != null) {
            // Style view
            if (mStyle == Constants.QUICK_REMINDER_STYLES.GDPR) {
                mLlContentScrollable.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
                mTitle.setVisibility(View.GONE);
                mPretitleTextView.setText(mTextTitle);
                mDoneButton.setText(getString(R.string.preferences_button_quick_reminder_text));
            } else
                trackScreen(ScreenName.MODAL_MAX_AMOUNT.getValue());

            mAdapter = new QuickReminderAdapter(mMessages);
            LinearLayoutManager llManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            mRecyclerView.setLayoutManager(llManager);
            mRecyclerView.setAdapter(mAdapter);

            // Title
            mTitle.setText(mTextTitle);

            // Animate content
            mRootView.animate().alpha(1f);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    public void setOnContinueListener(ContinueListener listener) {
        this.mContinueListener = listener;
    }

    @OnClick(R.id.action_button)
    public void onActionButtonClick() {
        if (mContinueListener != null) {
            mContinueListener.onContinueClick();
        }
        trackEvent(new UserActionEvent(
                ScreenCategory.TRANSFER.getValue(),
                "click_continue",
                "",
                getHierarchy(ScreenName.MODAL_MAX_AMOUNT.getValue()),
                "",
                "",
                "",
                "",
                "",
                ""
        ));
    }
}
