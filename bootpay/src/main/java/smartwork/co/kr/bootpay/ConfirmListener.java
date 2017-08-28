package smartwork.co.kr.bootpay;

@FunctionalInterface
public interface ConfirmListener {
    void onConfirmed(String message);
}
