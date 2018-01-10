package kr.co.bootpay.secure.type

import kr.co.bootpay.secure.model.PrefModel
import kr.co.bootpay.secure.security.EncryptedPreference
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.atomic.AtomicReference
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

abstract class AbstractPref<T : Any?> : ReadWriteProperty<PrefModel, T>, TypePreference<T> {

    private val lastUpdate: AtomicLong = AtomicLong(System.nanoTime())
    private val transaction: AtomicReference<T> = AtomicReference()

    operator override fun getValue(thisRef: PrefModel, property: KProperty<*>): T {
        if (!thisRef.inTransaction())
            return getFromPref(property, thisRef.pref)

        if (isAbleToTransaction(thisRef)) {
            transaction.set(getFromPref(property, thisRef.pref))
            lastUpdate.set(System.nanoTime())
        }
        return transaction.get()
    }

    private fun isAbleToTransaction(model: PrefModel) = lastUpdate.get() < model.lastUpdate()

    operator override fun setValue(thisRef: PrefModel, property: KProperty<*>, value: T) {
        if (thisRef.inTransaction()) {
            transaction.set(value)
            lastUpdate.set(System.nanoTime())
            setEditor(property, value, thisRef.editor)
        } else
            setToPref(property, value, thisRef.pref)

    }

    abstract override fun getFromPref(prop: KProperty<*>, pref: EncryptedPreference): T

    abstract override fun setToPref(prop: KProperty<*>, value: T, pref: EncryptedPreference)

    abstract override fun setEditor(prop: KProperty<*>, value: T, editor: EncryptedPreference.Editor)
}
