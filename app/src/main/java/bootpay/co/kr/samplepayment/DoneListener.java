package bootpay.co.kr.samplepayment;

@FunctionalInterface
public interface DoneListener {
    void onDone(String message);
}
