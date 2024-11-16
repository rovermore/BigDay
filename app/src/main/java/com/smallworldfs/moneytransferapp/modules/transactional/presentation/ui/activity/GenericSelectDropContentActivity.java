package com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.activity;

import static com.smallworldfs.moneytransferapp.utils.KeyboardUtils.hideKeyboard;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.smallworldfs.moneytransferapp.R;
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ScreenCategory;
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.UserActionEvent;
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.DialogExt;
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericActivity;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.KeyValueData;
import com.smallworldfs.moneytransferapp.modules.transactional.presentation.presenter.GenericSelectDropContent;
import com.smallworldfs.moneytransferapp.modules.transactional.presentation.presenter.implementation.GenericSelectDropContentPresenterImpl;
import com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.adapter.GenericDropContentAdapter;
import com.smallworldfs.moneytransferapp.utils.ActivityLargeDataTransferUtils;
import com.smallworldfs.moneytransferapp.utils.ConstantsKt;
import com.smallworldfs.moneytransferapp.utils.SamsungMemLeak;
import com.smallworldfs.moneytransferapp.utils.Utils;
import com.smallworldfs.moneytransferapp.utils.widget.DismissibleEditText;
import com.smallworldfs.moneytransferapp.utils.widget.StyledTextView;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.hilt.android.AndroidEntryPoint;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by luismiguel on 7/8/17
 */
@AndroidEntryPoint
public class GenericSelectDropContentActivity extends GenericActivity implements GenericSelectDropContent.View, TextWatcher {

    public static final String CONTENT_LIST_EXTRA = "CONTENT_LIST_EXTRA";
    public static final String CONTENT_STEP_ID = "CONTENT_STEP";
    public static final String CONTENT_FIELD_POSITION = "CONTENT_FIELD_POSITION";
    public static final String FIELD_STEP_TYPE = "FIELD_STEP_TYPE";
    public static final String SELECTED_VALUE = "SELECTED_VALUE";
    public static final String SEARCHABLE_EXTRA = "SEARCHABLE_EXTRA";
    public static final String ACTIVITY_NAME_EXTRA = "ACTIVITY_NAME_EXTRA";
    public static final String TYPE_CELL = "TYPE_CELL";
    public static final String URL_EXTRA_DATA = "URL_EXTRA_DATA";
    public static final String RESULT_DATA = "RESULT";
    public static final String CONTENT_FILE_NAME = "CONTENT_FILE_NAME";
    public static final String SCREEN_NAME = "SCREEN_NAME";
    public static final String FIELD_NAME = "FIELD_NAME";
    public static final String DELIVERY_METHOD = "DELIVERY_METHOD";
    private static final int ONE_SECOND = 1000;

