package kr.co.bootpay

//import kr.co.bootpay.model.Trace
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.AttributeSet
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.webkit.*
import kr.co.bootpay.listner.EventListener
import kr.co.bootpay.model.Request
import kr.co.bootpay.pref.UserInfo
import java.net.URISyntaxException

internal class BootpayWebView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0): WebView(context, attrs, defStyleAttr) {

    companion object {
        private const val BOOTPAY = "https://inapp.bootpay.co.kr/2.1.1/production.html"

        private const val CLOSE = -3

        private const val ERROR = -2

        private const val CANCEL = -1

        private const val READY = 0

        private const val CONFIRM = 1

        private const val DONE = 2
    }

    private var request: Request? = null

    private var isLoaded = false

    private val eventHandler = object: Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            val data = msg.obj.toString()
            when (msg.what) {
                CLOSE   -> onCloseHandled(data)
                ERROR   -> onErrorHandled(data)
                CANCEL  -> onCancelHandled(data)
                READY   -> onReadyHandled(data)
                CONFIRM -> onConfirmHandled(data)
                DONE    -> onDoneHandled(data)
            }
        }
    }

    private var listener: EventListener? = null

    private var dialog: Dialog? = null

    init {
        isFocusable = true
        isFocusableInTouchMode = true

        requestFocus()
        setting(context)

        setWebViewClient(object: WebViewClient() {

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                if (!isLoaded) { // recursive called under version 5.0 Lollipop OS
                    isLoaded = true
                    registerAppId()

                    setDevice()
                    setAnalyticsData(
                            UserInfo.bootpay_uuid,
                            UserInfo.bootpay_sk,
                            UserInfo.bootpay_last_time,
                            System.currentTimeMillis() - UserInfo.bootpay_last_time)

                    loadParams(
                            request(
                                    price(),
                                    applicationId(),
                                    name(),
                                    pg(),
//                                    userPhone(),
                                    agree(),
                                    method(),
                                    methods(),
                                    items(),
                                    params(),
                                    accountExpireAt(),
                                    orderId(),
                                    useOrderId(),
                                    userJson(),
                                    extraJson(),
                                    remoteLinkJson()
                            ),
                            error(),
                            cancel(),
                            ready(),
                            confirm(),
                            close(),
                            done()
                    )
                }
            }

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                val intent = parse(url)
                return if (isIntent(url)) {
                    if (isExistInfo(intent) or isExistPackage(intent))
                        start(intent)
                    else
                        gotoMarket(intent)
                }
                else if (isMarket(url))
                    start(intent)
                else if (isSpecialCase(url))
                    start(intent)
                else
                    url.contains("https://testquickpay")
            }

            override fun shouldOverrideKeyEvent(view: WebView, event: KeyEvent): Boolean {
                if (event.keyCode == KeyEvent.KEYCODE_BACK) back()
                return super.shouldOverrideKeyEvent(view, event)
            }
        })

        loadUrl(BOOTPAY)
    }

    fun setDialog(dialog: Dialog): BootpayWebView {
        this.dialog = dialog
        return this
    }

    fun back(): Boolean {
        if (canGoBack()) goBack()
        else dialog?.cancel()
        return true
    }

    private fun isIntent(url: String?) = url?.matches(Regex("^intent:?\\w*://\\S+$")) ?: false

    private fun isMarket(url: String?) = url?.matches(Regex("^market://\\S+$")) ?: false

    private fun isExistInfo(intent: Intent?): Boolean {
        return try {
            intent != null && context.packageManager.getPackageInfo(intent.`package`, PackageManager.GET_ACTIVITIES) != null
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }

    }

    private fun isExistPackage(intent: Intent?): Boolean =
            intent != null && context.packageManager.getLaunchIntentForPackage(intent.`package`) != null

    private fun isSpecialCase(url: String?) = url?.matches(Regex("^shinhan\\S+\$")) ?: false

    private fun parse(url: String): Intent? {
        return try {
            Intent.parseUri(url, Intent.URI_INTENT_SCHEME)
        } catch (e: URISyntaxException) {
            null
        }

    }

    private fun start(intent: Intent?): Boolean {
        intent?.let { context.startActivity(it) }
        return true
    }

    private fun setAnalyticsData(uuid: String, sk: String, sk_time: Long, time: Long) {
        load("window.BootPay.setAnalyticsData({uuid:'$uuid',sk:'$sk',sk_time:'$sk_time',time:'$time'});")
    }

    private fun setDevice() {
        load("window.BootPay.setDevice('ANDROID');")
    }

    private fun gotoMarket(intent: Intent?): Boolean {
        intent?.let { start(Intent(Intent.ACTION_VIEW).apply { data = Uri.parse("market://details?id=${it.`package`}") }) }
        return true
    }

    private fun request(vararg query: String) = "BootPay.request({${query.filter(String::isNotEmpty).joinToString()}})"

