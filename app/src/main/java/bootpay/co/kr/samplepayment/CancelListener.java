package bootpay.co.kr.samplepayment;

@FunctionalInterface
public interface CancelListener {
    void onCancel(String message);
}
