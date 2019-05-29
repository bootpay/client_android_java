package kr.co.bootpay;

import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import kr.co.bootpay.analytics.AnalyticsService;
import kr.co.bootpay.api.ApiPresenter;
import kr.co.bootpay.api.ApiService;
import kr.co.bootpay.enums.Method;
import kr.co.bootpay.enums.PG;
import kr.co.bootpay.enums.UX;
import kr.co.bootpay.listner.CancelListener;
import kr.co.bootpay.listner.CloseListener;
import kr.co.bootpay.listner.ConfirmListener;
import kr.co.bootpay.listner.DoneListener;
import kr.co.bootpay.listner.ErrorListener;
import kr.co.bootpay.listner.EventListener;
import kr.co.bootpay.listner.ReadyListener;
import kr.co.bootpay.model.BootExtra;
import kr.co.bootpay.model.Item;
import kr.co.bootpay.model.RemoteOrderForm;
import kr.co.bootpay.model.RemoteOrderPre;
import kr.co.bootpay.model.Request;
import kr.co.bootpay.model.BootUser;
import kr.co.bootpay.model.SMSPayload;
import kr.co.bootpay.pref.UserInfo;
import kr.co.bootpay.valid.PGAvailable;
import kr.co.bootpay.valid.ValidRequest;

public class BootpayBuilder {
    private Context context;
    private FragmentManager fragmentManager;
    protected Request request = new Request();
    protected EventListener listener;
    private ErrorListener error;
    private ReadyListener ready;
    private CloseListener close;
    private DoneListener done;
    private CancelListener cancel;
    private ConfirmListener confirm;
    private BootpayDialog dialog;
    private ApiPresenter presenter;


    private BootpayBuilder() {}

    public BootpayBuilder(Context context) {
        this.context = context;
    }

    public BootpayBuilder(FragmentManager manager) {
        fragmentManager = manager;
    }

    public BootpayBuilder setContext(Context context) {
        this.context = context;
        return this;
    }

    public BootpayBuilder setApplicationId(String id) {
        request.setApplicationId(id);
        return this;
    }

    public BootpayBuilder setPrice(int price) {
        request.setPrice(new Double(price));
        return this;
    }

    public BootpayBuilder setPrice(Double price) {
        request.setPrice(price);
        return this;
    }

    public BootpayBuilder setPG(String pg) {
        request.setPG(pg);
        return this;
    }

    public BootpayBuilder setPG(PG pg) {
        switch (pg) {
            case BOOTPAY:
                request.setPG("bootpay");
                break;
            case PAYAPP:
                request.setPG("payapp");
                break;
            case DANAL:
                request.setPG("danal");
                break;
            case KCP:
                request.setPG("kcp");
                break;
            case INICIS:
                request.setPG("inicis");
                break;
            case LGUP:
                request.setPG("lgup");
                break;
            case KAKAO:
                request.setPG("kakao");
                break;
            case EASYPAY:
                request.setPG("easypay");
                break;
            case KICC:
                request.setPG("easypay");
                break;
            case TPAY:
                request.setPG("tpay");
            case JTNET:
                request.setPG("tpay");
                break;
            case MOBILIANS:
                request.setPG("mobilians");
                break;
            case PAYLETTER:
                request.setPG("payletter");
                break;
            case NICEPAY:
                request.setPG("nicepay");
                break;
            case PAYCO:
                request.setPG("payco");
                break;
        }
        return this;
    }

    public BootpayBuilder setName(String name) {
        request.setName(name);
        return this;
    }

    public BootpayBuilder addItem(String name, int quantity, String primaryKey, int price) {
        request.addItem(new Item(name, quantity, primaryKey, new Double(price), "", "", ""));
        return this;
    }

    public BootpayBuilder addItem(String name, int quantity, String primaryKey, int price, String cat1, String cat2, String cat3) {
        request.addItem(new Item(name, quantity, primaryKey, new Double(price), cat1, cat2, cat3));
        return this;
    }


    public BootpayBuilder addItem(String name, int quantity, String primaryKey, Double price) {
        request.addItem(new Item(name, quantity, primaryKey, price, "", "", ""));
        return this;
    }

    public BootpayBuilder addItem(String name, int quantity, String primaryKey, Double price, String cat1, String cat2, String cat3) {
        request.addItem(new Item(name, quantity, primaryKey, price, cat1, cat2, cat3));
        return this;
    }

    public BootpayBuilder addItem(Item item) {
        request.addItem(item);
        return this;
    }

    public BootpayBuilder addItems(List<Item> items) {
        request.addItems(items);
        return this;
    }

    public BootpayBuilder setItems(List<Item> items) {
        request.setItems(items);
        return this;
    }

    public BootpayBuilder setIsShowAgree(Boolean isShow) {
        request.setIsShowAgree(isShow);
        return this;
    }

    public BootpayBuilder setOrderId(String orderId) {
        request.setOrderId(orderId);
        return this;
    }

