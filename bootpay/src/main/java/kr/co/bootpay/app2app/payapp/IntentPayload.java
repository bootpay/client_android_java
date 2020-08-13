package kr.co.bootpay.app2app.payapp;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import kr.co.bootpay.enums.UX;
import kr.co.bootpay.model.Request;

public class IntentPayload {
    public String intentScheme = "bootpay";
    public String intentMethod = "app2app";
    public String intentMethodMode = "request";
    public String phoneNumber;
    public Double goodPrice;
    public String goodName;
    public String appScheme;
    public String appSchemeHost;
    public String packageName;


    public IntentPayload(Context context, Request request) {
        updateIntentScheme(request);
        if(request.getBoot_user() != null && request.getBoot_user().getPhone().length() > 0) this.phoneNumber = request.getBoot_user().getPhone();
        this.goodPrice = request.getPrice();
        this.goodName = request.getName();
        if(request.getBootExtra(context) != null && request.getBootExtra(context).getApp_scheme() != null) this.appScheme = request.getBootExtra(context).getApp_scheme();
        if(request.getBootExtra(context) != null && request.getBootExtra(context).getApp_scheme_host() != null) this.appScheme = request.getBootExtra(context).getApp_scheme_host();
        this.packageName = context.getApplicationContext().getPackageName();
    }

    private String getIntentScheme() {
        return intentScheme + "://" + intentMethod;
    }

    private String getIntentMethodMode() {
        return "?mode=" + intentMethodMode;
    }

    private String getPhoneNumber() {
        if(phoneNumber == null) { return ""; }
        return phoneNumber.length() > 0 ? "&phoneNumber=" + phoneNumber.replace("-","") : "";
    }

    private String getGoodPrice() {
        if(goodPrice == null) { return ""; }
        return goodPrice.toString().length() > 0 ? "&goodPrice=" + goodPrice : "";
    }

    private String getGoodName() {
        if(goodName == null) { return ""; }
        return goodName.length() > 0 ? "&goodName=" + goodName : "";
    }

    private String getAppSchemeHost() {
        if(appSchemeHost == null) { return ""; }
        return appSchemeHost.length() > 0 ? "&callback_url=" + appSchemeHost : "";
    }

    private String getAppScheme() {
        if(appScheme == null) { return ""; }
        return appScheme.length() > 0 ? "&scheme=" + appScheme : "";
    }

    private String getPackageName() {
        if(packageName == null) { return ""; }
        return packageName.length() > 0 ? "&application_id=" + packageName : "";
    }

    private void updateIntentScheme(Request request) {
        UX ux = request.getUX();
        switch (ux) {
            case APP2APP_REMOTE:
                this.intentScheme = "payappRequest";
                break;
            case APP2APP_CARD_SIMPLE:
                this.intentScheme = "payappNotePayment";
                break;
            case APP2APP_NFC:
                this.intentScheme = "payappNfcPayment";
                break;
            case APP2APP_SAMSUNGPAY:
                this.intentScheme = "payappSamsungPayment";
                break;
            case APP2APP_SUBSCRIPT:
                this.intentScheme = "payappFixedPeriodPayment";
                break;
            case APP2APP_CASH_RECEIPT:
                this.intentScheme = "payappCashReceiptPayment";
                break;
            case APP2APP_OCR:
                this.intentScheme = "payappOcrPayment";
                break;
            case NONE:
            default:
                new RuntimeException("App2App Pay UX cannot be None");
                break;
        }
    }

    public Uri toIntentUri() {
        String value = getIntentScheme() +
                getIntentMethodMode() +
                getPhoneNumber() +
                getGoodPrice() +
                getGoodName() +
                getAppSchemeHost() +
                getAppScheme() +
                getPackageName();

        Log.d("value", value);

        return Uri.parse(value);
    }
}
