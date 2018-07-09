package kr.co.bootpay

@FunctionalInterface
interface CloseListener {
    fun onClose(message: String?)
}
