package kr.co.bootpay.secure.type

import kr.co.bootpay.secure.security.EncryptedPreference
import kotlin.reflect.KProperty

internal class StringPref(private val default: String) : AbstractPref<String>() {
    override fun getFromPref(prop: KProperty<*>, pref: EncryptedPreference): String =
            pref.getString(prop.name, default)

    override fun setToPref(prop: KProperty<*>, value: String, pref: EncryptedPreference) {
        pref.edit().putString(prop.name, value).apply()
    }

    override fun setEditor(prop: KProperty<*>, value: String, editor: EncryptedPreference.Editor) {
        editor.putString(prop.name, value).apply()
    }

}