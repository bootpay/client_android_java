package kr.co.bootpay.enums;

public enum Method {
    CARD,
    CARD_SIMPLE,
    BANK,
    VBANK,
    PHONE,
    AUTH, // 본인인증
    //    CARD_REBILL,
    SUBSCRIPT_CARD, // 정기결제
    SUBSCRIPT_PHONE, // 정기결제
    EASY, // 간편결제
    SELECT,
    PAYCO, // 페이코 직접호출
    KAKAO, // 카카오 직접호출
}
