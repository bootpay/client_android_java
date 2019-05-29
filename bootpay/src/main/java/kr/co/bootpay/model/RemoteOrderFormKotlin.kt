package kr.co.bootpay.model

import com.google.gson.Gson


class RemoteOrderFormKotlin {
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

    var is_r_n = false //구매자가 상품명을 입력을 허용할지 말지, is_receive_name
    var is_r_p = false //구매자가 가격 입력 허용할지 말지
    var is_addr = false // 주소 받을지 말지
    var is_da = false // 배송비 받을지 말지
    var is_memo = false

    var desc_html = ""
    var dap_jj = 0.0 // 제주지역 도서산간 추가비용
    var dap_njj = 0.0 // 제주 외 지역 도서산간 추가비용
    var o_key = ""

    fun setTaxFreePrice(value: Double): RemoteOrderFormKotlin {
        tfp = value
        return this
    }

    fun getTaxFreePrice() = tfp


    fun setPG(value: String): RemoteOrderFormKotlin {
        pg = value
        return this
    }

    fun getPG() = pg

    fun setMethods(value: MutableList<String>): RemoteOrderFormKotlin {
        fm = value
        return this
    }

    fun getMethods() = fm


    fun setName(value: String): RemoteOrderFormKotlin {
        n = value
        return this
    }

    fun getName() = n

    fun setCompanyName(value: String): RemoteOrderFormKotlin {
        cn = value
        return this
    }

    fun getCompanyName() = cn

    fun setItemPrice(value: Double): RemoteOrderFormKotlin {
        ip = value
        return this
    }

    fun getItemPrice() = ip

    //Deprecated 지금은 사용하지 않음
    fun setDisplayPrice(value: Double): RemoteOrderFormKotlin {
        dp = value
        return this
    }

    fun setDeliveryAreaPrice(value: Double): RemoteOrderFormKotlin {
        dap = value
        return this
    }

    fun getDeliveryAreaPrice() = dap

    fun setIsReceiverName(value: Boolean): RemoteOrderFormKotlin {
        is_r_n = value
        return this
    }

    fun getIsReceiverName() = is_r_n

    fun setIsReceiverPrice(value: Boolean): RemoteOrderFormKotlin {
        is_r_p = value
        return this
    }

    fun getIsReceiverPrice() = is_r_p

    fun setIsAddr(value: Boolean): RemoteOrderFormKotlin {
        is_addr = value
        return this
    }

    fun getIsAddr() = is_addr

    fun setIsDeliveryArea(value: Boolean): RemoteOrderFormKotlin {
        is_da = value
        return this
    }

    fun getIsDeliveryArea() = is_da

    fun setIsMemo(value: Boolean): RemoteOrderFormKotlin {
        is_memo = value
        return this
    }

    fun getIsMemo() = is_memo

    fun setDescHtml(value: String): RemoteOrderFormKotlin {
        desc_html = value
        return this
    }

    fun getDescHtml() = desc_html

    fun setDapJeju(value: Double): RemoteOrderFormKotlin {
        dap_jj = value
        return this
    }

    fun getDapJeju() = dap_jj

    fun setDapNonJeju(value: Double): RemoteOrderFormKotlin {
        dap_njj = value
        return this
    }

    fun setOrderFormKey(value: String): RemoteOrderFormKotlin {
        o_key = value
        return this
    }

    fun getOrderFormKey() = o_key

    fun toGson() = Gson().toJson(this)
}
