package com.smallworldfs.moneytransferapp.modules.status.presentation.ui.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

import com.smallworldfs.moneytransferapp.R;
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericActivity;

import com.smallworldfs.moneytransferapp.modules.status.domain.model.Bank;
import com.smallworldfs.moneytransferapp.modules.status.presentation.presenters.SelectBankChangePaymentPresenter;
import com.smallworldfs.moneytransferapp.modules.status.presentation.presenters.implementation.SelectBanlChangePaymentPresenterImpl;
import com.smallworldfs.moneytransferapp.modules.status.presentation.ui.adapter.SelectBankAdapter;
import com.smallworldfs.moneytransferapp.utils.widget.StyledTextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.hilt.android.AndroidEntryPoint;

/**
 * Created by luismiguel on 5/10/17.
 */
@AndroidEntryPoint
public class SelectBankChangePaymentActivity extends GenericActivity implements SelectBankChangePaymentPresenter.View {

    public static final String BANK_DATA_EXTRA = "BANK_DATA_EXTRA";
    public static final String RESULT_DATA = "RESULT_DATA";


    Unbinder mUnbinder;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;

    private SelectBanlChangePaymentPresenterImpl mPresenter;
    private ArrayList<Bank> mBankDataList;
    private SelectBankAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_bank_payment);

        mUnbinder = ButterKnife.bind(this);

        if(getIntent().getExtras() != null){
            mBankDataList = getIntent().getParcelableArrayListExtra(BANK_DATA_EXTRA);
        }

        mPresenter = new SelectBanlChangePaymentPresenterImpl(null, this, this, this);
        mPresenter.create();

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
    protected void onDestroy() {
        super.onDestroy();

        mPresenter.destroy();
        mUnbinder.unbind();
    }

    @Override
    public void configureView() {

        mToolbar.findViewById(R.id.left_title).setVisibility(View.VISIBLE);
        mToolbar.findViewById(R.id.genericToolbarTitle).setVisibility(View.GONE);

        ((StyledTextView) mToolbar.findViewById(R.id.left_title)).setText(getString(R.string.select_bank_title));
        mToolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_action_close_white));

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new SelectBankAdapter(mBankDataList);
        mAdapter.addTransactionClickListenter(new SelectBankAdapter.BankListener() {
            @Override
            public void onBankItemSelected(Bank bank) {
                mPresenter.onBankSelected(bank);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }
}
