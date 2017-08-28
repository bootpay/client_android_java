package smartwork.co.kr.bootpay;

@FunctionalInterface
public interface CancelListener {
    void onCancel(String message);
}
