package kr.co.bootpay;

import android.support.annotation.Nullable;

public interface EventListener extends CancelListener, ConfirmListener, ErrorListener, DoneListener {
    @Override
    void onError(@Nullable String message);

    @Override
    void onCancel(@Nullable String message);

    @Override
    void onConfirmed(@Nullable String message);

    @Override
    void onDone(@Nullable String message);
}
