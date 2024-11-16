package com.smallworldfs.moneytransferapp.utils;


import static com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.activity.GenericSelectDropContentActivity.ACTIVITY_NAME_EXTRA;
import static com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.activity.GenericSelectDropContentActivity.CONTENT_FIELD_POSITION;
import static com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.activity.GenericSelectDropContentActivity.CONTENT_FILE_NAME;
import static com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.activity.GenericSelectDropContentActivity.CONTENT_LIST_EXTRA;
import static com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.activity.GenericSelectDropContentActivity.CONTENT_STEP_ID;
import static com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.activity.GenericSelectDropContentActivity.FIELD_STEP_TYPE;
import static com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.activity.GenericSelectDropContentActivity.SEARCHABLE_EXTRA;
import static com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.activity.GenericSelectDropContentActivity.SELECTED_VALUE;
import static com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.activity.GenericSelectDropContentActivity.TYPE_CELL;
import static com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.activity.GenericSelectDropContentActivity.URL_EXTRA_DATA;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;

import com.google.gson.Gson;
import com.smallworldfs.moneytransferapp.R;
import com.smallworldfs.moneytransferapp.SmallWorldApplication;
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ScreenName;
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.DialogExt;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.KeyValueData;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.structure.Step;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.ValidationStepResponse;
import com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.activity.GenericSelectDropContentActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import rx.Completable;
import rx.Subscription;

/**
 * Created by luismiguel on 21/9/17
 */
public class FormUtils {

    /**
     * Prepare data to all kind of server requests
     */
    public static ArrayList<Pair<String, String>> fillDataFormToPerformRequest(ArrayList<Field> formData) {
        ArrayList<android.util.Pair<String, String>> list = new ArrayList<>();

        for (Field field : formData) {
            switch (field.getType()) {
                case Constants.FIELD_TYPE.TEXT:
                case Constants.FIELD_TYPE.TEXT_AREA:
                case Constants.FIELD_TYPE.PASSWORD:
                case Constants.FIELD_TYPE.FILE:
                case Constants.FIELD_TYPE.EMAIL:
                    list.add(new android.util.Pair<>(field.getName() == null ? "" : field.getName(), field.getValue() == null ? "" : field.getValue()));
                    break;
                case Constants.FIELD_TYPE.COMBO:
                    if (field.getSubtype().equalsIgnoreCase(Constants.FIELD_SUBTYPES.COMBO_API)) {
                        // Try to get keyValue data, and if is not present (Created beneficiary), try to get value from value attr
                        if (!TextUtils.isEmpty(field.getName())) {
                            String keyValue = field.getKeyValue();
                            if (TextUtils.isEmpty(keyValue)) {
                                keyValue = field.getValue();
                            }
                            list.add(new android.util.Pair<>(field.getName(), keyValue));
                        }
                    } else {
                        String value = field.getKeyValue() == null ? (field.getValue() == null ? "" : field.getValue()) : field.getKeyValue();
                        list.add(new android.util.Pair<>(field.getName() == null ? "" : field.getName(), value));
                    }
                    break;
                case Constants.FIELD_TYPE.GROUP:
                    if (field.getSubtype().equalsIgnoreCase(Constants.FIELD_SUBTYPES.TEXT_GROUP)) {
                        int position = 0;
                        for (Field fieldAux : field.getChilds()) {
                            list.add(new android.util.Pair<>(fieldAux.getName() == null ? "" : fieldAux.getName(),
                                    position == 0 ? fieldAux.getKeyValue() == null ? "" : fieldAux.getKeyValue() : fieldAux.getValue() == null ? "" : fieldAux.getValue()));
                            position++;
                        }
                    } else {
                        for (Field fieldAux : field.getChilds()) {
                            list.add(new android.util.Pair<>(fieldAux.getName() == null ? "" : fieldAux.getName(), fieldAux.getValue() == null ? "" : fieldAux.getValue()));
                        }
                    }
                    break;
                case Constants.FIELD_TYPE.CHECK_BOX:
                case Constants.FIELD_TYPE.RADIO_BUTTON:
                    // Convert String (true, false) to String (0, 1)
                    if (!TextUtils.isEmpty(field.getName()) && !TextUtils.isEmpty(field.getValue())) {
                        String value = Boolean.parseBoolean(field.getValue()) ? "1" : "0";
                        list.add(new android.util.Pair<>(field.getName(), value));
                    }
                    break;
            }
        }


        for (Pair<String, String> pair : list) {
            Log.d("Send", "" + pair.first + ":" + "\"" + pair.second + "\"");
        }

        return list;
    }

