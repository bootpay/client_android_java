package smartwork.co.kr.bootpay;

public interface EventListener extends CancelListener, ConfirmListener, ErrorListener, DoneListener {
    @Override
    void onError(String message);

    @Override
    void onCancel(String message);

    @Override
    boolean onConfirmed(String message);

    @Override
    void onDone(String message);
}
