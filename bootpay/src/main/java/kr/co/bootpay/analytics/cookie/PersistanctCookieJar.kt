package kr.co.bootpay.analytics.cookie

import okhttp3.Cookie
import okhttp3.HttpUrl
import java.util.concurrent.locks.ReentrantLock

internal class PersistanctCookieJar(private val cache: CookieCache, private val persister: CookiePersister) : ClearableCookieJar {

    init {
        cache.addAll(persister.loadAll())
    }

    override fun saveFromResponse(url: HttpUrl?, cookies: MutableList<Cookie>?) {
        cache.addAll(cookies as Collection<Cookie>)
        persister.saveAll(cookies)
    }

    override fun loadForRequest(url: HttpUrl?): MutableList<Cookie> {
        return cache
                .filterNot { isCookieExpired(it) }
                .filter { it.matches(url) }
                .toMutableList()
    }

    override fun clear() {
        val lock = ReentrantLock(true)
        try {
            lock.lock()
            cache.clear()
            persister.clear()
        } finally {
            lock.unlock()
        }
    }

    private fun isCookieExpired(cookie: Cookie): Boolean =
            cookie.expiresAt() < System.currentTimeMillis()

}