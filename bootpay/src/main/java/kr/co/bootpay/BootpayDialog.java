package kr.co.bootpay;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONObject;

import java.util.Collection;
import java.util.List;

import kr.co.bootpay.enums.Method;
import kr.co.bootpay.enums.PG;
import kr.co.bootpay.model.Item;
import kr.co.bootpay.model.Request;
import kr.co.bootpay.pref.UserInfo;

public class BootpayDialog extends DialogFragment {
    private Request result;
    private BootpayWebView bootpay;
    private EventListener listener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme_Holo_Light);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        bootpay = new BootpayWebView(inflater.getContext());
        afterViewInit();
        return bootpay;
    }

    @Override
    public void onDestroyView() {
        UserInfo.update();
        super.onDestroyView();
    }

    private void afterViewInit() {
        if (bootpay != null)
            bootpay.setData(result)
                    .setDialog(getDialog())
                    .setOnResponseListener(listener);
    }

    public BootpayDialog setOnResponseListener(EventListener listener) {
        this.listener = listener;
        return this;
    }

    public BootpayDialog setData(Request request) {
        result = request;
        return this;
    }

    private void transactionConfirm(String data) {
        if (bootpay != null)
            bootpay.transactionConfirm(data);
    }

    public static class Builder {
        private FragmentManager fragmentManager;
        private Request result = new Request();
        private EventListener listener;
        private ErrorListener error;
        private DoneListener done;
        private CancelListener cancel;
        private ConfirmListener confirm;
        private BootpayDialog dialog;

        private Builder() {
            // do nothing
        }

        public Builder(FragmentManager manager) {
            fragmentManager = manager;
        }

        public Builder setApplicationId(String id) {
            result.setApplication_id(id);
            return this;
        }

        public Builder setPrice(@IntRange(from = 0) int price) {
            result.setPrice(price);
            return this;
        }

        public Builder setPG(String pg) {
            result.setPg(pg);
            return this;
        }

        public Builder setPG(PG pg) {
            switch (pg) {
                case BOOTPAY:
                    result.setPg("bootpay");
                    break;
                case PAYAPP:
                    result.setPg("payapp");
                    break;
                case DANAL:
                    result.setPg("danal");
                    break;
                case KCP:
                    result.setPg("kcp");
                    break;
                case INICIS:
                    result.setPg("inicis");
                    break;
                case LGUP:
                    result.setPg("lgup");
                    break;
                case KAKAO:
                    result.setPg("kakao");
                    break;
                case JTNET:
                    result.setPg("jtnet");
                    break;
                case NICEPAY:
                    result.setPg("nicepay");
                    break;
                case PAYCO:
                    result.setPg("payco");
                    break;
            }
            return this;
        }

        public Builder setName(String name) {
            result.setName(name);
            return this;
        }

        public Builder addItem(String name, @IntRange(from = 1) int quantity, String primaryKey, int price) {
            result.addItem(new Item(name, quantity, primaryKey, price, "", "", ""));
            return this;
        }

        public Builder addItem(String name, @IntRange(from = 1) int quantity, String primaryKey, int price, String cat1, String cat2, String cat3) {
            result.addItem(new Item(name, quantity, primaryKey, price, cat1, cat2, cat3));
            return this;
        }

        public Builder addItem(Item item) {
            result.addItem(item);
            return this;
        }

        public Builder addItems(Collection<Item> items) {
            result.addItems(items);
            return this;
        }

        public Builder setItems(List<Item> items) {
            result.setItems(items);
            return this;
        }

        public Builder isShowAgree(Boolean isShow) {
            result.setShowAgree(isShow);
            return this;
        }

        public Builder setOrderId(String orderId) {
            result.setOrder_id(orderId);
            return this;
        }

        public Builder setMethod(String method) {
            result.setMethod(method);
            return this;
        }

        public Builder setMethod(Method method) {
            switch (method) {
                case CARD:
                    result.setMethod("card");
                    break;
                case CARD_SIMPLE:
                    result.setMethod("card_simple");
                    break;
                case BANK:
                    result.setMethod("bank");
                    break;
                case VBANK:
                    result.setMethod("vbank");
                    break;
                case PHONE:
                    result.setMethod("phone");
                    break;
                case SELECT:
                    result.setMethod("");
                    break;
            }
            return this;
        }

        public Builder setModel(Request request) {
            result = request;
            return this;
        }

        public Builder setEventListener(EventListener eventListener) {
            listener = eventListener;
            return this;
        }

        public Builder setParams(Object params) {
            result.setParams(params);
            return this;
        }

        public Builder setParams(String params) {
            result.setParams(params);
            return this;
        }

        public Builder setParams(JSONObject params) {
            result.setParams(params);
            return this;
        }

        public Builder onCancel(CancelListener listener) {
            cancel = listener;
            return this;
        }

        public Builder onConfirm(ConfirmListener listener) {
            confirm = listener;
            return this;
        }

        public Builder onError(ErrorListener listener) {
            error = listener;
            return this;
        }

        public Builder onDone(DoneListener listener) {
            done = listener;
            return this;
        }

        public Builder setUserEmail(String email) {
            result.setUserEmail(email);
            return this;
        }

        public Builder setUserName(String name) {
            result.setUserName(name);
            return this;
        }

        public Builder setUserAddr(String addr) {
            result.setUserAddr(addr);
            return this;
        }

        public Builder setUserPhone(String phone) {
            result.setUserPhone(phone);
            return this;
        }

        public void show() {
            if (isEmpty(result.getApplication_id()))
                error("Application id is not configured.");

            if (isEmpty(result.getPg()))
                error("PG is not configured.");

            if (result.getPrice() < 0)
                error("Price is not configured.");

            if (isEmpty(result.getOrder_id()))
                error("Order id is not configured.");

            if (listener == null && (error == null || cancel == null || confirm == null || done == null))
                error("Must to be required to handel events.");

            dialog = new BootpayDialog()
                    .setData(result)
                    .setOnResponseListener(listener != null ? listener : new EventListener() {
                        @Override
                        public void onError(@org.jetbrains.annotations.Nullable String message) {
                            error.onError(message);
                        }

                        @Override
                        public void onCancel(@org.jetbrains.annotations.Nullable String message) {
                            cancel.onCancel(message);
                        }

                        @Override
                        public void onConfirmed(@org.jetbrains.annotations.Nullable String message) {
                            confirm.onConfirmed(message);
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
                    UserInfo.finish();
                    Bootpay.finish();
                }

                @Override
                public void dismiss() {
                    if (dialog != null && dialog.bootpay != null)
                        dialog.bootpay.destroy();
                    dialog = null;
                    UserInfo.finish();
                    Bootpay.finish();
                }
            });

            UserInfo.update();
            if (fragmentManager != null && !fragmentManager.isDestroyed() && dialog != null)
                dialog.show(fragmentManager, "bootpay");
        }

        public void transactionConfirm(String data) {
            if (dialog != null)
                dialog.transactionConfirm(data);
        }

        private boolean isEmpty(String value) {
            return value == null || value.length() == 0;
        }

        private void error(String message) {
            throw new RuntimeException(message);
        }

    }
}
