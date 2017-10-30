package kr.co.bootpay

@FunctionalInterface
interface ConfirmListener {
    fun onConfirmed(message: String?)
}
