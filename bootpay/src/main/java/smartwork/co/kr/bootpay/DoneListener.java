package smartwork.co.kr.bootpay;

@FunctionalInterface
public interface DoneListener {
    void onDone(String message);
}
