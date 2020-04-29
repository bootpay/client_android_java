package bootpay.co.kr.samplepayment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import kr.co.bootpay.Bootpay;
import kr.co.bootpay.BootpayAnalytics;
import kr.co.bootpay.listener.CancelListener;
import kr.co.bootpay.listener.CloseListener;
import kr.co.bootpay.listener.DoneListener;
import kr.co.bootpay.listener.ErrorListener;
import kr.co.bootpay.enums.PG;
import kr.co.bootpay.enums.UX;
import kr.co.bootpay.model.BootExtra;
import kr.co.bootpay.model.BootUser;

public class App2AppActivity extends Activity {

    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app2app);
        BootpayAnalytics.init(this, "5b14c0ffb6d49c40cda92c4e");
        context = this;


    }

    public void goOcr(View v) {
        BootUser bootUser = new BootUser().setPhone("010-1234-5678"); // 구매자 정보
        BootExtra bootExtra = new BootExtra().setQuotas(new int[] {0,2,3}).setAppScheme("temp").setAppSchemeHost("ddd");  // 일시불, 2개월, 3개월 할부 허용, 할부는 최대 12개월까지 사용됨 (5만원 이상 구매시 할부허용 범위)
//        BootpayApp.init(context).show();

        Bootpay.init(context)
                .setApplicationId("5b14c0ffb6d49c40cda92c4e") // 해당 프로젝트(안드로이드)의 application id 값
                .setPG(PG.PAYAPP) // 결제할 PG 사
                .setBootUser(bootUser)
                .setBootExtra(bootExtra)

                .setUX(UX.APP2APP_OCR) // 결제수단
                .setName("맥\"북프로's 임다") // 결제할 상품명
                .setOrderId("1234") // 결제 고유번호expire_month
//                .setAccountExpireAt("2018-09-22") // 가상계좌 입금기간 제한 ( yyyy-mm-dd 포멧으로 입력해주세요. 가상계좌만 적용됩니다. 오늘 날짜보다 더 뒤(미래)여야 합니다 )

                .setPrice(10000) // 결제할 금액
                .addItem("마우's 스", 1, "ITEM_CODE_MOUSE", 100) // 주문정보에 담길 상품정보, 통계를 위해 사용
                .addItem("키보드", 1, "ITEM_CODE_KEYBOARD", 200, "패션", "여성상의", "블라우스") // 주문정보에 담길 상품정보, 통계를 위해 사용
                .onDone(new DoneListener() { // 결제완료시 호출, 아이템 지급 등 데이터 동기화 로직을 수행합니다
                    @Override
                    public void onDone(@Nullable String message) {
                        Log.d("done", message);
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
                .show();
    }

}
