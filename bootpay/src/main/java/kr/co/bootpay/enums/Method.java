package kr.co.bootpay.enums;

public enum Method {
    CARD,
    CARD_SIMPLE,
    BANK,
    VBANK,
    PHONE,
    AUTH, // 본인인증
    CARD_REBILL, // 정기결제, SUBSCRIPT_CARD 와 똑같다
    SUBSCRIPT_CARD, // 정기결제
    SUBSCRIPT_PHONE, // 정기결제
    EASY, // 간편결제
    SELECT,
    PAYCO, // 페이코 직접호출
    KAKAO, // 카카오 직접호출
    NPAY, // 네이버페이
    EASY_CARD, // 카드 등록 후 사용하는 간편 정기결제
    EASY_BANK, // 계좌 등록 후 사용하는 간편 현금결제
}
