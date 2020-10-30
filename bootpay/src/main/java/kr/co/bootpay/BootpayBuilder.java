package kr.co.bootpay;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import kr.co.bootpay.api.ApiPresenter;
import kr.co.bootpay.api.ApiService;
import kr.co.bootpay.bio.BootpayBioDialog;
import kr.co.bootpay.bio.activity.BootpayBioActivity;
import kr.co.bootpay.bio.memory.CurrentBioRequest;
import kr.co.bootpay.enums.Method;
import kr.co.bootpay.enums.PG;
import kr.co.bootpay.enums.UX;
import kr.co.bootpay.listener.CancelListener;
import kr.co.bootpay.listener.CloseListener;
import kr.co.bootpay.listener.ConfirmListener;
import kr.co.bootpay.listener.DoneListener;
import kr.co.bootpay.listener.ErrorListener;
import kr.co.bootpay.listener.EventListener;
import kr.co.bootpay.listener.ReadyListener;
import kr.co.bootpay.model.BootExtra;
import kr.co.bootpay.model.Item;
import kr.co.bootpay.model.RemoteOrderForm;
import kr.co.bootpay.model.RemoteOrderPre;
import kr.co.bootpay.model.Request;
import kr.co.bootpay.model.BootUser;
import kr.co.bootpay.model.bio.BioPayload;
import kr.co.bootpay.pref.UserInfo;
import kr.co.bootpay.valid.PGAvailable;
import kr.co.bootpay.valid.ValidRequest;

public class BootpayBuilder {
    private Context context;

    private FragmentManager fm;
    private androidx.fragment.app.FragmentManager fmx;
    protected Request request = new Request();
    protected EventListener listener;
    private ErrorListener error;
    private ReadyListener ready;
    private CloseListener close;
    private DoneListener done;
    private CancelListener cancel;
    private ConfirmListener confirm;
    private BootpayDialog dialog;
//    private BootpayBioDialog bioDialog;
    private ApiPresenter presenter;
//    private String


    private BootpayBuilder() {}

    public BootpayBuilder(Context context) {
        this.context = context;
    }

    public BootpayBuilder(FragmentManager manager) {
        this.fm = manager;
    }
    public BootpayBuilder(androidx.fragment.app.FragmentManager fmx) {
        this.fmx = fmx;
    }

