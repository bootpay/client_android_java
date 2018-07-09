package kr.co.bootpay

@FunctionalInterface
interface ReadyListener {
    fun onReady(message: String?)
}
