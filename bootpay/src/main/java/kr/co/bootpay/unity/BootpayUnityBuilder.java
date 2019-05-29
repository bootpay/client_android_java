package kr.co.bootpay.unity;

import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.google.gson.Gson;

import java.util.List;

import kr.co.bootpay.Bootpay;
import kr.co.bootpay.BootpayDialog;
import kr.co.bootpay.BootpayInnerActivity;
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
import kr.co.bootpay.model.BootUser;
import kr.co.bootpay.model.Item;
import kr.co.bootpay.model.RemoteOrderForm;
import kr.co.bootpay.model.RemoteOrderPre;
import kr.co.bootpay.model.Request;
import kr.co.bootpay.model.SMSPayload;
import kr.co.bootpay.pref.UserInfo;
import kr.co.bootpay.valid.PGAvailable;
import kr.co.bootpay.valid.ValidRequest;

public class BootpayUnityBuilder {
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

    private UnityView unityView;
    private ApiPresenter presenter;


    private BootpayUnityBuilder() {}

    public BootpayUnityBuilder(Context context) {
        this.context = context;
    }

    public BootpayUnityBuilder(FragmentManager manager) {
        fragmentManager = manager;
    }

    public BootpayUnityBuilder setContext(Context context) {
        this.context = context;
        return this;
    }

    public BootpayUnityBuilder setApplicationId(String id) {
        request.setApplicationId(id);
        return this;
    }

    public BootpayUnityBuilder setPrice(int price) {
        request.setPrice(new Double(price));
        return this;
    }

    public BootpayUnityBuilder setPrice(Double price) {
        request.setPrice(price);
        return this;
    }

    public BootpayUnityBuilder setPG(String pg) {
        request.setPG(pg);
        return this;
    }