//    private fun userJson(vararg info: String) = "user_info: {${info.filter(String::isNotEmpty).joinToString()}}"
//    private fun userPhone() = request?.bootUser?.phone?.takeIf(String::isNotEmpty)?.let { "phone: '$it'" } ?: ""
    private fun userJson() = request?.bootUser?.let { "user_info: ${it.toJson()}" } ?: ""
    private fun extraJson() = request?.bootExtra?.let { "bootExtra: ${it.toJson()}" } ?: ""
    private fun remoteLinkJson() = request?.remoteLink?.let { "remte_link_info: ${it.toJson()}" } ?: ""



//    private fun bootExtra(vararg etcs: String) = "bootExtra: {${etcs.filter(String::isNotEmpty).joinToString()}}"

//    private
//    private fun userName() = request?.bootUser?.name?.takeIf(String::isNotEmpty)?.let { "username: '$it'" } ?: ""
//
//    private fun userEmail() = request?.bootUser?.email?.takeIf(String::isNotEmpty)?.let { "email: '$it'" } ?: ""
//
//    private fun userAddr() = request?.user_addr?.takeIf(String::isNotEmpty)?.let { "addr: '$it'" } ?: ""
//
//    private fun userPhone() = request?.user_phone?.takeIf(String::isNotEmpty)?.let { "phone: '$it'" } ?: ""



//    private fun extraStartAt() = request?.extra_start_at?.let { "start_at: '$it'" } ?: ""
//    private fun extraEndAt() = request?.extra_end_at?.let { "end_at: '$it'" } ?: ""
//    private fun extraExpireMonth() = request?.extra_expire_month?.let { "expire_month: $it" } ?: ""
//    private fun extraVBankResult() = request?.extra_vbank_result?.let { "vbank_result: $it" } ?: ""
//    private fun extraQuota() = request?.extra_quotas?.let { "quota: '${it.joinToString()}'" } ?: ""
//    private fun extraAppScheme() = request?.extra_app_scheme?.let { "app_scheme: '${it}://${extraAppSchemeHost()}'" } ?: ""
//    private fun extraAppSchemeHost() = request?.extra_app_scheme_host?.let { "${it}" } ?: ""


    private fun error() = ".error(function(data){Android.error(JSON.stringify(data));})"

    private fun ready() = ".ready(function(data){Android.ready(JSON.stringify(data));})"

    private fun close() = ".close(function(data){Android.close('close');})"

    private fun confirm() = ".confirm(function(data){Android.confirm(JSON.stringify(data));})"

    private fun cancel() = ".cancel(function(data){Android.cancel(JSON.stringify(data));})"

    private fun done() = ".done(function(data){Android.done(JSON.stringify(data));})"

    private fun price() = request?.price?.let { "price:$it" } ?: ""

    private fun applicationId() = request?.application_id?.let { "application_id:'$it'" } ?: ""

    private fun name() = request?.name?.let { "name:'${it.replace("\"", "'").replace("'", "\\'")}'" } ?: ""

    private fun pg() = request?.pg?.let { "pg:'$it'" } ?: ""

    private fun agree() = "show_agree_window: ${if (request?.is_show_agree == true) 1 else 0}"

    private fun method() = request?.method?.let { "method:'$it'" } ?: ""

    private fun methods() = request?.methods?.let { "methods: ${listToString(it)}"} ?:""

    private fun items() = "items:${
    request?.items?.map { "{item_name:'${it.item_name.replace("\"", "'").replace("'", "\\'")}',qty:${it.qty},unique:'${it.unique}',price:${it.price},cat1:'${it.cat1.replace("\"", "'").replace("'", "\\'")}',cat2:'${it.cat2.replace("\"", "'").replace("'", "\\'")}',cat3:'${it.cat3.replace("\"", "'").replace("'", "\\'")}'}" }
    }"

    private fun orderId() = request?.order_id?.let { "order_id:'$it'" } ?: ""

    private fun useOrderId() = request?.use_order_id?.let { "use_order_id:'$it'" } ?: "0"

    private fun listToString(list: List<String>):String {
        var buffer = ""
        for(obj in list) {
            if(buffer.length > 0) buffer += ","
            buffer += "'$obj'"
        }
        return "[$buffer]"
    }

    private fun accountExpireAt() = request?.account_expire_at?.let { "account_expire_at:'$it'" } ?: ""

    private fun params(): String {
        return if (request?.params.isNullOrEmpty())
            "params:''"
        else
            "params:JSON.parse('${request!!.params}')"
    }

    private fun registerAppId() {
        load("window.BootPay.setApplicationId('${request!!.application_id}');")
//        load("$(\"script[data-boot-app-id]\").attr(\"data-boot-app-id\", '${request!!.application_id}');")
    }

    fun transactionConfirm(data: String?) {
        load("window.BootPay.transactionConfirm(JSON.parse('${data ?: ""}'));")
    }

    fun removePaymentWindow() {
        load("window.BootPay.removePaymentWindow();")
    }

