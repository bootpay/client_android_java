package bootpay.co.kr.samplepayment;

public interface EventListener {
    void onError(String message);

    void onCancel(String message);

    void onConfirmed(String message);

    void onDone(String message);
}
