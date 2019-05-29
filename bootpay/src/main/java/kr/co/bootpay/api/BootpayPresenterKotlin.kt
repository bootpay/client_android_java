package kr.co.bootpay.api

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import kr.co.bootpay.enums.UX
import kr.co.bootpay.model.RemoteOrderForm
import kr.co.bootpay.model.Request
import java.util.concurrent.Executors

internal class BootpayPresenterKotlin(context: Context) {
//    private val builder: BootpayBuilder by lazy { builder }
//    private val rest: BootpayRestService by lazy { BootpayRestService(context) }
//    private val executor: Scheduler by lazy { Schedulers.from(Executors.newCachedThreadPool()) }
//
//    fun request(request: Request) {
//        var ux = request.getUX()
//        request.bootExtra?.setUX(ux.name)
//        if(ux == UX.BOOTPAY_REMOTE_LINK) {
//            request_link(request)
//        } else if(ux == UX.BOOTPAY_REMOTE_ORDER) {
//            request_form(request)
//        } else if(ux == UX.BOOTPAY_REMOTE_PRE) {
//            request_pre(request)
//        }
//    }
//
//    fun request_link(request: Request) {
////        var phone: String = request.bootUser?.getPhone() ?: ""
//        var userJson = request.bootUser?.toGson() ?: ""
//        var extraJson = request.bootExtra?.toGson() ?: ""
////        var remoteLinkJson = request.remoteLink?.toGson() ?: ""
//        var methods = request.methods ?: mutableListOf()
//        var sms_payload = request.smsPayload?.toGson() ?: ""
//
////        Log.d("print", Gson().toJson(methods))
//        rest.api.request_link(
//                request.application_id,
//                "2", // Android
//                request.method,
//                Gson().toJson(methods),
//                request.pg,
//                request.price,
//                request.tax_free,
//                request.name,
//                Gson().toJson(request.items),
//                request.is_show_agree,
//                UserInfo.bootpay_uuid,
//                UserInfo.bootpay_sk,
//                System.currentTimeMillis(),
//                userJson,
//                UserInfo.bootpay_user_id,
//                request.boot_key,
//                params(request),
//                request.order_id,
//                request.use_order_id,
//                request.account_expire_at,
//                extraJson,
//                request.smsUse,
//                sms_payload
//        )
//        .retry(3)
//        .subscribeOn(executor)
//        .subscribe({
//            Log.d("Success------", it.toString())
//        }, Throwable::printStackTrace)
//    }
//
//    fun request_form(request: Request) {
//        var userJson = request.bootUser?.toGson() ?: ""
//        var extraJson = request.bootExtra?.toGson() ?: ""
//        var remoteFormJson = request.remoteForm?.toGson() ?: ""
//        var methods = request.methods ?: mutableListOf()
//        var sms_payload = request.smsPayload?.toGson() ?: ""
//
//        if(remoteFormJson.length == 0) {
//            var form = RemoteOrderForm().setName(request.name).setItemPrice(request.price).setPG(request.pg)
//            request.methods?.let { form.setMethods(it) }
//            remoteFormJson = form.toGson()
//        } else {
//            request.remoteForm?.let {
//                if(it.pg.isEmpty()) { it.pg = request.pg }
//                if(it.fm.size == 0) { request.methods?.let { methods -> it.fm = methods }}
//                if(it.ip == 0.0) { it.ip = request.price }
//                if(it.n.isEmpty()) { it.n = request.name }
//                remoteFormJson = it.toGson()
//            }
//        }
//
//
//        rest.api.request_form(
//                request.application_id,
//                "2", // Android
//                request.method,
//                Gson().toJson(methods),
//                request.pg,
//                request.price,
//                request.tax_free,
//                request.name,
//                Gson().toJson(request.items),
//                request.is_show_agree,
//                UserInfo.bootpay_uuid,
//                UserInfo.bootpay_sk,
//                System.currentTimeMillis(),
//                userJson,
//                UserInfo.bootpay_user_id,
//                request.boot_key,
//                params(request),
//                request.order_id,
//                request.use_order_id,
//                request.account_expire_at,
//                extraJson,
//                remoteFormJson,
//                request.smsUse,
//                sms_payload
//        )
//        .retry(3)
//        .subscribeOn(executor)
//        .subscribe({
//            Log.d("Success------", it.toString())
//        }, Throwable::printStackTrace)
//    }
//
//    fun request_pre(request: Request) {
//        var userJson = request.bootUser?.toGson() ?: ""
//        var extraJson = request.bootExtra?.toGson() ?: ""
//        var remotePreJson = request.remotePre?.toGson() ?: ""
//        var methods = request.methods ?: mutableListOf()
//        var sms_payload = request.smsPayload?.toGson() ?: ""
//
//
//        rest.api.request_pre(
//                request.application_id,
//                "2", // Android
//                request.method,
//                Gson().toJson(methods),
//                request.pg,
//                request.price,
//                request.tax_free,
//                request.name,
//                Gson().toJson(request.items),
//                request.is_show_agree,
//                UserInfo.bootpay_uuid,
//                UserInfo.bootpay_sk,
//                System.currentTimeMillis(),
//                userJson,
//                UserInfo.bootpay_user_id,
//                request.boot_key,
//                params(request),
//                request.order_id,
//                request.use_order_id,
//                request.account_expire_at,
//                extraJson,
//                remotePreJson,
//                request.smsUse,
//                sms_payload
//        )
//        .retry(3)
//        .subscribeOn(executor)
//        .subscribe({
//            Log.d("Success------", it.toString())
//        }, Throwable::printStackTrace)
//
//    }
//
//
//    fun params(request: Request)  = request?.params?.takeIf(String::isNotEmpty)?.let {Gson().toJson(it)} ?: ""
//    fun userInfo(vararg info: String) = "{${info.filter(String::isNotEmpty).joinToString()}}"


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
