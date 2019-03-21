package kr.co.bootpay;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kr.co.bootpay.listner.EventListener;
import kr.co.bootpay.model.Request;
import kr.co.bootpay.pref.UserInfo;

public class BootpayDialog extends DialogFragment {
    private Request result;
    protected BootpayWebView bootpay;
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

    protected void transactionConfirm(String data) {
        if (bootpay != null)
            bootpay.transactionConfirm(data);
    }

    protected void removePaymentWindow() {
        if (bootpay != null)
            bootpay.removePaymentWindow();
    }
}
