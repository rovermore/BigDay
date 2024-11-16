package com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.adapter;

import static com.smallworldfs.moneytransferapp.utils.Constants.FIELD_SUBTYPES.DOCUMENT_FIELDS_FROM_IMAGE;
import static com.smallworldfs.moneytransferapp.utils.Constants.FIELD_SUBTYPES.GROUP_DATE;
import static com.smallworldfs.moneytransferapp.utils.Constants.FIELD_SUBTYPES.PHONE;
import static com.smallworldfs.moneytransferapp.utils.Constants.FIELD_SUBTYPES.TEXT_GROUP;
import static com.smallworldfs.moneytransferapp.utils.Constants.FIELD_TYPE.CHECK_BOX;
import static com.smallworldfs.moneytransferapp.utils.Constants.FIELD_TYPE.TEXT;
import static com.smallworldfs.moneytransferapp.utils.Constants.FIELD_TYPE.TEXT_AREA;
import static com.smallworldfs.moneytransferapp.utils.Constants.FIELD_TYPE.WHITE_BOX;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.widget.CompoundButtonCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.smallworldfs.moneytransferapp.R;
import com.smallworldfs.moneytransferapp.SmallWorldApplication;
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.AnalyticsSender;
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.ImageViewExtKt;
import com.smallworldfs.moneytransferapp.modules.calculator.domain.interactor.implementation.CalculatorInteractorImpl;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.GenericFormField;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.KeyValueData;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field;
import com.smallworldfs.moneytransferapp.utils.Constants;
import com.smallworldfs.moneytransferapp.utils.FormUtils;
import com.smallworldfs.moneytransferapp.utils.Utils;
import com.smallworldfs.moneytransferapp.utils.widget.DismissibleEditText;
import com.smallworldfs.moneytransferapp.utils.widget.StyledTextView;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import dagger.hilt.EntryPoint;
import dagger.hilt.InstallIn;
import dagger.hilt.android.EntryPointAccessors;
import dagger.hilt.components.SingletonComponent;

/**
 * Created by luismiguel on 17/7/17
 */
