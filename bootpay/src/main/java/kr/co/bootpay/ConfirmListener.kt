package kr.co.bootpay

@FunctionalInterface
interface ConfirmListener {
    fun onConfirm(message: String?)
}
