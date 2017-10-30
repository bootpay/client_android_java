package kr.co.bootpay.pref

import android.annotation.TargetApi
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.preference.PreferenceManager
import android.provider.Settings
import android.util.Base64
import android.util.Log
import com.tozny.crypto.android.AesCbcWithIntegrity
import java.security.GeneralSecurityException
import java.security.MessageDigest

class SecurePreferences : SharedPreferences {

    private val sharedPreferences: SharedPreferences

    private val keys: AesCbcWithIntegrity.SecretKeys

    companion object {
        @Throws(GeneralSecurityException::class)
        fun generateAesKeyName(context: Context): String {
            val password = context.packageName
            val salt = getDeviceSerialNumber(context).toByteArray()
            val generatedKeyName = AesCbcWithIntegrity.generateKeyFromPassword(password, salt) ?: throw GeneralSecurityException("Key not generated")
            return hashPrefKey(generatedKeyName.toString())
        }

        private fun getDeviceSerialNumber(context: Context): String {
            return try {
                val deviceSerial = Build::class.java.getField("SERIAL").get(null) as String
                when (deviceSerial.isEmpty()) {
                    true  -> Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
                    false -> deviceSerial
                }
            } catch (ignored: Exception) {
                Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
            }
        }

        fun hashPrefKey(prefKey: String): String {
            val digest = MessageDigest.getInstance("SHA-256")
            val bytes = prefKey.toByteArray()
            digest.update(bytes, 0, bytes.size)
            return Base64.encodeToString(digest.digest(), AesCbcWithIntegrity.BASE64_FLAGS)
        }
    }

    constructor(context: Context) : this(context, "", "")

    constructor(context: Context, password: String, sharedPrefFilename: String) : this(context, null, password, sharedPrefFilename)

    constructor(context: Context, secretKey: AesCbcWithIntegrity.SecretKeys?, sharedPrefFilename: String) : this(context, secretKey, "", sharedPrefFilename)

    private constructor(context: Context, secretKey: AesCbcWithIntegrity.SecretKeys?, password: String, sharedPrefFilename: String) {
        sharedPreferences = getSharedPreferenceFile(context, sharedPrefFilename)
        keys = when {
            secretKey != null  -> secretKey
            password.isEmpty() -> {
                val key = generateAesKeyName(context)
                val ks = sharedPreferences.getString(key, null)
                when (ks.isNullOrEmpty()) {
                    true  -> AesCbcWithIntegrity.generateKey()
                    false -> AesCbcWithIntegrity.keys(ks)
                }
            }
            else               -> {
                val salt = getDeviceSerialNumber(context).toByteArray()
                AesCbcWithIntegrity.generateKeyFromPassword(password, salt)
            }
        }
    }


    private fun getSharedPreferenceFile(context: Context, prefFilename: String): SharedPreferences =
            when (prefFilename.isEmpty()) {
                true  -> PreferenceManager.getDefaultSharedPreferences(context)
                false -> context.getSharedPreferences(prefFilename, Context.MODE_PRIVATE)
            }

    private fun encrypt(cleartext: String?): String = when (cleartext.isNullOrEmpty()) {
        true -> cleartext ?: ""
        else -> AesCbcWithIntegrity.encrypt(cleartext, keys).toString()
    }

    private fun decrypt(ciphertext: String?): String = when (ciphertext.isNullOrEmpty()) {
        true -> ciphertext ?: ""
        else -> AesCbcWithIntegrity.decryptString(AesCbcWithIntegrity.CipherTextIvMac(ciphertext), keys)
    }

    override fun getAll(): Map<String, String> {
        val encryptedMap = sharedPreferences.all
        val decryptedMap = HashMap<String, String>(encryptedMap.size)
        encryptedMap.entries.forEach { entry ->
            try {
                val cipherText = entry.value as? String
                cipherText?.let { if (it != entry.key) decryptedMap.put(entry.key, decrypt(cipherText)) }
            } catch (ex: Exception) {
                decryptedMap.put(entry.key, entry.value as String)
            }
        }
        return decryptedMap
    }

    override fun getString(key: String, defaultValue: String): String {
        val encryptedValue = sharedPreferences.getString(hashPrefKey(key), null)
        return encryptedValue?.let { decrypt(encryptedValue) } ?: defaultValue
    }

    fun getUnencryptedString(key: String, defaultValue: String): String {
        val nonEncryptedValue = sharedPreferences.getString(hashPrefKey(key), null)
        return nonEncryptedValue ?: defaultValue
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    override fun getStringSet(key: String, defaultValues: Set<String>): Set<String> {
        val encryptedSet = sharedPreferences.getStringSet(hashPrefKey(key), null) ?: return defaultValues
        return encryptedSet.map { decrypt(it) }.toHashSet()
    }

    override fun getInt(key: String, defaultValue: Int): Int {
        val encryptedValue = sharedPreferences.getString(hashPrefKey(key), null) ?: return defaultValue
        return decrypt(encryptedValue).toInt()
    }

    override fun getLong(key: String, defaultValue: Long): Long {
        val encryptedValue = sharedPreferences.getString(hashPrefKey(key), null) ?: return defaultValue
        return decrypt(encryptedValue).toLong()
    }

    override fun getFloat(key: String, defaultValue: Float): Float {
        val encryptedValue = sharedPreferences.getString(hashPrefKey(key), null) ?: return defaultValue
        return decrypt(encryptedValue).toFloat()
    }

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        val encryptedValue = sharedPreferences.getString(hashPrefKey(key), null) ?: return defaultValue
        return decrypt(encryptedValue).toBoolean()
    }

    override fun contains(key: String): Boolean = sharedPreferences.contains(hashPrefKey(key))

    override fun edit(): SharedPreferences.Editor {
        return object : SharedPreferences.Editor {
            val mEditor: SharedPreferences.Editor = sharedPreferences.edit()

            override fun putString(key: String, value: String): SharedPreferences.Editor {
                mEditor.putString(hashPrefKey(key), encrypt(value))
                return this
            }

            fun putUnencryptedString(key: String, value: String): SharedPreferences.Editor {
                mEditor.putString(hashPrefKey(key), value)
                return this
            }

            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            override fun putStringSet(key: String, values: Set<String>): SharedPreferences.Editor {
                val encryptedValues = values.map { encrypt(it) }.toHashSet()
                mEditor.putStringSet(hashPrefKey(key), encryptedValues)
                return this
            }

            override fun putInt(key: String, value: Int): SharedPreferences.Editor {
                mEditor.putString(hashPrefKey(key), encrypt(Integer.toString(value)))
                return this
            }

            override fun putLong(key: String, value: Long): SharedPreferences.Editor {
                mEditor.putString(hashPrefKey(key), encrypt(value.toString()))
                return this
            }

            override fun putFloat(key: String, value: Float): SharedPreferences.Editor {
                mEditor.putString(hashPrefKey(key), encrypt(value.toString()))
                return this
            }

            override fun putBoolean(key: String, value: Boolean): SharedPreferences.Editor {
                mEditor.putString(hashPrefKey(key), encrypt(value.toString()))
                return this;
            }

            override fun remove(key: String): SharedPreferences.Editor {
                mEditor.remove(hashPrefKey(key))
                return this
            }

            override fun clear(): SharedPreferences.Editor {
                mEditor.clear()
                return this
            }

            override fun commit(): Boolean = mEditor.commit()

            @TargetApi(Build.VERSION_CODES.GINGERBREAD)
            override fun apply() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) mEditor.apply()
                else commit()
            }
        }
    }

    override fun registerOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
    }

    override fun unregisterOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
    }
}