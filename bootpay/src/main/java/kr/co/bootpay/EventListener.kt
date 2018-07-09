package kr.co.bootpay

interface EventListener : CancelListener, ReadyListener, ConfirmListener, ErrorListener, DoneListener, CloseListener {
    override fun onError(message: String?)

    override fun onCancel(message: String?)

    override fun onClose(message: String?)

    override fun onReady(message: String?)

    override fun onConfirm(message: String?)

    override fun onDone(message: String?)
}
