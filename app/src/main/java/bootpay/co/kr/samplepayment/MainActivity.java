package bootpay.co.kr.samplepayment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;

import bootpay.co.kr.samplepayment.model.Item;
import bootpay.co.kr.samplepayment.model.Request;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Request first = new Request();
//        first.setApplication_id("593f8febe13f332431a8ddae");
//        first.setPg("danal");
//        first.setPrice(2939);
//        first.setName("블링블리일");
//        first.setMethod("card");

        findViewById(R.id.main_button).setOnClickListener(view ->
                        new PaymentDialog.Builder(getFragmentManager())
//                                .setModel(first)
                                .setApplicationId("593f8febe13f332431a8ddae")
                                .setPG("danal")
                                .setMethod("card")
                                .setName("맥북프로임다")
                                .setOrderId(String.valueOf(System.currentTimeMillis()))
                                .setPrice(1000)
                                .addItem("마우스", 1, "123", 100)
                                .addItem("키보드", 1, "122", 200)
                                .onConfirm(s -> Log.d("confirm", s == null ? "null" : s))
                                .onDone(s -> Log.d("done", s == null ? "null" : s))
                                .onCancel(s -> Log.d("cancel", s == null ? "null" : s))
                                .onError(Throwable::printStackTrace)
                                .show()
        );
    }
}
