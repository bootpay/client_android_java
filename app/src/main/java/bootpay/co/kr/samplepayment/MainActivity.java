package bootpay.co.kr.samplepayment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

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
                                .setPG("inicis")
                                .setMethod("card")
                                .setName("맥북프로임다")
                                .setPrice(1000)
                                .onConfirm(s -> Log.d("confirm", s == null ? "null" : s))
                                .onDone(s -> Log.d("done", s == null ? "null" : s))
                                .onCancel(s -> Log.d("cancel", s == null ? "null" : s))
                                .onError(Throwable::printStackTrace)
                                .show()
        );
    }
}
