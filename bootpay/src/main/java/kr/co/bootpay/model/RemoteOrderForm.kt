package kr.co.bootpay.model

import com.google.gson.Gson


class RemoteOrderForm {
    // request fields
    var m_id = ""
    var pg = ""
    var fm = mutableListOf<String>() //filtered_methods, 결제수단에 사용할 method - card, bank, vbank, phone
    var tfp = 0.0 //tax_free_price
    var n = "" //상품명, name
    var cn = "" //company_name, 업체명
    var ip = 0.0 // item price - 상품가격
    var dp = 0.0  // display price - 할인된 가격을 표시하기 위함
    var dap = 0.0 // 기본배송비

    var is_r_n = false //구매자명 입력을 허용할지 말지, is_receive_name
    var is_r_p = false //구매자가 가격 입력 허용할지 말지
    var is_addr = false // 주소 받을지 말지
    var is_da = false // 배송비 받을지 말지
    var is_memo = false

    var desc_html = ""
    var dap_jj = 0.0 // 제주지역 도서산간 추가비용
    var dap_njj = 0.0 // 제주 외 지역 도서산간 추가비용
    var o_key = ""

    fun setTaxFreePrice(value: Double): RemoteOrderForm {
        tfp = value
        return this
    }

    fun setPG(value: String): RemoteOrderForm {
        pg = value
        return this
    }

    fun setMethods(value: MutableList<String>): RemoteOrderForm {
        fm = value
        return this
    }

    fun setName(value: String): RemoteOrderForm {
        n = value
        return this
    }

    fun setCompanyName(value: String): RemoteOrderForm {
        cn = value
        return this
    }

    fun setItemPrice(value: Double): RemoteOrderForm {
        ip = value
        return this
    }

    //Deprecated 지금은 사용하지 않음
    fun setDisplayPrice(value: Double): RemoteOrderForm {
        dp = value
        return this
    }

    fun setDeliveryAreaPrice(value: Double): RemoteOrderForm {
        dap = value
        return this
    }

    fun setIsReceiverName(value: Boolean): RemoteOrderForm {
        is_r_n = value
        return this
    }

    fun setIsReceiverPrice(value: Boolean): RemoteOrderForm {
        is_r_p = value
        return this
    }

    fun setIsAddr(value: Boolean): RemoteOrderForm {
        is_addr = value
        return this
    }

    fun setIsDeliveryArea(value: Boolean): RemoteOrderForm {
        is_da = value
        return this
    }

    fun setIsMemo(value: Boolean): RemoteOrderForm {
        is_memo = value
        return this
    }

    fun setDescHtml(value: String): RemoteOrderForm {
        desc_html = value
        return this
    }

    fun setDapJeju(value: Double): RemoteOrderForm {
        dap_jj = value
        return this
    }

    fun setDapNonJeju(value: Double): RemoteOrderForm {
        dap_njj = value
        return this
    }

    fun setObjectKey(value: String): RemoteOrderForm {
        o_key = value
        return this
    }

    fun toGson() = Gson().toJson(this)
}
