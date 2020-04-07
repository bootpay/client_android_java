package kr.co.bootpay.api;

import android.content.Context;

import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.reactivex.Observable;
import kr.co.bootpay.analytics.LoginResult;
import kr.co.bootpay.rest.BootpayRestImplement;
import kr.co.bootpay.rest.model.ResEasyPayUserToken;
import kr.co.bootpay.rest.model.ResRestToken;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
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
        @FormUrlEncoded
        @POST("/app/rest/remote_link")
        Observable<LoginResult> request_link(
                @Field("application_id") String application_id,
                @Field("device_type") String device_type,
                @Field("method") String method,
                @Field("methods") String methods,
                @Field("pg") String pg,

                @Field("price") Double price,
                @Field("tax_free") Double tax_free,
                @Field("name") String name,
                @Field("items") String items,
                @Field("show_agree_window") Boolean show_agree_window,

                @Field("uuid") String uuid,
                @Field("sk") String sk,
                @Field("time") Long time,
                @Field("user_info") String user_info,
                @Field("user_id") String user_id,

                @Field("boot_key") String boot_key,
                @Field("params") String params,
                @Field("order_id") String order_id,
                @Field("use_order_id") Boolean use_order_id,
                @Field("account_expire_at") String account_expire_at,

                @Field("boot_extra") String extra,
                @Field("sms_payload") String sms_payload);

        @FormUrlEncoded
        @POST("/app/rest/remote_form")
        Observable<LoginResult> request_form(
                @Field("application_id") String application_id,
                @Field("device_type") String device_type,
                @Field("method") String method,
                @Field("methods") String methods,
                @Field("pg") String pg,

                @Field("price") Double price,
                @Field("tax_free") Double tax_free,
                @Field("name") String name,
                @Field("items") String items,
                @Field("show_agree_window") Boolean show_agree_window,

                @Field("uuid") String uuid,
                @Field("sk") String sk,
                @Field("time") Long time,
                @Field("user_info") String user_info,
                @Field("user_id") String user_id,

                @Field("boot_key") String boot_key,
                @Field("params") String params,
                @Field("order_id") String order_id,
                @Field("use_order_id") Boolean use_order_id,
                @Field("account_expire_at") String account_expire_at,

                @Field("boot_extra") String extra,
                @Field("sms_payload") String sms_payload,
                @Field("remote_form") String remote_form);

        @FormUrlEncoded
        @POST("/app/rest/remote_pre")
        Observable<LoginResult> request_pre(
                @Field("application_id") String application_id,
                @Field("device_type") String device_type,
                @Field("method") String method,
                @Field("methods") String methods,
                @Field("pg") String pg,

                @Field("price") Double price,
                @Field("tax_free") Double tax_free,
                @Field("name") String name,
                @Field("items") String items,
                @Field("show_agree_window") Boolean show_agree_window,

                @Field("uuid") String uuid,
                @Field("sk") String sk,
                @Field("time") Long time,
                @Field("user_info") String user_info,
                @Field("user_id") String user_id,

                @Field("boot_key") String boot_key,
                @Field("params") String params,
                @Field("order_id") String order_id,
                @Field("use_order_id") Boolean use_order_id,
                @Field("account_expire_at") String account_expire_at,

                @Field("boot_extra") String extra,
                @Field("sms_payload") String sms_payload,
                @Field("remote_pre") String remote_pre);


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

    }
}
