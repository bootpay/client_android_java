package kr.co.bootpay.bio.memory;

import kr.co.bootpay.bio.activity.BootpayBioActivity;
import kr.co.bootpay.bio.listener.BioEventListener;
import kr.co.bootpay.listener.CancelListener;
import kr.co.bootpay.listener.CloseListener;
import kr.co.bootpay.listener.ConfirmListener;
import kr.co.bootpay.listener.DoneListener;
import kr.co.bootpay.listener.ErrorListener;
import kr.co.bootpay.listener.ReadyListener;
import kr.co.bootpay.model.Request;

public class CurrentBioRequest {
    private static CurrentBioRequest instance = null;
    public Request request;
    public BioEventListener listener;

    public ErrorListener error;
    public ReadyListener ready;
    public CloseListener close;
    public DoneListener done;
    public CancelListener cancel;
    public ConfirmListener confirm;

    public BootpayBioActivity bioActivity;

    public int type = -1;
    public String token;

    public long start_window_time = System.currentTimeMillis() - 3000;

    public static final int REQUEST_TYPE_NONE = -1;
    public static final int REQUEST_TYPE_VERIFY_PASSWORD = 1; // 생체인식 활성화용도
    public static final int REQUEST_TYPE_VERIFY_PASSWORD_FOR_PAY = 2; //비밀번호로 결제 용도
    public static final int REQUEST_TYPE_REGISTER_CARD = 3; //카드 생성
    public static final int REQUEST_TYPE_PASSWORD_CHANGE = 4; //카드 삭제
    public static final int REQUEST_TYPE_ENABLE_DEVICE = 5; //해당 기기 활성화
    public static final int REQUEST_TYPE_OTHER = 6; //다른 결제수단

    public static CurrentBioRequest getInstance() {
        if(instance == null) instance = new CurrentBioRequest();
        return instance;
    }
}

