package kr.co.bootpay.pref

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import java.util.*

abstract class SecurePrefModel {

    internal var inTransaction: Boolean = false
    internal var transactionStartTime: Long = 0

    val context: Context by lazy { SecurePref.context }

    internal val preference: SecurePrefs by lazy {
        SecurePrefs(SecurePreferences(context))
    }

    internal var editor: SecurePrefs.SecureprefEditor? = null

    fun clear() {
        beginBulkEdit()
        editor?.clear()
        commitBulkEdit()
    }

    protected fun stringPref(default: String = "", key: String? = null)
            : ReadWriteProperty<SecurePrefModel, String> = StringPref(default, key)

    protected fun stringPref(default: String = "", key: Int)
            : ReadWriteProperty<SecurePrefModel, String> = StringPref(default, context.getString(key))

    protected fun nullableStringPref(default: String? = null, key: String? = null)
            : ReadWriteProperty<SecurePrefModel, String?> = StringNullablePref(default, key)

    protected fun nullableStringPref(default: String? = null, key: Int)
            : ReadWriteProperty<SecurePrefModel, String?> = StringNullablePref(default, context.getString(key))

    protected fun intPref(default: Int = 0, key: String? = null)
            : ReadWriteProperty<SecurePrefModel, Int> = IntPref(default, key)

    protected fun intPref(default: Int = 0, key: Int)
            : ReadWriteProperty<SecurePrefModel, Int> = IntPref(default, context.getString(key))

    protected fun longPref(default: Long = 0L, key: String? = null)
            : ReadWriteProperty<SecurePrefModel, Long> = LongPref(default, key)

    protected fun longPref(default: Long = 0L, key: Int)
            : ReadWriteProperty<SecurePrefModel, Long> = LongPref(default, context.getString(key))

    protected fun floatPref(default: Float = 0F, key: String? = null)
            : ReadWriteProperty<SecurePrefModel, Float> = FloatPref(default, key)

    protected fun floatPref(default: Float = 0F, key: Int)
            : ReadWriteProperty<SecurePrefModel, Float> = FloatPref(default, context.getString(key))

    protected fun booleanPref(default: Boolean = false, key: String? = null)
            : ReadWriteProperty<SecurePrefModel, Boolean> = BooleanPref(default, key)

    protected fun booleanPref(default: Boolean = false, key: Int)
            : ReadWriteProperty<SecurePrefModel, Boolean> = BooleanPref(default, context.getString(key))

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    protected fun stringSetPref(default: Set<String> = LinkedHashSet<String>(), key: String? = null)
            : ReadOnlyProperty<SecurePrefModel, MutableSet<String>> = StringSetPref({ default }, key)

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    protected fun stringSetPref(default: Set<String> = LinkedHashSet<String>(), key: Int)
            : ReadOnlyProperty<SecurePrefModel, MutableSet<String>> = StringSetPref({ default }, context.getString(key))

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    protected fun stringSetPref(key: String? = null, default: () -> Set<String>)
            : ReadOnlyProperty<SecurePrefModel, MutableSet<String>> = StringSetPref(default, key)

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    protected fun stringSetPref(key: Int, default: () -> Set<String>)
            : ReadOnlyProperty<SecurePrefModel, MutableSet<String>> = StringSetPref(default, context.getString(key))

    @SuppressLint("SharedPreference")
    private fun beginBulkEdit() {
        inTransaction = true
        transactionStartTime = System.currentTimeMillis()
        editor = preference.SecureprefEditor(preference.edit())
    }

    private fun commitBulkEdit() {
        editor?.apply()
        inTransaction = false
    }

    private fun blockingCommitBulkEdit() {
        editor?.commit()
        inTransaction = false
    }

    private fun cancelBulkEdit() {
        editor = null
        inTransaction = false
    }
}