    public static Map<String, String> fillDataToPerformAttach(ArrayList<Field> fields) {
        Map<String, String> params = new HashMap<>();
        for (Field field : fields) {
            if (!field.getType().equalsIgnoreCase("file") && !field.getType().equalsIgnoreCase("whitebox")) {
                if (field.getChilds() != null && field.getChilds().size() > 0) {
                    for (Field child : field.getChilds()) {
                        if (child.getKeyValue() != null) {
                            params.put(child.getName(), child.getKeyValue());
                        } else if (child.getValue() != null) {
                            params.put(child.getName(), child.getValue());
                        }
                    }
                } else {
                    if (field.getKeyValue() != null) {
                        params.put(field.getName(), field.getKeyValue());
                    } else if (field.getValue() != null) {
                        params.put(field.getName(), field.getValue());
                    }
                }
            }
        }
        return params;
    }

    /**
     * Normalize data to use un Checkout perform request
     */
    public static ArrayList<KeyValueData> normalizeDataToFillInCheckout(HashMap<Step, HashMap<String, String>> formDataByStep) {
        if (formDataByStep != null) {
            ArrayList<KeyValueData> data = new ArrayList<>();
            for (Map.Entry<Step, HashMap<String, String>> entry : formDataByStep.entrySet()) {
                for (Map.Entry<String, String> entryData : entry.getValue().entrySet()) {
                    data.add(new KeyValueData(entryData.getKey(), entryData.getValue()));
                }
            }
            return data;
        }
        return null;
    }

    /**
     * Set Clear errors depending on validation step response.json
     */
    public static void setClearErrors(ValidationStepResponse validationStepResponse, ArrayList<Field> formData) {
        if (validationStepResponse == null) {
            // Clear previous errors
            for (Field field : formData) {
                if (!TextUtils.isEmpty(field.getErrorMessage())) {
                    field.setErrorMessage("");
                }
                if (field.getChilds() != null && field.getChilds().size() > 0) {
                    for (Field fieldAux : field.getChilds()) {
                        if (!TextUtils.isEmpty(fieldAux.getErrorMessage())) {
                            fieldAux.setErrorMessage("");
                        }
                    }
                }
            }

        } else {
            for (Map.Entry<String, ArrayList<String>> error : validationStepResponse.getValidationErrors().getValidationStepErrros().entrySet()) {
                for (Field field : formData) {
                    if (!TextUtils.isEmpty(field.getType()) && field.getType().equalsIgnoreCase(Constants.FIELD_TYPE.GROUP) && field.getSubtype().equalsIgnoreCase(Constants.FIELD_SUBTYPES.GROUP_DATE)) {
                        if (!TextUtils.isEmpty(field.getName()) && field.getName().equals(error.getKey())) {
                            field.setErrorMessage(error.getValue().get(0));
                        } else {
                            for (Field child : field.getChilds()) {
                                if (!TextUtils.isEmpty(child.getName()) && child.getName().equals(error.getKey())) {
                                    field.setErrorMessage(error.getValue().get(0));
                                }
                            }
                        }
                    } else {
                        if (field.getChilds() != null && field.getChilds().size() > 0) {
                            for (Field fieldChild : field.getChilds()) {
                                if (!TextUtils.isEmpty(fieldChild.getName()) && fieldChild.getName().equals(error.getKey())) {
                                    fieldChild.setErrorMessage(error.getValue().get(0));
                                }
                            }
                        } else {
                            if (!TextUtils.isEmpty(field.getName()) && field.getName().equals(error.getKey())) {
                                field.setErrorMessage(error.getValue().get(0));
                            }
                        }
                    }
                }
            }
        }
    }


