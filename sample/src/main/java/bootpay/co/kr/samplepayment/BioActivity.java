package bootpay.co.kr.samplepayment;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import java.util.Arrays;

import kr.co.bootpay.Bootpay;
import kr.co.bootpay.bio.BootpayBioDialog;
import kr.co.bootpay.enums.Method;
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
import kr.co.bootpay.model.bio.BioPayload;
import kr.co.bootpay.model.bio.BioPrice;
import kr.co.bootpay.rest.BootpayRest;
import kr.co.bootpay.rest.BootpayRestImplement;
import kr.co.bootpay.rest.model.RestEasyPayUserTokenData;
import kr.co.bootpay.rest.model.RestTokenData;

public class BioActivity extends FragmentActivity implements BootpayRestImplement {

    WebView webview;
    TextView bottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bio);

        webview = findViewById(R.id.webview);
        bottom = findViewById(R.id.bottom);

        String url = "https://devtest219.shop.blogpay.co.kr/view/good/2Fd3D";
        loadWebViewLoad(webview, url);
    }

    private void loadWebViewLoad(WebView webview, String url) {
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webview.getSettings().setSupportMultipleWindows(true);
        webview.setWebViewClient(new WebViewClient());
        webview.setWebChromeClient(new WebChromeClient());
        webview.loadUrl(url);
    }

    //이 로직은 서버사이드에서 수행되길 추천합니다. rest_application_id와 private_key는 절대로 노출되어서는 안되는 값입니다.
    //안드로이드에서 사용시 언제나 디컴파일이나 메모리 해킹등으로부터 노출될 수 있으니, 서버사이드에서 수행하셔야 합니다.
    //만약 이 값이 노출될 경우, 고객이 등록한 지불수단에 대하여 다른 고객이 결제를 진행하는 대참사가 일어날 수 있는데
    //개발자의 부주의로 이런 현상이 발생할 경우, 부트페이는 책임이 없음을 밝힙니다.
    //샘플 코드를 제공하는 이유는 빠르게 결제를 테스트 하길 원하시는 개발자들을 위함입니다.
    public void goBioPay(View v) {

        String rest_application_id = "5b8f6a4d396fa665fdc2b5ea";
        String prviate_key = "n9jO7MxVFor3o//c9X5tdep95ZjdaiDvVB4h1B5cMHQ=";

//        String rest_application_id = "5b9f51264457636ab9a07cde";
//        String prviate_key = "sfilSOSVakw+PZA+PRux4Iuwm7a//9CXXudCq9TMDHk=";

        BootpayRest.getRestToken(this, this, rest_application_id, prviate_key);
    }


    @Override
    public void callbackRestToken(RestTokenData acessToken) {
//        String user_id =  String.valueOf(System.currentTimeMillis()); //고유 user_id로 고객별로 유니크해야하며, 다른 고객과 절대로 중복되어서는 안됩니다
        String user_id = "1234_1234_1214434115";

        BootUser user = new BootUser();
        user.setID(user_id);
        user.setArea("서울");
        user.setGender(1); //1: 남자, 0: 여자
        user.setEmail("test1234@gmail.com");
        user.setPhone("010-1234-4567");
        user.setBirth("1988-06-10");
        user.setUsername("홍길동");

        BootpayRest.getEasyPayUserToken(this, this, acessToken.token, user);
    }

    @Override
    public void callbackEasyPayUserToken(RestEasyPayUserTokenData userToken) {

        String easyUserToken = userToken.user_token;

        BootUser bootUser = new BootUser().setPhone("010-1234-5678");
        BootExtra bootExtra = new BootExtra().setQuotas(new int[] {0,2,3}).setPopup(1).setQuickPopup(1);
//
//        String application_id = "5b9f51264457636ab9a07cdc";
        String application_id = "5b8f6a4d396fa665fdc2b5e8";

        BioPayload bioPayload = new BioPayload();
        bioPayload.setPg(PG.PAYAPP)
                .setName("bootpay test")
                .setPrice(89000.0) //최종 결제 금액
                .setOrder_id(String.valueOf(System.currentTimeMillis())) //개발사에서 관리하는 주문번호
                .setName("플리츠레이어 카라숏원피스")
                .setNames(Arrays.asList("플리츠레이어 카라숏원피", "블랙 (COLOR)", "55 (SIZE)")) //결제창에 나타날 상품목록
                .setPrices(Arrays.asList(new BioPrice("상품가격", 89000.0),  //결제창에 나타날 가격목록
                        new BioPrice("쿠폰적용", -25000.0),
                        new BioPrice("배송비", 2500.0)));
//        bioPayload.setNames()

        int stuck = 1; // 재고는 있다고 치자
//
        Bootpay.init(getSupportFragmentManager())
                .setContext(this)
                .setApplicationId(application_id) // 해당 프로젝트(안드로이드)의 application id 값
                .setBootExtra(bootExtra)
                .setBootUser(bootUser)
                .setBioPayload(bioPayload)
                .setEasyPayUserToken(easyUserToken)
//                .seteas
                .setAccountExpireAt("2019-07-16")
                .addItem("마우스", 1, "ITEM_CODE_MOUSE", 500) // 주문정보에 담길 상품정보, 통계를 위해 사용
                .addItem("키보드", 1, "ITEM_CODE_KEYBOARD", 500, "패션", "여성상의", "블라우스") // 주문정보에 담길 상품정보, 통계를 위해 사용
                .onConfirm(new ConfirmListener() { // 결제가 진행되기 바로 직전 호출되는 함수로, 주로 재고처리 등의 로직이 수행
                    @Override
                    public void onConfirm(@Nullable String data) {
                        if (0 < stuck) Bootpay.confirm(data); // 재고가 있을 경우.
                        else Bootpay.removePaymentWindow(); // 재고가 없어 중간에 결제창을 닫고 싶을 경우

                        Log.d("bootpay confirm", data);
                    }
                })
                .onDone(new DoneListener() { // 결제완료시 호출, 아이템 지급 등 데이터 동기화 로직을 수행합니다
                    @Override
                    public void onDone(@Nullable String data) {
                        Log.d("bootpay done", data);
                        Bootpay.dismiss();
                    }
                })
                .onReady(new ReadyListener() { // 가상계좌 입금 계좌번호가 발급되면 호출되는 함수입니다.
                    @Override
                    public void onReady(@Nullable String data) {
                        Log.d("bootpay ready", data);
                    }
                })
                .onCancel(new CancelListener() { // 결제 취소시 호출
                    @Override
                    public void onCancel(@Nullable String data) {

                        Log.d("bootpay cancel", data);
                    }
                })
                .onError(new ErrorListener() { // 에러가 났을때 호출되는 부분
                    @Override
                    public void onError(@Nullable String data) {
                        Log.d("bootpay error", data);
                        Bootpay.dismiss();
                    }
                })
                .onClose(
                        new CloseListener() { //결제창이 닫힐때 실행되는 부분
                            @Override
                            public void onClose(String data) {
                                Log.d("bootpay close", "close");
                            }
                        })
                .requestBio();

    }
}
