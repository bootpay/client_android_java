package kr.co.bootpay.valid;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.bootpay.enums.Method;
import kr.co.bootpay.enums.PG;
import kr.co.bootpay.enums.UX;
import kr.co.bootpay.model.Request;

public class PGAvailable {
    private static Map<PG, List<Method>> pgData = new HashMap<PG, List<Method>> () {{
        put(PG.KCP, Arrays.asList(Method.PHONE, Method.CARD, Method.BANK, Method.VBANK, Method.SUBSCRIPT_CARD));
        put(PG.DANAL, Arrays.asList(Method.PHONE, Method.CARD, Method.BANK, Method.VBANK, Method.SUBSCRIPT_CARD));
        put(PG.INICIS, Arrays.asList(Method.PHONE, Method.CARD, Method.BANK, Method.VBANK));
        put(PG.NICEPAY, Arrays.asList(Method.PHONE, Method.CARD, Method.BANK, Method.VBANK));
        put(PG.PAYAPP, Arrays.asList(Method.PHONE, Method.CARD, Method.BANK, Method.VBANK));
        put(PG.KAKAO, Arrays.asList(Method.EASY));
        put(PG.PAYCO, Arrays.asList(Method.EASY));
        put(PG.EASYPAY, Arrays.asList(Method.PHONE, Method.CARD, Method.BANK, Method.VBANK));
        put(PG.KICC, Arrays.asList(Method.PHONE, Method.CARD, Method.BANK, Method.VBANK));
        put(PG.JTNET, Arrays.asList(Method.PHONE, Method.CARD, Method.BANK, Method.VBANK));
        put(PG.TPAY, Arrays.asList(Method.PHONE, Method.CARD, Method.BANK, Method.VBANK));
        put(PG.MOBILIANS, Arrays.asList(Method.PHONE));
        put(PG.PAYLETTER, Arrays.asList(Method.PHONE, Method.CARD, Method.BANK, Method.VBANK));
    }};

    private static Map<UX, List<PG>> uxData = new HashMap<UX, List<PG>> () {{
        put(UX.BOOTPAY_IC, Arrays.asList(PG.EASYPAY, PG.KICC, PG.NICEPAY));
        put(UX.BOOTPAY_SWIPE, Arrays.asList(PG.KCP));
        put(UX.BOOTPAY_NFC, Arrays.asList(PG.PAYAPP));
        put(UX.BOOTPAY_SAMSUNGPAY, Arrays.asList(PG.PAYAPP));
        put(UX.APP2APP_REMOTE, Arrays.asList(PG.PAYAPP));
        put(UX.APP2APP_CARD_SIMPLE, Arrays.asList(PG.PAYAPP));
        put(UX.APP2APP_NFC, Arrays.asList(PG.PAYAPP));
        put(UX.APP2APP_SAMSUNGPAY, Arrays.asList(PG.PAYAPP));
        put(UX.APP2APP_SUBSCRIPT, Arrays.asList(PG.PAYAPP));
        put(UX.APP2APP_CASH_RECEIPT, Arrays.asList(PG.PAYAPP));
        put(UX.APP2APP_OCR, Arrays.asList(PG.PAYAPP));
    }};

    public static List<Method> getDefaultMethods(Request request) {
        PG pg = stringToPG(request.getPg());
        return pgData.get(pg);
    }

    public static List<PG> getBootpayUX(Request request) {
        return uxData.get(request.getUx());
    }

    public static PG stringToPG(String pg) {
        switch (pg) {
            case "kcp":
                return PG.KCP;
            case "danal":
                return PG.DANAL;
            case "inicis":
                return PG.INICIS;
            case "nicepay":
                return PG.NICEPAY;
            case "lgup":
                return PG.LGUP;
            case "payapp":
                return PG.PAYAPP;
            case "kakao":
                return PG.KAKAO;
            case "payco":
                return PG.PAYCO;
            case "tpay":
                return PG.TPAY;
            case "mobilians":
                return PG.MOBILIANS;
            case "payletter":
                return PG.PAYLETTER;
            default:
                throw new IllegalStateException(pg + " is not classified");
        }
    }

    public static String pgToString(PG pg) {
        switch (pg) {
            case BOOTPAY:
                return "bootpay";
            case PAYAPP:
                return  "payapp";
            case DANAL:
                return "danal";
            case KCP:
                return "kcp";
            case INICIS:
                return "inicis";
            case LGUP:
                return "lgup";
            case KAKAO:
                return "kakao";
            case EASYPAY:
                return "easypay";
            case KICC:
                return "easypay";
            case TPAY:
                return "tpay";
            case JTNET:
                return "tpay";
            case MOBILIANS:
                return "mobilians";
            case PAYLETTER:
                return "payletter";
            case NICEPAY:
                return "nicepay";
            case PAYCO:
                return "payco";
        }
        return "";
    }

    public static String methodToString(Method method) {
        switch (method) {
            case CARD:
                return "card";
            case CARD_SIMPLE:
                return "card_simple";
            case BANK:
                return "bank";
            case VBANK:
                return "vbank";
            case PHONE:
                return "phone";
            case SELECT:
                return "";
            case SUBSCRIPT_CARD:
                return "card_rebill";
            case SUBSCRIPT_PHONE:
                return "phone_rebill";
            case AUTH:
                return "auth";
        }
        return "";
    }

    public static boolean isUXPGDefault(UX ux) {
        if(ux == UX.PG_DIALOG) return true;
        return false;
    }

    public static boolean isUXPGSubscript(UX ux) {
        if(ux == UX.PG_SUBSCRIPT || ux == UX.BOOTPAY_SUBSCRIPT_SERVER || ux == UX.BOOTPAY_CHARGE_SERVER || ux == UX.BOOTPAY_RESERVE_SERVER) return true;
        return false;
    }

    public static boolean isUXBootpayApi(UX ux) {
//        if(ux == UX.PG_SUBSCRIPT || ux == UX.BOOTPAY_SUBSCRIPT_SERVER || ux == UX.BOOTPAY_CHARGE_SERVER
//                || ux == UX.BOOTPAY_CHARGE_LINK || ux == UX.BOOTPAY_RESERVE_SERVER) return true;
        if(ux == UX.BOOTPAY_CHARGE_LINK) return true;
        return false;
    }

    public static boolean isUXApp2App(UX ux) {
        if(ux == UX.BOOTPAY_IC || ux == UX.BOOTPAY_SWIPE || ux == UX.BOOTPAY_NFC
                || ux == UX.BOOTPAY_SAMSUNGPAY || ux == UX.APP2APP_REMOTE || ux == UX.APP2APP_CARD_SIMPLE
                || ux == UX.APP2APP_NFC || ux == UX.APP2APP_SAMSUNGPAY || ux == UX.APP2APP_SUBSCRIPT
                || ux == UX.APP2APP_CASH_RECEIPT || ux == UX.APP2APP_OCR) return true;
        return false;
    }

}
