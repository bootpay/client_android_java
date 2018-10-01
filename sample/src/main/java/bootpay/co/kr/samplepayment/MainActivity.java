package bootpay.co.kr.samplepayment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goNativeActivity(View v) {
        Intent intent = new Intent(this, NativeActivity.class);
        startActivity(intent);
    }

    public void goWebAppActivity(View v) {
        Intent intent = new Intent(this, WebAppActivity.class);
        startActivity(intent);
    }
}

