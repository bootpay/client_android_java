package kr.co.bootpay.pref

import android.annotation.TargetApi
import android.os.Build
import kotlin.reflect.KProperty

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
internal class StringSetPref(val default: () -> Set<String>, val key: String?) : ReadOnlyProperty<SecurePrefModel, MutableSet<String>> {

    private var stringSet: MutableSet<String>? = null
    private var lastUpdate: Long = 0L

    operator override fun getValue(thisRef: SecurePrefModel, property: KProperty<*>): MutableSet<String> {
        if (stringSet == null || lastUpdate < thisRef.transactionStartTime) {
            val prefSet = thisRef.preference.getStringSet(key ?: property.name, null)
            stringSet = PrefMutableSet(thisRef, prefSet ?: default.invoke().toMutableSet(), key ?: property.name)
            lastUpdate = System.currentTimeMillis()
        }
        return stringSet!!
    }

    internal inner class PrefMutableSet(val kotprefModel: SecurePrefModel, val set: MutableSet<String>, val key: String) : MutableSet<String> by set {

        init {
            addAll(set)
        }

        private var transactionData: MutableSet<String>? = null
            get() {
                field = field ?: set.toMutableSet()
                return field
            }

        internal fun syncTransaction() {
            synchronized(this) {
                transactionData?.let {
                    set.clear()
                    set.addAll(transactionData!!)
                    transactionData = null
                }
            }
        }

        override fun add(element: String): Boolean {
            if (kotprefModel.inTransaction) {
                val result = transactionData!!.add(element)
                kotprefModel.editor!!.putStringSet(key, transactionData, this)
                return result
            }
            val result = set.add(element)
            kotprefModel.preference.edit().putStringSet(key, set).apply()
            return result
        }

        override fun addAll(elements: Collection<String>): Boolean {
            if (kotprefModel.inTransaction) {
                val result = transactionData!!.addAll(elements)
                kotprefModel.editor!!.putStringSet(key, transactionData, this)
                return result
            }
            val result = set.addAll(elements)
            kotprefModel.preference.edit().putStringSet(key, set).apply()
            return result
        }

        override fun remove(element: String): Boolean {
            if (kotprefModel.inTransaction) {
                val result = transactionData!!.remove(element)
                kotprefModel.editor!!.putStringSet(key, transactionData, this)
                return result
            }
            val result = set.remove(element)
            kotprefModel.preference.edit().putStringSet(key, set).apply()
            return result
        }

        override fun removeAll(elements: Collection<String>): Boolean {
            if (kotprefModel.inTransaction) {
                val result = transactionData!!.removeAll(elements)
                kotprefModel.editor!!.putStringSet(key, transactionData, this)
                return result
            }
            val result = set.removeAll(elements)
            kotprefModel.preference.edit().putStringSet(key, set).apply()
            return result
        }

        override fun retainAll(elements: Collection<String>): Boolean {
            if (kotprefModel.inTransaction) {
                val result = transactionData!!.retainAll(elements)
                kotprefModel.editor!!.putStringSet(key, transactionData, this)
                return result
            }
            val result = set.retainAll(elements)
            kotprefModel.preference.edit().putStringSet(key, set).apply()
            return result
        }

        override fun clear() {
            if (kotprefModel.inTransaction) {
                val result = transactionData!!.clear()
                kotprefModel.editor!!.putStringSet(key, transactionData, this)
                return result
            }
            set.clear()
            kotprefModel.preference.edit().putStringSet(key, set).apply()
        }

        override fun contains(element: String): Boolean {
            if (kotprefModel.inTransaction) {
                return element in transactionData!!
            }
            return element in set
        }

        override fun containsAll(elements: Collection<String>): Boolean {
            if (kotprefModel.inTransaction) {
                return transactionData!!.containsAll(elements)
            }
            return set.containsAll(elements)
        }

        override fun iterator(): MutableIterator<String> {
            if (kotprefModel.inTransaction) {
                return transactionData!!.iterator()
            }
            return set.iterator()
        }

        override val size: Int
            get() {
                if (kotprefModel.inTransaction) {
                    return transactionData!!.size
                }
                return set.size
            }
    }
}
