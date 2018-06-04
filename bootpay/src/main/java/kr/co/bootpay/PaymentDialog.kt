package kr.co.bootpay

import android.app.DialogFragment
import android.app.FragmentManager
import android.content.DialogInterface
import android.os.Bundle
import android.support.annotation.IntRange
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kr.co.bootpay.PaymentDialog.Builder
import kr.co.bootpay.enums.Method
import kr.co.bootpay.enums.PG
import kr.co.bootpay.model.Item
import kr.co.bootpay.model.Request
import kr.co.bootpay.pref.UserInfo
import org.json.JSONObject

class PaymentDialog
/** @see Builder */
@Deprecated("") constructor() // not allowed
    : DialogFragment() {

    private var result: Request? = null
    private var bootpay: BootpayWebView? = null
    private var listener: EventListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme_Holo_Light)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        bootpay = BootpayWebView(inflater.context)
        afterViewInit()
        return bootpay
    }

    private fun setOnResponseListener(listener: EventListener): PaymentDialog {
        this.listener = listener
        return this
    }

    private fun afterViewInit() {
        bootpay?.setData(result)?.setDialog(dialog)?.setOnResponseListener(listener)
    }

    private fun setData(request: Request?): PaymentDialog {
        result = request
        return this
    }

    override fun onDestroyView() {
        UserInfo.update()
        super.onDestroyView()
    }

    private fun transactionConfirm(data: String?) {
        bootpay?.transactionConfirm(data)
    }

    class Builder {
        private var fm: FragmentManager? = null
        private var result: Request = Request()
        private var listener: EventListener? = null
        private var error: ErrorListener? = null
        private var done: DoneListener? = null
        private var cancel: CancelListener? = null
        private var confirm: ConfirmListener? = null
        private var dialog: PaymentDialog? = null

        private constructor() {
            // not allowed
        }

        constructor(fm: FragmentManager?) {
            this.fm = fm
        }

        fun setApplicationId(id: String): Builder {
            result.application_id = id
            return this
        }

        fun setPrice(price: Long): Builder {
            result.price = price
            return this
        }

        fun setPG(pg: String): Builder {
            result.pg = pg
            return this
        }

        fun setPG(pg: PG): Builder {
            result.pg = when (pg) {
                PG.BOOTPAY -> "bootpay"
                PG.PAYAPP  -> "payapp"
                PG.DANAL   -> "danal"
                PG.KCP     -> "kcp"
                PG.INICIS  -> "inicis"
                PG.LGUP  -> "lgup"
                PG.KAKAO  -> "kakao"
                PG.JTNET  -> "jtnet"
                PG.NICEPAY  -> "nicepay"
                PG.PAYCO  -> "payco"
            }
            return this
        }

        fun setName(name: String): Builder {
            result.name = name
            return this
        }

        fun addItem(name: String, @IntRange(from = 1) quantity: Int, primaryKey: String, price: Long): Builder {
            result.addItem(Item(name, quantity, primaryKey, price, "", "", ""))
            return this
        }

        fun addItem(name: String, @IntRange(from = 1) quantity: Int, primaryKey: String, price: Long, cat1: String, cat2: String, cat3: String): Builder {
            result.addItem(Item(name, quantity, primaryKey, price, cat1, cat2, cat3))
            return this
        }

        fun addItem(item: Item): Builder {
            result.addItem(item)
            return this
        }

        fun isShowAgree(show: Boolean): Builder {
            result.isShowAgree = show
            return this
        }

        fun addItems(items: Collection<Item>?): Builder {
            if (items != null) for (i in items) addItem(i)
            return this
        }

        fun setItems(items: MutableList<Item>): Builder {
            result.items = items
            return this
        }

        fun setOrderId(orderId: String): Builder {
            result.order_id = orderId
            return this
        }

        fun setMethod(method: String): Builder {
            result.method = method
            return this
        }

        fun setModel(request: Request): Builder {
            result = request
            return this
        }

        fun setMethod(method: Method): Builder {
            result.method = when (method) {
                Method.CARD        -> "card"
                Method.CARD_SIMPLE -> "card_simple"
                Method.BANK        -> "bank"
                Method.VBANK       -> "vbank"
                Method.PHONE       -> "phone"
                Method.AUTH        -> "auth"
                Method.CARD_REBILL -> "card_rebill"
                Method.EASY        -> "easy"
                Method.SELECT      -> ""
            }
            return this
        }

        fun setEventListener(listener: EventListener?): Builder {
            this.listener = listener
            return this
        }

        fun setParams(params: Any): Builder {
            result.setParams(params)
            return this
        }

        fun setParams(params: String): Builder {
            result.params = params
            return this
        }

        fun setParams(params: JSONObject): Builder {
            result.setParams(params)
            return this
        }

        fun onCancel(listener: CancelListener): Builder {
            cancel = listener
            return this
        }

        fun onConfirm(listener: ConfirmListener): Builder {
            confirm = listener
            return this
        }

        fun onDone(listener: DoneListener): Builder {
            done = listener
            return this
        }

        fun onError(listener: ErrorListener): Builder {
            error = listener
            return this
        }

        fun setUserEmail(email: String): Builder {
            result.userEmail = email
            return this
        }

        fun setUserName(name: String): Builder {
            result.userName = name
            return this
        }

        fun setUserAddr(address: String): Builder {
            result.userAddr = address
            return this
        }

        fun setUserPhone(number: String): Builder {
            result.userPhone = number
            return this
        }

        /**
         * Must have value:
         *
         * @see Request.application_id { @link https://alf001.bomgil.in/project/app }
         *
         * @see Request.pg { @value "bootpay", "payapp", "danal", "kcp", "inicis" }
         *
         * @see Request.price
         *
         * @see Request.orderId
         */
        fun show() {

            if (result.application_id.isEmpty()) error("Application id is not configured.")

            if (result.pg.isEmpty()) error("PG is not configured.")

            if (result.price < 0) error("Price is not configured.")

            if (result.order_id.isEmpty()) error("Order id is not configured.")

            if (listener == null && (error == null || cancel == null || confirm == null || done == null)) error("Must to be required to handle events.")

            dialog = PaymentDialog().setData(result)
                //                    .setTrace(trace)
                //                    .setUserInfo(user)
                .setOnResponseListener(listener ?: object: EventListener {
                    override fun onError(message: String?) {
                        error?.onError(message)
                    }

                    override fun onCancel(message: String?) {
                        cancel?.onCancel(message)
                    }

                    override fun onConfirmed(message: String?) {
                        confirm?.onConfirmed(message)
                    }

                    override fun onDone(message: String?) {
                        done?.onDone(message)
                    }
                })
            dialog?.onCancel(object: DialogInterface {
                override fun cancel() {
                    dialog?.bootpay?.destroy()
                    dialog = null
                    UserInfo.finish()
                    Bootpay.finish()

                }

                override fun dismiss() {
                    dialog?.bootpay?.destroy()
                    dialog = null
                    UserInfo.finish()
                    Bootpay.finish()
                }
            })
            UserInfo.update()
            if (fm?.isDestroyed == false) dialog?.show(fm, "bootpay")
        }

        fun transactionConfirm(data: String?) {
            dialog?.transactionConfirm(data)
        }

        private fun error(message: String?) {
            throw RuntimeException(message)
        }
    }

}
