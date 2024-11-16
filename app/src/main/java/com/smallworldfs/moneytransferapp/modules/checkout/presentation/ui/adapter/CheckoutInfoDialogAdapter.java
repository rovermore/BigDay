package com.smallworldfs.moneytransferapp.modules.checkout.presentation.ui.adapter;

import static com.smallworldfs.moneytransferapp.utils.StringExtensionsKt.toHtml;

import android.content.Context;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.smallworldfs.moneytransferapp.R;
import com.smallworldfs.moneytransferapp.modules.checkout.domain.model.TransactionErrors;
import com.smallworldfs.moneytransferapp.utils.Constants;
import com.smallworldfs.moneytransferapp.utils.widget.StyledTextView;

import java.util.ArrayList;

/**
 * Created by luismiguel on 28/9/17.
 */

public class CheckoutInfoDialogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<TransactionErrors> mData;
    private Context mContext;
    private String mStyle;

    public CheckoutInfoDialogAdapter(Context context, ArrayList<TransactionErrors> data, String style) {
        this.mData = data;
        this.mContext = context;
        this.mStyle = style;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_info_dialog_checkout, parent, false);
        viewHolder = new DialogInfoViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TransactionErrors blockError = mData.get(position);

        if (!blockError.getTitle().equals(blockError.getDescription())) {
            ((DialogInfoViewHolder) holder).title.setText(blockError.getTitle());
            String description = blockError.getDescription();
            ((DialogInfoViewHolder) holder).subtitle.setText(toHtml(description));
            ((DialogInfoViewHolder) holder).subtitle.setMovementMethod(LinkMovementMethod.getInstance());
        } else
            ((DialogInfoViewHolder) holder).title.setText(blockError.getTitle());

        if (mStyle.equalsIgnoreCase(Constants.DIALOG_CHECKOUT_STYLE.SUCCESS_STYLE) && position == 0) {
            ((DialogInfoViewHolder) holder).icon.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.checkout_icn_check));
            ((DialogInfoViewHolder) holder).title.setContentDescription("mtn_text");
        } else {
            ((DialogInfoViewHolder) holder).icon.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.checkout_icn_alert));
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class DialogInfoViewHolder extends RecyclerView.ViewHolder {

        StyledTextView title, subtitle;
        ImageView icon;

        public DialogInfoViewHolder(View itemView) {
            super(itemView);
            title = (StyledTextView) itemView.findViewById(R.id.title);
            subtitle = (StyledTextView) itemView.findViewById(R.id.subtitle);
            icon = (ImageView) itemView.findViewById(R.id.icon);
        }
    }
}
