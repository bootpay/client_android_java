package kr.co.bootpay;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import kr.co.bootpay.listener.EventListener;
import kr.co.bootpay.model.Request;

public class BootpayDialog extends DialogFragment {

    private Request request;
    protected BootpayWebView bootpay;
    private EventListener listener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme_Holo_Light);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        bootpay = new BootpayWebView(inflater.getContext());
        afterViewInit();
        return bootpay;
    }

    @Override
    public void onDestroyView() {

//        UserInfo.getInstance(getContext()).update();
        super.onDestroyView();
    }

    private void afterViewInit() {
        if (bootpay != null)
            bootpay.setRequest(request)
                    .setDialog(getDialog())
                    .setOnResponseListener(listener);
    }

    public BootpayDialog setOnResponseListener(EventListener listener) {
        this.listener = listener;
        return this;
    }

    public BootpayDialog setRequest(Request request) {
        this.request = request;
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
