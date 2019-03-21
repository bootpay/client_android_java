package kr.co.bootpay.api

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import kr.co.bootpay.model.Request
import kr.co.bootpay.pref.UserInfo
import rx.Scheduler
import rx.schedulers.Schedulers
import java.util.concurrent.Executors

internal class BootpayPresenter(context: Context) {
//    private val builder: BootpayBuilder by lazy { builder }
    private val rest: BootpayRestService by lazy { BootpayRestService(context) }
    private val executor: Scheduler by lazy { Schedulers.from(Executors.newCachedThreadPool()) }

    fun request(request: Request) {
//        rest.

//        rest.api.purchase()

//        var phone: String = request.bootUser?.getPhone() ?: ""
        var userJson = request.bootUser?.toJson() ?: ""
        var extraJson = request.bootExtra?.toJson() ?: ""
        var remoteLinkJson = request.remote_link?.toJson() ?: ""


//        var userInfo = userInfo(
//                userName(request),
//                userEmail(request),
//                userAddr(request),
//                userPhone(request)
//        )
//        var bootExtra = bootExtra(
//                extraExpireMonth(request),
//                extraVBankResult(request),
//                extraQuota(request),
//                extraAppScheme(request)
//        )


        rest.api.request(
                request.application_id,
                "2", // Android
                request.method,
                request.methods ?: listOf(),
                request.pg,
                request.price,
                request.tax_free,
                request.name,
                request.items,
                request.is_show_agree,
//                phone,
                UserInfo.bootpay_uuid,
                UserInfo.bootpay_sk,
                System.currentTimeMillis(),
                userJson ?: "",
                UserInfo.bootpay_user_id,
                request.boot_key,
                params(request),
                request.order_id,
                request.use_order_id,
                request.account_expire_at,
                extraJson ?: "",
                remoteLinkJson
        )
                .retry(3)
                .subscribeOn(executor)
                .subscribe({
                    Log.d("Success------", it.toString())
//                    System.out.println(it)
                }, Throwable::printStackTrace)

    }

    fun params(request: Request)  = request?.params?.takeIf(String::isNotEmpty)?.let {Gson().toJson(it)} ?: ""
    fun userInfo(vararg info: String) = "{${info.filter(String::isNotEmpty).joinToString()}}"
//    fun userName(request: Request) = request?.user_name?.takeIf(String::isNotEmpty)?.let { "username: '$it'" } ?: ""
//    fun userEmail(request: Request) = request?.user_email?.takeIf(String::isNotEmpty)?.let { "email: '$it'" } ?: ""
//    fun userAddr(request: Request) = request?.user_addr?.takeIf(String::isNotEmpty)?.let { "addr: '$it'" } ?: ""
//    fun userPhone(request: Request) = request?.user_phone?.takeIf(String::isNotEmpty)?.let { "phone: '$it'" } ?: ""
//
//    fun bootExtra(vararg info: String) =  "{${info.filter(String::isNotEmpty).joinToString()}}"
//    private fun extraExpireMonth(request: Request) = request?.extra_expire_month?.let { "expire_month: $it" } ?: ""
//    private fun extraVBankResult(request: Request) = request?.extra_vbank_result?.let { "vbank_result: $it" } ?: ""
//    private fun extraQuota(request: Request) = request?.extra_quotas?.let { "quota: '${it.joinToString()}'" } ?: ""
//    private fun extraAppScheme(request: Request) = request?.extra_app_scheme?.let { "app_scheme: '${it}://${extraAppSchemeHost(request)}'" } ?: ""
//    private fun extraAppSchemeHost(request: Request) = request?.extra_app_scheme_host?.let { "${it}" } ?: ""
}
