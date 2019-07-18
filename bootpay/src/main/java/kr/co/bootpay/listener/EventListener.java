package kr.co.bootpay.listener;


public interface EventListener extends CancelListener, ErrorListener, DoneListener, CloseListener,  ReadyListener, ConfirmListener {
    void onError(String var1);

    void onCancel(String var1);

    void onClose(String var1);

    void onReady(String var1);

    void onConfirm(String var1);

    void onDone(String var1);
}

