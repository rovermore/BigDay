package com.smallworldfs.moneytransferapp.utils;

import android.text.TextUtils;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

/**
 * Created by luismiguel on 25/10/17
 */
public class AmountFormatter {

    /**
     * Format number in String Mode
     *
     * @param number
     * @return
     */
    public static String formatStringNumber(String number) {

        String DECIMAL_SEPARATOR = String.valueOf(new DecimalFormat().getDecimalFormatSymbols().getDecimalSeparator());
        String UNITS_SEPARATOR = String.valueOf(new DecimalFormat().getDecimalFormatSymbols().getGroupingSeparator());

        if (!TextUtils.isEmpty(number)) {
            if (number.length() == 1 && (number.equals(DECIMAL_SEPARATOR) || number.equals(UNITS_SEPARATOR)))
                return "";
            try {

                DecimalFormat formatter = new DecimalFormat();
                String pattern = "#" + UNITS_SEPARATOR + "###" + DECIMAL_SEPARATOR + "##";
                formatter.applyLocalizedPattern(pattern);
                //Remove digits if more than 2 after decimal separator
                int numberOfDecimals;
                if (number.contains(DECIMAL_SEPARATOR)) {
                    String[] numberSplitted = number.split(String.format("\\%s", DECIMAL_SEPARATOR));
                    if (numberSplitted.length >= 2) {
                        numberOfDecimals = numberSplitted[1].length();

                        if (numberOfDecimals > 2) {
                            String result = formatDoubleAmountNumber(Double.parseDouble(number));
                            number = result;
                        }
                    }
                }
                if (number.endsWith("."))
                    number = number.substring(0, number.length() - 1) + DECIMAL_SEPARATOR;

                if (number.replaceAll("[^" + DECIMAL_SEPARATOR + "]+", "").length() > 1)
                    number = number.substring(0, number.length() - 1);
                String originalString = number.replaceAll("[^0-9" + DECIMAL_SEPARATOR + "]", "");

                Double doubleValue;

                originalString = originalString.replaceAll(String.format("\\%s", DECIMAL_SEPARATOR), ".");

                doubleValue = Double.parseDouble(originalString);

                String formattedString = formatter.format(doubleValue);
                String lastCharacter = number.substring(number.length() - 1, number.length());

                // need if value equals ###,0
                String lastTwoCharacter = "";
                if (number.length() > 1)
                    lastTwoCharacter = number.substring(number.length() - 2, number.length());
                if (!lastTwoCharacter.equals(DECIMAL_SEPARATOR + "0") )
                    return formattedString + (lastCharacter.equals(DECIMAL_SEPARATOR) ? DECIMAL_SEPARATOR : "");
                else
                    return formattedString + lastTwoCharacter;
            } catch (NumberFormatException nfe) {
                Log.e("STACK", "----------------------",nfe);
            }
        }
        return "";
    }

    /**
     * Format double to default precission
     *
     * @param amount
     * @return
     */
    public static String formatDoubleAmountNumber(double amount) {

        String doubleFormatted = String.format("%,.2f", amount);
        return forceCollonSeparator(doubleFormatted);
    }


    public static String formatDoubleRateNumber(double amount) {

        String doubleFormatted = String.format("%,.4f", amount);
        return forceCollonSeparator(doubleFormatted);
    }

    /**
     * Force semicollon such decimal separator
     *
     * @param amountProjection
     * @return
     */
    public static String forceCollonSeparator(String amountProjection) {
        String DECIMAL_SEPARATOR = String.valueOf(new DecimalFormat().getDecimalFormatSymbols().getDecimalSeparator());
        String UNITS_SEPARATOR = String.valueOf(new DecimalFormat().getDecimalFormatSymbols().getGroupingSeparator());
        // Solve Format Locale Errors
        NumberFormat nf = NumberFormat.getInstance();

        if (nf instanceof DecimalFormat) {
            DecimalFormatSymbols sym = ((DecimalFormat) nf).getDecimalFormatSymbols();
            char decSeparator = sym.getDecimalSeparator();

            if (String.valueOf(decSeparator).equalsIgnoreCase(UNITS_SEPARATOR)) {
                amountProjection = amountProjection.replaceAll(DECIMAL_SEPARATOR, "#");
                amountProjection = amountProjection.replaceAll(String.format("\\%s", UNITS_SEPARATOR), DECIMAL_SEPARATOR);
                amountProjection = amountProjection.replaceAll("#", UNITS_SEPARATOR);
            }
        }

        return amountProjection;
    }

