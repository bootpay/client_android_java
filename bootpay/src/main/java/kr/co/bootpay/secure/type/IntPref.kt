package bootpay.co.kr.securepref.type

import kr.co.bootpay.secure.security.EncryptedPreference
import kr.co.bootpay.secure.type.AbstractPref
import kotlin.reflect.KProperty

internal class IntPref(private val default: Int) : AbstractPref<Int>() {
    override fun getFromPref(prop: KProperty<*>, pref: EncryptedPreference): Int =
            pref.getInt(prop.name, default)

    override fun setToPref(prop: KProperty<*>, value: Int, pref: EncryptedPreference) {
        pref.edit().putInt(prop.name, value).apply()
    }

    override fun setEditor(prop: KProperty<*>, value: Int, editor: EncryptedPreference.Editor) {
        editor.putInt(prop.name, value).apply()
    }

}