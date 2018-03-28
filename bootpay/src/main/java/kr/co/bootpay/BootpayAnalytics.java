package kr.co.bootpay;

import android.content.Context;
import android.support.annotation.NonNull;

import kr.co.bootpay.analytics.BootpayAnalyticsPresenter;
import kr.co.bootpay.enums.Gender;
import kr.co.bootpay.pref.UserInfo;
import kr.co.bootpay.secure.SecurePreference;

public class BootpayAnalytics {

    private static BootpayAnalyticsPresenter presenter;

    public static void init(@NonNull Context context, @NonNull String applicationID) {
        if (applicationID.isEmpty()) throw new RuntimeException("Application ID is empty.");
        SecurePreference.init(context);
        if (presenter == null)
            presenter = new BootpayAnalyticsPresenter(context);
        UserInfo.INSTANCE.setBootpay_application_id(applicationID);
    }

    public static void login(
            String id
    ) {
        login(id, null);
    }

    public static void login(
            String id,
            String email
    ) {
        login(id, email, null);
    }

    public static void login(
            String id,
            String email,
            String userName
    ) {
        login(id, email, userName, "");
    }

    public static void login(
            String id,
            String email,
            String userName,
            String gender
    ) {
        login(id, email, userName, gender, null);
    }

    public static void login(
            String id,
            String email,
            String userName,
            Gender gender
    ) {
        login(id, email, userName, gender, null);
    }

    public static void login(
            String id,
            String email,
            String userName,
            String gender,
            String birth
    )  {
        login(id, email, userName, gender, birth, null);
    }

    public static void login(
            String id,
            String email,
            String userName,
            Gender gender,
            String birth
    ) {
        login(id, email, userName, gender, birth, null);
    }


    public static void login(
            String id,
            String email,
            String userName,
            Gender gender,
            String birth,
            String phone
    ) {
        login(id, email, userName, gender, birth, phone, null);
    }

    public static void login(
            String id,
            String email,
            String userName,
            String gender,
            String birth,
            String phone
    ) {
        login(id, email, userName, gender, birth, phone, null);
    }

    public static void login(
            String id,
            String email,
            String userName,
            Gender gender,
            String birth,
            String phone,
            String area
    ) {
        login(id, email, userName, gender == Gender.MALE ? "male" : "female", birth, phone, area);
    }

    public static void login(
            String id,
            String email,
            String userName,
            String gender,
            String birth,
            String phone,
            String area) {
        if (presenter == null) throw new IllegalStateException("Analytics is not initialized.");
        else presenter.login(id, email, userName, gender, birth, phone, area);
    }


    public static void start(@NonNull String url) {
        start(url, "", "", "", "");
    }

    public static void start(@NonNull String url,
                             @NonNull String page_type,
                             @NonNull String imageUrl,
                             @NonNull String itemUnique,
                             @NonNull String itemName) {
        if (presenter == null) throw new IllegalStateException("Analytics is not initialized.");
        else presenter.call(url, page_type, imageUrl, itemUnique, itemName);
    }
}

