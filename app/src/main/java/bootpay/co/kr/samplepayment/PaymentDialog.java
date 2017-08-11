package bootpay.co.kr.samplepayment;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.List;

import bootpay.co.kr.samplepayment.model.Item;
import bootpay.co.kr.samplepayment.model.Request;

public class PaymentDialog extends DialogFragment {

    BootpayWebView bootpay;
    EventListener listener;
    Request result;

    @Deprecated
    public PaymentDialog() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Holo_Light);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_payment, container);
        bootpay = view.findViewById(R.id.bootpay_web);
        afterViewInit();
        getDialog().setOnKeyListener((d, i, event) -> event.getAction() == KeyEvent.KEYCODE_BACK && bootpay != null && bootpay.back(getDialog()));
        return view;
    }

    PaymentDialog setOnResponseListener(EventListener listener) {
        this.listener = listener;
        return this;
    }

    void afterViewInit() {
        if (bootpay != null) {
            bootpay.setData(result);
            bootpay.setOnResponseListener(listener);
        }
    }

    PaymentDialog setData(Request request) {
        this.result = request;
        return this;
    }

    public static class Builder {
        private WeakReference<FragmentManager> fragmentManager;
        private Request result;
        private EventListener listener;
        private ErrorListener error;
        private DoneListener done;
        private CancelListener cancel;
        private ConfirmListener confirm;


        private Builder() {
            // not allow
        }

        public Builder(FragmentManager fm) {
            result = new Request();
            fragmentManager = new WeakReference<>(fm);
        }

        Builder setApplicationId(@NonNull String id) {
            result.setApplication_id(id);
            return this;
        }

        Builder setPrice(@IntRange(from = 0) int price) {
            result.setPrice(price);
            return this;
        }

        Builder setPG(@NonNull String pg) {
            result.setPg(pg);
            return this;
        }

        Builder setName(@NonNull String name) {
            result.setName(name);
            return this;
        }

        Builder addItem(@NonNull String name, @IntRange(from = 1) int quantity, String primaryKey, @FloatRange(from = 0.0) double price) {
            result.addItem(new Item(name, quantity, primaryKey, price));
            return this;
        }

        Builder addItem(Item item) {
            result.addItem(item);
            return this;
        }

        Builder addItems(Collection<Item> items) {
            if (items != null) items.forEach(this::addItem);
            return this;
        }

        Builder setItems(@NonNull List<Item> items) {
            result.setItems(items);
            return this;
        }

        Builder setOrderId(@NonNull String orderId) {
            result.setOrder_id(orderId);
            return this;
        }

        Builder setMethod(@NonNull String method) {
            result.setMethod(method);
            return this;
        }

        Builder setModel(Request request) {
            result = request;
            return this;
        }

        Builder setEventListener(EventListener listener) {
            this.listener = listener;
            return this;
        }

        Builder onCancel(CancelListener listener) {
            cancel = listener;
            return this;
        }

        Builder onConfirm(ConfirmListener listener) {
            confirm = listener;
            return this;
        }

        Builder onDone(DoneListener listener) {
            done = listener;
            return this;
        }

        Builder onError(ErrorListener listener) {
            error = listener;
            return this;
        }

        public void show() {

            if (isNullOrEmpty(result.getApplication_id()))
                error("Application id is not configured.");
            if (isNullOrEmpty(result.getPg())) error("PG is not configured.");
            if (isNullOrEmpty(result.getPrice())) error("Price is not configured.");
            if (isNullOrEmpty(result.getOrderId())) error("Order id is not configured.");
            if (listener == null && error == null || cancel == null || confirm == null || done == null)
                error("Must to be required to handle events.");

            new PaymentDialog()
                    .setData(result)
                    .setOnResponseListener(listener == null ? new EventListener() {
                        @Override
                        public void onError(String message) {
                            if (error != null) error.onError(message);
                        }

                        @Override
                        public void onCancel(String message) {
                            if (cancel != null) cancel.onCancel(message);
                        }

                        @Override
                        public void onConfirmed(String message) {
                            if (confirm != null) confirm.onConfirmed(message);
                        }

                        @Override
                        public void onDone(String message) {
                            if (done != null) done.onDone(message);
                        }
                    } : listener)
                    .show(fragmentManager.get(), "dialog");
        }

        private boolean error(String message) {
            throw new RuntimeException(message);
        }

        private boolean isNullOrEmpty(String value) {
            return (value == null) || (value.length() <= 0);
        }

        private boolean isNullOrEmpty(double value) {
            return value <= 0.0;
        }
    }

}
