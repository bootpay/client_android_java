package kr.co.bootpay.app2app.payapp

import android.content.Context
import android.util.Log
import kr.co.bootpay.BootpayActivity
import rx.Scheduler
import rx.schedulers.Schedulers
import java.util.concurrent.Executors

internal class PayappPresenter(activity: BootpayActivity, context: Context) {
    private val activity: BootpayActivity by lazy { activity }
    private val rest: PayappRestService by lazy { PayappRestService(context) }
    private val executor: Scheduler by lazy { Schedulers.from(Executors.newCachedThreadPool()) }

    fun purchase(
            user_id: String,
            item_name: String,
            price: Double,
            phone: String,
            app_scheme: String) {

        rest.api.purchase(
                "payrequest",
                user_id,
                item_name,
                price,
                phone,
                "n",
                app_scheme
                )
                .retry(3)
                .subscribeOn(executor)
                .subscribe({
                    Log.d("InApp", it)
//                    System.out.println(it)
                }, Throwable::printStackTrace)
    }
}
