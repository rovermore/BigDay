package com.smallworldfs.moneytransferapp.utils;


import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.text.TextUtils;
import android.util.TypedValue;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.crashlytics.internal.common.CommonUtils;
import com.smallworldfs.moneytransferapp.BuildConfig;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.KeyValueData;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Attributes;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by luismiguel on 3/5/17
 */
public class Utils {

    private static Map<Character, Character> MAP_NORM;


    /**
     * Convert dp to px
     *
     * @param dp
     * @return
     */
    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    /**
     * Remove accents and normalize String
     *
     * @param value
     * @return
     */
    public static String removeAccents(String value, boolean supportSpanishChatacter) {
        if (MAP_NORM == null || MAP_NORM.size() == 0) {
            MAP_NORM = new HashMap<Character, Character>();
            MAP_NORM.put('À', 'A');
            MAP_NORM.put('Á', 'A');
            MAP_NORM.put('Â', 'A');
            MAP_NORM.put('Ã', 'A');
            MAP_NORM.put('Ä', 'A');
            MAP_NORM.put('È', 'E');
            MAP_NORM.put('É', 'E');
            MAP_NORM.put('Ê', 'E');
            MAP_NORM.put('Ë', 'E');
            MAP_NORM.put('Í', 'I');
            MAP_NORM.put('Ì', 'I');
            MAP_NORM.put('Î', 'I');
            MAP_NORM.put('Ï', 'I');
            MAP_NORM.put('Ù', 'U');
            MAP_NORM.put('Ú', 'U');
            MAP_NORM.put('Û', 'U');
            MAP_NORM.put('Ü', 'U');
            MAP_NORM.put('Ò', 'O');
            MAP_NORM.put('Ó', 'O');
            MAP_NORM.put('Ô', 'O');
            MAP_NORM.put('Õ', 'O');
            MAP_NORM.put('Ö', 'O');
            MAP_NORM.put('Ç', 'C');
            MAP_NORM.put('ª', 'A');
            MAP_NORM.put('º', 'O');
            MAP_NORM.put('§', 'S');
            MAP_NORM.put('³', '3');
            MAP_NORM.put('²', '2');
            MAP_NORM.put('¹', '1');
            MAP_NORM.put('à', 'a');
            MAP_NORM.put('á', 'a');
            MAP_NORM.put('â', 'a');
            MAP_NORM.put('ã', 'a');
            MAP_NORM.put('ä', 'a');
            MAP_NORM.put('è', 'e');
            MAP_NORM.put('é', 'e');
            MAP_NORM.put('ê', 'e');
            MAP_NORM.put('ë', 'e');
            MAP_NORM.put('í', 'i');
            MAP_NORM.put('ì', 'i');
            MAP_NORM.put('î', 'i');
            MAP_NORM.put('ï', 'i');
            MAP_NORM.put('ù', 'u');
            MAP_NORM.put('ú', 'u');
            MAP_NORM.put('û', 'u');
            MAP_NORM.put('ü', 'u');
            MAP_NORM.put('ò', 'o');
            MAP_NORM.put('ó', 'o');
            MAP_NORM.put('ô', 'o');
            MAP_NORM.put('õ', 'o');
            MAP_NORM.put('ö', 'o');
            if (!supportSpanishChatacter) {
                MAP_NORM.put('Ñ', 'N');
                MAP_NORM.put('ñ', 'n');
            }
            MAP_NORM.put('ç', 'c');
        }

        if (value == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder(value);

        for (int i = 0; i < value.length(); i++) {
            Character c = MAP_NORM.get(sb.charAt(i));
            if (c != null) {
                sb.setCharAt(i, c.charValue());
            }
        }

        return sb.toString();
    }

    /**
     * Return float with pixels size
     *
     * @param dip
     * @param context
     * @return
     */
    public static float getDpInPixels(float dip, Context context) {
        Resources r = context.getResources();
        float px = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dip,
                r.getDisplayMetrics()
        );
        return px;
    }

