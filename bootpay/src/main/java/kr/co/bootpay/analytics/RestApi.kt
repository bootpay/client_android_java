package kr.co.bootpay.analytics

import kr.co.bootpay.model.StatItem
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import rx.Observable

internal interface RestApi {
    @FormUrlEncoded
    @POST("/login")
    fun login(
            @Field("application_id") applicationId: String,
            @Field("id") userId: String,
            @Field("email") email: String,
            @Field("username") userName: String,
            @Field("gender") gender: Int,
            @Field("birth") birth: String,
            @Field("phone") phone: String,
            @Field("area") area: String
    ): Observable<LoginResult>

    @FormUrlEncoded
    @POST("/call")
    fun call(
            @Field("application_id") applicationId: String,
            @Field("uuid") uuid: String,
            @Field("items") items: MutableList<StatItem>,
            @Field("sk") sk: String,
            @Field("user_id") userId: String,
            @Field("referer") referer: String
    ): Observable<LoginResult>

}