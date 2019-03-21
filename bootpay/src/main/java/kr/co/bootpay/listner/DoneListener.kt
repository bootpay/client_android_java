package kr.co.bootpay.listner

@FunctionalInterface
interface DoneListener {
    fun onDone(message: String?)
}