    public BootpayUnityBuilder setPG(PG pg) {
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

    public BootpayUnityBuilder setName(String name) {
        request.setName(name);
        return this;
    }

    public BootpayUnityBuilder addItem(String name, int quantity, String primaryKey, int price) {
        request.addItem(new Item(name, quantity, primaryKey, new Double(price), "", "", ""));
        return this;
    }

    public BootpayUnityBuilder addItem(String name, int quantity, String primaryKey, int price, String cat1, String cat2, String cat3) {
        request.addItem(new Item(name, quantity, primaryKey, new Double(price), cat1, cat2, cat3));
        return this;
    }


    public BootpayUnityBuilder addItem(String name, int quantity, String primaryKey, Double price) {
        request.addItem(new Item(name, quantity, primaryKey, price, "", "", ""));
        return this;
    }

    public BootpayUnityBuilder addItem(String name, int quantity, String primaryKey, Double price, String cat1, String cat2, String cat3) {
        request.addItem(new Item(name, quantity, primaryKey, price, cat1, cat2, cat3));
        return this;
    }

    public BootpayUnityBuilder addItem(Item item) {
        request.addItem(item);
        return this;
    }

    public BootpayUnityBuilder addItems(List<Item> items) {
        request.addItems(items);
        return this;
    }

    public BootpayUnityBuilder setItems(List<Item> items) {
        request.setItems(items);
        return this;
    }

    public BootpayUnityBuilder setIsShowAgree(Boolean isShow) {
        request.setIsShowAgree(isShow);
        return this;
    }

    public BootpayUnityBuilder setOrderId(String orderId) {
        request.setOrderId(orderId);
        return this;
    }

    public BootpayUnityBuilder setUseOrderId(boolean use_order_id) {
        request.setUseOrderId(use_order_id);
        return this;
    }

    public BootpayUnityBuilder setBootUser(BootUser bootUser) {
        request.setBootUser(bootUser);
        return this;
    }

    public BootpayUnityBuilder setBootExtra(BootExtra bootExtra) {
        request.setBootExtra(bootExtra);
        return this;
    }

    public BootpayUnityBuilder setRemoteOrderForm(RemoteOrderForm remoteForm) {
        request.setRemoteOrderForm(remoteForm);
        return this;
    }

    public BootpayUnityBuilder setRemoteOrderPre(RemoteOrderPre remotePre) {
        request.setRemoteOrderPre(remotePre);
        return this;
    }


    public BootpayUnityBuilder setSMSPayload(SMSPayload smsPayload) {
        request.setSmsPayload(smsPayload);
        return this;
    }

    public BootpayUnityBuilder setMethod(String method) {
        request.setMethod(method);
        return this;
    }

    public BootpayUnityBuilder setMethods(List<String> methods) {
        request.setMethods(methods);
        return this;
    }

    public BootpayUnityBuilder setMethod(Method method) {
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

    public BootpayUnityBuilder setAccountExpireAt(String account_expire_at) {
        request.setAccountExpireAt(account_expire_at);
        return this;
    }

    public BootpayUnityBuilder setModel(Request request) {
        this.request = request;
        return this;
    }

    public BootpayUnityBuilder setEventListener(EventListener eventListener) {
        listener = eventListener;
        return this;
    }

    public BootpayUnityBuilder setParams(Object params) {

        request.setParams(new Gson().toJson(params));
        return this;
    }

    public BootpayUnityBuilder setParams(String params) {
        request.setParams(params);
        return this;
    }

//    public BootpayBuilder setParams(JSONObject params) {
//        request.setParams(params);
//        return this;
//    }

    public BootpayUnityBuilder onCancel(CancelListener listener) {
        cancel = listener;
        return this;
    }

    public BootpayUnityBuilder onConfirm(ConfirmListener listener) {
        confirm = listener;
        return this;
    }

    public BootpayUnityBuilder onReady(ReadyListener listener) {
        ready = listener;
        return this;
    }

    public BootpayUnityBuilder onError(ErrorListener listener) {
        error = listener;
        return this;
    }

    public BootpayUnityBuilder onDone(DoneListener listener) {
        done = listener;
        return this;
    }

    public BootpayUnityBuilder onClose(CloseListener listener) {
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

    public BootpayUnityBuilder setUX(UX ux) {
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
//
//    public void Request(final String gameObject) {
//        if(context == null) throw new IllegalStateException("context cannot be null from " + request.getUX().toString());
//
//        validCheck();
//        UserInfo.getInstance(context).update();
//
//        BootpayUnity.builder.request = ValidRequest.validUXAvailablePG(BootpayUnity.builder.request);
//        UX ux = request.getUX();
//        if(PGAvailable.isUXPGDefault(ux)) requestDialog(gameObject);
//        else if(PGAvailable.isUXPGSubscript(ux)) {
//            BootpayUnity.builder.request.setMethod("card_rebill");
//            requestDialog(gameObject);
//        }
//        else if(PGAvailable.isUXBootpayApi(ux)) requestApi();
//        else if(PGAvailable.isUXApp2App(ux)) requestApp2App();
//        else throw new IllegalStateException(ux.toString() + " is not supported!");
//    }

//    public

//    private void requestDialog(String gameObject) {
//        EventListener listener = new EventListener() {
//            @Override
//            public void onError(String message) {
//                error.onError(message);
//            }
//
//            @Override
//            public void onCancel(String message) {
//                UserInfo.getInstance(context).finish();
//                BootpayUnity.finish();
//                cancel.onCancel(message);
//            }
//
//            @Override
//            public void onClose(String message) {
//                close.onClose(message);
//            }
//
//            @Override
//            public void onReady(String message) {
//                ready.onReady(message);
//            }
//
//            @Override
//            public void onConfirm(String message) {
//                confirm.onConfirm(message);
//            }
//
//            @Override
//            public void onDone(String message) {
//                done.onDone(message);
//            }
//        };
//        unityView = new UnityView();
//        unityView.init(gameObject, listener);
//        unityView.show();


//        dialog = new BootpayDialog().setRequest(request)
//            .setOnResponseListener(listener != null ? listener : new EventListener() {
//                @Override
//                public void onClose(@org.jetbrains.annotations.Nullable String message) {
//                    close.onClose(message);
//                }
//
//                @Override
//                public void onReady(@org.jetbrains.annotations.Nullable String message) {
//                    ready.onReady(message);
//                }
//
//                @Override
//                public void onError(@org.jetbrains.annotations.Nullable String message) {
//                    error.onError(message);
//                }
//
//                @Override
//                public void onCancel(@org.jetbrains.annotations.Nullable String message) {
//                    cancel.onCancel(message);
//                }
//
//                @Override
//                public void onConfirm(@org.jetbrains.annotations.Nullable String message) {
//                    confirm.onConfirm(message);
//                }
//
//                @Override
//                public void onDone(@org.jetbrains.annotations.Nullable String message) {
//                    done.onDone(message);
//                }
//        });
//
//        dialog.onCancel(new DialogInterface() {
//            @Override
//            public void cancel() {
//                if (dialog != null && dialog.bootpay != null)
//                    dialog.bootpay.destroy();
//                dialog = null;
//                UserInfo.getInstance(context).finish();
//                Bootpay.finish();
//            }
//
//            @Override
//            public void dismiss() {
//                if (dialog != null && dialog.bootpay != null)
//                    dialog.bootpay.destroy();
//                dialog = null;
//                UserInfo.getInstance(context).finish();
//                Bootpay.finish();
//            }
//        });
//
//        if (dialog != null) dialog.show(fragmentManager, "bootpay");
//    }

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
        if (unityView != null)
            unityView.transactionConfirm(data);
    }

    public void removePaymentWindow() {
        if (unityView != null)
            unityView.removePaymentWindow();
    }

    private boolean isEmpty(String value) {
        return value == null || value.length() == 0;
    }

    private void error(String message) {
        throw new RuntimeException(message);
    }
}
