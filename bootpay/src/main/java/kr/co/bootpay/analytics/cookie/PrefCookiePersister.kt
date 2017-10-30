package kr.co.bootpay.analytics.cookie

import android.content.Context
import okhttp3.Cookie

internal class PrefCookiePersister(context: Context) : CookiePersister {

    private val pref by lazy { context.getSharedPreferences("cookie", Context.MODE_PRIVATE) }

    override fun loadAll(): List<Cookie> =
            pref.all.entries.mapNotNull { SerializableCookie.decode(it.value as String) }.toList()

    override fun saveAll(cookies: Collection<Cookie>) {
        val editor = pref.edit()
        cookies.filter(Cookie::persistent)
                .forEach { editor.putString(createCookieKey(it), SerializableCookie.encode(it)) }
        editor.apply()
    }

    override fun removeAll(cookies: Collection<Cookie>) {
        val editor = pref.edit()
        cookies.forEach { editor.remove(createCookieKey(it)) }
        editor.apply()
    }

    override fun clear() {
        pref.edit()
                .clear()
                .apply()
    }

    private fun createCookieKey(cookie: Cookie): String {
        return StringBuilder()
                .append(if (cookie.secure()) "https" else "http")
                .append("://")
                .append(cookie.domain())
                .append(cookie.path())
                .append("|")
                .append(cookie.name())
                .toString()
    }

}