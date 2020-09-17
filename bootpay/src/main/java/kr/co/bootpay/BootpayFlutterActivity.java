package kr.co.bootpay;

import android.support.v7.app.AppCompatActivity;

import kr.co.bootpay.listener.EventListener;

public class BootpayFlutterActivity extends AppCompatActivity implements EventListener {
    @Override
    public void onError(String data) {}

    @Override
    public void onCancel(String data) {}

    @Override
    public void onClose(String data) {}

    @Override
    public void onReady(String data) {}

    @Override
    public void onConfirm(String data) {}

    @Override
    public void onDone(String data) {}
}

