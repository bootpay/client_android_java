package bootpay.co.kr.samplepayment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.List;

import kr.co.bootpay.Bootpay;
import kr.co.bootpay.BootpayAnalytics;
import kr.co.bootpay.BootpayKeyValue;
import kr.co.bootpay.enums.PG;
import kr.co.bootpay.enums.UX;
import kr.co.bootpay.listener.CancelListener;
import kr.co.bootpay.listener.CloseListener;
import kr.co.bootpay.listener.ConfirmListener;
import kr.co.bootpay.listener.DoneListener;
import kr.co.bootpay.listener.ErrorListener;
import kr.co.bootpay.listener.ReadyListener;
import kr.co.bootpay.model.BootExtra;
import kr.co.bootpay.model.BootUser;
import kr.co.bootpay.model.RemoteOrderForm;
import kr.co.bootpay.model.RemoteOrderPre;
import kr.co.bootpay.model.SMSPayload;

public class MainActivity extends AppCompatActivity {
//    final String application_id = "5c99d9b8b6d49c516e19099a"; //dev
//    final String application_id = "5b9f51264457636ab9a07cdc"; //dev2
//private ApiPresenter presenter;
//    ApiPresenter present;

//    private final String application_id = "5b14c0ffb6d49c40cda92c4e"; //pro
//    private final String application_id = "c42bf24f74b40034c5f484"; //pro

    private String application_id = "59a4d326396fa607cbe75de5";
    Context context;



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

        this.context = this;

