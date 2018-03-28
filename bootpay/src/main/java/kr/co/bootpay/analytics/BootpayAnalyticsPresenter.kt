package kr.co.bootpay.analytics

import android.content.Context
import android.util.Log
import kr.co.bootpay.pref.UserInfo
import rx.Scheduler
import rx.schedulers.Schedulers
import java.util.concurrent.Executors

internal class BootpayAnalyticsPresenter(context: Context) {

    private val rest: RestService by lazy { RestService(context) }
    private val executor: Scheduler by lazy { Schedulers.from(Executors.newCachedThreadPool()) }

    fun login(
            id: String?,
            email: String?,
            username: String?,
            gender: String?,
            birth: String?,
            phone: String?,
            area: String?) {
        rest.api.login(
                UserInfo.bootpay_application_id,
                id ?: "",
                email ?: "",
                username ?: "",
                if (gender?.trim()?.isEmpty() != false) -1 else gender.toInt(),
                birth ?: "",
                phone ?:"",
                area ?: "")
                .subscribeOn(executor)
                .subscribe({
                    UserInfo.bootpay_user_id = it.data?.user_id ?: ""
                }, Throwable::printStackTrace)
    }

    fun call(url: String?, page_type: String?, imageUrl: String?, itemUnique: String?, itemName: String?) {
        rest.api.call(
                UserInfo.bootpay_application_id,
                UserInfo.bootpay_uuid,
                url ?: "",
                page_type ?: "",
                imageUrl ?: "",
                itemUnique ?: "",
                itemName ?: "",
                UserInfo.bootpay_sk,
                UserInfo.bootpay_user_id,
                "")
                .subscribeOn(executor)
                .subscribe({
                    Log.d("BootpayAnalytics", url)
                }, Throwable::printStackTrace)
    }


}
