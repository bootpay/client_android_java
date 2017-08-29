package kr.co.bootpay;

import android.support.annotation.Nullable;

@FunctionalInterface
public interface DoneListener {
    void onDone(@Nullable String message);
}
