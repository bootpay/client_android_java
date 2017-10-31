package bootpay.co.kr.samplepayment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import kr.co.bootpay.Analytics;
import kr.co.bootpay.Bootpay;
import kr.co.bootpay.CancelListener;
import kr.co.bootpay.ConfirmListener;
import kr.co.bootpay.DoneListener;
import kr.co.bootpay.ErrorListener;
import kr.co.bootpay.enums.Method;
import kr.co.bootpay.enums.PG;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Analytics.init(this, "59a7e647396fa64fcad4a8c2");

        Analytics.login("legab12", "email", "username", "something", "", "", "");

        Analytics.start("테스트에요".toLowerCase());

        findViewById(R.id.main_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bootpay.init(getFragmentManager())
                        .setApplicationId("59a7e647396fa64fcad4a8c2")
                        .setPG(PG.KCP)
                        .setMethod(Method.CARD)
                        .setName("맥북프로임다")
                        .setOrderId(String.valueOf(System.currentTimeMillis()))
                        .setPrice(1000)
                        .addItem("마우스", 1, "123", 100)
                        .addItem("키보드", 1, "122", 200)
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

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        Analytics.destory();
//    }
}

