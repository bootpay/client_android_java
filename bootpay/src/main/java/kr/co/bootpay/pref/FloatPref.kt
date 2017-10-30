package kr.co.bootpay.pref

import android.content.SharedPreferences
import kotlin.reflect.KProperty

internal class FloatPref(val default: Float, val key: String?) : BasePref<Float>() {

    override fun getFromPreference(property: KProperty<*>, preference: SharedPreferences): Float =
            preference.getFloat(key ?: property.name, default)

    override fun setToPreference(property: KProperty<*>, value: Float, preference: SharedPreferences) {
        preference.edit().putFloat(key ?: property.name, value).apply()
    }

    override fun setToEditor(property: KProperty<*>, value: Float, editor: SharedPreferences.Editor) {
        editor.putFloat(key ?: property.name, value)
    }
}
