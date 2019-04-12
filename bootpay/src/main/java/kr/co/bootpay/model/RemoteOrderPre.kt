package kr.co.bootpay.model

import com.google.gson.Gson

// 사전예약 이벤트를 생성하기 위한 모델
class RemoteOrderPre {
    var e_p = ""
    var is_r_n = false //구매자명 입력을 허용할지 말지, is_receive_name

    var is_sale = false
    var s_at = System.currentTimeMillis() // 예약 시작일
    var e_at = 0L
    var desc_html = ""

    var n = "" //상품명, name
    var cn = "" //company_name, 업체명

    fun setExpectedPrice(value: String): RemoteOrderPre {
        e_p = value
        return this
    }

    fun setIsReceiveName(value: Boolean): RemoteOrderPre {
        is_r_n = value
        return this
    }

    fun setIsSale(value: Boolean): RemoteOrderPre {
        is_sale = value
        return this
    }

    fun setStartAt(value: Long): RemoteOrderPre {
        s_at = value
        return this
    }

    fun setEndAt(value: Long): RemoteOrderPre {
        e_at = value
        return this
    }

    fun setDescHtml(value: String): RemoteOrderPre {
        desc_html = value
        return this
    }

    fun setName(value: String): RemoteOrderPre {
        n = value
        return this
    }

    fun setCompanyName(value: String): RemoteOrderPre {
        cn = value
        return this
    }

    fun toGson() = Gson().toJson(this)
}
