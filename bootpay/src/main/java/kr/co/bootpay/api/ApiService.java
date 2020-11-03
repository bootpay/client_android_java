package kr.co.bootpay.api;

import android.content.Context;

import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;

import io.reactivex.Observable;
import kr.co.bootpay.analytics.LoginResult;
import kr.co.bootpay.model.Payload;
import kr.co.bootpay.model.bio.BioPayload;
import kr.co.bootpay.model.req.ReqBioPayload;
import kr.co.bootpay.model.res.ResEasyBiometric;
import kr.co.bootpay.model.res.ResReceiptID;
import kr.co.bootpay.model.res.ResWalletList;
import kr.co.bootpay.rest.BootpayRestImplement;
import kr.co.bootpay.rest.model.ResDefault;
import kr.co.bootpay.rest.model.ResEasyPayUserToken;
import kr.co.bootpay.rest.model.ResRestToken;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public class ApiService {
    private Context context;
    private ApiRestApi api;

    public ApiService(Context context) {
        this.context = context;

//        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//        logging.level(HttpLoggingInterceptor.Level.BODY);


        OkHttpClient client = new OkHttpClient
                .Builder()
//                .addInterceptor(logging)
                .cookieJar(new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context)))
                .build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        api =  new Retrofit.Builder()
                .baseUrl("https://api.bootpay.co.kr")
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(ApiRestApi.class);
    }

    public Context getContext() {
        return this.context;
    }

    public ApiRestApi getApi() { return api; }

    public interface ApiRestApi {


        //이 함수는 서버사이드에서 수행되길 추천합니다. rest_application_id와 private_key는 노출되어서는 안되는 값입니다.
        @Deprecated
        @FormUrlEncoded
        @POST("/request/token")
        Observable<ResRestToken> getRestToken(
                @Field("application_id") String rest_application_id,
                @Field("private_key") String private_key
        );


        //이 함수는 서버사이드에서 수행되길 추천합니다. rest_application_id와 private_key는 노출되어서는 안되는 값입니다.
        @Deprecated
        @FormUrlEncoded
        @POST("/request/user/token")
        Observable<ResEasyPayUserToken> getEasyPayUserToken(
                @Header("Authorization") String restToken,
                @Field("user_id") String user_id,
                @Field("email") String email,
                @Field("name") String name,
                @Field("gender") int gender,
                @Field("birth") String birth,
                @Field("phone") String phone
        );

        @FormUrlEncoded
        @POST("/app/easy/biometric")
        Observable<ResEasyBiometric> postEasyBiometric(
                @Header("BOOTPAY-DEVICE-UUID") String deviceUUID,
                @Header("BOOTPAY-USER-TOKEN") String userToken,
                @Field("password_token") String password_token,
                @Field("os") String os
        );

        @FormUrlEncoded
        @POST("/app/easy/biometric/register")
        Observable<ResEasyBiometric> postEasyBiometricRegister(
                @Header("BOOTPAY-DEVICE-UUID") String deviceUUID,
                @Header("BOOTPAY-USER-TOKEN") String userToken,
                @Field("otp") String otp
        );

        @GET("/app/easy/card/wallet")
        Observable<ResWalletList> getEasyCardWallet(
                @Header("BOOTPAY-DEVICE-UUID") String deviceUUID,
                @Header("BOOTPAY-USER-TOKEN") String userToken
        );

        @POST("/app/easy/card/request")
        Observable<ResReceiptID> postEasyCardRequest(
                @Header("BOOTPAY-DEVICE-UUID") String deviceUUID,
                @Header("BOOTPAY-USER-TOKEN") String userToken,
                @Body ReqBioPayload req
        );

        @FormUrlEncoded
        @POST("/app/easy/confirm")
        Observable<ResponseBody> postEasyConfirm(
                @Header("BOOTPAY-DEVICE-UUID") String deviceUUID,
                @Header("BOOTPAY-USER-TOKEN") String userToken,
                @Field("receipt_id") String receipt_id
        );

    }
}
