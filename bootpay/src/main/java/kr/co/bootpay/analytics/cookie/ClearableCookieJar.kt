package kr.co.bootpay.analytics.cookie

import okhttp3.CookieJar

interface ClearableCookieJar : CookieJar {
    fun clear()
}