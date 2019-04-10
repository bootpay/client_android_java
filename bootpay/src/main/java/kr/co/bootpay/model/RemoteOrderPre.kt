package kr.co.bootpay.model

import com.google.gson.Gson

// 사전예약 이벤트를 생성하기 위한 모델
class RemoteOrderPre {
    var e_p = ""
    var is_r_n = false //구매자명 입력을 허용할지 말지, is_receive_name

    var is_sale = false
    var s_at = Long // 예약 시작일
    var e_at = Long // 예약 종료일
    var desc_html = ""

    var n = "" //상품명, name
    var cn = "" //company_name, 업체명

    fun toGson() = Gson().toJson(this)
}
