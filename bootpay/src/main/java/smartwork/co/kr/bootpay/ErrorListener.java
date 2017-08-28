package smartwork.co.kr.bootpay;

@FunctionalInterface
public interface ErrorListener {
    void onError(String message);
}
