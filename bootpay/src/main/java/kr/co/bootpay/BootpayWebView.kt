package kr.co.bootpay

//import kr.co.bootpay.model.Trace
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.provider.Settings
import android.util.AttributeSet
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.webkit.*
import kr.co.bootpay.model.Request
import kr.co.bootpay.pref.UserInfo
import java.net.URISyntaxException

internal class BootpayWebView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : WebView(context, attrs, defStyleAttr) {

    companion object {
        private val BOOTPAY = "https://inapp.bootpay.co.kr/1.0.0/production.html"

        private val ERROR = -2

        private val CANCEL = -1

        private val CONFIRM = 1

        private val DONE = 2
    }

    private val connManager: ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val networkErrorDialog: AlertDialog = AlertDialog.Builder(context)
            .setMessage("인터넷 연결이 끊어져 있습니다. 인터넷 연결 상태를 확인해주세요.")
            .setPositiveButton("설정") { _, _ -> context.startActivity(Intent(Settings.ACTION_WIFI_SETTINGS)) }
            .setNegativeButton("확인") { dialogInterface, _ -> dialogInterface.dismiss() }
            .create()

    private var request: Request? = null

//    private var trace: Trace? = null

//    private var userData: UserData? = null

    private var isLoaded = false

    private val eventHandler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            val data = msg.obj.toString()
            when (msg.what) {
                ERROR   -> onErrorHandled(data)
                CANCEL  -> onCancelHandled(data)
                CONFIRM -> {
                    onConfirmeHandled(data)
                    transactionConfirm(data)
                }
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
        if (isNetworkConnected) {
            setting(context)

            setWebViewClient(object : WebViewClient() {

                override fun onPageFinished(view: WebView, url: String) {
                    super.onPageFinished(view, url)
                    if (!isLoaded) { // recursive called under version 5.0 Lollipop OS
                        isLoaded = true
                        registerAppId()

                        setDevice()
                        setAnalyticsData(UserInfo.bootpay_uuid, UserInfo.bootpay_sk, UserInfo.bootpay_last_time, System.currentTimeMillis() - UserInfo.bootpay_last_time)

//                        startLoginSession(
//                                user_id(),
//                                user_name(),
//                                user_email(),
//                                user_birth(),
//                                user_gender(),
//                                user_area()
//                        )
//
//                        startTrace(
//                                trace_page_type(),
//                                trace_main_category(),
//                                trace_middle_category(),
//                                trace_sub_category(),
//                                trace_item_image(),
//                                trace_item_name(),
//                                trace_item_unique()
//                        )

                        loadParams(
                                request(
                                        price(),
                                        application_id(),
                                        name(),
                                        pg(),
                                        method(),
                                        items(),
                                        params(),
                                        order_id()
                                ),
                                error(),
                                cancel(),
                                confirm(),
                                done()
                        )
                    }
                }

                override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                    val intent = parse(url)
                    return if (isIntentOf(url)) {
                        if (isExistInfo(intent) or isExistPackage(intent))
                            start(intent)
                        else
                            startMarket(intent)
                    } else if (isMarketOf(url))
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
        } else
            networkErrorDialog.show()
    }

    fun setDialog(dialog: Dialog): BootpayWebView {
        this.dialog = dialog
        return this
    }

    fun back(): Boolean {
        if (canGoBack()) goBack()
        else dialog?.dismiss()
        return true
    }

    private fun isIntentOf(url: String?) = url?.matches(Regex("^intent:?\\w*://\\S+$")) ?: false

    private fun isMarketOf(url: String?) = url?.matches(Regex("^market://\\S+$")) ?: false

    private fun isExistInfo(intent: Intent?): Boolean {
        return try {
            intent != null && context.packageManager.getPackageInfo(intent.`package`, PackageManager.GET_ACTIVITIES) != null
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }

    }

    private fun isExistPackage(intent: Intent?): Boolean =
            intent != null && context.packageManager.getLaunchIntentForPackage(intent.`package`) != null

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

    private fun startMarket(intent: Intent?): Boolean {
        intent?.let { start(Intent(Intent.ACTION_VIEW).apply { data = Uri.parse("market://details?id=${it.`package`}") }) }
        return true
    }

    private fun request(vararg query: String) = "BootPay.request({${query
            .filterNot { it.isEmpty() }
            .joinToString(",")
    }})"

    private fun error() = ".error(function(data){Android.error(JSON.stringify(data));})"

    private fun confirm() = ".confirm(function(data){Android.confirm(JSON.stringify(data));})"

    private fun cancel() = ".cancel(function(data){Android.cancel(JSON.stringify(data));})"

    private fun done() = ".done(function(data){Android.done(JSON.stringify(data));})"

    private fun price() = request?.price?.let { "price:$it" } ?: ""

    private fun application_id() = request?.application_id?.let { "application_id:'$it'" } ?: ""

    private fun name() = request?.name?.let { "name:'$it'" } ?: ""

    private fun pg() = request?.pg?.let { "pg:'$it'" } ?: ""

    private fun method() = request?.method?.let { "method:'$it'" } ?: ""

    private fun params(): String {
        return if (request?.params.isNullOrEmpty())
            "params:''"
        else
            "params:JSON.parse('${request!!.params}')"
    }

    private fun registerAppId() {
        load("$(\"script[data-boot-app-id]\").attr(\"data-boot-app-id\", '${request!!.application_id}');")
    }