public class FormDataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String SEPARATOR_TAG = "separator_line";
    private static final String ERROR_TEXT_TAG = "error_text";
    private static final String ERROR_VIEW_GROUP_TAG = "view_group_tag";
    private static final String PREFIX_DOMINICAN_REPUBLIC = "+1";
    private static final String AMENDED_NUMBER_DOMINICAN_REPUBLIC = "809";
    private static final int VIEW_TYPE_TEXT = 0;
    private static final int VIEW_TYPE_PASSWORD = 1;
    private static final int VIEW_TYPE_EMAIL = 2;
    private static final int VIEW_COMBO = 3;
    private static final int VIEW_TYPE_GROUP_SUBTYPE_PHONE = 7;
    private static final int VIEW_TYPE_GROUP_SUBTYPE_DATE = 8;
    private static final int VIEW_TYPE_GROUP_SUBTYPE_DOCUMENT = 9;
    private static final int VIEW_TYPE_GROUP_SUBTYPE_SELECTABLE_TEXT = 10;
    private static final int VIEW_TYPE_FILE = 11;
    private static final int VIEW_WHITE_BOX = 12;
    private static final int VIEW_CHECK_BOX = 13;
    private static final int VIEW_HIDDEN = 14;
    private static final int VIEW_RADIO_BUTTON = 15;
    private static final int VIEW_TYPE_TEXT_AREA = 16;
    private static final int VIEW_SWITCH = 17;
    private static final int VIEW_TYPE_UNKNOWN = 100;
    private static final int INPUT_NUMBER_SIZE_FOR_DOMINICAN = 7;
    // Headers types
    private static final int VIEW_HEADER_SECTION_TYPE = 201;
    private Typeface mType;
    private ArrayList<? extends Field> mData;
    private Context mContext;
    private FormClickListener mFormClickListener;
    private FormErrorListener mFormErrorListener;
    private FormEditTextListener mFormEditTextListener;
    private boolean mValidatedListeners = true;

    // Drawables
    private Drawable mRightBlueArrow;
    private Drawable mTinnyBlueArrow;

    private String errorFieldName = "";
    private boolean isExistFirstError = true;
    RecyclerView mRecyclerView;

    private LinkedHashSet<Integer> positionsEditableEditTexts = new LinkedHashSet<>();

    @EntryPoint
    @InstallIn(SingletonComponent.class)
    interface DaggerHiltEntryPoint {
        AnalyticsSender providesAnalyticsHandler();
    }

    AnalyticsSender analyticsHandler;

    public FormDataAdapter(Context context, ArrayList<? extends GenericFormField> data) {
        this.mContext = context;
        this.mData = (ArrayList<? extends Field>) data;
        this.mRightBlueArrow = mContext.getResources().getDrawable(R.drawable.login_icn_flagfinder3);
        this.mTinnyBlueArrow = ContextCompat.getDrawable(mContext, R.drawable.onboarding_btn_more2);

        DrawableCompat.setTint(this.mTinnyBlueArrow, ContextCompat.getColor(mContext, R.color.blue_color_control));
        DrawableCompat.setTintMode(this.mTinnyBlueArrow, PorterDuff.Mode.SRC_IN);

        DrawableCompat.setTint(mRightBlueArrow, ContextCompat.getColor(mContext, R.color.blue_color_control));
        DrawableCompat.setTintMode(mRightBlueArrow, PorterDuff.Mode.SRC_IN);

        this.mType = ResourcesCompat.getFont(context, R.font.nunito_semi_bold);

        FormDataAdapter.DaggerHiltEntryPoint hiltEntryPoint =
                EntryPointAccessors.fromApplication(
                        SmallWorldApplication.getApp(),
                        FormDataAdapter.DaggerHiltEntryPoint.class);
        analyticsHandler = hiltEntryPoint.providesAnalyticsHandler();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        mRecyclerView = recyclerView;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;

        switch (viewType) {
            case VIEW_TYPE_TEXT_AREA: {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_transactional_edittextarea_form_item, parent, false);
                viewHolder = new EditTextFormDataViewHolder(v);
                return viewHolder;
            }
            case VIEW_TYPE_TEXT: {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_transactional_edittext_form_item, parent, false);
                viewHolder = new EditTextFormDataViewHolder(v);
                return viewHolder;
            }
            case VIEW_TYPE_PASSWORD: {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_transactional_edittext_form_item, parent, false);
                viewHolder = new EditTextFormDataViewHolder(v);
                return viewHolder;
            }
            case VIEW_TYPE_EMAIL: {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_transactional_edittext_form_item, parent, false);
                viewHolder = new EditTextFormDataViewHolder(v);
                return viewHolder;
            }
            case VIEW_TYPE_GROUP_SUBTYPE_PHONE: {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_transactional_group_telephone_form_item, parent, false);
                viewHolder = new GroupFormDataViewHolder(v);
                return viewHolder;
            }
            case VIEW_TYPE_GROUP_SUBTYPE_DATE: {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_transactional_combo_form_item, parent, false);
                viewHolder = new ComboFormDataViewHolder(v);
                return viewHolder;
            }
            case VIEW_COMBO: {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_transactional_combo_form_item, parent, false);
                viewHolder = new ComboFormDataViewHolder(v);
                return viewHolder;
            }
            case VIEW_HEADER_SECTION_TYPE: {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_section_header, parent, false);
                viewHolder = new HeaderSectionViewHolder(v);
                return viewHolder;
            }
            case VIEW_TYPE_GROUP_SUBTYPE_DOCUMENT: {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_register_document_form_item, parent, false);
                viewHolder = new GroupDocumentViewHolder(v);
                return viewHolder;
            }
            case VIEW_TYPE_GROUP_SUBTYPE_SELECTABLE_TEXT: {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_register_selectable_text_group, parent, false);
                viewHolder = new GroupTextSelectableViewHolder(v);
                return viewHolder;
            }
            case VIEW_TYPE_FILE: {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_form_attach_document, parent, false);
                viewHolder = new FileButtonViewHolder(v);
                return viewHolder;
            }
            case VIEW_WHITE_BOX: {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_form_white_box_form_item, parent, false);
                viewHolder = new WhiteBoxViewHolder(v);
                return viewHolder;
            }
            case VIEW_CHECK_BOX: {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_form_checkbox_item, parent, false);
                viewHolder = new CheckBoxViewHolder(v);
                return viewHolder;
            }
            case VIEW_SWITCH: {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_from_switch_item, parent, false);
                viewHolder = new SwitchViewHolder(v);
                return viewHolder;
            }
            case VIEW_HIDDEN: {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_form_hidden_item, parent, false);
                viewHolder = new HiddenViewHolder(v);
                return viewHolder;
            }
            case VIEW_RADIO_BUTTON: {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_form_radiobutton_item, parent, false);
                viewHolder = new RadioButtonViewHolder(v);
                return viewHolder;
            }
            default: {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_transactional_unknown_form_item, parent, false);
                viewHolder = new UnknownFormDataViewHolder(v);
                return viewHolder;
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        Field field = mData.get(position);
        if (!TextUtils.isEmpty(field.getType())) {
            switch (field.getType()) {
                case TEXT_AREA:
                case TEXT:
                case Constants.FIELD_TYPE.PASSWORD:
                case Constants.FIELD_TYPE.EMAIL: {
                    processTextViewHolder((EditTextFormDataViewHolder) holder, field, position);
                    break;
                }
                case Constants.FIELD_TYPE.GROUP: {
                    if (field.getSubtype().equals(PHONE)) {
                        processGroupViewHolder((GroupFormDataViewHolder) holder, field, position);
                    } else if (field.getSubtype().equals(GROUP_DATE)) {
                        processGroupDateViewHolder((ComboFormDataViewHolder) holder, field, position);
                    } else if (field.getSubtype().equals(Constants.FIELD_SUBTYPES.DOCUMENT) || field.getSubtype().equalsIgnoreCase(DOCUMENT_FIELDS_FROM_IMAGE)) {
                        processGroupDocumentViewHolder((GroupDocumentViewHolder) holder, field, position);
                    } else if (field.getSubtype().equals(TEXT_GROUP)) {
                        processGroupSelectableTextViewHolder((GroupTextSelectableViewHolder) holder, field, position);
                    } else {
                        processUnknownViewHolder((UnknownFormDataViewHolder) holder, field);
                    }
                    break;
                }
                case Constants.FIELD_TYPE.COMBO: {
                    processComboViewHolder((ComboFormDataViewHolder) holder, field, position);
                    break;
                }
                case Constants.FIELD_TYPE.REGISTER_INTERN: {
                    processHeaderSection((HeaderSectionViewHolder) holder, field, position);
                    break;
                }
                case Constants.FIELD_TYPE.FILE: {
                    processFileViewHolder((FileButtonViewHolder) holder, field, position);
                    break;
                }
                case Constants.FIELD_TYPE.WHITE_BOX: {
                    processWhiteBoxHolder((WhiteBoxViewHolder) holder, field, position);
                    break;
                }
                case Constants.FIELD_TYPE.CHECK_BOX: {
                    processCheckBoxHolder((CheckBoxViewHolder) holder, field, position);
                    break;
                }
                case Constants.FIELD_TYPE.SWITCH: {
                    processSwitchHolder((SwitchViewHolder) holder, field, position);
                    break;
                }
                case Constants.FIELD_TYPE.HIDDEN: {
                    processHiddenHolder((HiddenViewHolder) holder, field, position);
                    break;
                }
                case Constants.FIELD_TYPE.RADIO_BUTTON: {
                    processRadioButtonHolder((RadioButtonViewHolder) holder, field, position);
                    break;
                }
                default: {
                    processUnknownViewHolder((UnknownFormDataViewHolder) holder, field);
                    break;
                }
            }
        } else {
            processUnknownViewHolder(null, null);
        }

    }

    private void processRadioButtonHolder(RadioButtonViewHolder holder, final Field field, final int position) {
        holder.switchButton.setContentDescription(field.getName());

        holder.message.setText(field.getPlaceholder() + optionalTag(field));
        holder.switchButton.setChecked(Boolean.parseBoolean(field.getValue()));

        holder.switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                field.setValue(String.valueOf(!Boolean.parseBoolean(field.getValue())));

                // Iterate through form to search checkboxes depends on radiobutton
                int relativeListPosition = 0;
                for (Field fieldIterated : mData) {
                    if (!TextUtils.isEmpty(fieldIterated.getTriggers()) && fieldIterated.getTriggers().equalsIgnoreCase(field.getName())) {
                        fieldIterated.setValue(String.valueOf(isChecked));
                        notifyItemChanged(relativeListPosition);
                    }
                    relativeListPosition++;
                }

                if (mFormClickListener != null) {
                    mFormClickListener.onRadioSwitchButtonClick(field, position);
                }
            }
        });
    }

    private void processCheckBoxHolder(final CheckBoxViewHolder holder, final Field field, final int position) {
        holder.checkbox.setContentDescription(field.getName());

        if (!TextUtils.isEmpty(field.getSubtype()) && field.getSubtype().equalsIgnoreCase(Constants.FIELD_SUBTYPES.RICH_TEXT)) {
            holder.message.setClickable(true);
            holder.message.setMovementMethod(LinkMovementMethod.getInstance());
            holder.message.setText(Html.fromHtml(field.getPlaceholder()));
        } else {
            holder.message.setText(field.getPlaceholder());
        }

        holder.checkbox.setChecked(Boolean.valueOf(field.getValue()));

        if (!TextUtils.isEmpty(field.getTriggers())) {
            boolean ownerRadioChecked = false;
            for (Field fieldCheckboxOwner : mData) {
                if (!TextUtils.isEmpty(fieldCheckboxOwner.getName()) && fieldCheckboxOwner.getName().equalsIgnoreCase(field.getTriggers())) {
                    ownerRadioChecked = Boolean.valueOf(fieldCheckboxOwner.getValue());
                    break;
                }
            }
            if (!ownerRadioChecked) {
                holder.checkbox.setEnabled(false);
                holder.checkbox.setClickable(false);
                holder.checkbox.setAlpha(0.75f);
                holder.message.setAlpha(0.6f);
            } else {
                holder.checkbox.setEnabled(true);
                holder.checkbox.setClickable(true);
                holder.checkbox.setAlpha(1f);
                holder.message.setAlpha(1f);
            }
        }


        holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                field.setValue(String.valueOf(isChecked));
                if (isChecked && !TextUtils.isEmpty(field.getErrorMessage())) {
                    field.setErrorMessage("");
                    notifyItemChanged(position);
                }
            }
        });

        // Process error
        String errorMessage = "";
        if (!TextUtils.isEmpty(field.getErrorMessage())) {
            errorMessage = field.getErrorMessage();
        }

        if (!TextUtils.isEmpty(errorMessage)) {
            // Show Error
            holder.errorMessage.setVisibility(View.VISIBLE);
            holder.errorMessage.setText(errorMessage);

            ColorStateList colorStateList = new ColorStateList(
                    new int[][]{
                            new int[]{-android.R.attr.state_checked}, // unchecked
                            new int[]{android.R.attr.state_checked}, // checked
                    },
                    new int[]{
                            mContext.getResources().getColor(R.color.colorRedError),
                            mContext.getResources().getColor(R.color.check_box_default_color),
                    }
            );
            CompoundButtonCompat.setButtonTintList(holder.checkbox, colorStateList);

            holder.containerView.setFocusable(true);
            holder.containerView.setFocusableInTouchMode(true);
            holder.containerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    holder.containerView.requestFocus();
                }
            }, 100);


            if (mFormErrorListener != null) {
                mFormErrorListener.onCheckBoxError(position);
            }

        } else {
            // Hide Error
            holder.errorMessage.setVisibility(View.INVISIBLE);
            ColorStateList colorStateList = new ColorStateList(
                    new int[][]{
                            new int[]{-android.R.attr.state_checked}, // unchecked
                            new int[]{android.R.attr.state_checked}, // checked
                    },
                    new int[]{
                            mContext.getResources().getColor(R.color.check_box_default_color),
                            mContext.getResources().getColor(R.color.check_box_default_color),
                    }
            );
            CompoundButtonCompat.setButtonTintList(holder.checkbox, colorStateList);
        }
    }

    private void processSwitchHolder(final SwitchViewHolder holder, final Field field, final int position) {
        holder.switchButton.setContentDescription(field.getName());

        if (!TextUtils.isEmpty(field.getSubtype()) && field.getSubtype().equalsIgnoreCase(Constants.FIELD_SUBTYPES.RICH_TEXT)) {
            holder.message.setClickable(true);
            holder.message.setMovementMethod(LinkMovementMethod.getInstance());
            holder.message.setText(Html.fromHtml(field.getPlaceholder()));
        } else {
            holder.message.setText(field.getPlaceholder());
        }

        holder.switchButton.setChecked(Boolean.valueOf(field.getValue()));

        if (!TextUtils.isEmpty(field.getTriggers())) {
            boolean ownerRadioChecked = false;
            for (Field fieldCheckboxOwner : mData) {
                if (!TextUtils.isEmpty(fieldCheckboxOwner.getName()) && fieldCheckboxOwner.getName().equalsIgnoreCase(field.getTriggers())) {
                    ownerRadioChecked = Boolean.valueOf(fieldCheckboxOwner.getValue());
                    break;
                }
            }
            if (!ownerRadioChecked) {
                holder.switchButton.setEnabled(false);
                holder.switchButton.setClickable(false);
                holder.switchButton.setAlpha(0.75f);
                holder.message.setAlpha(0.6f);
            } else {
                holder.switchButton.setEnabled(true);
                holder.switchButton.setClickable(true);
                holder.switchButton.setAlpha(1f);
                holder.message.setAlpha(1f);
            }
        }


        holder.switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                field.setValue(String.valueOf(isChecked));
                if (isChecked && !TextUtils.isEmpty(field.getErrorMessage())) {
                    field.setErrorMessage("");
                    notifyItemChanged(position);
                }
            }
        });

        // Process error
        String errorMessage = "";
        if (!TextUtils.isEmpty(field.getErrorMessage())) {
            errorMessage = field.getErrorMessage();
        }

        if (!TextUtils.isEmpty(errorMessage)) {
            // Show Error
            holder.errorMessage.setVisibility(View.VISIBLE);
            holder.errorMessage.setText(errorMessage);

            ColorStateList colorStateList = new ColorStateList(
                    new int[][]{
                            new int[]{-android.R.attr.state_checked}, // unchecked
                            new int[]{android.R.attr.state_checked}, // checked
                    },
                    new int[]{
                            mContext.getResources().getColor(R.color.colorRedError),
                            mContext.getResources().getColor(R.color.check_box_default_color),
                    }
            );
            CompoundButtonCompat.setButtonTintList(holder.switchButton, colorStateList);

            holder.containerView.setFocusable(true);
            holder.containerView.setFocusableInTouchMode(true);
            holder.containerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    holder.containerView.requestFocus();
                }
            }, 100);


            if (mFormErrorListener != null) {
                mFormErrorListener.onCheckBoxError(position);
            }

        } else {
            // Hide Error
            holder.errorMessage.setVisibility(View.INVISIBLE);
            ColorStateList colorStateList = new ColorStateList(
                    new int[][]{
                            new int[]{-android.R.attr.state_checked}, // unchecked
                            new int[]{android.R.attr.state_checked}, // checked
                    },
                    new int[]{
                            mContext.getResources().getColor(R.color.check_box_default_color),
                            mContext.getResources().getColor(R.color.check_box_default_color),
                    }
            );
            CompoundButtonCompat.setButtonTintList(holder.switchButton, colorStateList);
        }
    }

    private void processHiddenHolder(HiddenViewHolder holder, final Field field, int position) {

    }

    private void processWhiteBoxHolder(WhiteBoxViewHolder holder, Field field, int position) {
        holder.itemView.setContentDescription(field.getName());

        if (!TextUtils.isEmpty(field.getValue())) {
            if (!TextUtils.isEmpty(field.getSubtype()) && field.getSubtype().equalsIgnoreCase(Constants.FIELD_SUBTYPES.RICH_TEXT)) {
                holder.message.setClickable(true);
                holder.message.setMovementMethod(LinkMovementMethod.getInstance());
                holder.message.setText(Html.fromHtml(field.getValue()));
            } else {
                holder.message.setText(field.getValue());
            }
        } else {
            holder.message.setText("");
        }
    }

    private void processFileViewHolder(FileButtonViewHolder holder, final Field field, final int position) {
        holder.itemView.setContentDescription(field.getName());

        if (!TextUtils.isEmpty(field.getTitle())) {
            holder.title.setText(field.getTitle());
        } else {
            holder.title.setText(field.getName().substring(0, 1).toUpperCase(Locale.getDefault()) + field.getName().substring(1, field.getName().length()));
        }
        if (!TextUtils.isEmpty(field.getValue())) {
            holder.subtitle.setText(SmallWorldApplication.getStr(R.string.image_loaded_successfully));
            ImageViewExtKt.loadImage(
                    holder.imageIcon,
                    R.drawable.hardregister_icn_attachdocsuccess
            );
            holder.subtitle.setTextColor(ContextCompat.getColor(mContext, R.color.blue_accent_color));
        } else if (!TextUtils.isEmpty(field.getErrorMessage())) {
            holder.subtitle.setText(field.getPlaceholder());
            ImageViewExtKt.loadImage(
                    holder.imageIcon,
                    R.drawable.hardregister_icn_attachdocfail
            );
            holder.subtitle.setTextColor(ContextCompat.getColor(mContext, R.color.colorRedError));
        } else {
            ImageViewExtKt.loadImage(
                    holder.imageIcon,
                    R.drawable.hardregister_icn_attachdoc
            );
            holder.subtitle.setText(field.getPlaceholder());
            holder.subtitle.setTextColor(ContextCompat.getColor(mContext, R.color.blue_accent_color));
        }

        holder.subtitle.setTextColor(TextUtils.isEmpty(field.getErrorMessage()) ? ContextCompat.getColor(mContext, R.color.blue_accent_color) :
                ContextCompat.getColor(mContext, R.color.colorRedError));

        holder.itemView.setOnClickListener(v -> {
            if (mFormClickListener != null) {
                mFormClickListener.onAttachFileButtonClick(field, position);
            }
        });

        if (errorFieldName.equals(field.getTitle())) {
            if (mFormErrorListener != null) {
                mFormErrorListener.scrollToRelativeOffsetPosition(position);
            }
        }
    }

    private void processGroupSelectableTextViewHolder(final GroupTextSelectableViewHolder viewHolder, final Field field, final int position) {

        handleIMEOptionsForEditText(viewHolder.editText, position, field, viewHolder.textInputLayout, field.getSubtype());

        viewHolder.errorText.setTypeface(mType);
        viewHolder.editText.setTypeface(mType);
        viewHolder.editText.setContentDescription(field.getName());
        viewHolder.textInputLayout.setTypeface(mType);
        viewHolder.editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        if (field.getChilds() != null && field.getChilds().size() > 1) {
            viewHolder.selectableTextIndicator.setText(field.getChilds().get(0).getValue());
        }
        viewHolder.textInputLayout.setHint(field.getPlaceholder() + optionalTag(field));

        viewHolder.selectableTextIndicator.setCompoundDrawablesWithIntrinsicBounds(null, null, this.mTinnyBlueArrow, null);
        viewHolder.textSelectorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFormClickListener != null) {
                    mFormClickListener.onTextSelectableGroupClick(field, position);
                }
            }
        });

        viewHolder.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mValidatedListeners) {
                    if (field.getChilds() != null && field.getChilds().size() > 0) {
                        for (Field childField : field.getChilds()) {
                            childField.setErrorMessage("");
                            viewHolder.hideErrorMessage();
                            if (!TextUtils.isEmpty(childField.getName()) && childField.getData() == null) {
                                childField.setValue(s.toString());
                                break;
                            }
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        processError(viewHolder, field, position);
    }

    private void processGroupDocumentViewHolder(GroupDocumentViewHolder holder, final Field field, final int position) {
        holder.itemView.setContentDescription(field.getName());
        if (field.getValue() != null && !TextUtils.isEmpty(field.getKeyValue())) {
            holder.documentLabel.setVisibility(View.INVISIBLE);
            holder.groupValuesContainer.setVisibility(View.VISIBLE);
            holder.titleDocument.setText(field.getKeyValue());
            holder.subtitleDocument.setText(field.getValue());
            holder.iconContainer.setVisibility(View.VISIBLE);
        } else {
            holder.documentLabel.setVisibility(View.VISIBLE);
            holder.groupValuesContainer.setVisibility(View.INVISIBLE);
            holder.iconContainer.setVisibility(View.GONE);

            if (!TextUtils.isEmpty(field.getErrorMessage())) {
                holder.documentLabel.setTextColor(ContextCompat.getColor(mContext, R.color.colorRedError));
            } else {
                holder.documentLabel.setTextColor(ContextCompat.getColor(mContext, R.color.blue_accent_color));
            }
        }

        holder.itemView.setOnClickListener(v -> {
            if (mFormClickListener != null) {
                if (field.getSubtype().equalsIgnoreCase(PHONE) ||
                        field.getSubtype().equalsIgnoreCase(TEXT_GROUP)) {
                    mFormClickListener.onPhoneCountrySelectorClick(field, position);
                } else if (field.getSubtype().equalsIgnoreCase(GROUP_DATE)) {
                    mFormClickListener.onDateSelectorClick(field, position);
                } else if (field.getSubtype().equalsIgnoreCase(Constants.FIELD_SUBTYPES.DOCUMENT) || field.getSubtype().equalsIgnoreCase(DOCUMENT_FIELDS_FROM_IMAGE)) {
                    mFormClickListener.onUploadFileButtonClick(field, position);
                }
            }
        });

        processDocumentError(holder, field);
    }

    private void processHeaderSection(HeaderSectionViewHolder holder, Field field, int position) {

        holder.itemView.setContentDescription(field.getName());

        Drawable imageSection = null;
        switch (field.getSubtype()) {
            case Constants.FIELD_SUBTYPES.PROFILE_SECTION:
                imageSection = ContextCompat.getDrawable(mContext, R.drawable.hardregister_icn_profile);
                break;
            case Constants.FIELD_SUBTYPES.ADDRESS_SECTION:
                imageSection = ContextCompat.getDrawable(mContext, R.drawable.hardregister_icn_address);
                break;
            case Constants.FIELD_SUBTYPES.ACCOUNT_SECTION:
                imageSection = ContextCompat.getDrawable(mContext, R.drawable.hardregister_icn_account);
                break;
            default:
                imageSection = ContextCompat.getDrawable(mContext, R.drawable.hardregister_icn_account);
                break;
        }

        holder.imageSection.setImageDrawable(imageSection);
        holder.titleSection.setText(field.getValue());

    }

    @SuppressLint("ClickableViewAccessibility")
    private void processTextViewHolder(final EditTextFormDataViewHolder viewHolder, final Field field, final int position) {

        handleIMEOptionsForEditText(viewHolder.editText, position, field, viewHolder.editTextInputLayout, field.getSubtype());

        viewHolder.errorText.setTypeface(this.mType);
        viewHolder.editText.setTypeface(this.mType);

        viewHolder.editTextInputLayout.setTypeface(this.mType);

        viewHolder.editTextInputLayout.setHint(getHint(field));
        viewHolder.editText.setText(field.getValue());
        viewHolder.editText.setContentDescription(field.getName());

        if (TEXT.equalsIgnoreCase(field.getType())) {
            if (Constants.FIELD_SUBTYPES.TEXT_NUMERIC.equalsIgnoreCase(field.getSubtype())) {
                viewHolder.editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
            } else if (Constants.FIELD_SUBTYPES.TEXT_FILTER_ALPHANUM.equalsIgnoreCase(field.getSubtype())) {
                viewHolder.editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
                InputFilter[] filters = new InputFilter[viewHolder.editText.getFilters().length + 1];
                System.arraycopy(viewHolder.editText.getFilters(), 0, filters, 0, viewHolder.editText.getFilters().length);
                filters[filters.length - 1] = (input, start, end, dst, dStart, dEnd) -> {
                    String regex = "[^a-zA-Z0-9 -]";
                    if (input.length() > 0 && !Character.isLetterOrDigit(input.charAt(0)) && input.charAt(0) != '-') {
                        return "";
                    } else if (input.length() > 1 && !String.valueOf(input).matches(regex)) {
                        return String.valueOf(input).replaceAll(regex, "");
                    }
                    return null;
                };
                viewHolder.editText.setFilters(filters);
            } else if (Constants.FIELD_SUBTYPES.TEXT_ALPHANUM.equalsIgnoreCase(field.getSubtype()) || TextUtils.isEmpty(field.getSubtype())) {
                viewHolder.editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
            }
        } else if (Constants.FIELD_TYPE.PASSWORD.equalsIgnoreCase(field.getType())) {
            viewHolder.editText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        } else if (Constants.FIELD_TYPE.EMAIL.equalsIgnoreCase(field.getType())) {
            viewHolder.editText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        } else if (TEXT_AREA.equalsIgnoreCase(field.getType())) {
            viewHolder.editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
            viewHolder.editText.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                    }
                    return false;
                }
            });
        }

        viewHolder.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mValidatedListeners) {
                    if (viewHolder.editTextInputLayout.getHint().equals(getHint(field))) { // Little fix because the same listener is being set to more than one field, check where in the future (?)
                        field.setValue(s.toString());
                        field.setErrorMessage("");
                        viewHolder.hideErrorMessage();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        processError(viewHolder, field, position);
    }

    private void processGroupViewHolder(final GroupFormDataViewHolder viewHolder, final Field field, final int position) {

        handleIMEOptionsForEditText(viewHolder.editText, position, field, viewHolder.textInputLayout, field.getSubtype());

        viewHolder.errorText.setTypeface(this.mType);
        viewHolder.editText.setTypeface(this.mType);
        viewHolder.editText.setContentDescription(field.getName());
        viewHolder.textInputLayout.setTypeface(this.mType);
        viewHolder.prefix.setTypeface(this.mType);

        if (field.getChilds() != null && field.getChilds().size() > 1) {
            final String selectedCountryId = field.getValue();
            final String selectedCountryValue;
            final String selectedCountryPrefix;
            if (selectedCountryId != null) {
                final Map.Entry<String, String> selectedCountry = getCountryById(field.getData(), selectedCountryId);
                if (selectedCountry != null) {
                    selectedCountryValue = selectedCountry.getValue();
                } else {
                    selectedCountryValue = field.getChilds().get(0).getValue();
                }
            } else {
                selectedCountryValue = field.getChilds().get(0).getValue();
            }
            selectedCountryPrefix = FormUtils.extractCountryPhonePrefix(field.getChilds().get(0).getData(), selectedCountryValue);
            if (TextUtils.isEmpty(selectedCountryValue)) {
                field.getChilds().get(0).setValue(CalculatorInteractorImpl.getInstance().getPayoutCountryKey());
            }

            ImageViewExtKt.loadCircularImage(
                    viewHolder.countryChipCodeImage,
                    viewHolder.itemView.getContext(),
                    R.drawable.placeholder_country_adapter,
                    Constants.COUNTRY.FLAG_IMAGE_ASSETS + selectedCountryValue + Constants.COUNTRY.FLAG_IMAGE_EXTENSION
            );


            viewHolder.textInputLayout.setHint(getHintGroup(field));
            viewHolder.editText.setText(field.getChilds().get(1).getValue());
            /*viewHolder.prefix.setAutoSizeTextTypeUniformWithConfiguration(14, 18, 1,
                    TypedValue.COMPLEX_UNIT_SP);*/
            viewHolder.prefix.setText(selectedCountryPrefix);

            viewHolder.countrySelectorLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mFormClickListener != null) {
                        if (field.getSubtype().equalsIgnoreCase(PHONE) ||
                                field.getSubtype().equalsIgnoreCase(TEXT_GROUP)) {
                            field.setSearchable(true);
                            mFormClickListener.onPhoneCountrySelectorClick(field, position);
                        }
                    }
                }
            });

            if (field.getSubtype().equalsIgnoreCase(PHONE)) {
                viewHolder.editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            }

            viewHolder.editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (mValidatedListeners) {
                        if (field.getChilds() != null && field.getChilds().size() > 0) {
                            for (Field childField : field.getChilds()) {
                                childField.setErrorMessage("");
                                viewHolder.hideErrorMessage();

                                if (!TextUtils.isEmpty(childField.getName()) && childField.getData() == null) {
                                    childField.setValue(s.toString());
                                    break;
                                }
                            }
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (viewHolder.prefix.getText().equals(PREFIX_DOMINICAN_REPUBLIC)
                            && Objects.requireNonNull(viewHolder.editText.getText()).length() == INPUT_NUMBER_SIZE_FOR_DOMINICAN
                            && !Objects.requireNonNull(viewHolder.editText.getText()).toString().contains(AMENDED_NUMBER_DOMINICAN_REPUBLIC)
                    ) {
                        String amendedNumberForDOM = AMENDED_NUMBER_DOMINICAN_REPUBLIC + viewHolder.editText.getText();
                        viewHolder.editText.setText(amendedNumberForDOM);
                    }
                }
            });
            viewHolder.editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        if (viewHolder.prefix.getText().equals(PREFIX_DOMINICAN_REPUBLIC) && Objects.requireNonNull(viewHolder.editText.getText()).length() == INPUT_NUMBER_SIZE_FOR_DOMINICAN) {
                            String amendedNumberForDOM = AMENDED_NUMBER_DOMINICAN_REPUBLIC + viewHolder.editText.getText();
                            viewHolder.editText.setText(amendedNumberForDOM);
                        }
                    }
                }
            });

            processError(viewHolder, field, position);
        }
    }

    @Nullable
    private Map.Entry<String, String> getCountryById(@NonNull final ArrayList<TreeMap<String, String>> countries,
                                                     @NonNull final String id) {
        for (TreeMap<String, String> country : countries) {
            final Map.Entry<String, String> countryData = country.firstEntry();
            if (countryData != null && countryData.getKey() != null && countryData.getKey().equals(id)) {
                return countryData;
            }
        }
        return null;
    }

    private void processComboViewHolder(final ComboFormDataViewHolder viewHolder, final Field field, final int position) {


        viewHolder.errorText.setTypeface(this.mType);
        viewHolder.editText.setTypeface(this.mType);
        viewHolder.editText.setContentDescription(field.getName());
        viewHolder.editTextInputLayout.setTypeface(this.mType);

        viewHolder.editTextInputLayout.setHint(getHint(field));
        viewHolder.editText.setInputType(InputType.TYPE_NULL);

        viewHolder.comboContainer.setOnClickListener(v -> {
            if (mFormClickListener != null) {
                if (field.getSubtype().equalsIgnoreCase(Constants.FIELD_SUBTYPES.COMBO_OWN) || field.getSubtype().equalsIgnoreCase(Constants.FIELD_SUBTYPES.COMBO_REQUEST)) {
                    mFormClickListener.onComboOwnDataClick(field, position);
                } else if (field.getSubtype().equalsIgnoreCase(Constants.FIELD_SUBTYPES.COMBO_API)) {
                    mFormClickListener.onComboApiClick(field, position);
                }
            }
        });

        viewHolder.editText.setText(field.getValue());
        viewHolder.editText.setFocusableInTouchMode(false);
        viewHolder.editText.setFocusable(false);

        switch (field.getType()) {
            case TEXT:
            case Constants.FIELD_TYPE.PASSWORD:
            case Constants.FIELD_TYPE.EMAIL: {
                viewHolder.editText.setFocusable(true);
                break;
            }
            default: {
                viewHolder.editText.setFocusable(false);
                break;
            }
        }

        viewHolder.editText.setCompoundDrawablesWithIntrinsicBounds(null, null, mRightBlueArrow, null);
        viewHolder.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mValidatedListeners) {
                    if (viewHolder.editTextInputLayout.getHint().equals(getHint(field))) { // Little fix because the same listener is being set to more than one field, check where in the future (?)
                        field.setValue(s.toString());
                        field.setErrorMessage("");
                        viewHolder.hideErrorMessage();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        processError(viewHolder, field, position);
    }

    private void processGroupDateViewHolder(final ComboFormDataViewHolder viewHolder, final Field field, final int position) {

        handleIMEOptionsForEditText(viewHolder.editText, position, field, viewHolder.editTextInputLayout, field.getSubtype());

        viewHolder.errorText.setTypeface(this.mType);
        viewHolder.editText.setTypeface(this.mType);
        viewHolder.editText.setContentDescription(field.getName());
        viewHolder.editTextInputLayout.setTypeface(this.mType);

        viewHolder.editTextInputLayout.setHint(getHintDate(field));
        viewHolder.editText.setInputType(InputType.TYPE_NULL);
        viewHolder.editText.setFocusable(false);
        viewHolder.editText.setFocusableInTouchMode(false);


        if (field.getChilds() != null && field.getChilds().size() == 3) {

            Field fieldDayDate = field.getChilds().get(0);
            Field fieldMonthDate = field.getChilds().get(1);
            Field fieldYearDate = field.getChilds().get(2);

            if (!TextUtils.isEmpty(fieldDayDate.getValue()) &&
                    !TextUtils.isEmpty(fieldMonthDate.getValue()) &&
                    !TextUtils.isEmpty(fieldYearDate.getValue())) {
                viewHolder.editText.setText(Utils.formatDayMonthYearInCompleterDate(fieldDayDate.getValue(), fieldMonthDate.getValue(), fieldYearDate.getValue()));
            }
        }

        viewHolder.comboContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFormClickListener != null) {
                    if (field.getSubtype().equalsIgnoreCase(GROUP_DATE)) {
                        mFormClickListener.onDateSelectorClick(field, position);
                    }
                }
            }
        });

        DrawableCompat.setTint(mRightBlueArrow, mContext.getResources().getColor(R.color.blue_accent_color));
        DrawableCompat.setTintMode(mRightBlueArrow, PorterDuff.Mode.SRC_IN);
        viewHolder.editText.setCompoundDrawablesWithIntrinsicBounds(null, null, mRightBlueArrow, null);

        viewHolder.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mValidatedListeners) {
                    field.setValue(s.toString());
                    field.setErrorMessage("");
                    viewHolder.hideErrorMessage();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        processError(viewHolder, field, position);
    }

    private void processUnknownViewHolder(final UnknownFormDataViewHolder viewHolder, final Field field) {

    }

    private void processError(final GenericCellErrorViewHolder viewHolder, final Field field, final int position) {
        String errorMessage = getErrorMessage(field);

        if (!TextUtils.isEmpty(errorMessage)) {
            // Show Error
            viewHolder.separatorLine.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorRedError));
            viewHolder.errorText.setVisibility(View.VISIBLE);
            viewHolder.errorText.setText(errorMessage);
            if (errorFieldName.equals(field.getTitle())) {

                if (mFormErrorListener != null) {
                    mFormErrorListener.scrollToRelativeOffsetPosition(position);
                }
            }
        } else {
            // Hide Error
            viewHolder.separatorLine.setBackgroundColor(ContextCompat.getColor(mContext, R.color.default_grey_control));
            viewHolder.errorText.setVisibility(View.INVISIBLE);
        }
    }

    private void processDocumentError(final GenericCellErrorViewHolder viewHolder, final Field field) {
        String errorMessage = getErrorMessage(field);

        if (!TextUtils.isEmpty(errorMessage)) {
            // Show Error
            if (errorFieldName.equals(field.getTitle())) {
                viewHolder.relativeLayout.setFocusable(true);
                viewHolder.relativeLayout.setFocusableInTouchMode(true);
                viewHolder.relativeLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        viewHolder.relativeLayout.requestFocus();
                    }
                }, 100);
            }
        }
    }

    private String getErrorMessage(Field field) {
        String error = "";
        if (!TextUtils.isEmpty(field.getErrorMessage())) {
            error = field.getErrorMessage();
        } else if (field.getChilds() != null && field.getChilds().size() != 0) {
            for (Field fieldAux : field.getChilds()) {
                if (!TextUtils.isEmpty(fieldAux.getErrorMessage())) {
                    error = fieldAux.getErrorMessage();
                    break;
                }
            }
        }
        return error;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        Field field = mData.get(position);
        if (!TextUtils.isEmpty(field.getType())) {
            switch (field.getType()) {
                case TEXT_AREA:
                    return VIEW_TYPE_TEXT_AREA;
                case TEXT:
                    return VIEW_TYPE_TEXT;
                case Constants.FIELD_TYPE.PASSWORD:
                    return VIEW_TYPE_PASSWORD;
                case Constants.FIELD_TYPE.EMAIL:
                    return VIEW_TYPE_EMAIL;
                case Constants.FIELD_TYPE.COMBO:
                    return VIEW_COMBO;
                case Constants.FIELD_TYPE.GROUP:
                    switch (field.getSubtype()) {
                        case GROUP_DATE:
                            return VIEW_TYPE_GROUP_SUBTYPE_DATE;
                        case PHONE:
                            return VIEW_TYPE_GROUP_SUBTYPE_PHONE;
                        case Constants.FIELD_SUBTYPES.DOCUMENT:
                        case DOCUMENT_FIELDS_FROM_IMAGE:
                            return VIEW_TYPE_GROUP_SUBTYPE_DOCUMENT;
                        case TEXT_GROUP:
                            return VIEW_TYPE_GROUP_SUBTYPE_SELECTABLE_TEXT;
                        default:
                            return VIEW_TYPE_GROUP_SUBTYPE_PHONE;
                    }
                case Constants.FIELD_TYPE.FILE:
                    return VIEW_TYPE_FILE;
                case Constants.FIELD_TYPE.REGISTER_INTERN:
                    return VIEW_HEADER_SECTION_TYPE;
                case Constants.FIELD_TYPE.WHITE_BOX:
                    return VIEW_WHITE_BOX;
                case Constants.FIELD_TYPE.CHECK_BOX:
                    return VIEW_CHECK_BOX;
                case Constants.FIELD_TYPE.SWITCH:
                    return VIEW_SWITCH;
                case Constants.FIELD_TYPE.HIDDEN:
                    return VIEW_HIDDEN;
                case Constants.FIELD_TYPE.RADIO_BUTTON:
                    return VIEW_RADIO_BUTTON;
                default:
                    return VIEW_TYPE_UNKNOWN;
            }
        } else {
            return VIEW_TYPE_UNKNOWN;
        }
    }

    public Field getFieldByPosition( int position) {
        return mData.get(position);
    }

    public ArrayList<Field> getFields() { return (ArrayList<Field>) mData; }

    public Field getFieldByName(String name) {
        for (Field field : mData) {
            if (field.getName().equals(name)) {
                return field;
            }
        }
        return null;
    }

    public void updateField(ArrayList<KeyValueData> values, int fieldPosition) {
        if (fieldPosition < mData.size()) {
            // Check type to update values
            Field field = mData.get(fieldPosition);

            if (field != null && values != null) {

                if (field.getType().equalsIgnoreCase(Constants.FIELD_TYPE.COMBO)) {

                    field.setValue(values.get(0).getValue());
                    field.setKeyValue(values.get(0).getKey());

                    // Clear error
                    if (!TextUtils.isEmpty(field.getErrorMessage())) {
                        field.setErrorMessage("");
                    }

                    // Check if next field depends on combo selected value
                    if (fieldPosition < mData.size() - 2) {
                        Field nextField = mData.get(fieldPosition + 1);
                        if (!TextUtils.isEmpty(nextField.getSubtype())) {
                            if (nextField.getSubtype().equalsIgnoreCase(Constants.FIELD_SUBTYPES.COMBO_API) && !TextUtils.isEmpty(nextField.getTriggers()) &&
                                    nextField.getTriggers().equalsIgnoreCase(field.getName())) {

                                nextField.setValue("");
                                nextField.setKeyValue("");
                                notifySpecificPositionChanges(fieldPosition + 1);
                            }
                        }
                    }

                } else if (field.getType().equalsIgnoreCase(Constants.FIELD_TYPE.GROUP)) {
                    if (!TextUtils.isEmpty(field.getSubtype())) {
                        if (field.getSubtype().equalsIgnoreCase(PHONE)) {

                            field.getChilds().get(0).setValue(values.get(0).getKey());

                        } else if (field.getSubtype().equalsIgnoreCase(GROUP_DATE)) {

                            field.getChilds().get(0).setValue(values.get(0).getValue());
                            field.getChilds().get(1).setValue(values.get(1).getValue());
                            field.getChilds().get(2).setValue(values.get(2).getValue());

                        } else if (field.getSubtype().equalsIgnoreCase(TEXT_GROUP)) {
                            field.getChilds().get(0).setKeyValue(values.get(0).getKey());
                            field.getChilds().get(0).setValue(values.get(0).getValue());
                        }
                    }

                    // Clear error
                    if (!TextUtils.isEmpty(field.getErrorMessage())) {
                        field.setErrorMessage("");
                    }
                }
            }
        }
    }

    public void setFormClickListener(FormClickListener listener) {
        this.mFormClickListener = listener;
    }

    public void setFormErrorListener(FormErrorListener listener) {
        this.mFormErrorListener = listener;
    }

    public void setFormEditTextListener(FormEditTextListener listener) {
        this.mFormEditTextListener = listener;
    }

    public void notifySpecificPositionChanges(int fieldPosition) {
        setValidatedListeners(false);
        notifyItemChanged(fieldPosition);
        notifyDataSetChanged();
    }

    public void notifyGlobalChanges() {
        clearAmountEditableEditText();
        setValidatedListeners(false);
        resetErrorFieldName();
        for (Field field : mData) {
            setErrorFieldName(field);
            notifyItemChanged(mData.indexOf(field), field);
        }
    }

    private void setErrorFieldName(Field field) {
        String nameField = field.getTitle();
        if (isExistFirstError) {
            if (isErrorMessage(field) && field.getChilds() == null) {
                errorFieldName = nameField;
                isExistFirstError = false;
            } else if (field.getChilds() != null) {
                for (Field field1 : field.getChilds()) {
                    if (isErrorMessage(field1)) {
                        errorFieldName = nameField;
                        isExistFirstError = false;
                        return;
                    }
                }
            }
        }

    }

    private boolean isErrorMessage(Field field) {
        return field.getErrorMessage() != null && !field.getErrorMessage().isEmpty();
    }

    private void resetErrorFieldName() {
        errorFieldName = "";
        isExistFirstError = true;
    }

    public void notifyItemsAdded(int position, int count) {
        notifyItemRangeInserted(position, count);
    }

    public void notifyItemDeleted(int position) {
        notifyItemRemoved(position);
    }

    public void setValidatedListeners(boolean actived) {
        mValidatedListeners = actived;
    }

    public interface FormClickListener {
        void onAttachFileButtonClick(Field field, int position);

        void onPhoneCountrySelectorClick(Field field, int position);

        void onTextSelectableGroupClick(Field field, int position);

        void onDateSelectorClick(Field field, int position);

        void onComboApiClick(Field field, int position);

        void onComboOwnDataClick(Field field, int position);

        void onUploadFileButtonClick(Field field, int position);

        void onRadioSwitchButtonClick(Field field, int position);
    }

    public interface FormEditTextListener {
        void onEditTextActionNext();

        void onEditTextActionDone();

        void onEditTextChangedFocus(boolean hasFocus);
    }

    public interface FormErrorListener {
        void onCheckBoxError(int fieldPosition);

        void scrollToRelativeOffsetPosition(int fieldPosition);
    }

    private class EditTextFormDataViewHolder extends GenericCellErrorViewHolder {

        TextInputLayout editTextInputLayout;
        DismissibleEditText editText;
        View separatorLine;
        StyledTextView errorText;

        EditTextFormDataViewHolder(final View itemView) {
            super(itemView);

            editTextInputLayout = itemView.findViewById(R.id.edit_text_input_layout);
            editText = itemView.findViewById(R.id.edit_text_input);
            editText.setKeyListener(null);
            editTextInputLayout.setOnKeyListener(null);
            separatorLine = itemView.findViewWithTag(SEPARATOR_TAG);
            errorText = itemView.findViewWithTag(ERROR_TEXT_TAG);

            editText.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (mFormEditTextListener != null) {
                        mFormEditTextListener.onEditTextActionDone();
                    }
                    return false;
                } else if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    if (mFormEditTextListener != null) {
                        mFormEditTextListener.onEditTextActionNext();
                    }
                    return false;
                }
                return false;
            });
        }
    }

    private class GroupFormDataViewHolder extends GenericCellErrorViewHolder {

        ImageView countryChipCodeImage;
        TextInputLayout textInputLayout;
        ConstraintLayout countrySelectorLayout;
        DismissibleEditText editText;
        View separatorLine;
        StyledTextView errorText;
        StyledTextView prefix;

        GroupFormDataViewHolder(View itemView) {
            super(itemView);
            countryChipCodeImage = itemView.findViewById(R.id.country_chip_code_image);
            textInputLayout = itemView.findViewById(R.id.phone_name_text_input_layout);
            countrySelectorLayout = itemView.findViewById(R.id.country_code_selector);
            editText = itemView.findViewById(R.id.phone_name_edit_text);
            separatorLine = itemView.findViewWithTag(SEPARATOR_TAG);
            errorText = itemView.findViewWithTag(ERROR_TEXT_TAG);
            if (itemView.findViewById(R.id.prefix) != null) {
                prefix = itemView.findViewById(R.id.prefix);
            } else {
                prefix = null;
            }
        }
    }

    private class ComboFormDataViewHolder extends GenericCellErrorViewHolder {

        TextInputLayout editTextInputLayout;
        DismissibleEditText editText;
        RelativeLayout comboContainer;
        View separatorLine;
        StyledTextView errorText;

        ComboFormDataViewHolder(View itemView) {
            super(itemView);

            editTextInputLayout = itemView.findViewById(R.id.edit_text_input_layout);
            editText = itemView.findViewById(R.id.edit_text_input);
            comboContainer = itemView.findViewById(R.id.combo_container);
            separatorLine = itemView.findViewWithTag(SEPARATOR_TAG);
            errorText = itemView.findViewWithTag(ERROR_TEXT_TAG);
        }
    }

    private class UnknownFormDataViewHolder extends GenericCellErrorViewHolder {
        TextView fieldName;

        UnknownFormDataViewHolder(View itemView) {
            super(itemView);

            fieldName = itemView.findViewById(R.id.field_name);
        }
    }

    private class HeaderSectionViewHolder extends RecyclerView.ViewHolder {
        ImageView imageSection;
        StyledTextView titleSection;

        HeaderSectionViewHolder(View itemView) {
            super(itemView);

            imageSection = itemView.findViewById(R.id.image_section);
            titleSection = itemView.findViewById(R.id.section_text);
        }
    }

    private class GroupDocumentViewHolder extends GenericCellErrorViewHolder {

        LinearLayout groupValuesContainer;
        StyledTextView titleDocument, subtitleDocument, documentLabel;
        RelativeLayout iconContainer;

        GroupDocumentViewHolder(View itemView) {
            super(itemView);

            groupValuesContainer = itemView.findViewById(R.id.document_values_container);
            titleDocument = itemView.findViewById(R.id.document_type_text);
            subtitleDocument = itemView.findViewById(R.id.document_value_text);
            documentLabel = itemView.findViewById(R.id.document_label);
            iconContainer = itemView.findViewById(R.id.icon_container);


        }
    }

    private class GroupTextSelectableViewHolder extends GenericCellErrorViewHolder {

        StyledTextView selectableTextIndicator;
        TextInputLayout textInputLayout;
        RelativeLayout textSelectorLayout;
        DismissibleEditText editText;

        GroupTextSelectableViewHolder(View itemView) {
            super(itemView);

            selectableTextIndicator = itemView.findViewById(R.id.text_selectable_indicator);
            textInputLayout = itemView.findViewById(R.id.value_text_input_layout);
            editText = itemView.findViewById(R.id.value_edit_text);
            separatorLine = itemView.findViewWithTag(SEPARATOR_TAG);
            textSelectorLayout = itemView.findViewById(R.id.text_selector_layout);
            errorText = itemView.findViewWithTag(ERROR_TEXT_TAG);

        }
    }

    private class FileButtonViewHolder extends RecyclerView.ViewHolder {

        StyledTextView title, subtitle;
        ImageView imageIcon;

        FileButtonViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            subtitle = itemView.findViewById(R.id.subtitle);
            imageIcon = itemView.findViewById(R.id.image_file_icon);
        }
    }


    private class WhiteBoxViewHolder extends RecyclerView.ViewHolder {

        StyledTextView message;

        WhiteBoxViewHolder(View itemView) {
            super(itemView);

            message = itemView.findViewById(R.id.message);
        }
    }


    private class CheckBoxViewHolder extends RecyclerView.ViewHolder {

        LinearLayout containerView;
        StyledTextView message, errorMessage;
        CheckBox checkbox;

        CheckBoxViewHolder(View itemView) {
            super(itemView);

            containerView = itemView.findViewById(R.id.container_view);
            message = itemView.findViewById(R.id.content);
            checkbox = itemView.findViewById(R.id.checkbox);
            errorMessage = itemView.findViewWithTag(ERROR_TEXT_TAG);
        }
    }

    private class SwitchViewHolder extends RecyclerView.ViewHolder {

        LinearLayout containerView;
        StyledTextView message, errorMessage;
        Switch switchButton;

        SwitchViewHolder(View itemView) {
            super(itemView);

            containerView = itemView.findViewById(R.id.container_view);
            message = itemView.findViewById(R.id.content);
            switchButton = itemView.findViewById(R.id.switch_button);
            errorMessage = itemView.findViewWithTag(ERROR_TEXT_TAG);
        }
    }

    private class HiddenViewHolder extends RecyclerView.ViewHolder {

        StyledTextView message, errorMessage;

        HiddenViewHolder(View itemView) {
            super(itemView);

            message = itemView.findViewById(R.id.content);


        }
    }

    private class RadioButtonViewHolder extends RecyclerView.ViewHolder {

        StyledTextView message, errorMessage;
        Switch switchButton;

        RadioButtonViewHolder(View itemView) {
            super(itemView);

            message = itemView.findViewById(R.id.content);
            switchButton = itemView.findViewById(R.id.switch_button);
            errorMessage = itemView.findViewWithTag(ERROR_TEXT_TAG);
        }
    }


    private class GenericCellErrorViewHolder extends RecyclerView.ViewHolder {

        View separatorLine;
        StyledTextView errorText;
        RelativeLayout relativeLayout;

        GenericCellErrorViewHolder(View itemView) {
            super(itemView);

            separatorLine = itemView.findViewWithTag(SEPARATOR_TAG);
            errorText = itemView.findViewWithTag(ERROR_TEXT_TAG);
            relativeLayout = itemView.findViewWithTag(ERROR_VIEW_GROUP_TAG);
            if (errorText != null) errorText.setFocusable(true);
        }

        void hideErrorMessage() {
            // Hide Error
            if (separatorLine != null && errorText != null) {
                separatorLine.setBackgroundColor(mContext.getResources().getColor(R.color.default_grey_control));
                errorText.setVisibility(View.INVISIBLE);
            }
            relativeLayout.setFocusable(false);
            relativeLayout.setFocusableInTouchMode(false);
        }
    }

    private boolean isLastEditableEditText(int positionEditableEdiText) {
        try {
            int lastEditableEditText = (int) positionsEditableEditTexts.toArray()[positionsEditableEditTexts.size() - 1];
            return lastEditableEditText == positionEditableEdiText;

        } catch (IndexOutOfBoundsException e) {
            return false;
        }

    }

    private void addEditableEditTextPositionToHashSet(int editableEditTextPosition) {
        positionsEditableEditTexts.add(editableEditTextPosition);
    }

    private void clearAmountEditableEditText() {
        positionsEditableEditTexts.clear();

    }

    private void handleIMEOptionsForEditText(final DismissibleEditText editText, final int position, final Field field, final TextInputLayout textInputLayout,
                                             String type) {

        if (type != null && !TextUtils.isEmpty(type) && !type.equalsIgnoreCase(GROUP_DATE)) {
            addEditableEditTextPositionToHashSet(position);
        }
        editText.setTag(position);

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (isLastEditableEditText((int) v.getTag())) {
                        ((EditText) v).setImeOptions(EditorInfo.IME_ACTION_DONE);
                    } else {
                        ((EditText) v).setImeOptions(EditorInfo.IME_ACTION_NEXT);
                    }
                    if (mFormErrorListener != null) {
                        mFormErrorListener.scrollToRelativeOffsetPosition(position);
                    }
                }
                if (mFormEditTextListener != null) {
                    mFormEditTextListener.onEditTextChangedFocus(hasFocus);
                }
            }
        });

        editText.setDismissListener(new DismissibleEditText.DismissKeyboardListener() {
            @Override
            public void onDismissKeyboard() {
                if (mFormEditTextListener != null) {
                    editText.clearFocus();
                    mFormEditTextListener.onEditTextChangedFocus(false);
                }
            }
        });
    }

    public void updateData(ArrayList<Field> listForm) {
        if (mData != null && mData.size() > 1) {
            mData.clear();
        }
        ArrayList<Field> fieldsToUse = new ArrayList<>();
        for (Field field : listForm) {
            if (field != null && (field.getType().equalsIgnoreCase(CHECK_BOX) ||
                    field.getType().equalsIgnoreCase(WHITE_BOX))) {
                fieldsToUse.add(field);
            }
        }
        mData = fieldsToUse;
        notifyDataSetChanged();
    }

    private String getHintDate(Field field) {
        return field.getTitle() + optionalTag(field);
    }

    private String getHintGroup(Field field) {
        return field.getPlaceholder() + optionalTag(field);
    }

    private String getHint(Field field) {
        return field.getPlaceholder() + optionalTag(field);
    }

    private String optionalTag(Field field) {
        return (field.isRequired() ? "" : " " + mContext.getString(R.string.optional_tag));
    }
}
