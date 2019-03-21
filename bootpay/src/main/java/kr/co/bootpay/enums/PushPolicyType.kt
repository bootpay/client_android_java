package kr.co.bootpay.enums


class PushPolicyType {
    companion object {
        const val NONE = 0 // 설정하지 않음
        const val SMART = 1 //알림톡 실패시 SMS 발송
        const val SMS = 2 //SMS, LMS, MMS
        const val KAKAO = 3 // 알림톡
    }
}
