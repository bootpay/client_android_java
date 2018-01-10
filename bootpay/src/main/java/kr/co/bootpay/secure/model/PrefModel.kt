package kr.co.bootpay.secure.model

import android.provider.Settings
import bootpay.co.kr.securepref.type.IntPref
import kr.co.bootpay.secure.SecurePreference
import kr.co.bootpay.secure.security.AesCbc
import kr.co.bootpay.secure.security.EncryptedPreference
import kr.co.bootpay.secure.security.PrefInject
import kr.co.bootpay.secure.type.*
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicLong
import kotlin.properties.ReadWriteProperty

abstract class PrefModel {

    private val inTransaction: AtomicBoolean = AtomicBoolean(false)
    private val update: AtomicLong = AtomicLong(System.nanoTime())

    private val securePref = PrefInject(EncryptedPreference(SecurePreference.context, AesCbc.generateKey(SecurePreference.context.packageName, Settings.Secure.getString(SecurePreference.context.contentResolver, Settings.Secure.ANDROID_ID).toByteArray())))

    internal val editor = securePref.preferences.edit()

    internal val pref: EncryptedPreference
        get() = securePref.preferences

    internal fun inTransaction() = inTransaction.get()

    internal fun lastUpdate() = update.get()

    protected fun <T> pref(default: T): ReadWriteProperty<PrefModel, T> = when (default) {
        is String  -> StringPref(default)
        is String? -> NullableStringPref(default)
        is Int     -> IntPref(default)
        is Long    -> LongPref(default)
        is Float   -> FloatPref(default)
        is Boolean -> BooleanPref(default)
        else       -> throw IllegalArgumentException("Unsupported type")

    } as ReadWriteProperty<PrefModel, T>

    protected fun stringPref(default: String = "")
            : ReadWriteProperty<PrefModel, String> = StringPref(default)

    protected fun nullableStringPref(default: String? = null)
            : ReadWriteProperty<PrefModel, String?> = NullableStringPref(default)

    protected fun intPref(default: Int = 0)
            : ReadWriteProperty<PrefModel, Int> = IntPref(default)

    protected fun longPref(default: Long = 0L)
            : ReadWriteProperty<PrefModel, Long> = LongPref(default)

    protected fun floatPref(default: Float = 0f)
            : ReadWriteProperty<PrefModel, Float> = FloatPref(default)

    protected fun booleanPref(default: Boolean = false)
            : ReadWriteProperty<PrefModel, Boolean> = BooleanPref(default)

    protected fun clear() {
        beginBulkEdit()
        editor.clear()
        commitBulkEdit()
    }

    fun commitBulkEdit() {
        inTransaction.set(false)
        editor.apply()
        update.set(System.nanoTime())
    }

    fun beginBulkEdit() {
        inTransaction.set(true)
        update.set(System.nanoTime())
    }

    fun blockingCommitBulkEdit() {
        editor.commit()
        inTransaction.set(false)
    }

    fun cancelBulkEdit() {
        inTransaction.set(false)
    }
}