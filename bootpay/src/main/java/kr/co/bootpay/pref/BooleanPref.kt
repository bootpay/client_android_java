package kr.co.bootpay.pref

import android.content.SharedPreferences
import kotlin.reflect.KProperty

internal class BooleanPref(val default: Boolean, val key: String?) : BasePref<Boolean>() {

    override fun getFromPreference(property: KProperty<*>, preference: SharedPreferences): Boolean =
            preference.getBoolean(key ?: property.name, default)

    override fun setToPreference(property: KProperty<*>, value: Boolean, preference: SharedPreferences) {
        preference.edit().putBoolean(key ?: property.name, value).apply()
    }

    override fun setToEditor(property: KProperty<*>, value: Boolean, editor: SharedPreferences.Editor) {
        editor.putBoolean(key ?: property.name, value)
    }
}
