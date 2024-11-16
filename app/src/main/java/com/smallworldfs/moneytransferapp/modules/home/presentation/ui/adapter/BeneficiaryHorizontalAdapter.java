package com.smallworldfs.moneytransferapp.modules.home.presentation.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.smallworldfs.moneytransferapp.R;
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.ImageViewExtKt;
import com.smallworldfs.moneytransferapp.presentation.account.beneficiary.list.model.BeneficiaryUIModel;
import com.smallworldfs.moneytransferapp.utils.Constants;
import com.smallworldfs.moneytransferapp.utils.widget.StyledTextView;

import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by luismiguel on 20/6/17.
 */

public class BeneficiaryHorizontalAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<BeneficiaryUIModel> mData;
    private Context mContext;
    private int mSelectedPosition = 1;
    private ClickBeneficiaryListener mListener;


    public BeneficiaryHorizontalAdapter(Context context) {
        this.mContext = context;
        this.mData = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        if (viewType == 1) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_horizontal_beneficiary, parent, false);
            viewHolder = new BeneficiaryHorizontalAdapter.BeneficiaryViewHolder(v);
            return viewHolder;
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_horizontal_new_transfer, parent, false);
            viewHolder = new BeneficiaryHorizontalAdapter.NewTransferViewHolder(v);
            return viewHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof BeneficiaryViewHolder) {
            BeneficiaryUIModel beneficiary = mData.get(position);

            String nameOrNickName = beneficiary.getNameOrNickName();

            String beneficiaryFirstLetter = !TextUtils.isEmpty(nameOrNickName) ? nameOrNickName.substring(0, 1).toUpperCase(Locale.getDefault()) : "";
            String beneficiarySecondLetter = getSecondCapitalizedLetterFor(beneficiary);

            ((BeneficiaryViewHolder) holder).mUserNameLetterText.setText(beneficiaryFirstLetter);
            ((BeneficiaryViewHolder) holder).mUserNameLetterText2.setText(beneficiarySecondLetter);


            if (!TextUtils.isEmpty(beneficiary.getPayoutCountry().getIso3())) {
                ImageViewExtKt.loadCircularImage(
                        ((BeneficiaryViewHolder) holder).mCountryFlag,
                        holder.itemView.getContext(),
                        R.drawable.placeholder_beneficiary_adapter,
                        Constants.COUNTRY.FLAG_IMAGE_ASSETS + beneficiary.getPayoutCountry().getIso3() + Constants.COUNTRY.FLAG_IMAGE_EXTENSION
                );
            }

            ((BeneficiaryViewHolder) holder).mUserNameText.setText(nameOrNickName);
            if (!TextUtils.isEmpty(beneficiary.getDeliveryMethod().getName())) {
                ((BeneficiaryViewHolder) holder).mUserBankMethod.setText(beneficiary.getDeliveryMethod().getName());
            }

            if (position == mSelectedPosition) {
                ((BeneficiaryViewHolder) holder).mSelectedBackground.setVisibility(View.VISIBLE);
            } else {
                ((BeneficiaryViewHolder) holder).mSelectedBackground.setVisibility(View.INVISIBLE);
            }

            holder.itemView.setOnClickListener(v -> {
                if (mListener != null) {
                    mListener.onBeneficiaryClick(position, mData.get(position));
                }
            });

        } else if (holder instanceof NewTransferViewHolder) {

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        if ((mData != null) &&
                                (mData.size() > mSelectedPosition) && mSelectedPosition >= 0) {
                            BeneficiaryUIModel beneficiary = mData.get(mSelectedPosition);
                            if (beneficiary != null) {
                                holder.itemView.setContentDescription("new_beneficiary_button");
                                mListener.onNewBeneficiaryClick(beneficiary);
                            }
                        }

                }
            }
        });
    }}

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void updateData(ArrayList<BeneficiaryUIModel> listBeneficiary) {
        if (this.mData != null) {
            this.mData.clear();
            this.mData.add(new BeneficiaryUIModel());
            this.mData.addAll(listBeneficiary);
        }
        notifyDataSetChanged();
    }

    public void clearSelection() {
        if (mData != null && (mSelectedPosition < 1 || mSelectedPosition > mData.size() - 1)) {
            final int selectectedPosition = mSelectedPosition;
            mSelectedPosition = 1;
            notifyItemChanged(selectectedPosition);
        }
    }

    public void updateBeneficiarySelected(int selectedPosition) {
        if (selectedPosition != 0) {
            int oldPosition = this.mSelectedPosition;
            this.mSelectedPosition = selectedPosition;
            notifyItemRangeChanged(oldPosition, 1);
            notifyItemRangeChanged(this.mSelectedPosition, 1);
        }
    }

    public void updateBeneficiarySelectedWithBeneficiary(BeneficiaryUIModel beneficiary) {
        if (mData != null) {
            int position = 0;
            for (BeneficiaryUIModel beneficiaryAux : mData) {
                if (beneficiaryAux.getId().equalsIgnoreCase(beneficiary.getId())) {
                    break;
                }
                position++;
            }
            if (position <= mData.size() - 1) {
                updateBeneficiarySelected(position);
            } else {
                this.mSelectedPosition = -1;
                notifyDataSetChanged();
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        BeneficiaryUIModel item = mData.get(position);
        if (item.getId().equals("") && item.getClientId().equals("")) {
            return 0;
        } else return 1;
    }

    public void setClickBeneficiaryListener(ClickBeneficiaryListener clickBeneficiaryListener) {
        this.mListener = clickBeneficiaryListener;
    }

    public interface ClickBeneficiaryListener {
        void onBeneficiaryClick(int position, BeneficiaryUIModel beneficiary);

        void onNewBeneficiaryClick(BeneficiaryUIModel beneficiary);
    }

    class BeneficiaryViewHolder extends RecyclerView.ViewHolder {

        ImageView mCountryFlag;
        StyledTextView mUserNameLetterText, mUserNameLetterText2, mUserNameText, mUserBankMethod;
        ImageView mSelectedBackground;

        public BeneficiaryViewHolder(View itemView) {
            super(itemView);
            mCountryFlag = (ImageView) itemView.findViewById(R.id.country_flag);
            mUserNameLetterText = (StyledTextView) itemView.findViewById(R.id.user_name_letter_text);
            mUserNameLetterText2 = (StyledTextView) itemView.findViewById(R.id.user_name_letter_text_2);
            mUserNameText = (StyledTextView) itemView.findViewById(R.id.beneficiary_name_text);
            mUserBankMethod = (StyledTextView) itemView.findViewById(R.id.beneficiary_operation_text);
            mSelectedBackground = (ImageView) itemView.findViewById(R.id.background_selected_beneficiary);

        }
    }

    class NewTransferViewHolder extends RecyclerView.ViewHolder {

        public NewTransferViewHolder(View itemView) {
            super(itemView);
        }
    }

    private String getSecondCapitalizedLetterFor(BeneficiaryUIModel beneficiary) {
        String nameOrNickname = beneficiary.getNameOrNickName();
        String beneficiarySecondLetter = "";
        if (!TextUtils.isEmpty(beneficiary.getAlias())) {
            Pattern pattern = Pattern.compile("\\s([A-Za-z-0-9]+)");
            Matcher matcher = pattern.matcher(nameOrNickname);
            if (matcher.find()) {
                beneficiarySecondLetter = matcher.group(1).substring(0, 1).toUpperCase(Locale.getDefault());
            }
        } else {
            beneficiarySecondLetter = !TextUtils.isEmpty(beneficiary.getSurname()) ? beneficiary.getSurname().substring(0, 1).toUpperCase(Locale.getDefault()) : "";
        }
        return beneficiarySecondLetter;
    }
}
