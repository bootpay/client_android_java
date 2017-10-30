package kr.co.bootpay

@FunctionalInterface
interface DoneListener {
    fun onDone(message: String?)
}
