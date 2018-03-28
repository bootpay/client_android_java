package kr.co.bootpay.inapp

import android.content.Context
import android.util.Log
import kr.co.bootpay.pref.UserInfo
import rx.Scheduler
import rx.schedulers.Schedulers
import java.util.concurrent.Executors

internal class InAppPresenter(context: Context) {
    private val rest: InAppRestService by lazy { InAppRestService(context) }
    private val executor: Scheduler by lazy { Schedulers.from(Executors.newCachedThreadPool()) }

    fun purchase(
            application_id: String,
            order_id: String,
            itemName: String,
            price: String) {
        rest.api.purchase(
                application_id,
                2,
                "inapp_android",
                itemName,
                price,
                order_id,
                "",
                UserInfo.bootpay_uuid,
                UserInfo.bootpay_sk,
                UserInfo.bootpay_last_time)
                .retry(3)
                .subscribeOn(executor)
                .subscribe({
                    success(application_id, it.data?.receipt_id ?: "")
                }, Throwable::printStackTrace)
    }

    fun success(application_id: String, receipt_id: String) {
        rest.api.success(
                application_id,
                "",
                receipt_id)
                .retry(3)
                .subscribeOn(executor)
                .subscribe({
                    Log.d("InApp", "Success")
                }, Throwable::printStackTrace)
    }


}
