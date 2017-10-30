package kr.co.bootpay.analytics.cookie

import okhttp3.Cookie

internal class CachedCookieIter(cookies: Set<IdentifiableCookie>) : Iterator<Cookie> {
    private val iterator: Iterator<IdentifiableCookie> by lazy { cookies.iterator() }

    override fun hasNext(): Boolean = iterator.hasNext()

    override fun next(): Cookie = iterator.next().cookie

}
