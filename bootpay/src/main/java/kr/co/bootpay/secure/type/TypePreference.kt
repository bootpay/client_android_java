package kr.co.bootpay.secure.type

import kr.co.bootpay.secure.security.EncryptedPreference
import kotlin.reflect.KProperty

interface TypePreference<T> {
    fun getFromPref(prop: KProperty<*>, pref: EncryptedPreference): T

    fun setToPref(prop: KProperty<*>, value: T, pref: EncryptedPreference)

    fun setEditor(prop: KProperty<*>, value: T, editor: EncryptedPreference.Editor)
}