    /**
     * Format Double parse such String
     *
     * @param value
     * @return
     */
    public static String normalizeDoubleString(String value) {
        String DECIMAL_SEPARATOR = String.valueOf(new DecimalFormat().getDecimalFormatSymbols().getDecimalSeparator());
        if (TextUtils.isEmpty(value))
            return "";
        if (value.contains(DECIMAL_SEPARATOR)) {
            String valueSplitted[] = value.split(String.format("\\%s", DECIMAL_SEPARATOR));
            String lastCharacter = value.substring(value.length() - 1);
            if (!lastCharacter.matches("[0-9]"))
                value = value.substring(0, value.length() - 1);
            if (valueSplitted.length == 2) {
                if (valueSplitted[1].equals("0"))
                    return valueSplitted[0] + DECIMAL_SEPARATOR + "00";
                else
                    return value;
            }
        }
        return value + DECIMAL_SEPARATOR + "00";
    }

    public static String parseStringCurrentCountryNumbers(String value) {
        Character DECIMAL_SEPARATOR = new DecimalFormat().getDecimalFormatSymbols().getDecimalSeparator();
        Character UNITS_SEPARATOR = new DecimalFormat().getDecimalFormatSymbols().getGroupingSeparator();
        String numberText = "";
        try {
            double number = Double.parseDouble(value);
            DecimalFormat df = (DecimalFormat) DecimalFormat.getCurrencyInstance();
            DecimalFormatSymbols dfs = new DecimalFormatSymbols();
            dfs.setCurrencySymbol("");
            dfs.setMonetaryDecimalSeparator(DECIMAL_SEPARATOR);
            dfs.setGroupingSeparator(UNITS_SEPARATOR);
            df.setDecimalFormatSymbols(dfs);
            numberText = df.format(number);
        } catch (NumberFormatException e) {
            return value;
        }
        return numberText;
    }

    public static String convertServerValueToLocalized(String value) {
        String DECIMAL_SEPARATOR = String.valueOf(new DecimalFormat().getDecimalFormatSymbols().getDecimalSeparator());
        if (TextUtils.isEmpty(value))
            return "";
        value = value.replace(".", DECIMAL_SEPARATOR);

        return value;

    }


    public static String typedMoreThanTwoDecimals(String value){
        String UNITS_SEPARATOR = String.valueOf(new DecimalFormat().getDecimalFormatSymbols().getGroupingSeparator());
        String DECIMAL_SEPARATOR = String.valueOf(new DecimalFormat().getDecimalFormatSymbols().getDecimalSeparator());

        DecimalFormat formatter = new DecimalFormat();
        String pattern = "#" + UNITS_SEPARATOR + "###" + DECIMAL_SEPARATOR + "##";
        formatter.applyLocalizedPattern(pattern);
        //Remove digits if more than 2 after decimal separator
        int numberOfDecimals = 0;
        if (value.contains(DECIMAL_SEPARATOR)) {
            String[] numberSplitted = value.split(String.format("\\%s", DECIMAL_SEPARATOR));
            if (numberSplitted.length >= 2) {
                numberOfDecimals = numberSplitted[1].length();
            }
        }
        if (numberOfDecimals > 2) {
            value = value.substring(0, value.length() - 1); //delete last digit
            return typedMoreThanTwoDecimals(value);
        }else{
            return value;
        }
    }

    /**
     * Normalize amount to email to server
     *
     * @param amount
     * @return
     */
    public static String normalizeAmountToSend(String amount) {
        String UNITS_SEPARATOR = String.valueOf(new DecimalFormat().getDecimalFormatSymbols().getGroupingSeparator());
        String DECIMAL_SEPARATOR = String.valueOf(new DecimalFormat().getDecimalFormatSymbols().getDecimalSeparator());
        String formattedNumber = formatStringNumber(amount);

        String valueWithoutUnitsSeparator = formattedNumber.replaceAll(String.format("\\%s", UNITS_SEPARATOR), "");
        return valueWithoutUnitsSeparator.replaceAll(String.format("\\%s", DECIMAL_SEPARATOR), ".");
    }
}