    /**
     * Process Api Field type
     */
    public static void processApiFieldType(Context context, Activity activity, Field field, int position, String type, ArrayList<Field> formData, String stepId, String deliveryMethod) {

        String urlFormatted = "";
        // Combo type api
        if (TextUtils.isEmpty(field.getTriggers())) {
            // normal api type
            urlFormatted = Utils.formatUrlWithFields(field.getRefApi().getUrl(), field.getRefApi().getParams(), null);

        } else {
            // Trigger api type
            // First search field trigger
            for (Field fieldAux : formData) {
                if (!TextUtils.isEmpty(fieldAux.getName()) && fieldAux.getName().equalsIgnoreCase(field.getTriggers())) {
                    urlFormatted = Utils.formatUrlWithFields(field.getRefApi().getUrl(), field.getRefApi().getParams(), fieldAux.getKeyValue());
                    break;
                }
            }
        }

        if (!TextUtils.isEmpty(urlFormatted)) {

            String name = field.getPlaceholder();
            String typeCell = field.getSubtype();

            Intent intent = new Intent(context, GenericSelectDropContentActivity.class);

            intent.putParcelableArrayListExtra(CONTENT_LIST_EXTRA, null);
            intent.putExtra(CONTENT_FIELD_POSITION, position);
            intent.putExtra(FIELD_STEP_TYPE, type);
            intent.putExtra(SELECTED_VALUE, field.getKeyValue());
            intent.putExtra(SEARCHABLE_EXTRA, field.isSearchable());
            intent.putExtra(URL_EXTRA_DATA, urlFormatted);

            intent.putExtra(GenericSelectDropContentActivity.SCREEN_NAME, mapFieldNameForAnalytics(field.getName()));
            intent.putExtra(GenericSelectDropContentActivity.FIELD_NAME, field.getName());
            intent.putExtra(GenericSelectDropContentActivity.DELIVERY_METHOD, deliveryMethod);
            if (!TextUtils.isEmpty(stepId)) {
                intent.putExtra(CONTENT_STEP_ID, stepId);
            }

            if (TextUtils.isEmpty(field.getName()) && field.getChilds() != null && field.getChilds().get(0) != null) {
                name = field.getChilds().get(0).getName();
            }

            intent.putExtra(ACTIVITY_NAME_EXTRA, name);
            intent.putExtra(TYPE_CELL, typeCell);

            ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(activity, R.anim.from_right_to_left, R.anim.from_position_to_left);
            activity.startActivityForResult(intent, Constants.REQUEST_CODES.GENERIC_DROP_SELECTOR_REQUEST_CODE, compat.toBundle());

        } else {

            if (position > 0) {
                Field prevField = formData.get(position - 1);
                if (prevField != null) {
                    new DialogExt().showInfoDialog(context,
                            context.getString(R.string.empty_field_remember_text),
                            String.format(context.getString(R.string.remember_text), prevField.getName()),
                            null,
                            null, SmallWorldApplication.getStr(R.string.accept_text), SmallWorldApplication.getStr(R.string.cancel));

                }
            }
        }
    }