    public BootpayBuilder setContext(Context context) {
        Bootpay.setContext(context);
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
        request.setPG(Bootpay.getPG(pg));
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

    public BootpayBuilder setUseOrderId(int use_order_id) {
        request.setUseOrderId(use_order_id);
        return this;
    }

    public BootpayBuilder setBootUser(BootUser bootUser) {
        request.setBoot_user(bootUser);
        return this;
    }

    public BootpayBuilder setBootExtra(BootExtra bootExtra) {
        request.setBoot_extra(bootExtra);
        return this;
    }

    public BootpayBuilder setTaxFree(Double tax_free) {
        request.setTaxFree(tax_free);
        return this;
    }

//    public BootpayBuilder setRemoteLink(RemoteLink remoteLink) {
//        request.setRemoteLink(remoteLink);
//        return this;
//    }

    public BootpayBuilder setRemoteOrderForm(RemoteOrderForm remoteForm) {
        request.setRemote_order_form(remoteForm);
        return this;
    }

    public BootpayBuilder setRemoteOrderPre(RemoteOrderPre remotePre) {
        request.setRemote_order_pre(remotePre);
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

//    public BootpayBuilder setSMSPayload(SMSPayload smsPayload) {
//        request.setSms_payload(smsPayload);
//        return this;
//    }

//    public BootpayBuild

    public BootpayBuilder setMethod(String method) {
        request.setMethod(method);
        return this;
    }

    public BootpayBuilder setMethods(List<String> methods) {
        request.setMethods(methods);
        return this;
    }

//    public BootpayBuilder setSms_use(Boolean sms_use) {
//        request.setSms_use(sms_use);
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
        request.setMethod(getMethodString(method));
        return this;
    }

    public BootpayBuilder setMethodList(List<Method> methods) {
        List<String> methodList = new ArrayList<>();
        for(Method method : methods) {
            methodList.add(getMethodString(method));
        }

        request.setMethods(methodList);
        return this;
    }

    private String getMethodString(Method method) {

        switch (method) {
            case CARD:
                return "card";
            case CARD_SIMPLE:
                return "card_simple";
            case BANK:
                return "bank";
            case VBANK:
                return "vbank";
            case PHONE:
                return "phone";
            case SELECT:
                return "";
            case SUBSCRIPT_CARD:
                return "card_rebill";
            case CARD_REBILL:
                return "card_rebill";
            case SUBSCRIPT_PHONE:
                return "phone_rebill";
            case AUTH:
                return "auth";
            case EASY:
                return "easy";
            case PAYCO:
                return "payco";
            case KAKAO:
                return "kakao";
            case NPAY:
                return "npay";
            case EASY_CARD:
                return "easy_card";
            case EASY_BANK:
                return "easy_bank";

        }
        return "";
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

    public BootpayBuilder setBioPayload(BioPayload bioPayload) {
        request.setBioPayload(bioPayload);
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

    public BootpayBuilder setEasyPayUserToken(String userToken) {
        request.setEasyPayUserToken(userToken);
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
            if (fm == null || fm.isDestroyed()) { error("fragment 값은 null 이 될 수 없습니다."); }

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

    public void requestBio() {
        if(context == null) {
            throw new IllegalStateException("context cannot be null from " + request.getUX().toString());
        }
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                long current = System.currentTimeMillis();
                if(current - CurrentBioRequest.getInstance().start_window_time > 1000) {
                    CurrentBioRequest.getInstance().start_window_time = current;
                    requestBioDialog();
                }
            }
        });

    }


    public void request() {
        if(context == null) {
            throw new IllegalStateException("context cannot be null from " + request.getUX().toString());
        }

        validCheck();
        UserInfo.getInstance(context).update();

        Bootpay.builder.request = ValidRequest.validUXAvailablePG(context, Bootpay.builder.request);


        UX ux = request.getUX();
        if(PGAvailable.isUXPGDefault(ux)) requestDialog();
        else if(PGAvailable.isUXPGSubscript(ux)) {
            Bootpay.builder.request.setMethod("card_rebill");
            requestDialog();
        }
        else if(PGAvailable.isUXBootpayApi(ux)) requestApi();
        else if(PGAvailable.isUXApp2App(ux)) requestApp2App();
        else {
            final String msg = ux.toString() + " is not supported!";
            new AlertDialog.Builder(context)
                    .setTitle("Bootpay Android Dev Error")
                    .setMessage(msg)
                    .setCancelable(true)
                    .setPositiveButton("종료",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialog, int id) {
                                    // 프로그램을 종료한다
                                    throw new IllegalStateException(msg);
                                }
                            }).create().show();
        }


    }

    private void requestBioDialog() {
//        bioDialog =  BootpayBioDialog();
        if(close != null) CurrentBioRequest.getInstance().close = close;
        if(cancel != null) CurrentBioRequest.getInstance().cancel = cancel;
        if(ready != null) CurrentBioRequest.getInstance().ready = ready;
        if(confirm != null) CurrentBioRequest.getInstance().confirm = confirm;
        if(done != null) CurrentBioRequest.getInstance().done = done;

        CurrentBioRequest.getInstance().request = request;

        Intent intent = new Intent(context, BootpayBioActivity.class);
//        context.pen
        context.startActivity(intent);
//        context.ov
//                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

//        bioDialog = new BootpayBioDialog().setRequest(request)
//                .setOnResponseListener(listener != null ? listener : new EventListener() {
//            @Override
//            public void onClose(String data) {
//                if(close != null) close.onClose(data);
//            }
//
//            @Override
//            public void onReady(String data) {
//                if(ready != null ) ready.onReady(data);
//            }
//
//            @Override
//            public void onError(String data) {
//                if(error != null ) error.onError(data);
//            }
//
//            @Override
//            public void onCancel(String data) {
//                if(cancel != null ) cancel.onCancel(data);
//            }
//
//            @Override
//            public void onConfirm(String data) {
//                if(confirm != null ) confirm.onConfirm(data);
//            }
//
//            @Override
//            public void onDone(String data) {
//                if(done != null ) done.onDone(data);
//            }
//        });
//
//        bioDialog.onCancel(new DialogInterface() {
//            @Override
//            public void cancel() {
//                if (bioDialog != null)
//                    bioDialog.onDestroy();
//                bioDialog = null;
//                UserInfo.getInstance(context).finish();
//                Bootpay.finish();
//            }
//
//            @Override
//            public void dismiss() {
//                if (bioDialog != null)
//                    bioDialog.onDestroy();
//                bioDialog = null;
//                UserInfo.getInstance(context).finish();
//                Bootpay.finish();
//            }
//        });
////        bioDialog.getatt
//
//        if (bioDialog != null || fmx != null) {
//            try {
//                FragmentTransaction ft = fmx.beginTransaction();
//                ft.add(bioDialog, String.valueOf(System.currentTimeMillis()));
//                ft.commitAllowingStateLoss();
//
////                getFragmentManager().beginTransaction().add(mDialogFragment, "DialogFragment Tag").commitAllowingStateLoss();
//
//
//
////                bioDialog.show(fmx, "bootpay");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
    }



    private void requestDialog() {
        dialog = new BootpayDialog().setRequest(request)
            .setOnResponseListener(listener != null ? listener : new EventListener() {
                @Override
                public void onClose(String data) {
                    if(close != null ) close.onClose(data);
                }

                @Override
                public void onReady(String data) {
                    if(ready != null ) ready.onReady(data);
                }

                @Override
                public void onError(String data) {
                    if(error != null ) error.onError(data);
                }

                @Override
                public void onCancel(String data) {
                    if(cancel != null ) cancel.onCancel(data);
                }

                @Override
                public void onConfirm(String data) {
                    if(confirm != null ) confirm.onConfirm(data);
                }

                @Override
                public void onDone(String data) {
                    if(done != null ) done.onDone(data);
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

        if (dialog != null) dialog.show(fm, "bootpay");
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
//        if(bioDialog != null)
//            bioDialog.transactionConfirm(data);
    }

    public void removePaymentWindow() {
        if (dialog != null)
            dialog.removePaymentWindow();
    }

    public void dismiss() {
        if(dialog != null) dialog.dismiss();
//        if(bioDialog != null) {
//            if(CurrentBioRequest.getInstance().type != CurrentBioRequest.REQUEST_TYPE_OTHER)
//                bioDialog.dismiss();
//        }
    }

    private boolean isEmpty(String value) {
        return value == null || value.length() == 0;
    }

    private void error(String message) {
        throw new RuntimeException(message);
    }


}