    public BootpayBuilder setUseOrderId(boolean use_order_id) {
        request.setUseOrderId(use_order_id);
        return this;
    }

    public BootpayBuilder setBootUser(BootUser bootUser) {
        request.setBootUser(bootUser);
        return this;
    }

    public BootpayBuilder setBootExtra(BootExtra bootExtra) {
        request.setBootExtra(bootExtra);
        return this;
    }

//    public BootpayBuilder setRemoteLink(RemoteLink remoteLink) {
//        request.setRemoteLink(remoteLink);
//        return this;
//    }

    public BootpayBuilder setRemoteOrderForm(RemoteOrderForm remoteForm) {
        request.setRemoteOrderForm(remoteForm);
        return this;
    }

    public BootpayBuilder setRemoteOrderPre(RemoteOrderPre remotePre) {
        request.setRemoteOrderPre(remotePre);
        return this;
    }

//    public BootpayBuilder setExpireMonth(int expireMonth) {
//        request.setExtra_expire_month(expireMonth);
//        return this;
//    }
//
//    public BootpayBuilder setVBankResult(int vbankResult) {
//        request.setExtra_vbank_result(vbankResult);
//        return this;
//    }
//
//    public BootpayBuilder setQuotas(int[] quotas) {
//        request.setExtra_quotas(quotas);
//        return this;
//    }

    public BootpayBuilder setSMSPayload(SMSPayload smsPayload) {
        request.setSmsPayload(smsPayload);
        return this;
    }

//    public BootpayBuild

    public BootpayBuilder setMethod(String method) {
        request.setMethod(method);
        return this;
    }

    public BootpayBuilder setMethods(List<String> methods) {
        request.setMethods(methods);
        return this;
    }

//    public BootpayBuilder setSmsUse(Boolean sms_use) {
//        request.setSmsUse(sms_use);
//        return this;
//    }



//    public BootpayBuilder setExtraStartAt(String startAt) {
//        if(!DateValid.isValidFormat("yyyy-MM-dd", startAt)) throw new IllegalStateException("Format Error! extra_start_at should be yyyy-MM-dd! The given value is " +  startAt);
//        request.setExtra_start_at(startAt);
//        return this;
//    }
//
//    public BootpayBuilder setExtraEndAt(String endAt) {
//        if(!DateValid.isValidFormat("yyyy-MM-dd", endAt)) throw new IllegalStateException("Format Error! extra_end_at should be yyyy-MM-dd! The given value is " +  endAt);
//        request.setExtra_end_at(endAt);
//        return this;
//    }

    public BootpayBuilder setMethod(Method method) {
        switch (method) {
            case CARD:
                request.setMethod("card");
                break;
            case CARD_SIMPLE:
                request.setMethod("card_simple");
                break;
            case BANK:
                request.setMethod("bank");
                break;
            case VBANK:
                request.setMethod("vbank");
                break;
            case PHONE:
                request.setMethod("phone");
                break;
            case SELECT:
                request.setMethod("");
                break;
            case SUBSCRIPT_CARD:
                request.setMethod("card_rebill");
                break;
            case SUBSCRIPT_PHONE:
                request.setMethod("phone_rebill");
                break;
            case AUTH:
                request.setMethod("auth");
                break;
        }
        return this;
    }

    public BootpayBuilder setAccountExpireAt(String account_expire_at) {
        request.setAccountExpireAt(account_expire_at);
        return this;
    }

    public BootpayBuilder setModel(Request request) {
        this.request = request;
        return this;
    }

    public BootpayBuilder setEventListener(EventListener eventListener) {
        listener = eventListener;
        return this;
    }

    public BootpayBuilder setParams(Object params) {

        request.setParams(new Gson().toJson(params));
        return this;
    }

    public BootpayBuilder setParams(String params) {
        request.setParams(params);
        return this;
    }

//    public BootpayBuilder setParams(JSONObject params) {
//        request.setParams(params);
//        return this;
//    }

    public BootpayBuilder onCancel(CancelListener listener) {
        cancel = listener;
        return this;
    }

    public BootpayBuilder onConfirm(ConfirmListener listener) {
        confirm = listener;
        return this;
    }

    public BootpayBuilder onReady(ReadyListener listener) {
        ready = listener;
        return this;
    }

    public BootpayBuilder onError(ErrorListener listener) {
        error = listener;
        return this;
    }

    public BootpayBuilder onDone(DoneListener listener) {
        done = listener;
        return this;
    }

    public BootpayBuilder onClose(CloseListener listener) {
        close = listener;
        return this;
    }

//    public BootpayBuilder setUserEmail(String email) {
//        request.setUser_email(email);
//        return this;
//    }

//    public BootpayBuilder setUserName(String name) {
//        request.setUser_name(name);
//        return this;
//    }

//    public BootpayBuilder setUserAddr(String addr) {
//        request.setUser_addr(addr);
//        return this;
//    }

//    public BootpayBuilder setUserPhone(String phone) {
//        request.setUser_phone(phone);
//        return this;
//    }

