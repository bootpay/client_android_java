package kr.co.bootpay.analytics.cookie

import okhttp3.Cookie
import java.util.*

internal class CachedCookie : CookieCache {

    val cookie = HashSet<IdentifiableCookie>()

    override fun addAll(cookies: Collection<Cookie>) {
        updateCookies(IdentifiableCookie.decorateAll(cookies).toMutableList())
    }

    override fun clear() {
        cookie.clear()
    }

    override fun iterator(): Iterator<Cookie> = CachedCookieIter(cookie)

    private fun updateCookies(cookies: MutableCollection<IdentifiableCookie>) {
        cookies.removeAll(cookies)
        cookies.addAll(cookies)
    }
}