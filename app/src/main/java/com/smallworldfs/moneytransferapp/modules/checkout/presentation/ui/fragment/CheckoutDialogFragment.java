package com.smallworldfs.moneytransferapp.modules.checkout.presentation.ui.fragment;

import static com.smallworldfs.moneytransferapp.utils.Constants.COUNTRY.US_COUNTRY_VALUE;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.smallworldfs.moneytransferapp.R;
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ScreenCategory;
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ScreenName;
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.UserActionEvent;
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.ImageViewExtKt;
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.Transaction;
import com.smallworldfs.moneytransferapp.modules.checkout.domain.model.TransactionCheckOutDialogSummary;
import com.smallworldfs.moneytransferapp.modules.checkout.domain.model.TransactionErrors;
import com.smallworldfs.moneytransferapp.modules.checkout.presentation.presenter.CheckoutDialogPresenter;
import com.smallworldfs.moneytransferapp.modules.checkout.presentation.presenter.implementation.CheckoutDialogPresenterImpl;
import com.smallworldfs.moneytransferapp.modules.checkout.presentation.ui.adapter.CheckoutInfoDialogAdapter;
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericFragment;
import com.smallworldfs.moneytransferapp.modules.login.domain.model.User;
import com.smallworldfs.moneytransferapp.modules.login.domain.repository.LoginRepository;
import com.smallworldfs.moneytransferapp.presentation.status.transaction.model.TransactionUIModel;
import com.smallworldfs.moneytransferapp.utils.Constants;
import com.smallworldfs.moneytransferapp.utils.widget.StyledTextView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dagger.hilt.android.AndroidEntryPoint;

/**
 * Created by luismiguel on 27/9/17.
 */
@AndroidEntryPoint
public class CheckoutDialogFragment extends GenericFragment implements CheckoutDialogPresenter.View {

    @Inject
    LoginRepository loginRepository;