    /**
     * Generic Drop Activity
     */
    public static void showGenericSelectorActivity(@NonNull final Activity activity,
                                                   @Nullable final ArrayList<TreeMap<String, String>> dataField,
                                                   @NonNull final String typeCell,
                                                   int position,
                                                   @NonNull final String type,
                                                   @NonNull final String stepId,
                                                   @NonNull final Field field,
                                                   @NonNull final String deliveryMethod) {
        if (dataField != null && dataField.size() != 0) {
            final Intent intent = new Intent(activity, GenericSelectDropContentActivity.class);
            intent.putExtra(GenericSelectDropContentActivity.SCREEN_NAME, mapFieldNameForAnalytics(field.getName()));
            intent.putExtra(GenericSelectDropContentActivity.FIELD_NAME, field.getName());
            intent.putExtra(GenericSelectDropContentActivity.DELIVERY_METHOD, deliveryMethod);
            intent.putExtra(CONTENT_FIELD_POSITION, position);
            intent.putExtra(FIELD_STEP_TYPE, type);
            intent.putExtra(SELECTED_VALUE, field.getKeyValue());
            intent.putExtra(CONTENT_STEP_ID, stepId);
            intent.putExtra(SEARCHABLE_EXTRA, field.isSearchable());
            intent.putExtra(ACTIVITY_NAME_EXTRA, field.getTitle());
            final String dataFileName = System.currentTimeMillis() + "";
            ActivityLargeDataTransferUtils.saveFile(activity, dataFileName, new Gson().toJson(dataField), new Completable.CompletableSubscriber() {
                @Override
                public void onCompleted() {
                    intent.putExtra(CONTENT_FILE_NAME, dataFileName);
                    String name = field.getPlaceholder();

                    if (TextUtils.isEmpty(name) && field.getChilds() != null && field.getChilds().get(0) != null)
                        name = field.getChilds().get(0).getName();

                    intent.putExtra(ACTIVITY_NAME_EXTRA, name);
                    intent.putExtra(TYPE_CELL, typeCell);

                    ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(activity, R.anim.from_right_to_left, R.anim.from_position_to_left);
                    activity.startActivityForResult(intent, Constants.REQUEST_CODES.GENERIC_DROP_SELECTOR_REQUEST_CODE, compat.toBundle());
                }

                @Override
                public void onError(Throwable e) {
                }

                @Override
                public void onSubscribe(Subscription d) {
                }
            });
        }
    }

    public static String extractCountryPhonePrefix(ArrayList<TreeMap<String, String>> data, String countryKey) {
        for (TreeMap<String, String> country : data) {
            for (TreeMap.Entry<String, String> countryData : country.entrySet()) {
                if (countryData.getKey().equalsIgnoreCase(countryKey)) {
                    return extractPrefix(countryData.getValue());
                }
            }
        }
        return "";
    }

    public static String extractPrefix(String country) {
        if (country.contains("(") && country.contains(")")) {
            int firstIdx = country.indexOf("(") + 1;
            int lastIdx = country.indexOf(")");
            return "+" + country.substring(firstIdx, lastIdx);
        } else {
            return "";
        }
    }

    public static String mapFieldNameForAnalytics(String fieldName) {
        String screenName = "";
        if (fieldName != null) {
            switch (fieldName) {
                case "mtn_purpose":
                    screenName = ScreenName.TRANSACTION_PURPOSE_SCREEN.getValue();
                    break;
                case "clientRelation":
                    screenName = ScreenName.RELATIONSHIP_WITH_BENEFICIARY_SCREEN.getValue();
                    break;
                case "state":
                    screenName = ScreenName.REGION_SCREEN.getValue();
                    break;
                case "paymentMethod":
                    screenName = ScreenName.PAYMENT_METHOD_SCREEN.getValue();
                    break;
                case "sourceoffunds":
                    screenName = ScreenName.SOURCE_FUNDS_SCREEN.getValue();
                    break;
                case "ocupation":
                    screenName = ScreenName.OCCUPATION_SCREEN.getValue();
                    break;
                case "city":
                    screenName = ScreenName.CITY_SCREEN.getValue();
                    break;
                default:
                    screenName = "";
            }
        }
        return screenName;
    }
}
