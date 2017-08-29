package kr.co.bootpay;

import android.app.FragmentManager;

public class BootpayDialog {
    public static PaymentDialog.Builder builder;

    public static PaymentDialog.Builder init(FragmentManager fm) {
        return builder = new PaymentDialog.Builder(fm);
    }

    public static void finish() {
        builder = null;
    }

    public static void confirm(String data) {
        if (builder != null) builder.transactionConfirm(data);
    }
}
