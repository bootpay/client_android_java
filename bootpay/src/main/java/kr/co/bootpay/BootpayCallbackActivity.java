package kr.co.bootpay;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;


public class BootpayCallbackActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getIntent() != null){
            Uri uri = getIntent().getData();
            if(uri != null){
                if(Bootpay.builder.listener != null) {
                    Bootpay.builder.listener.onError(uri.toString());
                }
            }
        }
    }
}
