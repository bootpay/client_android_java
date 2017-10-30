package kr.co.bootpay.pref

import java.util.*

object UserInfo : SecurePrefModel() {
    var bootpay_uuid by StringPref(UUID.randomUUID().toString())
    var bootpay_last_time by longPref(System.currentTimeMillis())
    var bootpay_sk by StringPref("$bootpay_uuid$bootpay_last_time")
    var bootpay_application_id by StringPref("")
    var bootpay_user_id by StringPref("")

    fun update() {
        System.currentTimeMillis().let { time ->
            if (time isExpired bootpay_last_time) bootpay_sk = newSk(time)
            val returnTime = (time - bootpay_last_time) / 1000L
            bootpay_last_time = time
        }
    }

    fun finish() {
        bootpay_last_time = System.currentTimeMillis()
    }

    private infix fun Long.isExpired(time: Long) = this - time > 30 * 60 * 1000L

    private fun newSk(time: Long) = "$bootpay_uuid$time"
}