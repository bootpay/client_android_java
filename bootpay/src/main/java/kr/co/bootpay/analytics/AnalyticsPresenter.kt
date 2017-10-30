package kr.co.bootpay.analytics

import android.content.Context
import android.util.Log
import kr.co.bootpay.pref.UserInfo
import rx.Scheduler
import rx.schedulers.Schedulers
import java.util.concurrent.Executors

internal class AnalyticsPresenter(context: Context) {
    private val rest: RestService by lazy { RestService(context) }
    private val executor: Scheduler by lazy { Schedulers.from(Executors.newCachedThreadPool()) }

    fun login(
            id: String,
            email: String = "",
            username: String = "",
            gender: String = "",
            birth: String = "",
            phone: String = "",
            area: String = ""
    ) {
        rest.api.login(
                UserInfo.bootpay_application_id,
                id,
                email,
                username,
                if (gender.trim() == "") -1 else gender.toInt(),
                birth,
                phone,
                area)
                .retry(3)
                .subscribeOn(executor)
                .subscribe({
                    UserInfo.bootpay_user_id = it.data?.user_id ?: ""
                }, { it.printStackTrace() })
    }

    fun call(page: String, imageUrl: String = "") {
        rest.api.call(
                UserInfo.bootpay_application_id,
                UserInfo.bootpay_uuid,
                page,
                page,
                imageUrl,
                UserInfo.bootpay_sk,
                UserInfo.bootpay_user_id)
                .retry(3)
                .subscribeOn(executor)
                .subscribe({
                    Log.d("Analytics", page)
                }, { it.printStackTrace() })
    }
}
