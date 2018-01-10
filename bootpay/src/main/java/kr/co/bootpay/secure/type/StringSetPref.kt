package kr.co.bootpay.secure.type

import kr.co.bootpay.secure.model.PrefModel
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.locks.ReentrantLock
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

internal class StringSetPref(private val key: String) : ReadOnlyProperty<PrefModel, MutableSet<String>> {

    private var tempSet: MutableSet<String>? = null
    private val update = AtomicLong(0L)

    operator override fun getValue(model: PrefModel, property: KProperty<*>): MutableSet<String> {
        if (tempSet == null || update.get() < model.lastUpdate()) {
            model.pref.getStringSet(key, null).takeIf { it.isNotEmpty() }?.let {
                tempSet = PrefSet(model, it, key)
                update.set(System.currentTimeMillis())
            }
        }
        return tempSet ?: mutableSetOf()
    }

    internal inner class PrefSet(
            private val model: PrefModel,
            private val prefSet: MutableSet<String>,
            private val key: String) : MutableSet<String> by prefSet {
        private val lock = ReentrantLock(true)
        private val transaction: MutableSet<String> = prefSet.toMutableSet()

        init {
            addAll(prefSet)
        }

        internal fun syncTransaction() {
            if (lock.tryLock()) {
                prefSet.clear()
                prefSet.addAll(transaction)

            }
        }

        override fun add(element: String): Boolean =
                if (model.inTransaction())
                    transaction.add(element).also {
                        model.editor.putStringSet(key, transaction)
                    }
                else // directly writable
                    prefSet.add(element).also {
                        model.pref.edit().putStringSet(key, prefSet).apply()
                    }

        override fun addAll(elements: Collection<String>): Boolean =
                if (model.inTransaction())
                    transaction.addAll(elements).also {
                        model.editor.putStringSet(key, transaction)
                    }
                else // directly writable
                    prefSet.addAll(elements).also {
                        model.pref.edit().putStringSet(key, prefSet).apply()
                    }

        override fun remove(element: String): Boolean =
                if (model.inTransaction())
                    transaction.remove(element).also {
                        model.editor.putStringSet(key, transaction)
                    }
                else // directly writable
                    prefSet.remove(element).also {
                        model.pref.edit().putStringSet(key, prefSet).apply()
                    }

        override fun removeAll(elements: Collection<String>): Boolean =
                if (model.inTransaction())
                    transaction.removeAll(elements).also {
                        model.editor.putStringSet(key, transaction)
                    }
                else // directly writable
                    prefSet.removeAll(elements).also {
                        model.pref.edit().putStringSet(key, prefSet).apply()
                    }

        override fun clear() {
            if (model.inTransaction())
                transaction.clear().also {
                    model.editor.putStringSet(key, transaction)
                }
            else
                prefSet.clear().also {
                    model.pref.edit().putStringSet(key, prefSet).apply()
                }
        }

        override fun contains(element: String): Boolean =
                if (model.inTransaction()) transaction.contains(element)
                else prefSet.contains(element)

        override fun containsAll(elements: Collection<String>): Boolean =
                if (model.inTransaction()) transaction.containsAll(elements)
                else prefSet.containsAll(elements)

        override fun iterator(): MutableIterator<String> =
                if (model.inTransaction()) transaction.iterator()
                else prefSet.iterator()

        override val size: Int
            get() =
                if (model.inTransaction()) transaction.size
                else prefSet.size
    }
}