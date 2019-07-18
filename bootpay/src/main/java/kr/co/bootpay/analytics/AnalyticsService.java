package kr.co.bootpay.analytics;

import android.content.Context;

import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class AnalyticsService {
    private Context context;
    private AnalyticsRestApi api;

    public AnalyticsService(Context context) {
        this.context = context;
        OkHttpClient client = new OkHttpClient
                .Builder()
                .cookieJar(new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context)))
                .build();


        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        api =  new Retrofit.Builder()
                .baseUrl("https://analytics.bootpay.co.kr")
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(AnalyticsRestApi.class);
    }

    public Context getContext() {
        return this.context;
    }

    public AnalyticsRestApi getApi() { return api; }

    public interface AnalyticsRestApi {
        @FormUrlEncoded
        @POST("/login")
        Observable<LoginResult> login(
                @Field("data") String data,
                @Field("session_key") String sessionKey
        );

        @FormUrlEncoded
        @POST("/call")
        Observable<LoginResult> call(
                @Field("data") String data,
                @Field("session_key") String sessionKey
        );
    }
}
