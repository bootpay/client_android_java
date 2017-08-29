package kr.co.bootpay;

import android.support.annotation.Nullable;

@FunctionalInterface
public interface CancelListener {
    void onCancel(@Nullable String message);
}
