package kr.co.bootpay.model

import com.google.gson.Gson
import kr.co.bootpay.enums.UX
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class Request {
    var application_id: String = "" //부트페이에서 발급받은 android application id, 노출되도 괜찮다

    var pg: String = ""
    var method: String = "" // PG사 결제창에서 사용할 결제수단
    var methods: List<String>? = null // 부트페이 통합결제창에서 사용할 결제수단

    var name: String = "" //결제창에 표시될 상품명
    var price: Double = 0.0 //결제금액

    var tax_free: Double = 0.0 //비과세금액 (결제금액 - 과세금액 = 비과세금액)

    var order_id: String = "" // 개발사가 발급한 고유 주문번호 (중복되지 않게 발급해야한다)
    var use_order_id: Int = 0 // PG사 전산에서 개발사의 order_id를 key로 사용할지 여부 (기본값은 사용하지 않는다 = 부트페이가 발급한 키로 사용한다)
    var params: String? = null // 개발사 -> 부트페이로 보낼 파라미터, 결제 후 부트페이 -> 개발사로 동일하게 리턴해줌


    var account_expire_at: String = "" //  입금 가능한 마지막 날짜 (가상계좌 이용시), 허용 범위는 PG사마다 다르다
    var unit: String = "" // 화폐단위, 일반적으로 사용하지 않는다
//    var feedback_url: String = ""
    var is_show_agree: Boolean = false // 결제시 약관동의를 받을지 여부

    var boot_key: String = "" // 부트페이 로켓 정기결제에 사용될 키 - 유저 고유값, 유저키 이거나 전화번호이거나
    var items: MutableList<Item> = LinkedList() // 결제할 상품들 정보


    var ux: String = "" // 결제상품
    var bootUser: BootUser? = null
    var bootExtra: BootExtra? = null
    var remote_link: RemoteLink? = null


//    var user_name: String? = null // 결제할 사용자 이름 정보
//    var user_email: String? = null // 결제할 사용자 이메일 정보
//    var user_addr: String? = null // 결제할 사용자 주소 정보
//    var user_phone: String? = null // 결제할 사용자 전화번호 정보

//    var extra_start_at: String? = null // 정기 결제 시작일 - 시작일을 지정하지 않으면 그 날 당일로부터 결제가 가능한 Billing key 지급
//    var extra_end_at: String? = null // 정기결제 만료일 -  기간 없음 - 무제한
//    var extra_expire_month: Int? = null //정기결제가 적용되는 개월 수 (정기결제 사용시)
//    var extra_vbank_result: Int? = null //가상계좌 결과창을 볼지 말지 (가상계좌 사용시)
//    var extra_quotas: IntArray? = null //할부허용 범위 (5만원 이상 구매시)
//    var extra_app_scheme: String? = null //app2app 결제시 return 받을 intent scheme
//    var extra_app_scheme_host: String? = null //app2app 결제시 return 받을 intent scheme host


//    var delivery_price: Double = 0.0 //배송비 (결제폼 이용시)
//    var delivery_area_price: Double = 0.0 //도서산간지역 추가배송비 (결제폼 이용시)
//    var auto_delivery_area_price: Boolean = false //도서산간지역 추가배송비를 미리 설정된 정책에 따를지 여부 (결제폼 이용시)

    //링크 결제 관련
//    var member: String? = null // 부트페이에서 발급한 부계정 고유 키
//    var is_receive_member: Boolean? = null // 구매자 이름 입력 허용할지 말지
//    var seller_name: String? = null // 보여질 판매자명, 없으면 등록된 상점명이 보여짐
//    var memo: String? = null // 판매자 메모, 없으면 보여주지 않음
//    var imgUrl: String? = null // 상품 대표 이미지 URL, 없으면 보여주지 않음
//    var descHtml: String? = null // 상품 설명, 없으면 보여주지 않음
//    var delivery_area_price_jeju: Double? = null // 도서산간비용 제주
//    var delivery_area_price_nonjeju: Double? = null // 도서산간비용 제주 외 지역
//
//    // 링크결제에서는 전화번호는 필수다, user_phone 으로 채워지느냐 마느냐일 뿐이다
//    var is_addr: Boolean? = null // 구매자에게 주소를 받을지 말지
//    var is_delivery_area: Boolean? = null // 도서산간 지역 비용 추가 할지 말지
//    var is_memo: Boolean? = null // 구매자에게 한줄메시지 받을지 말지

    fun addItem(item: Item): MutableList<Item> {
        items.add(item)
        return items
    }

    fun addItems(item: Collection<Item>): MutableList<Item> {
        items.addAll(item)
        return items
    }

    @Throws(JSONException::class)
    fun paramOfJson(): JSONObject = JSONObject(params)

    @Throws(JSONException::class)
    fun <T> getParamsOfObject(cls: Class<T>): T = Gson().fromJson(params, cls)

    fun setAccountExpireAt(value: String) {
        this.account_expire_at = value
    }

    fun setParams(json: JSONObject) {
        this.params = json.toString()
    }

    fun setParams(`object`: Any) {
        this.params = Gson().toJson(`object`)
    }

    fun setRemoteLink(value: RemoteLink) {
        this.remote_link = value
    }

    fun getUX(): UX {
        if(this.ux == null || this.ux.length == 0) return UX.PG_DIALOG
        return UX.valueOf(this.ux)
    }

//    fun getPhone(): String {
//        return user_phone ?: ""
//    }
}
