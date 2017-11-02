package kr.co.bootpay;

import android.content.Context;
import android.support.annotation.NonNull;

import com.chibatching.kotpref.Kotpref;

import kr.co.bootpay.analytics.BootpayAnalyticsPresenter;
import kr.co.bootpay.pref.UserInfo;

public class BootpayAnalytics {

    private static BootpayAnalyticsPresenter presenter;

    public static void init(@NonNull Context context, @NonNull String applicationID) {
        if (applicationID.isEmpty()) throw new RuntimeException("Application ID is empty.");
        Kotpref.INSTANCE.init(context);
        presenter = new BootpayAnalyticsPresenter(context);
        UserInfo.INSTANCE.setBootpay_application_id(applicationID);
    }

    public static void login(@NonNull String id) {
        login(id, "", "", "", "", "", "");
    }

    public static void login(
            @NonNull String id,
            @NonNull String email,
            @NonNull String userName,
            @NonNull String gender,
            @NonNull String birth,
            @NonNull String phone,
            @NonNull String area) {
        presenter.login(id, email, userName, gender, birth, phone, area);
    }


    public static void start(@NonNull String url, @NonNull String page_type) {
        start(url, page_type, "");
    }

    public static void start(@NonNull String url, @NonNull String page_type, @NonNull String imageUrl) {
        presenter.call(url, page_type, imageUrl);
    }
}

