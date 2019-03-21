package kr.co.bootpay.api

import kr.co.bootpay.api.model.ResDefault
import kr.co.bootpay.model.Item
import kr.co.bootpay.model.RemoteLink
import retrofit2.http.*
import rx.Observable

internal interface BootpayRestApi {

    @FormUrlEncoded
    @POST("/app/rest")
    fun request(
            @Field("application_id") application_id: String,
            @Field("device_type") device_type: String,
            @Field("method") method: String,
            @Field("methods") methods: List<String>,
            @Field("pg") pg: String,
            @Field("price") price: Double, // 결제금액
            @Field("tax_free") tax_free: Double, // 비과세 금액
            @Field("name") name: String, //  상품명
            @Field("items") items: List<Item>,
            @Field("show_agree_window") show_agree_window: Boolean,
//            @Field("phone") phone: String,
            @Field("uuid") uuid: String,
            @Field("sk") sk: String,
            @Field("time") time: Long,
            @Field("user_info") user_info: String,
            @Field("user_id") user_id: String,
            @Field("boot_key") boot_key: String,
            @Field("params") params: String,
            @Field("order_id") order_id: String,
            @Field("use_order_id") use_order_id: Int,
            @Field("account_expire_at") account_expire_at: String,
            @Field("bootExtra") extra: String,
            @Field("remote_link") remote_link: String
    ): Observable<ResDefault>
}