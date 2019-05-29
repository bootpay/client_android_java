package kr.co.bootpay.model

import com.google.gson.Gson
import kr.co.bootpay.enums.PushType


class SMSPayloadKotlin {
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

    fun getOrderObjectId() = o_id

    fun setOrderObjectId(value: String): SMSPayloadKotlin {
        o_id = value
        return this
    }

    fun getOrderType() = o_t


    fun setOrderType(value: Int): SMSPayloadKotlin {
        o_t = value
        return this
    }

    fun getSubject() = sj

    fun setSubject(value: String): SMSPayloadKotlin {
        sj = value
        return this
    }

    fun getMessage() = msg

    fun setMessage(value: String): SMSPayloadKotlin {
        msg = value
        return this
    }

    fun getPushType() = pt

    fun setPushType(value: Int): SMSPayloadKotlin {
        pt = value
        return this
    }

    fun getSenderPhone() = sp

    fun setSenderPhone(value: String): SMSPayloadKotlin {
        sp = value
        return this
    }

    fun getReceieverPhones() = rps

    fun setReceieverPhones(value: MutableList<String>): SMSPayloadKotlin {
        rps = value
        return this
    }

    fun getStartAt() = s_at

    fun setStartAt(value: Long): SMSPayloadKotlin {
        s_at = value
        return this
    }

    fun getRequestAt() = rq_at

    fun setRequestAt(value: Long): SMSPayloadKotlin {
        rq_at = value
        return this
    }

    fun getKakaoTemplateId() = k_tp_id

    fun setKakaoTemplateId(value: String): SMSPayloadKotlin {
        k_tp_id = value
        return this
    }

    fun getKakaoMessage() = k_msg

    fun setKakaoMessage(value: String): SMSPayloadKotlin {
        k_msg = value
        return this
    }

    fun getKakaoValues() = k_vals

    fun setKakaoValues(value: MutableMap<String, Any>): SMSPayloadKotlin {
        k_vals = value
        return this
    }

    fun getKakaoButtons() = k_btns

    fun setKakaoButtons(value: MutableList<LinkedHashMap<String, String>>): SMSPayloadKotlin {
        k_btns = value
        return this
    }

    fun getKakaoSMSType() = k_sms_t

    fun setKakaoSMSType(value: Int): SMSPayloadKotlin {
        k_sms_t = value
        return this
    }

    fun getImageUrl() = img_url

    fun setImageUrl(value: String): SMSPayloadKotlin {
        img_url = value
        return this
    }

    fun getImageLink() = img_link

    fun setImageLink(value: String): SMSPayloadKotlin {
        img_link = value
        return this
    }

    fun getAdValue() = ad

    fun setAd(value: Int): SMSPayloadKotlin {
        ad = value
        return this
    }

    fun toGson() = Gson().toJson(this)
}