    /**
     * Check if Valid Email Formed
     *
     * @param target
     * @return
     */
    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }


    public static ArrayList<KeyValueData> convertMapToKeyValueList(ArrayList<TreeMap<String, String>> mapList) {
        ArrayList<KeyValueData> listConverted = new ArrayList<>();
        if (mapList != null) {
            for (TreeMap<String, String> map : mapList) {
                if (map != null && map.firstEntry() != null) {
                    listConverted.add(new KeyValueData(map.firstKey(), map.firstEntry().getValue()));
                }
            }
        }
        return listConverted;
    }

    public static ArrayList<TreeMap<String, String>> convertKeyListValueToTreeMap(ArrayList<KeyValueData> data) {
        ArrayList<TreeMap<String, String>> listData = new ArrayList<>();
        if (data != null) {
            for (KeyValueData dataValue : data) {
                TreeMap<String, String> map = new TreeMap<String, String>();
                map.put(dataValue.getKey(), dataValue.getValue());
                listData.add(map);
            }
        }
        return listData;
    }

    public static String formatDayMonthYearInCompleterDate(String day, String month, String year) {
        if (day.contains("T") && day.split("T").length > 1) {
            String formattedDay = day.split("T")[0];
            if (formattedDay != null) {
                if (formattedDay.length() > 1) {
                    day = formattedDay;
                } else {
                    day = "0" + formattedDay;
                }
            }
        } else if (day.length() == 1) {
            day = "0" + day;
        }
        if (month.length() == 1) {
            month = "0" + month;
        }

        return day + "/" + month + "/" + year;
    }

    public static boolean isLowerThan21SDK() {
        // Check if we're running on Android 5.0 or higher
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP;
    }

    public static String formatUrlWithFields(String baseUrl, ArrayList<String> fields, String valueTrigger) {
        if (TextUtils.isEmpty(valueTrigger)) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        builder.append(baseUrl);
        builder.append("?");
        for (String param : fields) {
            if (param.contains("=") && param.split("=").length > 1) {
                builder.append(param);
                builder.append("&");
            } else {
                if (!TextUtils.isEmpty(valueTrigger)) {
                    param = param + valueTrigger;
                    builder.append(param);
                    builder.append("&");
                }
            }
        }
        String formattedUrl = builder.toString().substring(0, builder.toString().length() - 1);
        if (formattedUrl.contains("http://")) {
            formattedUrl = formattedUrl.replace("http://", "https://");
        }
        return formattedUrl;
    }

    public static int getKeyboardDesplazateDimen(Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        if (density == 0.75f) {
            // LDPI
        } else if (density >= 1.0f && density < 1.5f) {
            // MDPI
        } else if (density == 1.5f) {
            // HDPI
        } else if (density > 1.5f && density <= 2.0f) {
            // XHDPI
            return Constants.CALCULATOR.CALCULATOR_SOFT_KEYBOARD_TRANSLATE_XHDPI;
        } else if (density > 2.0f && density <= 3.0f) {
            // XXHDPI
            return Constants.CALCULATOR.CALCULATOR_SOFT_KEYBOARD_TRANSLATE_XXHDPI;
        } else {
            // XXXHDPI
            return Constants.CALCULATOR.CALCULATOR_SOFT_KEYBOARD_TRANSLATE_XXXHDPI;
        }
        return Constants.CALCULATOR.CALCULATOR_SOFT_KEYBOARD_TRANSLATE_XHDPI;
    }

    public static void logCrashlyticsScreen(String screen) {
        // Crashlytics event
        FirebaseCrashlytics.getInstance().setCustomKey(Constants.SCREENS_IDENTIFIFERS.SCREEN, screen);
    }

    public static void logActionCrashlytics(String action) {
        // Crashlytics event
        FirebaseCrashlytics.getInstance().log(action);
    }

    public static int getPasswordStrength(String password, Attributes passwordRequirements) {
        List<String> passRequirements = new ArrayList<>();
        boolean alphaNum = false;
        boolean specialChar = false;
        boolean upperCase = false;
        if (password != null && passwordRequirements != null) {
            if (passwordRequirements.getMin() != null && password.length() > 0 && password.length() >= Integer.valueOf(passwordRequirements.getMin())) {
                passRequirements.add("min");
            }
            if (passwordRequirements.getMax() != null && password.length() > 0 && password.length() <= Integer.valueOf(passwordRequirements.getMax())) {
                passRequirements.add("max");
            }
            if (passwordRequirements.getRecommended() != null && password.length() >= Integer.valueOf(passwordRequirements.getRecommended())) {
                passRequirements.add("recommended");
            }
            if (passwordRequirements.isAlphaNum()) {
                alphaNum = true;
                if (password.matches(".*[a-zA-Z].*") && password.matches("(.)*(\\d)(.)*")) {
                    passRequirements.add("alpha_num");
                }
            }
            if (passwordRequirements.isSpecialChar()) {
                specialChar = true;
                if (password.matches(".*[:?!@#$%^&*(),.;\\-_'+/=\"].*")) {
                    passRequirements.add("special_char");
                }
            }
            if (passwordRequirements.isUpperCase()) {
                upperCase = true;
                if (password.matches(".*[A-Z].*")) {
                    passRequirements.add("upper_case");
                }
            }
        }

        if (!passRequirements.isEmpty()) {
            if (passRequirements.contains("min") && passRequirements.contains("max")) {
                if (alphaNum && specialChar && upperCase) {
                    if (passRequirements.contains("alpha_num") &&
                            passRequirements.contains("special_char") &&
                            passRequirements.contains("upper_case")) {
                        if (passRequirements.contains("recommended")) {
                            return 3;
                        } else {
                            return 2;
                        }
                    } else {
                        return 1;
                    }
                }

                if (alphaNum && specialChar) {
                    if (passRequirements.contains("alpha_num") &&
                            passRequirements.contains("special_char")) {
                        if (passRequirements.contains("recommended")) {
                            return 3;
                        } else {
                            return 2;
                        }
                    } else {
                        return 1;
                    }
                }

                if (alphaNum && upperCase) {
                    if (passRequirements.contains("alpha_num") &&
                            passRequirements.contains("upper_case")) {
                        if (passRequirements.contains("recommended")) {
                            return 3;
                        } else {
                            return 2;
                        }
                    } else {
                        return 1;
                    }
                }

                if (specialChar && upperCase) {
                    if (passRequirements.contains("upper_case") &&
                            passRequirements.contains("special_char")) {
                        if (passRequirements.contains("recommended")) {
                            return 3;
                        } else {
                            return 2;
                        }
                    } else {
                        return 1;
                    }
                }

                if (alphaNum) {
                    if (passRequirements.contains("alpha_num")) {
                        if (passRequirements.contains("recommended")) {
                            return 3;
                        } else {
                            return 2;
                        }
                    } else {
                        return 1;
                    }
                }

                if (specialChar) {
                    if (passRequirements.contains("special_char")) {
                        if (passRequirements.contains("recommended")) {
                            return 3;
                        } else {
                            return 2;
                        }
                    } else {
                        return 1;
                    }
                }

                if (upperCase) {
                    if (passRequirements.contains("upper_case")) {
                        if (passRequirements.contains("recommended")) {
                            return 3;
                        } else {
                            return 2;
                        }
                    } else {
                        return 1;
                    }
                }

                if (passRequirements.contains("recommended")) {
                    return 3;
                } else {
                    return 2;
                }

            }
            if (password.length() > 0) {
                return 1;
            }
        }
        return 0;
    }

    public static boolean isDeviceRooted() {
        return (checkRootMethod1() || checkRootMethod2() || checkRootMethod3() || CommonUtils.isRooted()) && !BuildConfig.DEBUG;
    }

    private static boolean checkRootMethod1() {
        String buildTags = android.os.Build.TAGS;
        return buildTags != null && buildTags.contains("test-keys");
    }

    private static boolean checkRootMethod2() {
        String[] paths = {"/system/app/Superuser.apk", "/sbin/su", "/system/bin/su", "/system/xbin/su", "/data/local/xbin/su", "/data/local/bin/su", "/system/sd/xbin/su",
                "/system/bin/failsafe/su", "/data/local/su", "/su/bin/su"};
        for (String path : paths) {
            if (new File(path).exists()) return true;
        }
        return false;
    }

    private static boolean checkRootMethod3() {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(new String[]{"/system/xbin/which", "su"});
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            return in.readLine() != null;
        } catch (Throwable t) {
            return false;
        } finally {
            if (process != null) process.destroy();
        }
    }
}
