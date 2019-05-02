package kr.co.bootpay.api

import android.content.Context
import kr.co.bootpay.analytics.cookie.CachedCookie
import kr.co.bootpay.analytics.cookie.PersistanctCookieJar
import kr.co.bootpay.analytics.cookie.PrefCookiePersister
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

internal class BootpayRestService(context: Context) {

    private val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
                .cookieJar(PersistanctCookieJar(CachedCookie(), PrefCookiePersister(context))).build()
    }

    internal val api: BootpayRestApi by lazy {
//        https@ //api.play-pickle.com/account/auth/verification/phoneNumber

        Retrofit.Builder()
                .baseUrl("https://api-ehowlsla.bootpay.co.kr")
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(BootpayRestApi::class.java)
    }
}