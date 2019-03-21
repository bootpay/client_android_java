package kr.co.bootpay.listner

@FunctionalInterface
interface ConfirmListener {
    fun onConfirm(message: String?)
}