        spinner_pg = findViewById(R.id.spinner_pg);
        spinner_method = findViewById(R.id.spinner_method);
        spinner_ux = findViewById(R.id.spinner_ux);
        edit_price = findViewById(R.id.edit_price);
        edit_non_tax = findViewById(R.id.edit_non_tax);
    }

    public void goRequest(View v) {
//        runOnUiThread();

//        BootpayRestService

//        Spinner mySpinner = (Spinner) findViewById(R.id.your_spinner);
//        String text = mySpinner.getSelectedItem().toString();
        BootUser bootUser = new BootUser().setPhone("010-1234-5678"); // 구매자 정보
        BootExtra bootExtra = new BootExtra().setQuotas(new int[] {0,2,3});  // 일시불, 2개월, 3개월 할부 허용, 할부는 최대 12개월까지 사용됨 (5만원 이상 구매시 할부허용 범위)
        Double price = 1000d;
        try {
            price = Double.parseDouble(edit_price.getText().toString());
        } catch (Exception e){}


        String pg = BootpayKeyValue.getPGCode(spinner_pg.getSelectedItem().toString());
        String method = BPValue.methodToString(spinner_method.getSelectedItem().toString());
        UX ux = UX.valueOf(spinner_ux.getSelectedItem().toString());
        Context context = this;

        BootpayAnalytics.init(this, application_id);

//        Bootpay.init()

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
                .addItem("마우's 스", 1, "ITEM_CODE_MOUSE", 100d) // 주문정보에 담길 상품정보, 통계를 위해 사용
                .addItem("키보드", 1, "ITEM_CODE_KEYBOARD", 200d, "패션", "여성상의", "블라우스") // 주문정보에 담길 상품정보, 통계를 위해 사용
                .onConfirm(new ConfirmListener() { // 결제가 진행되기 바로 직전 호출되는 함수로, 주로 재고처리 등의 로직이 수행
                    @Override
                    public void onConfirm(@Nullable String data) {

                        if (true) Bootpay.confirm(data); // 재고가 있을 경우.
                        else Bootpay.removePaymentWindow(); // 재고가 없어 중간에 결제창을 닫고 싶을 경우
                        Log.d("confirm", data);
                    }
                })
                .onDone(new DoneListener() { // 결제완료시 호출, 아이템 지급 등 데이터 동기화 로직을 수행합니다
                    @Override
                    public void onDone(@Nullable String data) {
                        Log.d("done", data);
                    }
                })
                .onReady(new ReadyListener() { // 가상계좌 입금 계좌번호가 발급되면 호출되는 함수입니다.
                    @Override
                    public void onReady(@Nullable String data) {
                        Log.d("ready", data);
                    }
                })
                .onCancel(new CancelListener() { // 결제 취소시 호출
                    @Override
                    public void onCancel(@Nullable String data) {
                        Log.d("cancel", data);
                    }
                })
                .onError(new ErrorListener() { // 에러가 났을때 호출되는 부분
                    @Override
                    public void onError(@Nullable String data) {

                        Toast.makeText(context, data, Toast.LENGTH_SHORT).show();
                        Log.d("error", data);
                    }
                })
                .onClose(
                        new CloseListener() { //결제창이 닫힐때 실행되는 부분
                            @Override
                            public void onClose(String data) {
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
                .addItem("키보드", 1, "ITEM_CODE_KEYBOARD", 200d, "패션", "여성상의", "블라우스") // 주문정보에 담길 상품정보, 통계를 위해 사용
                .onConfirm(new ConfirmListener() { // 결제가 진행되기 바로 직전 호출되는 함수로, 주로 재고처리 등의 로직이 수행
                    @Override
                    public void onConfirm(@Nullable String data) {
                        if (true) Bootpay.confirm(data); // 재고가 있을 경우.
                        else Bootpay.removePaymentWindow(); // 재고가 없어 중간에 결제창을 닫고 싶을 경우
                        Log.d("confirm", data);
                    }
                })
                .onDone(new DoneListener() { // 결제완료시 호출, 아이템 지급 등 데이터 동기화 로직을 수행합니다
                    @Override
                    public void onDone(@Nullable String data) {
                        Log.d("done", data);
                    }
                })
                .onReady(new ReadyListener() { // 가상계좌 입금 계좌번호가 발급되면 호출되는 함수입니다.
                    @Override
                    public void onReady(@Nullable String data) {
                        Log.d("ready", data);
                    }
                })
                .onCancel(new CancelListener() { // 결제 취소시 호출
                    @Override
                    public void onCancel(@Nullable String data) {
                        Log.d("cancel", data);
                    }
                })
                .onError(new ErrorListener() { // 에러가 났을때 호출되는 부분
                    @Override
                    public void onError(@Nullable String data) {
                        Toast.makeText(context, data, Toast.LENGTH_SHORT).show();
                        Log.d("error", data);
                    }
                })
                .onClose(
                        new CloseListener() { //결제창이 닫힐때 실행되는 부분
                            @Override
                            public void onClose(String data) {
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

    public void goNativeXActivity(View v) {
        Intent intent = new Intent(this, NativeXActivity.class);
        startActivity(intent);
    }

    public void goBioActivity(View v) {
        Intent intent = new Intent(this, BioActivity.class);
        startActivity(intent);
    }

    public void getAuthActivity(View v){
        Intent intent = new Intent(this, AuthActivity.class);
        startActivity(intent);
    }

//    public void goApp2AppActivity(View v) {
//        Intent intent = new Intent(this, App2AppActivity.class);
//        startActivity(intent);
//    }

//    public void goRemoteLink(View v) {
//        BootUser bootUser = new BootUser().setPhone("010-1234-5678");
//        BootExtra bootExtra = new BootExtra().setQuotas(new int[] {0,2,3});
//        SMSPayload payload = new SMSPayload().setMessage("결제링크 안내입니다 [결제링크]")
//                .setSenderPhone("010-4033-4678")
//                .setReceieverPhones(Arrays.asList("010-4033-4678"));
//
////        결제호출
//        Bootpay.init(getFragmentManager())
//                .setContext(this)
//                .setApplicationId(application_id) // 해당 프로젝트(안드로이드)의 application id 값
//                .setPG(PG.DANAL) // 결제할 PG 사
//                .setMethods(Arrays.asList("card", "phone"))
//                .setBootUser(bootUser)
//                .setBootExtra(bootExtra)
//                .setUX(UX.BOOTPAY_REMOTE_LINK)
//                .setName("맥\"북프로's 임다") // 결제할 상품명
//                .setOrderId("1234") // 결제 고유번호expire_month
//                .setPrice(10000) // 결제할 금액
//                .addItem("마우's 스", 1, "ITEM_CODE_MOUSE", 100) // 주문정보에 담길 상품정보, 통계를 위해 사용
//                .addItem("키보드", 1, "ITEM_CODE_KEYBOARD", 200, "패션", "여성상의", "블라우스") // 주문정보에 담길 상품정보, 통계를 위해 사용
//                .request();
//    }
//
//    public void goRemoteForm(View v) {
//        goRemoteFormV2(v);
//        return;
//    }
//
//    public void goRemoteFormV2(View v) {
//        BootUser bootUser = new BootUser().setPhone("010-1234-5678");
//        BootExtra bootExtra = new BootExtra().setQuotas(new int[] {0,2,3});
//        SMSPayload payload = new SMSPayload()
//                .setMessage("결제링크 안내입니다\n[결제링크]")
//                .setSenderPhone("010-4033-4678") // 발신자 번호
//                .setReceieverPhones(Arrays.asList("01040334678")); // 수신할 번호 배열
//
//        RemoteOrderForm form = new RemoteOrderForm()
//                .setIsReceiverName(true) //상품명을 고객으로부터 입력받을지
//                .setIsReceiverPrice(true) //상품가격을 고객으로부터 입력받을지 여부
//                .setDeliveryAreaPrice(2500) //기본배송비
//                .setIsDeliveryArea(true) //배송비 설정할지 여부
//                .setIsAddr(true); // 주소를 입력받을지
//
//
////        결제호출
//        Bootpay.init(getFragmentManager())
//                .setContext(this)
//                .setApplicationId(application_id) // 해당 프로젝트(안드로이드)의 application id 값
//                .setPG(PG.DANAL) // 결제할 PG 사, 지정하지 않을 경우 통합 결제
//                .setMethods(Arrays.asList("card", "phone")) // 사용하길 원하는 결제수단을 배열로 지정 가능
//                .setBootUser(bootUser)
//                .setBootExtra(bootExtra)
//                .setUX(UX.BOOTPAY_REMOTE_ORDER)
//                .setRemoteOrderForm(form)
////                .setUserPhone("010-1234-5678") // 구매자 전화번호
//                .setName("맥\"북프로's 임다") // 결제할 상품명
//                .setOrderId("1234") // 결제 고유번호expire_month
////                .setAccountExpireAt("2018-09-22") // 가상계좌 입금기간 제한 ( yyyy-mm-dd 포멧으로 입력해주세요. 가상계좌만 적용됩니다. 오늘 날짜보다 더 뒤(미래)여야 합니다 )
////                .setQuotas(new int[] {0,2,3}) // 일시불, 2개월, 3개월 할부 허용, 할부는 최대 12개월까지 사용됨 (5만원 이상 구매시 할부허용 범위)
//                .setPrice(10000) // 결제할 금액
//                .setSMSPayload(payload)
//                .addItem("마우's 스", 1, "ITEM_CODE_MOUSE", 100) // 주문정보에 담길 상품정보, 통계를 위해 사용
//                .addItem("키보드", 1, "ITEM_CODE_KEYBOARD", 200, "패션", "여성상의", "블라우스") // 주문정보에 담길 상품정보, 통계를 위해 사용
//                .request();
//    }
//
//    public void goRemotePre(View v) {
//        BootExtra bootExtra = new BootExtra().setQuotas(new int[] {0,2,3});
//        RemoteOrderPre pre = new RemoteOrderPre().setName("사전예약 이벤트").setExpectedPrice("10만원 이하");
//
//
////        결제호출
//        Bootpay.init(getFragmentManager())
//                .setContext(this)
//                .setApplicationId(application_id) // 해당 프로젝트(안드로이드)의 application id 값
//                .setBootExtra(bootExtra)
//                .setUX(UX.BOOTPAY_REMOTE_PRE)
////                .setUserPhone("010-1234-5678") // 구매자 전화번호
//                .setName("맥\"북프로's 임다") // 결제할 상품명
//                .setOrderId("1234") // 결제 고유번호expire_month
//                .setRemoteOrderPre(pre)
////                .setAccountExpireAt("2018-09-22") // 가상계좌 입금기간 제한 ( yyyy-mm-dd 포멧으로 입력해주세요. 가상계좌만 적용됩니다. 오늘 날짜보다 더 뒤(미래)여야 합니다 )
////                .setQuotas(new int[] {0,2,3}) // 일시불, 2개월, 3개월 할부 허용, 할부는 최대 12개월까지 사용됨 (5만원 이상 구매시 할부허용 범위)
//
//                .setPrice(10000) // 결제할 금액
//                .addItem("마우's 스", 1, "ITEM_CODE_MOUSE", 100) // 주문정보에 담길 상품정보, 통계를 위해 사용
//                .addItem("키보드", 1, "ITEM_CODE_KEYBOARD", 200, "패션", "여성상의", "블라우스") // 주문정보에 담길 상품정보, 통계를 위해 사용
//                .request();
//
//    }
}

