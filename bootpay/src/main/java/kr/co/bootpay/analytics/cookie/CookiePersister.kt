package kr.co.bootpay.analytics.cookie

import okhttp3.Cookie

internal interface CookiePersister {
    fun loadAll(): List<Cookie>
    fun saveAll(cookies: Collection<Cookie>)
    fun removeAll(cookies: Collection<Cookie>)
    fun clear()
}