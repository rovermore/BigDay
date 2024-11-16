package com.smallworldfs.moneytransferapp.modules.calculator.domain.model;

import static com.smallworldfs.moneytransferapp.utils.ConstantsKt.STRING_EMPTY;

import androidx.core.util.Pair;

import com.smallworldfs.moneytransferapp.domain.migrated.calculator.model.CalculatorDataDTO;
import com.smallworldfs.moneytransferapp.domain.migrated.calculator.model.Currency;
import com.smallworldfs.moneytransferapp.domain.migrated.calculator.model.DeliveryMethodDTO;
import com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.model.CountryDTO;
import com.smallworldfs.moneytransferapp.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by luis on 13/7/17.
 */

public class CalculatorData {

    private Pair<String, String> originCountry;

    private Pair<String, String> payoutCountry;

    private Pair<String, String> sendingCurrency;

    private Pair<String, String> payoutCurrency;

    private ArrayList<Method> listMethods;

    private Pair<String, String> deliveryMethod;

    private String amount;

    private Method payoutMethod;

    private String operationType;

    private String beneficiaryId;

    private String youPay = Constants.CALCULATOR.DEFAULT_AMOUNT;

    private String representativeCode;

    private RateValues currentCalculator;

    private String beneficiaryType;

    public CalculatorData() {

    }

    public CalculatorData(CalculatorData data) {
        this.originCountry = data.getOriginCountry();
        this.payoutCountry = data.getPayoutCountry();
        this.sendingCurrency = data.getSendingCurrency();
        this.listMethods = data.getListMethods();
        this.payoutCurrency = data.getPayoutCurrency();
        this.payoutMethod = data.getPayoutMethod();
        this.operationType = data.getOperationType();
        this.deliveryMethod = data.getDeliveryMethod();
        this.amount = data.getAmount();
        this.youPay = data.getYouPay();
        this.currentCalculator = data.getCurrentCalculator();
        this.beneficiaryType = data.getBeneficiaryType();
    }

    public Pair<String, String> getOriginCountry() {
        return originCountry;
    }

    public void setOriginCountry(Pair<String, String> originCountry) {
        this.originCountry = originCountry;
    }

    public Pair<String, String> getPayoutCountry() {
        return payoutCountry;
    }

    public void setPayoutCountry(Pair<String, String> payoutCountry) {
        this.payoutCountry = payoutCountry;
    }

    public Pair<String, String> getSendingCurrency() {
        return sendingCurrency;
    }

    public void setSendingCurrency(Pair<String, String> sendingCurrency) {
        this.sendingCurrency = sendingCurrency;
    }

    public ArrayList<Method> getListMethods() {
        return listMethods;
    }

    public void setListMethods(ArrayList<Method> listMethods) {
        this.listMethods = listMethods;
    }

    public Pair<String, String> getPayoutCurrency() {
        return payoutCurrency;
    }

    public void setPayoutCurrency(Pair<String, String> payoutCurrency) {
        this.payoutCurrency = payoutCurrency;
    }

    public Method getPayoutMethod() {
        return payoutMethod;
    }

    public void setPayoutMethod(Method payoutMethod) {
        this.payoutMethod = payoutMethod;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public Pair<String, String> getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(Pair<String, String> deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getYouPay() {
        return youPay;
    }

    public void setYouPay(String youPay) {
        this.youPay = youPay;
    }

    public RateValues getCurrentCalculator() {
        return currentCalculator;
    }

    public void setCurrentCalculator(RateValues currentCalculator) {
        this.currentCalculator = currentCalculator;
    }

    public String getBeneficiaryId() {
        if (beneficiaryId != null) {
            return beneficiaryId;
        } else {
            return "";
        }
    }

    public void setBeneficiaryId(String beneficiaryId) {
        this.beneficiaryId = beneficiaryId;
    }

    public String getRepresentativeCode() {
        return representativeCode;
    }

    public void setRepresentativeCode(String representativeCode) {
        this.representativeCode = representativeCode;
    }

    public String getBeneficiaryType() {
        return beneficiaryType;
    }

    public void setBeneficiaryType(String beneficiaryType) {
        this.beneficiaryType = beneficiaryType;
    }

    public CalculatorDataDTO mapToDTO(String currencyType) {
        CountryDTO originCountryDTO = new CountryDTO();
        if (originCountry != null) {
            originCountryDTO = new CountryDTO(originCountry.first, originCountry.second, STRING_EMPTY, STRING_EMPTY, STRING_EMPTY, false);
        }

        CountryDTO payoutCountryDTO = new CountryDTO();
        if (payoutCountry != null) {
            payoutCountryDTO = new CountryDTO(payoutCountry.first, payoutCountry.second, STRING_EMPTY, STRING_EMPTY, STRING_EMPTY, false);
        }

        Currency sendingCurrencyDTO = new Currency();
        if (sendingCurrency != null) {
            sendingCurrencyDTO = new Currency(sendingCurrency.second, sendingCurrency.first);
        }

        Currency payoutCurrencyDTO = new Currency();
        if (payoutCurrency != null) {
            payoutCurrencyDTO = new Currency(payoutCurrency.second, payoutCurrency.first);
        }

        DeliveryMethodDTO deliveryMethodDTO = new DeliveryMethodDTO();
        if (deliveryMethod != null) {
            deliveryMethodDTO = new DeliveryMethodDTO(deliveryMethod.first, deliveryMethod.second);
        }

        List<Method> methodList = new ArrayList<>();
        if (listMethods != null) {
            methodList = listMethods;
        }

        Method method = new Method(new TreeMap<>());
        if (payoutMethod != null) {
            method = payoutMethod;
        }

        String amountDTO = STRING_EMPTY;
        if (amount != null) {
            amountDTO = amount;
        }

        String operationTypeDTO = STRING_EMPTY;
        if (operationType != null) {
            operationTypeDTO = operationType;
        }

        String beneficiaryIdDTO = STRING_EMPTY;
        if (beneficiaryId != null) {
            beneficiaryIdDTO = beneficiaryId;
        }

        String youPayDTO = STRING_EMPTY;
        if (youPay != null) {
            youPayDTO = youPay;
        }

        String representativeCodeDTO = STRING_EMPTY;
        if (representativeCode != null) {
            representativeCodeDTO = representativeCode;
        }

        String beneficiaryTypeDTO = STRING_EMPTY;
        if (beneficiaryType != null) {
            beneficiaryTypeDTO = beneficiaryType;
        }

        String currencyTypeDTO = STRING_EMPTY;
        if (currencyType != null) {
            currencyTypeDTO = currencyType;
        }

        return new CalculatorDataDTO(
                originCountryDTO,
                payoutCountryDTO,
                sendingCurrencyDTO,
                payoutCurrencyDTO,
                methodList,
                deliveryMethodDTO,
                amountDTO,
                method,
                operationTypeDTO,
                beneficiaryIdDTO,
                youPayDTO,
                representativeCodeDTO,
                currentCalculator,
                beneficiaryTypeDTO,
                currencyTypeDTO
        );
    }
}
