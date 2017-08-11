package bootpay.co.kr.samplepayment;

@FunctionalInterface
public interface ErrorListener {
    void onError(Exception e);
}
