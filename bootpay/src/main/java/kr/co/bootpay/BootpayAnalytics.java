package kr.co.bootpay;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import kr.co.bootpay.analytics.AnalyticsService;
import kr.co.bootpay.analytics.AnalyticsPresenter;
import kr.co.bootpay.enums.Gender;
import kr.co.bootpay.model.StatItem;
import kr.co.bootpay.pref.UserInfo;

public class BootpayAnalytics {
    private static AnalyticsService restService;
    private static AnalyticsPresenter presenter;

    public static void init(Context context, String applicationID) {
        if (applicationID.isEmpty()) throw new RuntimeException("Application ID is empty.");
        if (presenter == null) {
            restService = new AnalyticsService(context);
            presenter = new AnalyticsPresenter(restService);
        }

        UserInfo.getInstance(context).setBootpayApplicationId(applicationID);
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
        login(id, email, userName, Gender.UNKNOWN);
    }

    public static void login(
            String id,
            String email,
            String userName,
            int gender
    ) {
        login(id, email, userName, gender, null);
    }

    public static void login(
            String id,
            String email,
            String userName,
            int gender,
            String birth
    )  {
        login(id, email, userName, gender, birth, null);
    }


    public static void login(
            String id,
            String email,
            String userName,
            int gender,
            String birth,
            String phone
    ) {
        login(id, email, userName, gender, birth, phone, null);
    }

    public static void login(
            String id,
            String email,
            String userName,
            int gender,
            String birth,
            String phone,
            String area) {
        if (presenter == null) throw new IllegalStateException("Analytics is not initialized.");
        else presenter.login(id, email, userName, gender, birth, phone, area);
    }


    public static void start(String url, String page_type) {
        start(url, page_type, new ArrayList<StatItem>());
    }

    public static void start(String url, String page_type, List<StatItem> items) {
        if (presenter == null) throw new IllegalStateException("Analytics is not initialized.");
        else presenter.call(url, page_type, items);
    }
}

