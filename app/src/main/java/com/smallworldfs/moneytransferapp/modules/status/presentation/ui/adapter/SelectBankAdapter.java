package com.smallworldfs.moneytransferapp.modules.status.presentation.ui.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smallworldfs.moneytransferapp.R;
import com.smallworldfs.moneytransferapp.modules.status.domain.model.Bank;
import com.smallworldfs.moneytransferapp.utils.widget.StyledTextView;

import java.util.ArrayList;

/**
 * Created by luis on 30/9/17
 */
public class SelectBankAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Bank> mData;
    private BankListener mListener;

    public SelectBankAdapter(ArrayList<Bank> data) {
        this.mData = data;
    }

    public interface BankListener {
        void onBankItemSelected(Bank bank);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_generic_drop_content, parent, false);
        SelectBankViewHolder viewHolder = new SelectBankViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Bank bank = mData.get(position);
        ((SelectBankViewHolder) holder).title.setText(bank.getBank());

        ((SelectBankViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onBankItemSelected(bank);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void addTransactionClickListenter(BankListener listener) {
        this.mListener = listener;
    }

    class SelectBankViewHolder extends RecyclerView.ViewHolder {
        StyledTextView title;
        public SelectBankViewHolder(View itemView) {
            super(itemView);
            title = (StyledTextView) itemView.findViewById(R.id.text);
        }
    }

}
