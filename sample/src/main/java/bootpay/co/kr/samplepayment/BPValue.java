package bootpay.co.kr.samplepayment;

public class BPValue {
    public static String pgToString(String key) {
        switch (key) {
            case "KCP":
                return "kcp";
            case "다날":
                return "danal";
            case "LGU+":
                return "lgup";
            case "이니시스":
                return "inicis";
            case "나이스페이":
                return "nicepay";
            case "카카오페이":
                return "kakao";
            case "TPAY":
                return "tpay";
            case "페이레터":
                return "payletter";
            case "KICC":
                return "easypay";
        }
        return "";
    }

    public static String methodToString(String key) {
        switch (key) {
            case "카드결제":
                return "card";
            case "휴대폰소액결제":
                return "phone";
            case "가상계좌":
                return "vbank";
            case "계좌이체":
                return "bank";
            case "카드정기결제":
                return "card_rebill";
            case "간편결제":
                return "";
            case "주문결제":
                return "";
            case "부트페이 간편결제":
                return "";
            case "본인인증":
                return "auth";
        }
        return "";
    }
}
