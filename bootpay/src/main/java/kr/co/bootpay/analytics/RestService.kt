package kr.co.bootpay.analytics

import android.content.Context
import kr.co.bootpay.analytics.cookie.PersistanctCookieJar
import kr.co.bootpay.analytics.cookie.PrefCookiePersister
import kr.co.bootpay.analytics.cookie.CachedCookie
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

internal class RestService(context: Context) {

    private val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
                .cookieJar(PersistanctCookieJar(CachedCookie(), PrefCookiePersister(context))).build()
    }

    internal val api: RestApi by lazy {
        Retrofit.Builder()
                .baseUrl("https://analytics.bootpay.co.kr")
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RestApi::class.java)
    }
}