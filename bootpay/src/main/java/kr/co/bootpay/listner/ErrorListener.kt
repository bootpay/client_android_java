package kr.co.bootpay.listner

@FunctionalInterface
interface ErrorListener {
    fun onError(message: String?)
}
