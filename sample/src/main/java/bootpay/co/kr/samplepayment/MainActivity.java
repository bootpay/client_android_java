package bootpay.co.kr.samplepayment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import kr.co.bootpay.BootpayDialog;
import kr.co.bootpay.CancelListener;
import kr.co.bootpay.ConfirmListener;
import kr.co.bootpay.DoneListener;
import kr.co.bootpay.ErrorListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.main_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BootpayDialog.init(getFragmentManager())
                        .setApplicationId("593f8febe13f332431a8ddae")
                        .setPG("danal")
                        .setMethod("card")
                        .setName("맥북프로임다")
                        .setOrderId(String.valueOf(System.currentTimeMillis()))
                        .setPrice(1000)
                        .addItem("마우스", 1, "123", 100)
                        .addItem("키보드", 1, "122", 200)
                        .setParams(new Test("test", 100, 10000))
                        .onConfirm(new ConfirmListener() {
                            @Override
                            public void onConfirmed(@Nullable String message) {
                                Log.d("confirm", message);
                            }
                        })
                        .onDone(new DoneListener() {
                            @Override
                            public void onDone(@Nullable String message) {
                                Log.d("done", message);
                            }
                        })
                        .onCancel(new CancelListener() {
                            @Override
                            public void onCancel(@Nullable String message) {
                                Log.d("cancel", message);
                            }
                        })
                        .onError(new ErrorListener() {
                            @Override
                            public void onError(@Nullable String message) {
                                Log.d("error", message);
                            }
                        })
                        .show();
            }
        });
    }
}

