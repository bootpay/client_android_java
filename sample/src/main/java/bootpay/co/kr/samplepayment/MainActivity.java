package bootpay.co.kr.samplepayment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import kr.co.bootpay.Bootpay;
import kr.co.bootpay.BootpayAnalytics;
import kr.co.bootpay.BootpayKeyValue;
import kr.co.bootpay.enums.Method;
import kr.co.bootpay.enums.PG;
import kr.co.bootpay.enums.UX;
import kr.co.bootpay.listner.CancelListener;
import kr.co.bootpay.listner.CloseListener;
import kr.co.bootpay.listner.ConfirmListener;
import kr.co.bootpay.listner.DoneListener;
import kr.co.bootpay.listner.ErrorListener;
import kr.co.bootpay.listner.ReadyListener;
import kr.co.bootpay.model.BootExtra;
import kr.co.bootpay.model.BootUser;
import kr.co.bootpay.model.RemoteOrderForm;
import kr.co.bootpay.model.SMSPayload;

public class MainActivity extends AppCompatActivity {
//    final String application_id = "59a4d4a1929b3f3b8b6422c8"; //dev
    final String application_id = "5b9f51264457636ab9a07cdc"; //dev2

//    private final String application_id = "5b14c0ffb6d49c40cda92c4e"; //pro

    Spinner spinner_pg;
    Spinner spinner_method;
    Spinner spinner_ux;
    EditText edit_price;
    EditText edit_non_tax;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BootpayAnalytics.init(this, application_id);

