package kr.co.bootpay;

import android.support.annotation.Nullable;

@FunctionalInterface
public interface ConfirmListener {
    void onConfirmed(@Nullable String message);
}
