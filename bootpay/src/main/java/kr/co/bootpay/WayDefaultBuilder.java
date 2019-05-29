package kr.co.bootpay;

import android.app.FragmentManager;
import android.content.DialogInterface;

import org.json.JSONObject;

import java.util.Collection;
import java.util.List;

import kr.co.bootpay.enums.Method;
import kr.co.bootpay.enums.PG;
import kr.co.bootpay.listner.CancelListener;
import kr.co.bootpay.listner.CloseListener;
import kr.co.bootpay.listner.ConfirmListener;
import kr.co.bootpay.listner.DoneListener;
import kr.co.bootpay.listner.ErrorListener;
import kr.co.bootpay.listner.EventListener;
import kr.co.bootpay.listner.ReadyListener;
import kr.co.bootpay.model.Item;
import kr.co.bootpay.model.Request;
import kr.co.bootpay.pref.UserInfo;

public class WayDefaultBuilder {
//    private FragmentManager fragmentManager;
//    private Request result = new Request();
//    private EventListener listener;
//    private ErrorListener error;
//    private ReadyListener ready;
//    private CloseListener close;
//    private DoneListener done;
//    private CancelListener cancel;
//    private ConfirmListener confirm;
//    private BootpayDialog dialog;
//
//    private WayDefaultBuilder() {
//        // do nothing
//    }
//
//    public WayDefaultBuilder(FragmentManager manager) {
//        fragmentManager = manager;
//    }
//
//    public WayDefaultBuilder setApplicationId(String id) {
//        result.setApplication_id(id);
//        return this;
//    }
//
//    public WayDefaultBuilder setPrice(@IntRange(from = 0) int price) {
//        result.setPrice(price);
//        return this;
//    }
//
//    public WayDefaultBuilder setPG(String pg) {
//        result.setPg(pg);
//        return this;
//    }
//
//    public WayDefaultBuilder setPG(PG pg) {
//        switch (pg) {
//            case BOOTPAY:
//                result.setPg("bootpay");
//                break;
//            case PAYAPP:
//                result.setPg("payapp");
//                break;
//            case DANAL:
//                result.setPg("danal");
//                break;
//            case KCP:
//                result.setPg("kcp");
//                break;
//            case INICIS:
//                result.setPg("inicis");
//                break;
//            case LGUP:
//                result.setPg("lgup");
//                break;
//            case KAKAO:
//                result.setPg("kakao");
//                break;
//            case JTNET:
//                result.setPg("jtnet");
//                break;
//            case NICEPAY:
//                result.setPg("nicepay");
//                break;
//            case PAYCO:
//                result.setPg("payco");
//                break;
//        }
//        return this;
//    }
//
//    public WayDefaultBuilder setName(String name) {
//        result.setName(name);
//        return this;
//    }
//
//    public WayDefaultBuilder addItem(String name, @IntRange(from = 1) int quantity, String primaryKey, int price) {
//        result.addItem(new Item(name, quantity, primaryKey, price, "", "", ""));
//        return this;
//    }
//
//    public WayDefaultBuilder addItem(String name, @IntRange(from = 1) int quantity, String primaryKey, int price, String cat1, String cat2, String cat3) {
//        result.addItem(new Item(name, quantity, primaryKey, price, cat1, cat2, cat3));
//        return this;
//    }
//
//    public WayDefaultBuilder addItem(Item item) {
//        result.addItem(item);
//        return this;
//    }
//
//    public WayDefaultBuilder addItems(Collection<Item> items) {
//        result.addItems(items);
//        return this;
//    }
//
//    public WayDefaultBuilder setItems(List<Item> items) {
//        result.setItems(items);
//        return this;
//    }
//
//    public WayDefaultBuilder is_show_agree(Boolean isShow) {
//        result.set_show_agree(isShow);
//        return this;
//    }
//
//    public WayDefaultBuilder setOrderId(String orderId) {
//        result.setOrder_id(orderId);
//        return this;
//    }
//
//    public WayDefaultBuilder setUseOrderId(Integer use_order_id) {
//        result.setUse_order_id(use_order_id);
//        return this;
//    }
//
//    public WayDefaultBuilder setExpireMonth(int expireMonth) {
//        result.setExtra_expire_month(expireMonth);
//        return this;
//    }
//
//    public WayDefaultBuilder setVBankResult(int vbankResult) {
//        result.setExtra_vbank_result(vbankResult);
//        return this;
//    }
//
//    public WayDefaultBuilder setQuotas(int[] quotas) {
//        result.setExtra_quotas(quotas);
//        return this;
//    }
//
//    public WayDefaultBuilder setMethod(String method) {
//        result.setMethod(method);
//        return this;
//    }
//
//    public WayDefaultBuilder setMethod(Method method) {
//        switch (method) {
//            case CARD:
//                result.setMethod("card");
//                break;
//            case CARD_SIMPLE:
//                result.setMethod("card_simple");
//                break;
//            case BANK:
//                result.setMethod("bank");
//                break;
//            case VBANK:
//                result.setMethod("vbank");
//                break;
//            case PHONE:
//                result.setMethod("phone");
//                break;
//            case SELECT:
//                result.setMethod("");
//                break;
//        }
//        return this;
//    }
//
//    public WayDefaultBuilder setAccountExpireAt(String account_expire_at) {
//        result.setAccountExpireAt(account_expire_at);
//        return this;
//    }
//
//    public WayDefaultBuilder setModel(Request request) {
//        result = request;
//        return this;
//    }
//
//    public WayDefaultBuilder setEventListener(EventListener eventListener) {
//        listener = eventListener;
//        return this;
//    }
//
//    public WayDefaultBuilder setParams(Object params) {
//        result.setParams(params);
//        return this;
//    }
//
//    public WayDefaultBuilder setParams(String params) {
//        result.setParams(params);
//        return this;
//    }
//
//    public WayDefaultBuilder setParams(JSONObject params) {
//        result.setParams(params);
//        return this;
//    }
//
//    public WayDefaultBuilder onCancel(CancelListener listener) {
//        cancel = listener;
//        return this;
//    }
//
//    public WayDefaultBuilder onConfirm(ConfirmListener listener) {
//        confirm = listener;
//        return this;
//    }
//
//    public WayDefaultBuilder onReady(ReadyListener listener) {
//        ready = listener;
//        return this;
//    }
//
//    public WayDefaultBuilder onError(ErrorListener listener) {
//        error = listener;
//        return this;
//    }
//
//    public WayDefaultBuilder onDone(DoneListener listener) {
//        done = listener;
//        return this;
//    }
//
//    public WayDefaultBuilder onClose(CloseListener listener) {
//        close = listener;
//        return this;
//    }
//
//    public WayDefaultBuilder setUser_email(String email) {
//        result.setUser_email(email);
//        return this;
//    }
//
//    public WayDefaultBuilder setUser_name(String name) {
//        result.setUser_name(name);
//        return this;
//    }
//
//    public WayDefaultBuilder setUser_addr(String addr) {
//        result.setUser_addr(addr);
//        return this;
//    }
//
//    public WayDefaultBuilder setUser_phone(String phone) {
//        result.setUser_phone(phone);
//        return this;
//    }
//
//    public void show() {
//        if (isEmpty(result.getApplication_id()))
//            error("Application id is not configured.");
//
//        if (isEmpty(result.getPg()))
//            error("PG is not configured.");
//
//        if (result.getPrice() < 0)
//            error("Price is not configured.");
//
//        if (isEmpty(result.getOrder_id()))
//            error("Order id is not configured.");
//
//        if (listener == null && (error == null || cancel == null || confirm == null || done == null))
//            error("Must to be required to handel events.");
//
//        dialog = new BootpayDialog()
//                .setData(result)
//                .setOnResponseListener(listener != null ? listener : new EventListener() {
//                    @Override
//                    public void onClose(@org.jetbrains.annotations.Nullable String message) {
//                        close.onClose(message);
//                    }
//
//                    @Override
//                    public void onReady(@org.jetbrains.annotations.Nullable String message) {
//                        ready.onReady(message);
//                    }
//
//                    @Override
//                    public void onError(@org.jetbrains.annotations.Nullable String message) {
//                        error.onError(message);
//                    }
//
//                    @Override
//                    public void onCancel(@org.jetbrains.annotations.Nullable String message) {
//                        cancel.onCancel(message);
//                    }
//
//                    @Override
//                    public void onConfirm(@org.jetbrains.annotations.Nullable String message) {
//                        confirm.onConfirm(message);
//                    }
//
//                    @Override
//                    public void onDone(@org.jetbrains.annotations.Nullable String message) {
//                        done.onDone(message);
//                    }
//                });
//        dialog.onCancel(new DialogInterface() {
//            @Override
//            public void cancel() {
//                if (dialog != null && dialog.bootpay != null)
//                    dialog.bootpay.destroy();
//                dialog = null;
//                UserInfo.finish();
//                Bootpay.finish();
//            }
//
//            @Override
//            public void dismiss() {
//                if (dialog != null && dialog.bootpay != null)
//                    dialog.bootpay.destroy();
//                dialog = null;
//                UserInfo.finish();
//                Bootpay.finish();
//            }
//        });
//
//        UserInfo.update();
//        if (fragmentManager != null && !fragmentManager.isDestroyed() && dialog != null)
//            dialog.show(fragmentManager, "bootpay");
//    }
//
//    public void transactionConfirm(String data) {
//        if (dialog != null)
//            dialog.transactionConfirm(data);
//    }
//
//    public void removePaymentWindow() {
//        if (dialog != null)
//            dialog.removePaymentWindow();
//    }
//
//    private boolean isEmpty(String value) {
//        return value == null || value.length() == 0;
//    }
//
//    private void error(String message) {
//        throw new RuntimeException(message);
//    }
}
