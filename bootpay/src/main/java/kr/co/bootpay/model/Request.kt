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
    var methods: MutableList<String>? = null // 부트페이 통합결제창에서 사용할 결제수단

    var name: String = "" //결제창에 표시될 상품명
    var price: Double = 0.0 //결제금액

    var tax_free: Double = 0.0 //비과세금액 (결제금액 - 과세금액 = 비과세금액)

    var order_id: String = "" // 개발사가 발급한 고유 주문번호 (중복되지 않게 발급해야한다)
    var use_order_id = false // PG사 전산에서 개발사의 order_id를 key로 사용할지 여부 (기본값은 사용하지 않는다 = 부트페이가 발급한 키로 사용한다)
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
    var remoteLink: RemoteLink? = null
    var remoteForm: RemoteOrderForm? = null
    var remotePre: RemoteOrderPre? = null
    var smsPayload: SMSPayload? = null
    var smsUse = false


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

//    fun setRemoteLink(value: RemoteLink) {
//        this.remote_link = value
//    }

    fun getUX(): UX {
        if(this.ux == null || this.ux.length == 0) return UX.PG_DIALOG
        return UX.valueOf(this.ux)
    }
}
