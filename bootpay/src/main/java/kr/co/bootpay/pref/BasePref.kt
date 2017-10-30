package kr.co.bootpay.pref

import android.content.SharedPreferences
import kotlin.reflect.KProperty

internal abstract class BasePref<T : Any?> : ReadWriteProperty<SecurePrefModel, T> {

    private var lastUpdate: Long = 0
    private var transactionData: Any? = null

    operator override fun getValue(thisRef: SecurePrefModel, property: KProperty<*>): T {
        if (!thisRef.inTransaction) return getFromPreference(property, thisRef.preference)
        if (lastUpdate < thisRef.transactionStartTime) {
            transactionData = getFromPreference(property, thisRef.preference)
            lastUpdate = System.currentTimeMillis()
        }
        @Suppress("UNCHECKED_CAST")
        return transactionData as T
    }

    operator override fun setValue(thisRef: SecurePrefModel, property: KProperty<*>, value: T) {
        if (thisRef.inTransaction) {
            transactionData = value
            lastUpdate = System.currentTimeMillis()
            setToEditor(property, value, thisRef.editor!!)
        } else {
            setToPreference(property, value, thisRef.preference)
        }
    }

    abstract fun getFromPreference(property: KProperty<*>, preference: SharedPreferences): T
    abstract fun setToPreference(property: KProperty<*>, value: T, preference: SharedPreferences)
    abstract fun setToEditor(property: KProperty<*>, value: T, editor: SharedPreferences.Editor)
}