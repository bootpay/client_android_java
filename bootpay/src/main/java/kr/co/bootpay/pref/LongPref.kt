package kr.co.bootpay.pref

import android.content.SharedPreferences
import kotlin.reflect.KProperty

internal class LongPref(val default: Long, val key: String?) : BasePref<Long>() {

    override fun getFromPreference(property: KProperty<*>, preference: SharedPreferences): Long =
            preference.getLong(key ?: property.name, default)

    override fun setToPreference(property: KProperty<*>, value: Long, preference: SharedPreferences) {
        preference.edit().putLong(key ?: property.name, value).apply()
    }

    override fun setToEditor(property: KProperty<*>, value: Long, editor: SharedPreferences.Editor) {
        editor.putLong(key ?: property.name, value)
    }
}
