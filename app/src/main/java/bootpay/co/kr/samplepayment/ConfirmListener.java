package bootpay.co.kr.samplepayment;

@FunctionalInterface
public interface ConfirmListener {
    void onConfirmed(String message);
}
