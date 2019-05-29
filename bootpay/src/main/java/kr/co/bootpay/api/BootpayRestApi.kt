package kr.co.bootpay.api

import kr.co.bootpay.api.model.ResDefault
import retrofit2.http.*
//import rx.Observable

internal interface BootpayRestApi {
//    @FormUrlEncoded
//    @POST("/app/rest/remote_link")
//    fun request_link(
//            @Field("application_id") application_id: String,
//            @Field("device_type") device_type: String,
//            @Field("method") method: String,
//            @Field("methods") methods: String,
//            @Field("pg") pg: String,
//            @Field("price") price: Double, // 결제금액
//            @Field("tax_free") tax_free: Double, // 비과세 금액
//            @Field("name") name: String, //  상품명
//            @Field("items") items: String,
//            @Field("show_agree_window") show_agree_window: Boolean,
//            @Field("uuid") uuid: String,
//            @Field("sk") sk: String,
//            @Field("time") time: Long,
//            @Field("user_info") user_info: String,
//            @Field("user_id") user_id: String,
//            @Field("boot_key") boot_key: String,
//            @Field("params") params: String,
//            @Field("order_id") order_id: String,
//            @Field("use_order_id") use_order_id: Boolean,
//            @Field("account_expire_at") account_expire_at: String,
//            @Field("boot_extra") extra: String,
//            @Field("sms_use") sms_use: Boolean,
//            @Field("sms_payload") sms_payload: String
//    ): Observable<ResDefault>
//
//    @FormUrlEncoded
//    @POST("/app/rest/remote_form")
//    fun request_form(
//            @Field("application_id") application_id: String,
//            @Field("device_type") device_type: String,
//            @Field("method") method: String,
//            @Field("methods") methods: String,
//            @Field("pg") pg: String,
//            @Field("price") price: Double, // 결제금액
//            @Field("tax_free") tax_free: Double, // 비과세 금액
//            @Field("name") name: String, //  상품명
//            @Field("items") items: String,
//            @Field("show_agree_window") show_agree_window: Boolean,
//            @Field("uuid") uuid: String,
//            @Field("sk") sk: String,
//            @Field("time") time: Long,
//            @Field("user_info") user_info: String,
//            @Field("user_id") user_id: String,
//            @Field("boot_key") boot_key: String,
//            @Field("params") params: String,
//            @Field("order_id") order_id: String,
//            @Field("use_order_id") use_order_id: Boolean,
//            @Field("account_expire_at") account_expire_at: String,
//            @Field("bootExtra") extra: String,
//            @Field("remote_form") remote_form: String,
//            @Field("sms_use") sms_use: Boolean,
//            @Field("sms_payload") sms_payload: String
//    ): Observable<ResDefault>
//
//    @FormUrlEncoded
//    @POST("/app/rest/remote_pre")
//    fun request_pre(
//            @Field("application_id") application_id: String,
//            @Field("device_type") device_type: String,
//            @Field("method") method: String,
//            @Field("methods") methods: String,
//            @Field("pg") pg: String,
//            @Field("price") price: Double, // 결제금액
//            @Field("tax_free") tax_free: Double, // 비과세 금액
//            @Field("name") name: String, //  상품명
//            @Field("items") items: String,
//            @Field("show_agree_window") show_agree_window: Boolean,
//            @Field("uuid") uuid: String,
//            @Field("sk") sk: String,
//            @Field("time") time: Long,
//            @Field("user_info") user_info: String,
//            @Field("user_id") user_id: String,
//            @Field("boot_key") boot_key: String,
//            @Field("params") params: String,
//            @Field("order_id") order_id: String,
//            @Field("use_order_id") use_order_id: Boolean,
//            @Field("account_expire_at") account_expire_at: String,
//            @Field("bootExtra") extra: String,
//            @Field("remote_pre") remote_pre: String,
//            @Field("sms_use") sms_use: Boolean,
//            @Field("sms_payload") sms_payload: String
//    ): Observable<ResDefault>
}