    public BootpayBuilder setUX(UX ux) {
        request.setUX(ux);
        return this;
    }

//    public BootpayBuilder setAppScheme(String appScheme) {
//        request.setExtra_app_scheme(appScheme);
//        return this;
//    }

//    public BootpayBuilder setAppSchemeHost(String appSchemeHost) {
//        request.setExtra_app_scheme_host(appSchemeHost);
//        return this;
//    }

    private void validCheck() {
        if (isEmpty(request.getApplicationId()))
            error("Application id is not configured.");

//        if (isEmpty(request.getPg()))
//            error("PG is not configured.");

        if (request.getPrice() < 0)
            error("Price is not configured.");

        if (isEmpty(request.getOrderId()))
            error("Order id is not configured.");

//        if (listener == null && (error == null || cancel == null || confirm == null || done == null))
//            error("Must to be required to handel events.");

        UX ux =request.getUX();
        if(ux == null || ux == UX.NONE) { request.setUX(UX.PG_DIALOG); }
        ux = request.getUX();
        if(ux == UX.PG_DIALOG) {
            if (fragmentManager == null && fragmentManager.isDestroyed()) { error("fragment 값은 null 이 될 수 없습니다."); }

        } else if(ux == UX.APP2APP_CARD_SIMPLE
                || ux == UX.APP2APP_NFC
                || ux == UX.APP2APP_SAMSUNGPAY
                || ux == UX.APP2APP_SUBSCRIPT
                || ux == UX.APP2APP_CASH_RECEIPT
                || ux == UX.APP2APP_OCR) {
            if (context == null) { error("Context 값은 null 이 될 수 없습니다."); }
        }
    }

    /**
     * @deprecated 더 정확한 표현을 위해 {@link #request()} 함수를 사용하기를 추천합니다.
     */
    @Deprecated
    public void show() {
        request();
    }


    public void request() {
        if(context == null) throw new IllegalStateException("context cannot be null from " + request.getUX().toString());

        validCheck();
        UserInfo.getInstance(context).update();

        Bootpay.builder.request = ValidRequest.validUXAvailablePG(Bootpay.builder.request);
        UX ux = request.getUX();
        if(PGAvailable.isUXPGDefault(ux)) requestDialog();
        else if(PGAvailable.isUXPGSubscript(ux)) {
            Bootpay.builder.request.setMethod("card_rebill");
            requestDialog();
        }
        else if(PGAvailable.isUXBootpayApi(ux)) requestApi();
        else if(PGAvailable.isUXApp2App(ux)) requestApp2App();
        else throw new IllegalStateException(ux.toString() + " is not supported!");
    }

//    public

    private void requestDialog() {
        dialog = new BootpayDialog().setRequest(request)
            .setOnResponseListener(listener != null ? listener : new EventListener() {
                @Override
                public void onClose(@org.jetbrains.annotations.Nullable String message) {
                    close.onClose(message);
                }

                @Override
                public void onReady(@org.jetbrains.annotations.Nullable String message) {
                    ready.onReady(message);
                }

                @Override
                public void onError(@org.jetbrains.annotations.Nullable String message) {
                    error.onError(message);
                }

                @Override
                public void onCancel(@org.jetbrains.annotations.Nullable String message) {
                    cancel.onCancel(message);
                }

                @Override
                public void onConfirm(@org.jetbrains.annotations.Nullable String message) {
                    confirm.onConfirm(message);
                }

                @Override
                public void onDone(@org.jetbrains.annotations.Nullable String message) {
                    done.onDone(message);
                }
        });

        dialog.onCancel(new DialogInterface() {
            @Override
            public void cancel() {
                if (dialog != null && dialog.bootpay != null)
                    dialog.bootpay.destroy();
                dialog = null;
                UserInfo.getInstance(context).finish();
                Bootpay.finish();
            }

            @Override
            public void dismiss() {
                if (dialog != null && dialog.bootpay != null)
                    dialog.bootpay.destroy();
                dialog = null;
                UserInfo.getInstance(context).finish();
                Bootpay.finish();
            }
        });

        if (dialog != null) dialog.show(fragmentManager, "bootpay");
//        System.currentTimeMillis()
    }

    private void requestApi() {
        if(context == null) throw new IllegalStateException("context cannot be null from " + request.getUX());
        if (presenter == null) presenter = new ApiPresenter(new ApiService(context));
        presenter.request(request);
    }

    private void requestApp2App() {
        Intent intent = new Intent(context, BootpayInnerActivity.class);
        context.startActivity(intent);
    }

    public void transactionConfirm(String data) {
        if (dialog != null)
            dialog.transactionConfirm(data);
    }

    public void removePaymentWindow() {
        if (dialog != null)
            dialog.removePaymentWindow();
    }

    private boolean isEmpty(String value) {
        return value == null || value.length() == 0;
    }

    private void error(String message) {
        throw new RuntimeException(message);
    }
}
