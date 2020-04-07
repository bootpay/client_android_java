package kr.co.bootpay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import kr.co.bootpay.app2app.payapp.IntentPayload;
import kr.co.bootpay.model.Request;
import kr.co.bootpay.pref.UserInfo;

public class BootpayInnerActivity extends Activity {

    public static final int REQUEST_INTERNAL = 1000; // 원격결제
    public static final int REQUEST_NOTEPAYMENT = 1001; // 수기결제
    public static final int REQUEST_NFCPAYMENT = 1002; // NFC 결제
    public static final int REQUEST_SAMSUNGPAYMENT = 1003; // 삼성페이
    public static final int REQUEST_FIXEDPERIODPAYMENT = 1004; // 정기결제
    public static final int REQUEST_CASHRECEIPTPAYMENT = 1005; // 현금영수증 발행 요청
    public static final int REQUEST_OCRPAYMENT = 1006; // 카메라 결제

    Context context;
    
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        SecurePreference.context = this;
        this.context = this;
        UserInfo.getInstance(getApplicationContext()).update();
        startApp2App();



//        setContentView();
    }

    void startApp2App() {
        if ("payapp".equals(Bootpay.builder.request.getPG())) {
            startPayapp();
        }

    }

    void startPayapp() {
        try {
            IntentPayload intentPayload = new IntentPayload(this, Bootpay.builder.request);
            Intent intent = new Intent(Intent.ACTION_VIEW, intentPayload.toIntentUri());
            startActivityForResult(intent, 1);
        } catch (Exception e) {
            Log.d("intent", e.getMessage());
        }
    }


    private int getResultCode(Request request) {
        return 0;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        Bootpay.builder
        if(data != null) {
            Log.d("result", "onActivityResult : " + data.getData().toString());

            Toast.makeText(context, data.getData().toString(), Toast.LENGTH_SHORT).show();
        }
    }

}
