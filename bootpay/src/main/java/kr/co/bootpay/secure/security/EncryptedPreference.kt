package kr.co.bootpay.secure.security

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Base64
import java.security.MessageDigest

class EncryptedPreference(context: Context, private val keys: SecretKeys) : SharedPreferences {

    companion object {
        private const val HASH_DIGEST = "SHA-512"
    }

    internal val pref: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun hash(key: String?): String? = key?.let {
        val digest = MessageDigest.getInstance(HASH_DIGEST).apply { update(key.toByteArray()) }
        Base64.encodeToString(digest.digest(), 2)
    }

    private fun encrypt(text: String): String =
            if (text.isEmpty()) text
            else AesCbc.encrypt(text, keys).toString()

    private fun decrypt(text: String): String =
            if (text.isEmpty()) text
            else AesCbc.decryptString(text, keys)

    override fun contains(key: String?): Boolean = pref.contains(hash(key))

    override fun registerOnSharedPreferenceChangeListener(l: SharedPreferences.OnSharedPreferenceChangeListener?) {
        pref.registerOnSharedPreferenceChangeListener(l)
    }

    override fun unregisterOnSharedPreferenceChangeListener(l: SharedPreferences.OnSharedPreferenceChangeListener?) {
        pref.unregisterOnSharedPreferenceChangeListener(l)
    }

    override fun getAll(): MutableMap<String, *> = pref.all.entries
            .filter { it.value is String }
            .filter { it.key != it.value as String }
            .map { it.key to decrypt(it.value as String) }
            .toMap()
            .toMutableMap()

    override fun edit(): Editor = Editor()

    override fun getBoolean(key: String?, default: Boolean): Boolean = getValue(key)?.toBoolean() ?: default
    override fun getInt(key: String?, default: Int): Int = getValue(key)?.toIntOrNull() ?: default
    override fun getLong(key: String?, default: Long): Long = getValue(key)?.toLongOrNull() ?: default
    override fun getFloat(key: String?, default: Float): Float = getValue(key)?.toFloatOrNull() ?: default
    override fun getString(key: String?, default: String?): String = getValue(key) ?: default ?: ""
    override fun getStringSet(key: String?, default: MutableSet<String>?): MutableSet<String> =
            pref.getStringSet(key, default)?.map { decrypt(it) }?.toHashSet() ?: default ?: mutableSetOf()

    private fun getValue(key: String?): String? =
            pref.getString(hash(key), null)?.let { decrypt(it) }

    inner class Editor : SharedPreferences.Editor {

        private val editor: SharedPreferences.Editor = pref.edit()

        override fun clear(): SharedPreferences.Editor {
            editor.clear()
            return this
        }

        override fun putLong(key: String?, value: Long): SharedPreferences.Editor {
//            putValue(key, value)
            editor.putString(hash(key), encrypt("$value"))
            return this
        }

        override fun putInt(key: String?, value: Int): SharedPreferences.Editor {
//            putValue(key, value)
            editor.putString(hash(key), encrypt("$value"))
            return this

        }

        override fun remove(key: String?): SharedPreferences.Editor {
            editor.remove(hash(key))
            return this
        }

        override fun putBoolean(key: String?, value: Boolean): SharedPreferences.Editor {
            editor.putString(hash(key), encrypt("$value"))
            return this
        }

        override fun putStringSet(key: String?, values: MutableSet<String>?): SharedPreferences.Editor {
            editor.putStringSet(hash(key), values?.map { encrypt(it) }?.toHashSet())
            return this
        }

        override fun commit(): Boolean = editor.commit()

        override fun putFloat(key: String?, value: Float): SharedPreferences.Editor {
            editor.putString(hash(key), encrypt("$value"))
            return this
        }

        override fun apply() {
            editor.apply()
        }

        override fun putString(key: String?, value: String?): SharedPreferences.Editor {
            editor.putString(hash(key), encrypt(value ?: ""))
            return this
        }

        fun <T> putValue(key: String?, value: T) {
            editor.putString(hash("$value"), encrypt("$value"))
        }
    }

}