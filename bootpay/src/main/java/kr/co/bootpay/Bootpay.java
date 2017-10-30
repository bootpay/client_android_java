package kr.co.bootpay;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;

public class Bootpay {
    private static PaymentDialog.Builder builder;

    public static PaymentDialog.Builder init(Activity activity) {
        return init(activity.getFragmentManager());
    }

    public static PaymentDialog.Builder init(Fragment fragment) {
        return init(fragment.getFragmentManager());
    }

    public static PaymentDialog.Builder init(FragmentManager fragmentManager) {
        return builder = new PaymentDialog.Builder(fragmentManager);
    }

    public static void finish() {
        builder = null;
    }

    public static void confirm(String data) {
        if (builder != null) builder.transactionConfirm(data);
    }
}