        spinner_pg = findViewById(R.id.spinner_pg);
        spinner_method = findViewById(R.id.spinner_method);
        spinner_ux = findViewById(R.id.spinner_ux);
        edit_price = findViewById(R.id.edit_price);
        edit_non_tax = findViewById(R.id.edit_non_tax);
    }

    public void goRequest(View v) {
//        Spinner mySpinner = (Spinner) findViewById(R.id.your_spinner);
//        String text = mySpinner.getSelectedItem().toString();
        BootUser bootUser = new BootUser().setPhone("010-1234-5678"); // 구매자 정보
        BootExtra bootExtra = new BootExtra().setQuotas(new int[] {0,2,3});  // 일시불, 2개월, 3개월 할부 허용, 할부는 최대 12개월까지 사용됨 (5만원 이상 구매시 할부허용 범위)
        int price = 1000;
        try {
            price = Integer.parseInt(edit_price.getText().toString());
        } catch (Exception e){}


        String pg = BootpayKeyValue.getPGCode(spinner_pg.getSelectedItem().toString());
        String method = BPValue.methodToString(spinner_method.getSelectedItem().toString());
        UX ux = UX.valueOf(spinner_ux.getSelectedItem().toString());
        Context context = this;

        BootpayAnalytics.init(this, application_id);
        Bootpay.init(getFragmentManager())
                .setContext(context)
                .setApplicationId(application_id) // 해당 프로젝트(안드로이드)의 application id 값
                .setPG(pg) // 결제할 PG 사
                .setBootUser(bootUser)
                .setBootExtra(bootExtra)
//                .setUserPhone("010-1234-5678") // 구매자 전화번호
                .setUX(ux)
                .setMethod(method) // 결제수단
                .setName("맥\"북프로's 임다") // 결제할 상품명
                .setOrderId("1234") // 결제 고유번호expire_month
//                .setAccountExpireAt("2018-09-22") // 가상계좌 입금기간 제한 ( yyyy-mm-dd 포멧으로 입력해주세요. 가상계좌만 적용됩니다. 오늘 날짜보다 더 뒤(미래)여야 합니다 )

                .setPrice(price) // 결제할 금액
                .addItem("마우's 스", 1, "ITEM_CODE_MOUSE", 100) // 주문정보에 담길 상품정보, 통계를 위해 사용
                .addItem("키보드", 1, "ITEM_CODE_KEYBOARD", 200, "패션", "여성상의", "블라우스") // 주문정보에 담길 상품정보, 통계를 위해 사용
                .onConfirm(new ConfirmListener() { // 결제가 진행되기 바로 직전 호출되는 함수로, 주로 재고처리 등의 로직이 수행
                    @Override
                    public void onConfirm(@Nullable String message) {

                        if (true) Bootpay.confirm(message); // 재고가 있을 경우.
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

    public void goBootpayWindow(View v) {

        BootUser bootUser = new BootUser().setPhone("010-1234-5678"); // 구매자 정보
        BootExtra bootExtra = new BootExtra().setQuotas(new int[] {0,2,3});  // 일시불, 2개월, 3개월 할부 허용, 할부는 최대 12개월까지 사용됨 (5만원 이상 구매시 할부허용 범위)
        List<String> methods = Arrays.asList("card", "phone");
        int price = 1000;
        try {
            price = Integer.parseInt(edit_price.getText().toString());
        } catch (Exception e){}


        Bootpay.init(getFragmentManager())
                .setApplicationId(application_id) // 해당 프로젝트(안드로이드)의 application id 값

                .setName("맥\"북프로's 임다") // 결제할 상품명
                .setOrderId("1234") // 결제 고유번호expire_month
                .setMethods(methods)
//                .setPG(PG.DANAL)
//                .setMethod(Method.SUBSCRIPT_CARD)
//                .setMe
//                .setMethods
//                .setExpireMonth()
//                .setAccountExpireAt("2018-09-22") // 가상계좌 입금기간 제한 ( yyyy-mm-dd 포멧으로 입력해주세요. 가상계좌만 적용됩니다. 오늘 날짜보다 더 뒤(미래)여야 합니다 )
                .setBootUser(bootUser)
                .setBootExtra(bootExtra)
                .setPrice(price) // 결제할 금액
                .addItem("마우's 스", 1, "ITEM_CODE_MOUSE", 100) // 주문정보에 담길 상품정보, 통계를 위해 사용
                .addItem("키보드", 1, "ITEM_CODE_KEYBOARD", 200, "패션", "여성상의", "블라우스") // 주문정보에 담길 상품정보, 통계를 위해 사용
                .onConfirm(new ConfirmListener() { // 결제가 진행되기 바로 직전 호출되는 함수로, 주로 재고처리 등의 로직이 수행
                    @Override
                    public void onConfirm(@Nullable String message) {
                        if (true) Bootpay.confirm(message); // 재고가 있을 경우.
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

    public void goWebAppActivity(View v) {
        Intent intent = new Intent(this, WebAppActivity.class);
        startActivity(intent);
    }

    public void goLocalActivity(View v) {
        Intent intent = new Intent(this, LocalHtmlActivity.class);
        startActivity(intent);
    }

    public void goNativeActivity(View v) {
        Intent intent = new Intent(this, NativeActivity.class);
        startActivity(intent);
    }

    public void goApp2AppActivity(View v) {
        Intent intent = new Intent(this, App2AppActivity.class);
        startActivity(intent);
    }

    public void goFlutterActivity(View v) {
        Intent intent = new Intent(this, FlutterActivity.class);
        startActivity(intent);
    }

    public void goRemoteLink(View v) {
        BootUser bootUser = new BootUser().setPhone("010-4033-4678");
        BootExtra bootExtra = new BootExtra().setQuotas(new int[] {0,2,3});
        SMSPayload payload = new SMSPayload().setMessage("결제링크 안내입니다\n[결제링크]").setSenderPhone("010-4033-4678").setReceieverPhones(Arrays.asList("01040334678"));

//        결제호출
        Bootpay.init(getFragmentManager())
                .setContext(this)
                .setApplicationId(application_id) // 해당 프로젝트(안드로이드)의 application id 값
                .setPG(PG.DANAL) // 결제할 PG 사
                .setMethods(Arrays.asList("card", "phone"))
                .setBootUser(bootUser)
                .setBootExtra(bootExtra)
                .setUX(UX.BOOTPAY_REMOTE_LINK)
//                .setUserPhone("010-1234-5678") // 구매자 전화번호
                .setName("맥\"북프로's 임다") // 결제할 상품명
                .setOrderId("1234") // 결제 고유번호expire_month
//                .setAccountExpireAt("2018-09-22") // 가상계좌 입금기간 제한 ( yyyy-mm-dd 포멧으로 입력해주세요. 가상계좌만 적용됩니다. 오늘 날짜보다 더 뒤(미래)여야 합니다 )
//                .setQuotas(new int[] {0,2,3}) // 일시불, 2개월, 3개월 할부 허용, 할부는 최대 12개월까지 사용됨 (5만원 이상 구매시 할부허용 범위)
                .setPrice(10000) // 결제할 금액
                .setSmsUse(1)
                .setSMSPayload(payload)
                .addItem("마우's 스", 1, "ITEM_CODE_MOUSE", 100) // 주문정보에 담길 상품정보, 통계를 위해 사용
                .addItem("키보드", 1, "ITEM_CODE_KEYBOARD", 200, "패션", "여성상의", "블라우스") // 주문정보에 담길 상품정보, 통계를 위해 사용
                .request();
    }

    public void goRemoteForm(View v) {
        BootUser bootUser = new BootUser().setPhone("010-4033-4678");
        BootExtra bootExtra = new BootExtra().setQuotas(new int[] {0,2,3});
        SMSPayload payload = new SMSPayload().setMessage("결제링크 안내입니다\n[결제링크]").setSenderPhone("010-4033-4678").setReceieverPhones(Arrays.asList("01040334678"));

//        RemoteOrderForm orderForm = new RemoteOrderForm().setName("초코파이");



//        결제호출
        Bootpay.init(getFragmentManager())
                .setContext(this)
                .setApplicationId(application_id) // 해당 프로젝트(안드로이드)의 application id 값
                .setPG(PG.DANAL) // 결제할 PG 사
                .setMethods(Arrays.asList("card", "phone"))
                .setBootUser(bootUser)
                .setBootExtra(bootExtra)
                .setUX(UX.BOOTPAY_REMOTE_ORDER)
//                .setUserPhone("010-1234-5678") // 구매자 전화번호
                .setName("맥\"북프로's 임다") // 결제할 상품명
                .setOrderId("1234") // 결제 고유번호expire_month
//                .setAccountExpireAt("2018-09-22") // 가상계좌 입금기간 제한 ( yyyy-mm-dd 포멧으로 입력해주세요. 가상계좌만 적용됩니다. 오늘 날짜보다 더 뒤(미래)여야 합니다 )
//                .setQuotas(new int[] {0,2,3}) // 일시불, 2개월, 3개월 할부 허용, 할부는 최대 12개월까지 사용됨 (5만원 이상 구매시 할부허용 범위)
                .setPrice(10000) // 결제할 금액
                .setSmsUse(1)
                .setSMSPayload(payload)
                .addItem("마우's 스", 1, "ITEM_CODE_MOUSE", 100) // 주문정보에 담길 상품정보, 통계를 위해 사용
                .addItem("키보드", 1, "ITEM_CODE_KEYBOARD", 200, "패션", "여성상의", "블라우스") // 주문정보에 담길 상품정보, 통계를 위해 사용
                .request();
    }

    public void goRemotePre(View v) {

    }
}

