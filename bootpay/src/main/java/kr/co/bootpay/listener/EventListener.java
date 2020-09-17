package kr.co.bootpay.listener;


public interface EventListener extends CancelListener, ErrorListener, DoneListener, CloseListener,  ReadyListener, ConfirmListener {
    void onError(String data);

    void onCancel(String data);

    void onClose(String data);

    void onReady(String data);

    void onConfirm(String data);

    void onDone(String data);
}

