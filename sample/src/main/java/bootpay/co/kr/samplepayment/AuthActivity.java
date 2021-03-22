package bootpay.co.kr.samplepayment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.Date;

import kr.co.bootpay.Bootpay;
import kr.co.bootpay.BootpayAnalytics;
import kr.co.bootpay.enums.Method;
import kr.co.bootpay.enums.PG;
import kr.co.bootpay.enums.UX;
import kr.co.bootpay.listener.CancelListener;
import kr.co.bootpay.listener.CloseListener;
import kr.co.bootpay.listener.ConfirmListener;
import kr.co.bootpay.listener.DoneListener;
import kr.co.bootpay.listener.ErrorListener;
import kr.co.bootpay.listener.ReadyListener;


public class AuthActivity extends Activity {

    private String application_id = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        BootpayAnalytics.init(this, application_id);
    }


    public void getAuth(View view) {
        /**
         * 다날 본인인증은 테스트 모드를 지원하지 않습니다.
         * 관리자 화면(https://admin.bootpay.co.kr/install/method) 에서 [다날]->[인증]->cpid, cppwd 를 설정해야 사용하실 수 있습니다.
         * 해당 인증창은 다날 사이트를 띄우는 것이기 때문에 부트페이에서는 화면 커스텀 및 인증결과 데이터를 추가 및 수정할 수 없습니다.
         * 개발 문서 (https://docs.bootpay.co.kr/deep/auth)
         */
        Bootpay.init(getFragmentManager())
                .setApplicationId(application_id) // 해당 프로젝트(안드로이드)의 application id 값
                .setPG(PG.DANAL) // 결제할 PG 사
                .setMethod(Method.AUTH) // 결제수단
                .setContext(this)
                .setOrderId(String.valueOf(new Date().getTime())) // 고객이 설정한 고유값
                .setName("본인인증_테스트")    // 고객이 설정한 명칭
                .setUX(UX.PG_DIALOG)
                .onConfirm(new ConfirmListener() { // 결제가 진행되기 바로 직전 호출되는 함수로, 주로 재고처리 등의 로직이 수행
                    @Override
                    public void onConfirm(@Nullable String message) {
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
