package kr.co.bootpay.analytics

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import kr.co.bootpay.model.StatCall
import kr.co.bootpay.model.StatItem
import kr.co.bootpay.model.StatLogin
import kr.co.bootpay.pref.UserInfo
import kr.co.bootpay.secure.BootpaySimpleAES256
import rx.Scheduler
import rx.schedulers.Schedulers
import java.util.concurrent.Executors

internal class BootpayAnalyticsPresenter(context: Context) {

    private val rest: RestService by lazy { RestService(context) }
    private val executor: Scheduler by lazy { Schedulers.from(Executors.newCachedThreadPool()) }

    private val ver = "2.0.121"

    fun login(
            id: String?,
            email: String?,
            username: String?,
            gender: String?,
            birth: String?,
            phone: String?,
            area: String?) {

        val login = StatLogin(
                ver,
                UserInfo.bootpay_application_id,
                id ?: "",
                email ?: "",
                username ?: "",
                if (gender?.trim()?.isEmpty() != false) -1 else gender.toInt(),
                birth ?: "",
                phone ?:"",
                area ?: ""
        )
        val json = Gson().toJson(login)
        val aes = BootpaySimpleAES256()



        rest.api.login(
                aes.strEncode(json),
                aes.sessionKey)
                .subscribeOn(executor)
                .subscribe({
                    UserInfo.bootpay_user_id = it.data?.user_id ?: ""
                }, Throwable::printStackTrace)
    }

    fun call(url: String, page_type: String, items: MutableList<StatItem>) {

        val call = StatCall(
                ver,
                UserInfo.bootpay_application_id,
                UserInfo.bootpay_uuid,
                url,
                page_type,
                items,
                UserInfo.bootpay_sk,
                UserInfo.bootpay_user_id,
                ""
        )

        val json = Gson().toJson(call)
        val aes = BootpaySimpleAES256()


        rest.api.call(
                aes.strEncode(json),
                aes.sessionKey)
                .subscribeOn(executor)
                .subscribe({
                    Log.d("BootpayAnalytics", "call")
                }, Throwable::printStackTrace)
    }


}
