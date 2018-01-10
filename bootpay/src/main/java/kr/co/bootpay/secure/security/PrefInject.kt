package kr.co.bootpay.secure.security

import android.content.SharedPreferences
import android.os.Build
import android.support.annotation.RequiresApi
import kr.co.bootpay.secure.type.StringSetPref
import java.util.*

internal class PrefInject(internal val preferences: EncryptedPreference) : SharedPreferences by preferences {

    override fun edit(): SharedPreferences.Editor = Editor(preferences.edit())

    internal inner class Editor(val editor: EncryptedPreference.Editor) : SharedPreferences.Editor by editor {
        private val prefSet: MutableMap<String, StringSetPref.PrefSet> by lazy { HashMap<String, StringSetPref.PrefSet>() }

        override fun apply() {
            syncTransaction()
            editor.apply()
        }

        override fun commit(): Boolean {
            syncTransaction()
            return editor.commit()
        }

        @RequiresApi(Build.VERSION_CODES.HONEYCOMB)
        internal fun putStringSet(key: String, values: MutableSet<String>?, pref: StringSetPref.PrefSet): SharedPreferences.Editor {
            prefSet.put(key, pref)
            return this
        }

        @RequiresApi(Build.VERSION_CODES.HONEYCOMB)
        fun syncTransaction() {
            prefSet.keys.forEach { key ->
                prefSet[key]?.let {
                    editor.putStringSet(key, it)
                    it.syncTransaction()
                }
            }
            prefSet.clear()
        }
    }
}