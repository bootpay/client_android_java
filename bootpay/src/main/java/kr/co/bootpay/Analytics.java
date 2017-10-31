package kr.co.bootpay;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.regex.Pattern;

import kr.co.bootpay.analytics.AnalyticsPresenter;
import kr.co.bootpay.pref.SecurePref;
import kr.co.bootpay.pref.UserInfo;

public class Analytics {

    private static AnalyticsPresenter presenter;

    public static void init(@NonNull Context context, @NonNull String applicationID) {
        if (applicationID.isEmpty()) throw new RuntimeException("Application ID is empty.");
        SecurePref.init(context);
        presenter = new AnalyticsPresenter(context);
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


    public static void start(@NonNull String page) {
        start(page, "");
    }

    public static void start(@NonNull String page, @NonNull String imageUrl) {
        presenter.call(page, imageUrl);
    }

    public static void destory() {
        UserInfo.INSTANCE.finish();
    }
}