//    private fun user_id() =
//        return if (userData != null && !isNullOrEmpty(userData!!.userID)) String.format(locale, "'id':'%s'", userData!!.userID) else ""
//    }
//
//    private fun user_name(): String {
//        return if (userData != null && !isNullOrEmpty(userData!!.userName)) String.format(locale, "'username':'%s'", userData!!.userName) else ""
//    }
//
//    private fun user_birth(): String {
//        return if (userData != null && !isNullOrEmpty(userData!!.userBirth)) String.format(locale, "'birth':'%s'", userData!!.userBirth) else ""
//    }
//
//    private fun user_email(): String {
//        return if (userData != null && !isNullOrEmpty(userData!!.userEmail)) String.format(locale, "'email':'%s'", userData!!.userEmail) else ""
//    }
//
//    private fun user_gender(): String {
//        return if (userData != null && userData!!.userGender >= 0) String.format(locale, "'gender:'%d'", userData!!.userGender) else ""
//    }
//
//    private fun user_area(): String {
//        return if (userData != null && !isNullOrEmpty(userData!!.userArea)) String.format(locale, "'locale':'%s'", userData!!.userArea) else ""
//    }
//
//    private fun trace_page_type(): String {
//        return if (trace != null && !isNullOrEmpty(trace!!.pageType)) String.format(locale, "'page_type':'%s'", trace!!.pageType) else ""
//    }
//
//    private fun trace_main_category(): String {
//        return if (trace != null && !isNullOrEmpty(trace!!.mainCategory)) String.format(locale, "'cat1':'%s'", trace!!.mainCategory) else ""
//    }
//
//    private fun trace_middle_category(): String {
//        return if (trace != null && !isNullOrEmpty(trace!!.middleCategory)) String.format(locale, "'cat2':'%s'", trace!!.middleCategory) else ""
//    }
//
//    private fun trace_sub_category(): String {
//        return if (trace != null && !isNullOrEmpty(trace!!.subCategory)) String.format(locale, "'cat3':'%s'", trace!!.subCategory) else ""
//    }
//
//    private fun trace_item_image(): String {
//        return if (trace != null && !isNullOrEmpty(trace!!.itemImage)) String.format(locale, "'item_img':'%s'", trace!!.itemName) else ""
//    }
//
//    private fun trace_item_name(): String {
//        return if (trace != null && !isNullOrEmpty(trace!!.itemName)) String.format(locale, "'item_item':'%s'", trace!!.itemName) else ""
//    }
//
//    private fun trace_item_unique(): String {
//        return if (trace != null && !isNullOrEmpty(trace!!.itemUnique)) String.format(locale, "'unique':'%s'", trace!!.itemUnique) else ""
//    }

    fun transactionConfirm(data: String?) {
        load("BootPay.transactionConfirm(JSON.parse('$${data ?: ""}'));")
    }

    private fun items() = "items:${
    request?.
            getItems()?.
            map { "{item_name:${it.name},qty:${it.quantity},unique:'${it.primaryKey}',price:${it.price}}," }?.
            dropLast(2)
    }"

    private fun order_id() = request?.orderId?.let { "order_id:'$it'" } ?: ""

    fun setData(request: Request?): BootpayWebView {
        this.request = request
        return this
    }

//    fun setTrace(trace: Trace?): BootpayWebView {
//        this.trace = trace
//        return this
//    }
//
//    fun setUserInfo(data: UserData?): BootpayWebView {
//        userData = data
//        return this
//    }

    private val isNetworkConnected: Boolean
        get() = connManager.activeNetworkInfo != null

//    private fun startTrace(vararg params: String) {
//        if (trace == null) return
//        val builder = StringBuilder()
//        params
//                .filter { it.isNotEmpty() }
//                .joinToString(",")
//                .dropLast(1)
//        load(String.format(locale, "BootPay.startTrace({%s});", builder.toString()))
//    }
//
//    private fun startLoginSession(vararg params: String) {
//        if (userData == null) return
//        val builder = StringBuilder()
//        for (param in params)
//            if (!isNullOrEmpty(param))
//                builder.append(param).append(",")
//        if (builder.length > 0) builder.setLength(builder.length - 1)
//
//        load(String.format(locale, "BootPay.startLoginSession({%s});", builder.toString()))
//    }

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
                mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
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

    private inner class AndroidBridge : IScriptFuction {

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

    private fun onErrorHandled(data: String) {
        listener?.onError(data)
        dialog?.dismiss()
    }

    private fun onCancelHandled(data: String) {
        listener?.onCancel(data)
        dialog?.dismiss()
    }

    private fun onConfirmeHandled(data: String) {
        listener?.onConfirmed(data)
    }

    private fun onDoneHandled(data: String) {
        listener?.onDone(data)
        dialog?.dismiss()
    }

    private inner class Client : WebChromeClient() {

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