    Unbinder mUnbinder;

    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.error_bottom_container)
    View mErrorBottomContainer;
    @BindView(R.id.success_bottom_container)
    View mSuccessBottomContainer;
    @BindView(R.id.orange_circle_error_view)
    View mOrangeCircleErrorView;
    @BindView(R.id.country_beneficiary_transaction_flag)
    ImageView mCountryFlagHeader;
    @BindView(R.id.circle_container_success)
    View mCircleContainerSuccess;
    @BindView(R.id.dialog_text_title)
    StyledTextView mDialogTextTitle;
    @BindView(R.id.dialog_text_subtitle)
    StyledTextView mDialogTextSubtitle;
    @BindView(R.id.action_button)
    StyledTextView mActionButton;
    @BindView(R.id.fragmentCheckOutErrorDialogSummaryInformation)
    ConstraintLayout fragmentCheckOutErrorDialogSummaryInformation;
    @BindView(R.id.fragmentCheckOutErrorDialogSummaryInformationTransferAmountKey)
    StyledTextView fragmentCheckOutErrorDialogSummaryInformationTransferAmountKey;
    @BindView(R.id.fragmentCheckOutErrorDialogSummaryInformationTransferAmountValue)
    StyledTextView fragmentCheckOutErrorDialogSummaryInformationTransferAmountValue;
    @BindView(R.id.fragmentCheckOutErrorDialogSummaryInformationTransferFeeKey)
    StyledTextView fragmentCheckOutErrorDialogSummaryInformationTransferFeeKey;
    @BindView(R.id.fragmentCheckOutErrorDialogSummaryInformationTransferFeValue)
    StyledTextView fragmentCheckOutErrorDialogSummaryInformationTransferFeValue;
    @BindView(R.id.fragmentCheckOutErrorDialogSummaryInformationTransferTaxesKey)
    StyledTextView fragmentCheckOutErrorDialogSummaryInformationTransferTaxesKey;
    @BindView(R.id.fragmentCheckOutErrorDialogSummaryInformationTransferTaxesValue)
    StyledTextView fragmentCheckOutErrorDialogSummaryInformationTransferTaxesValue;
    @BindView(R.id.fragmentCheckOutErrorDialogSummaryInformationTotalKey)
    StyledTextView fragmentCheckOutErrorDialogSummaryInformationTotalKey;
    @BindView(R.id.fragmentCheckOutErrorDialogSummaryInformationTotalValue)
    StyledTextView fragmentCheckOutErrorDialogSummaryInformationTotalValue;
    @BindView(R.id.fragmentCheckOutErrorDialogSummaryInformationExchangeRateKey)
    StyledTextView fragmentCheckOutErrorDialogSummaryInformationExchangeRateKey;
    @BindView(R.id.fragmentCheckOutErrorDialogSummaryInformationExchangeRatValue)
    StyledTextView fragmentCheckOutErrorDialogSummaryInformationExchangeRatValue;
    @BindView(R.id.fragmentCheckOutErrorDialogSummaryInformationTransferAmount2Key)
    StyledTextView fragmentCheckOutErrorDialogSummaryInformationTransferAmount2Key;
    @BindView(R.id.fragmentCheckOutErrorDialogSummaryInformationTransferAmount2Value)
    StyledTextView fragmentCheckOutErrorDialogSummaryInformationTransferAmount2Value;
    @BindView(R.id.fragmentCheckOutErrorDialogSummaryInformationOtherFeesKey)
    StyledTextView fragmentCheckOutErrorDialogSummaryInformationOtherFeesKey;
    @BindView(R.id.fragmentCheckOutErrorDialogSummaryInformationOtherFeeValue)
    StyledTextView fragmentCheckOutErrorDialogSummaryInformationOtherFeeValue;
    @BindView(R.id.fragmentCheckOutErrorDialogSummaryInformationTotalToRecipientKey)
    StyledTextView fragmentCheckOutErrorDialogSummaryInformationTotalToRecipientKey;
    @BindView(R.id.fragmentCheckOutErrorDialogSummaryInformationTotalToRecipientValue)
    StyledTextView fragmentCheckOutErrorDialogSummaryInformationTotalToRecipientValue;
    @BindView(R.id.fragmentCheckOutErrorDialogSummaryInformationLegalText)
    StyledTextView fragmentCheckOutErrorDialogSummaryInformationLegalText;


    private CheckoutDialogPresenterImpl mPresenter;
    private CloseListener mActionsListener;
    private CheckoutInfoDialogAdapter mAdapter;
    private String mDialogStyle;
    private ArrayList<TransactionErrors> mListBlockErrors;
    private Transaction mTransactionData;
    private TransactionCheckOutDialogSummary mSummary;

    public static final String STYLE_EXTRA = "STYLE_EXTRA";
    public static final String LIST_TRANSACTION_BLOCK = "LIST_TRANSACTION_BLOCK";
    public static final String TRANSACTION_DATA = "TRANSACTION_DATA";
    public static final String TRANSACTION_SUMMARY = "TRANSACTION_SUMMARY";

    public static CheckoutDialogFragment getInstance(String style, ArrayList<TransactionErrors> blockErrorsTransaction, Transaction transaction, TransactionCheckOutDialogSummary summary) {
        CheckoutDialogFragment f = new CheckoutDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(STYLE_EXTRA, style);
        bundle.putParcelable(TRANSACTION_DATA, transaction);
        bundle.putParcelableArrayList(LIST_TRANSACTION_BLOCK, blockErrorsTransaction);
        bundle.putParcelable(TRANSACTION_SUMMARY, summary);

        f.setArguments(bundle);
        return f;
    }

    public interface CloseListener {
        void closeDialog();

        void checkErrorsNow();

        void closeDialogAndShowTransactions(Transaction transaction);
        void closeDialogAndShowTransactions(TransactionUIModel transaction);

        void closeAndRequestHelpEmail();

        void closeAndGoToPayNow(String mtn);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkout_error_dialog, null, false);
        ButterKnife.bind(this, view);

        if (getArguments() != null) {
            mDialogStyle = getArguments().getString(STYLE_EXTRA);
            mListBlockErrors = getArguments().getParcelableArrayList(LIST_TRANSACTION_BLOCK);
            mTransactionData = getArguments().getParcelable(TRANSACTION_DATA);
            mSummary = getArguments().getParcelable(TRANSACTION_SUMMARY);
        }

        mPresenter = new CheckoutDialogPresenterImpl(null, getActivity(), mDialogStyle, this, mListBlockErrors, mTransactionData, getActivity(), mSummary);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mPresenter.create();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.destroy();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }


    public void setCloseListener(CloseListener listener) {
        this.mActionsListener = listener;
    }


    //-----------------
    // VIEW METHODS
    //-----------------

    @Override
    public void configureView(String style, Transaction transactionData) {
        if (style.equalsIgnoreCase(Constants.DIALOG_CHECKOUT_STYLE.ERROR_STYLE)) {

            mErrorBottomContainer.setVisibility(View.VISIBLE);
            mSuccessBottomContainer.setVisibility(View.GONE);
            mOrangeCircleErrorView.setVisibility(View.VISIBLE);
            mCircleContainerSuccess.setVisibility(View.GONE);

            mErrorBottomContainer.setVisibility(View.VISIBLE);

            mDialogTextTitle.setText(R.string.checkout_popup_error_title);
            mDialogTextSubtitle.setText(R.string.checkout_popup_error_subtitle);
            trackScreen(ScreenName.TRANSACTION_ORDER_FAILED.getValue());
        } else {
            if (transactionData != null) {
                mSuccessBottomContainer.setVisibility(View.GONE);
                mSuccessBottomContainer.setVisibility(View.VISIBLE);
                mOrangeCircleErrorView.setVisibility(View.GONE);
                mCircleContainerSuccess.setVisibility(View.VISIBLE);
                mCountryFlagHeader.setVisibility(View.VISIBLE);

                mErrorBottomContainer.setVisibility(View.GONE);

                mDialogTextTitle.setText(R.string.checkout_popup_success_title);

                User user = loginRepository.getUser();
                if (user != null && user.getCountry().firstKey().equals(US_COUNTRY_VALUE)) {
                    mDialogTextSubtitle.setText(R.string.checkout_popup_success_subtitle_usa);
                } else {
                    mDialogTextSubtitle.setText(R.string.checkout_popup_success_subtitle);
                }

                ImageViewExtKt.loadCircularImage(
                        mCountryFlagHeader,
                        requireContext(),
                        R.drawable.placeholder_country_adapter,
                        Constants.COUNTRY.FLAG_IMAGE_ASSETS + transactionData.getBeneficiaryCountry() + Constants.COUNTRY.FLAG_IMAGE_EXTENSION
                );
                trackScreen(ScreenName.ORDER_PLACED_SUCCESSFULLY.getValue());
            }
        }
    }


    @Override
    public void checkErrorsNow() {
        if (mActionsListener != null) {
            mActionsListener.checkErrorsNow();
        }
    }

    @Override
    public void fillListBlockErrors(ArrayList<TransactionErrors> listBlockErrors) {
        mAdapter = new CheckoutInfoDialogAdapter(getActivity(), listBlockErrors, mDialogStyle);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);

        fragmentCheckOutErrorDialogSummaryInformation.setVisibility(View.GONE);
    }

    @Override
    public void fillSummary(ArrayList<TransactionErrors> header, TransactionCheckOutDialogSummary summary) {
        mAdapter = new CheckoutInfoDialogAdapter(getActivity(), header, mDialogStyle);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);

        // Set summary details
        fragmentCheckOutErrorDialogSummaryInformation.setVisibility(View.VISIBLE);
        fillSummaryFields(summary);
    }

    private void fillSummaryFields(TransactionCheckOutDialogSummary summary) {
        fragmentCheckOutErrorDialogSummaryInformationTransferAmountKey.setVisibility(summary.getSender().getAmount().getLabel().isEmpty() || summary.getSender().getAmount().getValue().isEmpty() ? View.GONE : View.VISIBLE);
        fragmentCheckOutErrorDialogSummaryInformationTransferAmountValue.setVisibility(summary.getSender().getAmount().getLabel().isEmpty() || summary.getSender().getAmount().getValue().isEmpty() ? View.GONE : View.VISIBLE);
        fragmentCheckOutErrorDialogSummaryInformationTransferAmountKey.setText(summary.getSender().getAmount().getLabel());
        fragmentCheckOutErrorDialogSummaryInformationTransferAmountValue.setText(summary.getSender().getAmount().getValue());

        fragmentCheckOutErrorDialogSummaryInformationTransferFeeKey.setVisibility(summary.getSender().getFee().getLabel().isEmpty() || summary.getSender().getFee().getValue().isEmpty() ? View.GONE : View.VISIBLE);
        fragmentCheckOutErrorDialogSummaryInformationTransferFeValue.setVisibility(summary.getSender().getFee().getLabel().isEmpty() || summary.getSender().getFee().getValue().isEmpty() ? View.GONE : View.VISIBLE);
        fragmentCheckOutErrorDialogSummaryInformationTransferFeeKey.setText(summary.getSender().getFee().getLabel());
        fragmentCheckOutErrorDialogSummaryInformationTransferFeValue.setText(summary.getSender().getFee().getValue());

        fragmentCheckOutErrorDialogSummaryInformationTransferTaxesKey.setVisibility(summary.getSender().getTaxes().getLabel().isEmpty() || summary.getSender().getTaxes().getValue().isEmpty() ? View.GONE : View.VISIBLE);
        fragmentCheckOutErrorDialogSummaryInformationTransferTaxesValue.setVisibility(summary.getSender().getTaxes().getLabel().isEmpty() || summary.getSender().getTaxes().getValue().isEmpty() ? View.GONE : View.VISIBLE);
        fragmentCheckOutErrorDialogSummaryInformationTransferTaxesKey.setText(summary.getSender().getTaxes().getLabel());
        fragmentCheckOutErrorDialogSummaryInformationTransferTaxesValue.setText(summary.getSender().getTaxes().getValue());

        fragmentCheckOutErrorDialogSummaryInformationTotalKey.setVisibility(summary.getSender().getTotal().getLabel().isEmpty() || summary.getSender().getTotal().getValue().isEmpty() ? View.GONE : View.VISIBLE);
        fragmentCheckOutErrorDialogSummaryInformationTotalValue.setVisibility(summary.getSender().getTotal().getLabel().isEmpty() || summary.getSender().getTotal().getValue().isEmpty() ? View.GONE : View.VISIBLE);
        fragmentCheckOutErrorDialogSummaryInformationTotalKey.setText(summary.getSender().getTotal().getLabel());
        fragmentCheckOutErrorDialogSummaryInformationTotalValue.setText(summary.getSender().getTotal().getValue());

        fragmentCheckOutErrorDialogSummaryInformationExchangeRateKey.setVisibility(summary.getExchange_rate().getLabel().isEmpty() || summary.getExchange_rate().getValue().isEmpty() ? View.GONE : View.VISIBLE);
        fragmentCheckOutErrorDialogSummaryInformationExchangeRatValue.setVisibility(summary.getExchange_rate().getLabel().isEmpty() || summary.getExchange_rate().getValue().isEmpty() ? View.GONE : View.VISIBLE);
        fragmentCheckOutErrorDialogSummaryInformationExchangeRateKey.setText(summary.getExchange_rate().getLabel());
        fragmentCheckOutErrorDialogSummaryInformationExchangeRatValue.setText(summary.getExchange_rate().getValue());

        fragmentCheckOutErrorDialogSummaryInformationTransferAmount2Key.setVisibility(summary.getReceipt().getAmount().getLabel().isEmpty() || summary.getReceipt().getAmount().getValue().isEmpty() ? View.GONE : View.VISIBLE);
        fragmentCheckOutErrorDialogSummaryInformationTransferAmount2Value.setVisibility(summary.getReceipt().getAmount().getLabel().isEmpty() || summary.getReceipt().getAmount().getValue().isEmpty() ? View.GONE : View.VISIBLE);
        fragmentCheckOutErrorDialogSummaryInformationTransferAmount2Key.setText(summary.getReceipt().getAmount().getLabel());
        fragmentCheckOutErrorDialogSummaryInformationTransferAmount2Value.setText(summary.getReceipt().getAmount().getValue());

        fragmentCheckOutErrorDialogSummaryInformationOtherFeesKey.setVisibility(summary.getReceipt().getFee().getLabel().isEmpty() || summary.getReceipt().getFee().getValue().isEmpty() ? View.GONE : View.VISIBLE);
        fragmentCheckOutErrorDialogSummaryInformationOtherFeeValue.setVisibility(summary.getReceipt().getFee().getLabel().isEmpty() || summary.getReceipt().getFee().getValue().isEmpty() ? View.GONE : View.VISIBLE);
        fragmentCheckOutErrorDialogSummaryInformationOtherFeesKey.setText(summary.getReceipt().getFee().getLabel());
        fragmentCheckOutErrorDialogSummaryInformationOtherFeeValue.setText(summary.getReceipt().getFee().getValue());

        fragmentCheckOutErrorDialogSummaryInformationTotalToRecipientKey.setVisibility(summary.getReceipt().getTotal().getLabel().isEmpty() || summary.getReceipt().getTotal().getValue().isEmpty() ? View.GONE : View.VISIBLE);
        fragmentCheckOutErrorDialogSummaryInformationTotalToRecipientValue.setVisibility(summary.getReceipt().getTotal().getLabel().isEmpty() || summary.getReceipt().getTotal().getValue().isEmpty() ? View.GONE : View.VISIBLE);
        fragmentCheckOutErrorDialogSummaryInformationTotalToRecipientKey.setText(summary.getReceipt().getTotal().getLabel());
        fragmentCheckOutErrorDialogSummaryInformationTotalToRecipientValue.setText(summary.getReceipt().getTotal().getValue());

        fragmentCheckOutErrorDialogSummaryInformationLegalText.setVisibility(summary.getFooter().isEmpty() ? View.GONE : View.VISIBLE);
        fragmentCheckOutErrorDialogSummaryInformationLegalText.setText(summary.getFooter());
    }

    @Override
    public void setupPayNowButton(String transactionsStringStatus, Drawable drawableSecondaryActionButton) {
        mActionButton.setText(transactionsStringStatus);
        mActionButton.setCompoundDrawablesWithIntrinsicBounds(drawableSecondaryActionButton, null, null, null);
    }

    @Override
    public void setupTransferDetailsButton(String transactionsStringStatus, Drawable drawableSecondaryActionButton) {
        mActionButton.setText(transactionsStringStatus);
        mActionButton.setCompoundDrawablesWithIntrinsicBounds(drawableSecondaryActionButton, null, null, null);
    }

    @Override
    public void closeDialogAndShowTransactionDetails(Transaction transaction) {
        if (mActionsListener != null) {
            mActionsListener.closeDialogAndShowTransactions(transaction);
        }
    }

    @Override
    public void helpRequested() {
        if (mActionsListener != null) {
            mActionsListener.closeAndRequestHelpEmail();
        }
    }

    @Override
    public void closeDialogAndPayNow(String mtn) {
        if (mActionsListener != null) {
            mActionsListener.closeAndGoToPayNow(mtn);
        }
    }

    //-----------------
    // CLICK METHODS
    //-----------------

    @OnClick(R.id.close_button)
    public void onCloseButtonClick() {
        //mPresenter.checkIfShouldShowTransferWalkThrough();
        if (mActionsListener != null) {
            mActionsListener.closeDialog();
        }
        if (mDialogStyle.equalsIgnoreCase(Constants.DIALOG_CHECKOUT_STYLE.ERROR_STYLE))
            registerEvent("click_close",ScreenName.TRANSACTION_ORDER_FAILED.getValue());

        else
            registerEvent("click_close", ScreenName.ORDER_PLACED_SUCCESSFULLY.getValue());
    }

    @OnClick(R.id.help_button)
    public void onHelpButtonClick() {
        mPresenter.helpButtonClick();
        registerEvent("click_get_help",ScreenName.TRANSACTION_ORDER_FAILED.getValue());
    }

    @OnClick(R.id.action_button)
    public void onActionButtonClick() {
        mPresenter.actionButtonClick();
        registerEvent("click_pay_now",ScreenName.ORDER_PLACED_SUCCESSFULLY.getValue());
    }

    private void registerEvent(String eventAction, String hierarchyName) {
        trackEvent(new UserActionEvent(
                ScreenCategory.TRANSFER.getValue(),
                eventAction,
                "",
                getHierarchy(hierarchyName),
                "",
                "",
                "",
                "",
                "",
                ""
        ));
    }
}
