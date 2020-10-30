package bootpay.co.kr.samplepayment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import kr.co.bootpay.Bootpay;
import kr.co.bootpay.BootpayAnalytics;
import kr.co.bootpay.BootpayFlutterActivity;
import kr.co.bootpay.enums.Method;
import kr.co.bootpay.enums.PG;
import kr.co.bootpay.model.BootExtra;
import kr.co.bootpay.model.BootUser;
import kr.co.bootpay.model.StatItem;

public class FlutterActivity extends BootpayFlutterActivity {
    private int stuck = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flutter);

        // 초기설정 - 해당 프로젝트(안드로이드)의 application id 값을 설정합니다. 결제와 통계를 위해 꼭 필요합니다.
        BootpayAnalytics.init(this, "5b14c0ffb6d49c40cda92c4e");
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

//        결제호출
        Bootpay.init(getFragmentManager())
                .setApplicationId("5b14c0ffb6d49c40cda92c4e") // 해당 프로젝트(안드로이드)의 application id 값
                .setPG(PG.INICIS) // 결제할 PG 사
                .setContext(this)
                .setBootUser(bootUser)
                .setBootExtra(bootExtra)
//                .setUserPhone("010-1234-5678") // 구매자 전화번호
                .setMethod(Method.BANK) // 결제수단
                .setName("맥\"북프로's 임다") // 결제할 상품명
                .setOrderId("1234") // 결제 고유번호expire_month
//                .setAccountExpireAt("2018-09-22") // 가상계좌 입금기간 제한 ( yyyy-mm-dd 포멧으로 입력해주세요. 가상계좌만 적용됩니다. 오늘 날짜보다 더 뒤(미래)여야 합니다 )
//                .setQuotas(new int[] {0,2,3}) // 일시불, 2개월, 3개월 할부 허용, 할부는 최대 12개월까지 사용됨 (5만원 이상 구매시 할부허용 범위)
                .setPrice(10000d) // 결제할 금액
                .addItem("마우's 스", 1, "ITEM_CODE_MOUSE", 100d) // 주문정보에 담길 상품정보, 통계를 위해 사용
                .addItem("키보드", 1, "ITEM_CODE_KEYBOARD", 200d, "패션", "여성상의", "블라우스") // 주문정보에 담길 상품정보, 통계를 위해 사용
                .onConfirm(this)
                .onDone(this)
                .onReady(this)
                .onCancel(this)
                .onError(this)
                .onClose(this)
                .request();
    }


    @Override
    public void onError(String data) {
        Log.d("bootpay  error", data);
    }

    @Override
    public void onCancel(String data) {
        Log.d("bootpay  cancel", data);
    }

    @Override
    public void onClose(String data) {
        Log.d("bootpay  close", "close");
    }

    @Override
    public void onReady(String data) {
        Log.d("bootpay  ready", data);
    }

    @Override
    public void onConfirm(String data) {
        Log.d("bootpay  confirm", data);
    }

    @Override
    public void onDone(String data) {
        Log.d("bootpay  done", data);
    }
}
