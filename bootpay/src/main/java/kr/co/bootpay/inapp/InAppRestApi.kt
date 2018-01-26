package kr.co.bootpay.inapp

import retrofit2.http.*
import rx.Observable

internal interface InAppRestApi {

    @POST("/notify")
    fun purchase(
            @Field("application_id") application_id: String,
            @Field("device_type") device_type: Int,
            @Field("method") method: String,
            @Field("name") name: String,
            @Field("price") price: String,
            @Field("order_id") order_id: String,
            @Field("user_id") user_id: String,
            @Field("uuid") uuid: String,
            @Field("sk") sk: String,
            @Field("time") time: Long
    ): Observable<NotifyResult>

    @PUT("notify/{receipt_id}")
    fun success(
            @Field("application_id") application_id: String,
            @Field("data") data: String,
            @Path("receipt_id") receipt_id: String
    ): Observable<NotifyResult>
}