    Unbinder mUnbinder;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.search_container)
    RelativeLayout mSearchContainer;
    @BindView(R.id.search_edit_text)
    DismissibleEditText mSearchEditText;
    @BindView(R.id.search_empty_view)
    View mEmptyView;
    @BindView(R.id.error_view_title)
    StyledTextView mSearchTextTitle;
    @BindView(R.id.general_loading_view)
    View mLoadingView;
    private GenericSelectDropContentPresenterImpl mPresenter;
    private ArrayList<KeyValueData> mData;
    private String mStepIdSourceData;
    private int mPositionField;
    private String mSelectedValue;
    private String mStepType;
    private boolean mSearchable;
    private String mNameScreen;
    private String mTypeCell;
    private GenericDropContentAdapter mAdapter;
    private String mUrlApi;
    private String mDataFileName;
    private Subscriber<String> mFileReaderSubscriber;
    private String fieldName = ConstantsKt.STRING_EMPTY;
    private String analyticsScreenName = ConstantsKt.STRING_EMPTY;
    private String processCategory = ConstantsKt.STRING_EMPTY;
    private long lastTimeEventSend = 0L;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_drop_content);

        mUnbinder = ButterKnife.bind(this);
        if (getIntent().getExtras() != null) {
            mStepIdSourceData = getIntent().getStringExtra(CONTENT_STEP_ID);
            if (getIntent().getStringExtra(SCREEN_NAME) != null) {
                analyticsScreenName = getIntent().getStringExtra(SCREEN_NAME);
                trackScreen(analyticsScreenName);
            }
            mPositionField = getIntent().getIntExtra(CONTENT_FIELD_POSITION, -1);
            mStepType = getIntent().getStringExtra(FIELD_STEP_TYPE);
            mSelectedValue = getIntent().getStringExtra(SELECTED_VALUE);
            mSearchable = getIntent().getBooleanExtra(SEARCHABLE_EXTRA, false);
            mNameScreen = getIntent().getStringExtra(ACTIVITY_NAME_EXTRA);
            mTypeCell = getIntent().getStringExtra(TYPE_CELL);
            mUrlApi = getIntent().getStringExtra(URL_EXTRA_DATA);
            mDataFileName = getIntent().getStringExtra(CONTENT_FILE_NAME);
            if (getIntent().getStringExtra(FIELD_NAME) != null)
                fieldName = getIntent().getStringExtra(FIELD_NAME);
            if (getIntent().getStringExtra(DELIVERY_METHOD) != null)
                processCategory = getIntent().getStringExtra(DELIVERY_METHOD);
        }
        mPresenter = new GenericSelectDropContentPresenterImpl(AndroidSchedulers.mainThread(), Schedulers.io(), this, this, mUrlApi, mNameScreen, this);
        mPresenter.create();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mData != null) {
            mPresenter.onDataReceived(Utils.convertKeyListValueToTreeMap(mData));
        } else if (mDataFileName != null) {
            if (mFileReaderSubscriber == null || mFileReaderSubscriber.isUnsubscribed()) {
                mFileReaderSubscriber = new FileReaderSubscriber();
            }
            ActivityLargeDataTransferUtils.readAndDeleteFile(this, mDataFileName, mFileReaderSubscriber);
            mDataFileName = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPresenter != null) {
            mPresenter.resume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mPresenter != null) {
            mPresenter.pause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mFileReaderSubscriber != null && !mFileReaderSubscriber.isUnsubscribed()) mFileReaderSubscriber.unsubscribe();
    }

    @Override
    public void configureView() {

        mToolbar.setBackgroundColor(getResources().getColor(R.color.default_grey_control));
        mToolbar.findViewById(R.id.left_title).setVisibility(View.VISIBLE);
        ((StyledTextView) mToolbar.findViewById(R.id.left_title)).setTextColor(getResources().getColor(R.color.dark_grey_text));
        mToolbar.findViewById(R.id.genericToolbarTitle).setVisibility(View.GONE);
        String title = mNameScreen;
        if (mNameScreen.length() > 1)
            title = mNameScreen.substring(0, 1).toUpperCase(Locale.getDefault()) + mNameScreen.substring(1);
        ((StyledTextView) mToolbar.findViewById(R.id.left_title)).setText(title);
        mToolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_action_arrow_back_grey));

        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);

        if (!mSearchable) {
            mSearchContainer.setVisibility(View.GONE);
        } else {
            // Configure search container and listeners
            Typeface type = ResourcesCompat.getFont(this, R.font.nunito_semi_bold);
            mSearchTextTitle.setText(getString(R.string.generic_title_empty_view_search_content));

            mSearchEditText.setTypeface(type);

            // Search Edit TextWatcher Listener
            mSearchEditText.addTextChangedListener(this);

        }

    }

    @Override
    public void onFilterApplied(ArrayList<KeyValueData> data) {
        if (mAdapter != null) {
            mAdapter.updateData(data);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        registerEvent("click_back", ConstantsKt.STRING_EMPTY, "");
        hideKeyboard(this, mSearchEditText);
        cancelAction();
        GenericSelectDropContentActivity.this.overridePendingTransition(R.anim.from_left_to_right, R.anim.from_position_to_right);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null)
            mPresenter.destroy();
        mUnbinder.unbind();
        SamsungMemLeak.onDestroy(getApplicationContext());
    }

    private void returnData(KeyValueData data) {
        hideKeyboard(this, mSearchEditText);
        Intent returnIntent = new Intent();
        returnIntent.putExtra(RESULT_DATA, data);
        returnIntent.putExtra(CONTENT_FIELD_POSITION, mPositionField);
        returnIntent.putExtra(CONTENT_STEP_ID, mStepIdSourceData);
        returnIntent.putExtra(FIELD_STEP_TYPE, mStepType);

        setResult(Activity.RESULT_OK, returnIntent);
        finish();
        GenericSelectDropContentActivity.this.overridePendingTransition(R.anim.from_left_to_right, R.anim.from_position_to_right);

    }

    private void registerEvent(String eventAction, String eventLabel, String formType) {
        trackEvent(
                new UserActionEvent(
                        ScreenCategory.TRANSFER.getValue(),
                        eventAction,
                        eventLabel,
                        getHierarchy(analyticsScreenName),
                        formType,
                        processCategory,
                        "",
                        "",
                        "",
                        ""
                )
        );
    }

    private void cancelAction() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence text, int start, int before, int count) {

        mPresenter.performSearch(text.toString());
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() > 2 && (System.currentTimeMillis() - lastTimeEventSend) > ONE_SECOND) {
            registerEvent(
                    "search_" + fieldName,
                    s.toString(),
                    "search"
            );
            lastTimeEventSend = System.currentTimeMillis();
        }
    }

    @Override
    public void showSearchEmptyView() {
        mEmptyView.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);

    }

    @Override
    public void setupAdapter(ArrayList<KeyValueData> data) {
        if (data != null) {
            mData = data;
        }
        if (mData != null) {
            mAdapter = new GenericDropContentAdapter(this, this.mData, mSelectedValue, mTypeCell);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setSelectDataItemListener(data1 -> {
                if (data1 != null) {
                    registerEvent("click_" + fieldName + "_list", data1.getValue(), "");
                    returnData(data1);
                }
            });
        }
    }

    @Override
    public void hideSearchEmptyView() {
        mEmptyView.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoadingView() {
        mLoadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingView() {
        mLoadingView.setVisibility(View.GONE);
    }

    private final class FileReaderSubscriber extends Subscriber<String> {

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(String s) {
            final ArrayList<TreeMap<String, String>> fileData =
                    new Gson().fromJson(s, new TypeToken<ArrayList<TreeMap<String, String>>>() {
                    }.getType());
            mData = Utils.convertMapToKeyValueList(fileData);
            mPresenter.onDataReceived(Utils.convertKeyListValueToTreeMap(mData));
        }
    }


    /**
     * If there is no information available to choose
     */
    private Dialog mProgressDialog;

    @Override
    public void showDialogAndFinish() {
        if (this.mProgressDialog == null && mUrlApi != null && !mUrlApi.isEmpty() && mUrlApi.contains("branches")) {
            this.mProgressDialog = new DialogExt().showSingleActionInfoDialog(this, getString(R.string.change_payment_method_dialog_text_title), getString(R.string.generic_select_drop_content_no_data_dialog_body), getString(R.string.generic_select_drop_content_no_data_dialog_button), this::dismissDialogAndFinishActivity, this::dismissDialogAndFinishActivity);
        } else {
            finish();
        }
    }

    private void dismissDialogAndFinishActivity() {
        this.mProgressDialog.dismiss();
        this.mProgressDialog = null;
        finish();
    }
}
