package kr.co.bootpay.listner

interface EventListener : CancelListener, ErrorListener, DoneListener, CloseListener,  ReadyListener, ConfirmListener {
    override fun onError(message: String?)

    override fun onCancel(message: String?)

    override fun onClose(message: String?)

    override fun onReady(message: String?)

    override fun onConfirm(message: String?)

    override fun onDone(message: String?)
}
