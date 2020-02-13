package bootpay.co.kr.samplepayment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import kr.co.bootpay.Bootpay;
import kr.co.bootpay.BootpayAnalytics;
import kr.co.bootpay.enums.UX;
import kr.co.bootpay.listener.CancelListener;
import kr.co.bootpay.listener.CloseListener;
import kr.co.bootpay.listener.ConfirmListener;
import kr.co.bootpay.listener.DoneListener;
import kr.co.bootpay.listener.ErrorListener;
import kr.co.bootpay.listener.ReadyListener;
import kr.co.bootpay.enums.Method;
import kr.co.bootpay.enums.PG;
import kr.co.bootpay.model.BootExtra;
import kr.co.bootpay.model.StatItem;
import kr.co.bootpay.model.BootUser;

public class NativeActivity extends Activity {
    private int stuck = 1;
//5b9f51264457636ab9a07cdc
    private String application_id = "5b8f6a4d396fa665fdc2b5e8";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native);

        // 초기설정 - 해당 프로젝트(안드로이드)의 application id 값을 설정합니다. 결제와 통계를 위해 꼭 필요합니다.
        BootpayAnalytics.init(this, application_id);
//        BootpayAnalytics.init(this, "5b14c0ffb6d49c40cda92c4e");

        Bootpay.useOnestoreApi(this);

//        BootpayAnalytics.init(this, "59a7e647396fa64fcad4a8c2");

        // 통계 - 유저 로그인 시점에 호출
        BootpayAnalytics.login(
                "testUser", // bootUser 고유 id 혹은 로그인 아이디
                "testUser@gmail.com", // bootUser email
                "username", // bootUser 이름
                0, //1: 남자, 0: 여자
                "861014", // bootUser 생년월일 앞자리
                "01012345678", // bootUser 휴대폰 번호
                "충청"); //  서울|인천|대구|대전|광주|부산|울산|경기|강원|충청북도|충북|충청남도|충남|전라북도|전북|전라남도|전남|경상북도|경북|경상남도|경남|제주|세종 중 택 1

        startTrace();
    }

    public void startTrace() {
//        통계 - 페이지 추적
        List<StatItem> items = new ArrayList<>();
        items.add(new StatItem("마우스", "https://image.mouse.com/1234", "ITEM_CODE_MOUSE", "", "", ""));
        items.add(new StatItem("키보드", "https://image.keyboard.com/12345", "ITEM_CODE_KEYBOARD", "패션", "여성상의", "블라우스"));
        BootpayAnalytics.start("ItemListActivity", "item_list", items);
    }

    public void onClick_request(View v) {
        BootUser bootUser = new BootUser().setPhone("010-1234-5678");
        BootExtra bootExtra = new BootExtra().setQuotas(new int[] {0,2,3});

//        bootExtra.setEndAt()
//        bootExtra.setStartAt()

        Bootpay.init(getFragmentManager())
                .setContext(this)
                .setApplicationId(application_id) // 해당 프로젝트(안드로이드)의 application id 값
                .setPG(PG.PAYAPP) // 결제할 PG 사
                .setMethod(Method.KAKAO)
                .setBootExtra(bootExtra)
                .setBootUser(bootUser)
//                .setUserPhone("010-1234-5678") // 구매자 전화번호
                .setUX(UX.PG_DIALOG)
//                .setMethod(Method.CARD) // 결제수단
                //.isShowAgree(true)
                .setName("bootpay kb card test") // 결제할 상품명
                .setOrderId("1234") // 결제 고유번호
                .setPrice(1000) // 결제할 금액
                .setAccountExpireAt("2019-07-16")
                .addItem("마우스", 1, "ITEM_CODE_MOUSE", 500) // 주문정보에 담길 상품정보, 통계를 위해 사용
                .addItem("키보드", 1, "ITEM_CODE_KEYBOARD", 500, "패션", "여성상의", "블라우스") // 주문정보에 담길 상품정보, 통계를 위해 사용
                .onConfirm(new ConfirmListener() { // 결제가 진행되기 바로 직전 호출되는 함수로, 주로 재고처리 등의 로직이 수행
                    @Override
                    public void onConfirm(@Nullable String message) {
                        if (0 < stuck) Bootpay.confirm(message); // 재고가 있을 경우.
                        else Bootpay.removePaymentWindow(); // 재고가 없어 중간에 결제창을 닫고 싶을 경우
                        Log.d("confirm", message);
                    }
                })
                .onDone(new DoneListener() { // 결제완료시 호출, 아이템 지급 등 데이터 동기화 로직을 수행합니다
                    @Override
                    public void onDone(@Nullable String message) {
                        Log.d("done", message);
                    }
                })
                .onReady(new ReadyListener() { // 가상계좌 입금 계좌번호가 발급되면 호출되는 함수입니다.
                    @Override
                    public void onReady(@Nullable String message) {
                        Log.d("ready", message);
                    }
                })
                .onCancel(new CancelListener() { // 결제 취소시 호출
                    @Override
                    public void onCancel(@Nullable String message) {

                        Log.d("cancel", message);
                    }
                })
                .onError(new ErrorListener() { // 에러가 났을때 호출되는 부분
                    @Override
                    public void onError(@Nullable String message) {
                        Log.d("error", message);
                    }
                })
                .onClose(
                        new CloseListener() { //결제창이 닫힐때 실행되는 부분
                    @Override
                    public void onClose(String message) {
                        Log.d("close", "close");
                    }
                })
                .request();
    }
}
