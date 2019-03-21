package kr.co.bootpay.listner

@FunctionalInterface
interface CloseListener {
    fun onClose(message: String?)
}
