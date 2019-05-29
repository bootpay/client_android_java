package kr.co.bootpay.pref

import java.util.*


//internal object UserInfoKotlin: PrefModel() {
//    var bootpay_uuid by stringPref()
//    var bootpay_last_time by longPref(System.currentTimeMillis())
//    var bootpay_sk by stringPref()
//    var bootpay_application_id by stringPref()
//    var bootpay_user_id by stringPref()
//    var bootpay_receipt_id by stringPref()
//    var developerPayload by stringPref()
//
//    var enable_onstore by booleanPref()
//    var sim_operator by stringPref()
//    var install_package_market by stringPref()
//    var ad_id by stringPref()
//
//    @JvmStatic
//    fun setEnableOneStore(value: Boolean) {
//        enable_onstore = value
//    }
//
//    @JvmStatic
//    fun setSimOperator(value: String) {
//        sim_operator = value
//    }
//
//    @JvmStatic
//    fun setInstallPackageMarket(value: String) {
//        install_package_market = value
//    }
//
//    @JvmStatic
//    fun setAdId(value: String) {
//        ad_id = value
//    }
//
//
//    @JvmStatic
//    fun update() {
//        if (bootpay_uuid.isEmpty()) bootpay_uuid = UUID.randomUUID().toString()
//        if (bootpay_sk.isEmpty()) bootpay_sk = "${bootpay_uuid}_$bootpay_last_time"
//        System.currentTimeMillis().let { time ->
//            if (time isExpired bootpay_last_time) bootpay_sk = newSk(time)
//            val returnTime = (time - bootpay_last_time) / 1000L
//            bootpay_last_time = time
//        }
//    }
//
////    @JvmStatic
////    fun
//
//    @JvmStatic
//    fun finish() {
//        bootpay_last_time = System.currentTimeMillis()
//    }
//
//    private infix fun Long.isExpired(time: Long) = this - time > 30 * 60 * 1000L
//
//    private fun newSk(time: Long) = "${bootpay_uuid}_$time"
//}