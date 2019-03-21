package kr.co.bootpay;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;

public class Bootpay {
    private static Context context;
    protected  static BootpayBuilder builder;

    /******************
     * 부트페이 앱투앱 관련 로직
     ******************/
    public static BootpayBuilder init(Context context) {
        Bootpay.context = context;
        return builder = new BootpayBuilder(context);
    }

    /******************
     * 부트페이 일반결제 관련 로직
     ******************/
    public static BootpayBuilder init(Activity activity) {
        return init(activity.getFragmentManager());
    }

    public static BootpayBuilder init(Fragment fragment) {
        return init(fragment.getFragmentManager());
    }

    public static BootpayBuilder init(FragmentManager fragmentManager) {
        return builder = new BootpayBuilder(fragmentManager);
    }

    public static void finish() {
        builder = null;
    }

    public static void confirm(String data) {
        if (builder != null) builder.transactionConfirm(data);
    }

    public static void removePaymentWindow() {
        if (builder != null) builder.removePaymentWindow();
    }
}