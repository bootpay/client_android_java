package kr.co.bootpay.pref

import android.content.SharedPreferences
import kotlin.reflect.KProperty

internal class StringNullablePref(val default: String?, val key: String? = null) : BasePref<String?>() {

    override fun getFromPreference(property: KProperty<*>, preference: SharedPreferences): String? =
            preference.getString(key ?: property.name, default)

    override fun setToPreference(property: KProperty<*>, value: String?, preference: SharedPreferences) {
        preference.edit().putString(key ?: property.name, value).apply()
    }

    override fun setToEditor(property: KProperty<*>, value: String?, editor: SharedPreferences.Editor) {
        editor.putString(key ?: property.name, value)
    }
}