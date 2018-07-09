package kr.co.bootpay

import android.webkit.JavascriptInterface

interface IScriptFuction {
    @JavascriptInterface
    fun error(data: String)

    @JavascriptInterface
    fun close(data: String)

    @JavascriptInterface
    fun cancel(data: String)

    @JavascriptInterface
    fun ready(data: String)

    @JavascriptInterface
    fun confirm(data: String)

    @JavascriptInterface
    fun done(data: String)
}
