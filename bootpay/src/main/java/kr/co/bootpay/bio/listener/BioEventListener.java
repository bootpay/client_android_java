package kr.co.bootpay.bio.listener;


import kr.co.bootpay.listener.CancelListener;
import kr.co.bootpay.listener.CloseListener;
import kr.co.bootpay.listener.ConfirmListener;
import kr.co.bootpay.listener.DoneListener;
import kr.co.bootpay.listener.ErrorListener;
import kr.co.bootpay.listener.ReadyListener;

public interface BioEventListener extends EasyCancelListener, EasyErrorListener, EasySuccessListener, CancelListener, ErrorListener, DoneListener, CloseListener,  ReadyListener, ConfirmListener {
    void onEasyCancel(String data);

    void onEasyError(String data);

    void onEasySuccess(String data);

    void onError(String data);

    void onCancel(String data);

    void onClose(String data);

    void onReady(String data);

    void onConfirm(String data);

    void onDone(String data);
}

