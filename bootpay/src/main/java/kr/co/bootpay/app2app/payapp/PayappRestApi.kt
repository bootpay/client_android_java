package kr.co.bootpay.app2app.payapp

import retrofit2.http.*
import rx.Observable

internal interface PayappRestApi {

    // 페이앱 시작

    @POST("/apiLoad.html")
    fun purchase(
            @Field("cmd") cmd: String,
            @Field("userid") user_id: String,
            @Field("goodname") item_name: String,
            @Field("price") price: Double,
            @Field("recvphone") phone: String,
            @Field("smsuse") sms_use: String,
            @Field("appurl") app_scheme: String
    ): Observable<String>

//    @PUT("notify/{receipt_id}")
//    fun success(
//            @Field("application_id") application_id: String,
//            @Field("data") data: String,
//            @Path("receipt_id") receipt_id: String
//    ): Observable<NotifyResult>
}