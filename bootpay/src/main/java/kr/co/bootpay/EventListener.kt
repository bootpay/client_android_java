package kr.co.bootpay

interface EventListener : CancelListener, ConfirmListener, ErrorListener, DoneListener {
    override fun onError(message: String?)

    override fun onCancel(message: String?)

    override fun onConfirmed(message: String?)

    override fun onDone(message: String?)
}
