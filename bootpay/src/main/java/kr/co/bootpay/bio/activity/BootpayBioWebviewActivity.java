package kr.co.bootpay.bio.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import kr.co.bootpay.R;
import kr.co.bootpay.api.ApiService;
import kr.co.bootpay.bio.IBioPayFunction;
import kr.co.bootpay.bio.api.BioApiPresenter;
import kr.co.bootpay.bio.listener.BioEventListener;
import kr.co.bootpay.bio.memory.CurrentBioRequest;
import kr.co.bootpay.model.Request;
import kr.co.bootpay.model.res.EventSuccessDevice;
import kr.co.bootpay.model.res.ResReceiptID;
import kr.co.bootpay.pref.UserInfo;

public class BootpayBioWebviewActivity extends Activity implements BioEventListener, IBioPayFunction {

//    Context context;
//    BioApiPresenter presenter;
//    Request request;
    BootpayBioWebView webView;


    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "결제를 종료하시려면 '뒤로' 버튼을 한번 더 눌러주세요.", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CurrentBioRequest.getInstance().listener = this;
        CurrentBioRequest.getInstance().activity = this;

        setContentView(R.layout.layout_bio_activity);
        webView = findViewById(R.id.webview);
    }

    @Override
    public void transactionConfirm(String data) {
        webView.transactionConfirm(data);
    }

    @Override
    public void activityFinish() {

    }


    @Override
    public void onEasyCancel(String data) {
        Log.d("bootpay", data);
        setResult(-1);
        finish();
    }

    @Override
    public void onEasyError(String data) {
        Log.d("bootpay", data);
        setResult(-2);
        finish();
    }

    @Override
    public void onEasySuccess(String data) {
        Log.d("bootpay", data);
        setResult(1);
        if(CurrentBioRequest.getInstance().type == CurrentBioRequest.REQUEST_TYPE_ENABLE_DEVICE || CurrentBioRequest.getInstance().type == CurrentBioRequest.REQUEST_TYPE_VERIFY_PASSWORD) {
            EventSuccessDevice res = new Gson().fromJson(data, EventSuccessDevice.class);
            CurrentBioRequest.getInstance().token = res.data.token;
            finish();
        } else if (CurrentBioRequest.getInstance().type == CurrentBioRequest.REQUEST_TYPE_REGISTER_CARD) {
            finish();
        } else if(CurrentBioRequest.getInstance().type == CurrentBioRequest.REQUEST_TYPE_VERIFY_PASSWORD_FOR_PAY) {
            EventSuccessDevice res = new Gson().fromJson(data, EventSuccessDevice.class);
            CurrentBioRequest.getInstance().token = res.data.token;
//            finish();
        } else if(CurrentBioRequest.getInstance().type == CurrentBioRequest.REQUEST_TYPE_OTHER) {
            finish();
        }
    }


    @Override
    public void onError(String data) {
//        Log.d("bootpay", data);
        if(CurrentBioRequest.getInstance().error != null) CurrentBioRequest.getInstance().error.onError(data);
        setResult(-2);
        finish();
    }

    @Override
    public void onCancel(String data) {
        if(CurrentBioRequest.getInstance().cancel != null) CurrentBioRequest.getInstance().cancel.onCancel(data);
//        Log.d("bootpay", data);
        setResult(-1);
        finish();
    }

    @Override
    public void onClose(String data) {
        if(CurrentBioRequest.getInstance().close != null) CurrentBioRequest.getInstance().close.onClose(data);
//        Log.d("bootpay", data);
    }

    @Override
    public void onReady(String data) {
        if(CurrentBioRequest.getInstance().ready != null) CurrentBioRequest.getInstance().ready.onReady(data);
//        Log.d("bootpay", data);
        setResult(1);
        finish();
    }

    @Override
    public void onConfirm(String data) {
        if(CurrentBioRequest.getInstance().confirm != null) CurrentBioRequest.getInstance().confirm.onConfirm(data);

//        Log.d("bootpay", data);
    }

    @Override
    public void onDone(String data) {
        if(CurrentBioRequest.getInstance().done != null) CurrentBioRequest.getInstance().done.onDone(data);
//        Log.d("bootpay", data);
        setResult(1);
        finish();
    }
}
