package kr.co.bootpay;

import android.support.annotation.Nullable;

@FunctionalInterface
public interface ErrorListener {
    void onError(@Nullable String message);
}
