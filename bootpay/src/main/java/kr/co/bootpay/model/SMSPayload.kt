package kr.co.bootpay.model

import com.google.gson.Gson
import kr.co.bootpay.enums.PushType


class SMSPayload {
    // 변수
    private val REMOTE_ORDER_TYPE_FORM = 1
    private val REMOTE_ORDER_TYPE_PRE = 2

    // key
    var o_id = ""
    var o_t = REMOTE_ORDER_TYPE_FORM

    // 공통
    var sj = ""
    var msg = ""
    var pt = PushType.SMS
    var sp = ""
    var rps = mutableListOf<String>()
    var rq_at = System.currentTimeMillis()
    var s_at = System.currentTimeMillis()

    // 알림톡 관련
    var k_tp_id = "" // 템플릿 코드
    var k_msg = ""  // 알림톡 메시지
    var k_vals = mutableMapOf<String, Any>()
    var k_btns = mutableListOf<LinkedHashMap<String, String>>()
    var k_sms_t = PushType.SMS

    // 친구톡 관련
    var img_url = ""
    var img_link = ""
    var ad = 1 //1: 표기함, 0: 표기안함, default 1



    // MMS 관련
    var files = mutableListOf<String>()

    fun setOrderId(value: String): SMSPayload {
        o_id = value
        return this
    }

    fun setOrderType(value: Int): SMSPayload {
        o_t = value
        return this
    }

    fun setSubject(value: String): SMSPayload {
        sj = value
        return this
    }

    fun setMessage(value: String): SMSPayload {
        msg = value
        return this
    }

    fun setPushType(value: Int): SMSPayload {
        pt = value
        return this
    }

    fun setSenderPhone(value: String): SMSPayload {
        sp = value
        return this
    }

    fun setReceieverPhones(value: MutableList<String>): SMSPayload {
        rps = value
        return this
    }

    fun setStartAt(value: Long): SMSPayload {
        s_at = value
        return this
    }

    fun setRequestAt(value: Long): SMSPayload {
        rq_at = value
        return this
    }

    fun setKakaoTemplateId(value: String): SMSPayload {
        k_tp_id = value
        return this
    }

    fun setKakaoMessage(value: String): SMSPayload {
        k_msg = value
        return this
    }

    fun setKakaoValues(value: MutableMap<String, Any>): SMSPayload {
        k_vals = value
        return this
    }

    fun setKakaoButtons(value: MutableList<LinkedHashMap<String, String>>): SMSPayload {
        k_btns = value
        return this
    }

    fun setKakaoSMSType(value: Int): SMSPayload {
        k_sms_t = value
        return this
    }

    fun setImageUrl(value: String): SMSPayload {
        img_url = value
        return this
    }

    fun setImageLink(value: String): SMSPayload {
        img_link = value
        return this
    }

    fun setAd(value: Int): SMSPayload {
        ad = value
        return this
    }



    fun toGson() = Gson().toJson(this)
}
