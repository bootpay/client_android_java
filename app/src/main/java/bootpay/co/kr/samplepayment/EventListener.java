package bootpay.co.kr.samplepayment;

public interface EventListener extends CancelListener, ConfirmListener, ErrorListener, DoneListener {
    @Override
    void onError(Exception e);

    @Override
    void onCancel(String message);

    @Override
    void onConfirmed(String message);

    @Override
    void onDone(String message);
}
