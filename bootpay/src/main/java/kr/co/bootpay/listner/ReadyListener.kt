package kr.co.bootpay.listner

@FunctionalInterface
interface ReadyListener {
    fun onReady(message: String?)
}
