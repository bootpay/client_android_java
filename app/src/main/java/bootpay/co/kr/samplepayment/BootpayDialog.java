package bootpay.co.kr.samplepayment;

import android.app.FragmentManager;

public class BootpayDialog {
    private static PaymentDialog.Builder builder;

    public static PaymentDialog.Builder init(FragmentManager fm) {
        if (builder == null)
            synchronized (BootpayDialog.class) {
                if (builder == null) builder = new PaymentDialog.Builder(fm);
            }
        return builder;
    }

    public static void confirm() {
        builder.transactionConfirm();
    }
}
