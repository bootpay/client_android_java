package smartwork.co.kr.bootpay;

@FunctionalInterface
public interface ConfirmListener {
    /**
     * 재고가 남아있는지 확인하는 과정입니다.
     * 사용자가 재고를 확인하고 실제 결제가 이뤄질 수 있을 경우에만 결제가 완료됩니다.
     * @return 재고가 남아있는지 확인. true: 결제 가능 / false: 결제 불가능
     */
    boolean onConfirmed(String message);
}
