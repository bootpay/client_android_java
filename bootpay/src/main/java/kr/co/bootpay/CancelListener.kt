package kr.co.bootpay

@FunctionalInterface
interface CancelListener {
    fun onCancel(message: String?)
}
