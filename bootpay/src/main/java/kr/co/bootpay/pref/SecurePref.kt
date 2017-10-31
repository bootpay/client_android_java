package kr.co.bootpay.pref

import android.content.Context

object SecurePref {
    internal lateinit var context: Context
        private set

    @JvmStatic
    fun init(context: Context) {
        SecurePref.context = context.applicationContext
    }
}