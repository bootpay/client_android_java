package kr.co.bootpay.rest;

import android.content.Context;

import kr.co.bootpay.api.ApiPresenter;
import kr.co.bootpay.api.ApiService;
import kr.co.bootpay.model.BootUser;

// 이 로직은 서버사이드에서 수행되어야 합니다.
// rest_application_id와 prviate_key는 보안상 절대로 노출되어서 안되는 값입니다.
// 개발자의 부주의로 고객의 결제가 무단으로 사용될 경우, 부트페이는 책임이 없음을 밝힙니다.
@Deprecated
public class BootpayRest {
    private static ApiPresenter presenter;



    @Deprecated
    public static void getRestToken(Context context, BootpayRestImplement parent, String restApplicationId, String privateKey) {
        if (presenter == null) presenter = new ApiPresenter(new ApiService(context), parent);
        presenter.getRestToken(restApplicationId, privateKey);
    }

    @Deprecated
    public static void getEasyPayUserToken(Context context, BootpayRestImplement parent, String restToken, BootUser user) {
        if (presenter == null) presenter = new ApiPresenter(new ApiService(context), parent);
        presenter.getEasyPayUserToken(restToken, user);

    }
}
