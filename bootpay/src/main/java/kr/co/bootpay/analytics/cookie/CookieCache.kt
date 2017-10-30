package kr.co.bootpay.analytics.cookie

import okhttp3.Cookie

internal interface CookieCache : Iterable<Cookie> {
    fun addAll(cookies: Collection<Cookie>)
    fun clear()
}