//    private fun items() = "items:${
//    request?.items?.map { "{item_name:${it.name},qty:${it.qty},unique:'${it.unique}',price:${it.price}},cat1:${it.cat1},cat2:${it.cat2},cat3:${it.cat3}," }?.dropLast(2)
//    }"


    fun setData(request: Request?): BootpayWebView {
        this.request = request
        return this
    }

    private fun load(script: String) {
        loadUrl("javascript:(function(){$script})()")
    }

    private fun loadParams(vararg script: String) {
        load("${script.joinToString("")};")
    }

    @SuppressLint("setJavaScriptEnabled")
    private fun setting(context: Context) {
        setWebChromeClient(Client())
        addJavascriptInterface(AndroidBridge(), "Android")
        CookieManager.getInstance().setAcceptCookie(true)

        settings.apply {
            if (Build.VERSION.SDK_INT >= 21) {
                mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                CookieManager.getInstance().setAcceptCookie(true)
                CookieManager.getInstance().setAcceptThirdPartyCookies(this@BootpayWebView, true)
            }
            javaScriptEnabled = true
            javaScriptCanOpenWindowsAutomatically = true
            domStorageEnabled = true
            setSupportMultipleWindows(true)
            loadsImagesAutomatically = true
            useWideViewPort = true
            loadWithOverviewMode = true
            setAppCacheEnabled(true)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            context.applicationInfo.flags = context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE
            if (0 != (context.applicationInfo.flags))
                WebView.setWebContentsDebuggingEnabled(true)
        }
    }

    fun setOnResponseListener(listener: EventListener?) {
        this.listener = listener
    }

    private inner class AndroidBridge: IScriptFuction {
        @JavascriptInterface
        override fun close(data: String) {
            eventHandler.sendMessage(Message.obtain(eventHandler, CLOSE, data))
        }

        @JavascriptInterface
        override fun ready(data: String) {
            eventHandler.sendMessage(Message.obtain(eventHandler, READY, data))
        }

        @JavascriptInterface
        override fun error(data: String) {
            eventHandler.sendMessage(Message.obtain(eventHandler, ERROR, data))
        }

        @JavascriptInterface
        override fun cancel(data: String) {
            eventHandler.sendMessage(Message.obtain(eventHandler, CANCEL, data))
        }

        @JavascriptInterface
        override fun confirm(data: String) {
            eventHandler.sendMessage(Message.obtain(eventHandler, CONFIRM, data))
        }

        @JavascriptInterface
        override fun done(data: String) {
            eventHandler.sendMessage(Message.obtain(eventHandler, DONE, data))
        }
    }

    private fun onCloseHandled(data: String) {
        listener?.onClose(data)
        dialog?.dismiss()
    }

    private fun onErrorHandled(data: String) {
        listener?.onError(data)
    }

    private fun onCancelHandled(data: String) {
        listener?.onCancel(data)
    }

    private fun onReadyHandled(data: String) {
        listener?.onReady(data)
    }

    private fun onConfirmHandled(data: String) {
        listener?.onConfirm(data)
    }

    private fun onDoneHandled(data: String) {
        listener?.onDone(data)
    }

    private inner class Client: WebChromeClient() {

        override fun onCreateWindow(view: WebView, isDialog: Boolean, isUserGesture: Boolean, resultMsg: Message?): Boolean {
            val result = view.hitTestResult
            val url = result.extra

            if (isNewWindow(url)) {
                start(view, createFrom(url))
            } else if (resultMsg != null) {
                val newWindow = WebView(view.context)
                addView(newWindow)
                val tr = resultMsg.obj as WebView.WebViewTransport
                tr.webView = newWindow
                resultMsg.sendToTarget()
            }
            return true
        }

        override fun onCloseWindow(window: WebView) {
            super.onCloseWindow(window)
            removeView(window)
            window.visibility = View.GONE

        }

        override fun onJsAlert(view: WebView, url: String, message: String, result: JsResult): Boolean {
            AlertDialog.Builder(view.context)
                    .setMessage(message)
                    .setCancelable(true)
                    .create()
                    .apply {
                        setCanceledOnTouchOutside(true)
                        show()
                    }
            result.confirm()
            return true
        }

        private fun start(view: View, intent: Intent) {
            view.context.startActivity(intent)
        }

        private fun isNewWindow(url: String?): Boolean =
                url != null && !url.contains("___target=_blank")

        private fun createFrom(url: String): Intent {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            return intent
        }
    }
}
