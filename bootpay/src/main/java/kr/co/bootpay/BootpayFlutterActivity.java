package kr.co.bootpay;

import android.support.v7.app.AppCompatActivity;

import kr.co.bootpay.listner.EventListener;

public class BootpayFlutterActivity extends AppCompatActivity implements EventListener {
    @Override
    public void onError(String message) {}

    @Override
    public void onCancel(String message) {}

    @Override
    public void onClose(String message) {}

    @Override
    public void onReady(String message) {}

    @Override
    public void onConfirm(String message) {}

    @Override
    public void onDone(String message) {}
}
