package kr.co.bootpay

@FunctionalInterface
interface ErrorListener {
    fun onError(message: String?)
}
