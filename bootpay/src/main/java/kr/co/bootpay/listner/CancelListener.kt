package kr.co.bootpay.listner

@FunctionalInterface
interface CancelListener {
    fun onCancel(message: String?)
}
