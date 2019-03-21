package kr.co.bootpay;

import java.util.HashMap;

public class BootpayKeyValue {
    static HashMap<String, String> pgHash = new HashMap<>();
    static { init(); }

    public static void init() {
        pgHashInit();
    }

    private static void pgHashInit() {
        pgHash.put("KCP", "kcp");
        pgHash.put("다날", "danal");
        pgHash.put("LGU+", "lgup");
        pgHash.put("이니시스", "inicis");
        pgHash.put("나이스페이", "nicepay");
        pgHash.put("페이앱", "payapp");
        pgHash.put("네이버페이", "naverpay");
        pgHash.put("카카오페이", "kakao");
        pgHash.put("TPAY", "tpay");
        pgHash.put("페이레터", "payletter");
        pgHash.put("KICC", "easypay");

    }

    public static String getPGCode(String key) { return pgHash.get(key); }
}
