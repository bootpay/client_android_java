package kr.co.bootpay.analytics.cookie

import okhttp3.Cookie

internal class IdentifiableCookie(val cookie: Cookie) {
    companion object {
        fun decorateAll(cookies: Collection<Cookie>): List<IdentifiableCookie> =
                cookies.map(::IdentifiableCookie)
    }

    override fun equals(other: Any?): Boolean = when {
        other !is IdentifiableCookie                 -> false
        other.cookie.name() != cookie.name()         -> false
        other.cookie.domain() != cookie.domain()     -> false
        other.cookie.path() != cookie.path()         -> false
        other.cookie.secure() != cookie.secure()     -> false
        other.cookie.hostOnly() != cookie.hostOnly() -> false
        else                                         -> true
    }

    override fun hashCode(): Int {
        var hash = 17
        hash = 31 * hash + cookie.name().hashCode()
        hash = 31 * hash + cookie.domain().hashCode()
        hash = 31 * hash + cookie.path().hashCode()
        hash = 31 * hash + if (cookie.secure()) 1 else 0
        hash = 31 * hash + if (cookie.hostOnly()) 1 else 0
        return hash
    }
}