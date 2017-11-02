package kr.co.bootpay

import android.app.DialogFragment
import android.app.FragmentManager
import android.content.DialogInterface
import android.os.Bundle
import android.support.annotation.IntRange
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import org.json.JSONObject

import kr.co.bootpay.enums.Method
import kr.co.bootpay.enums.PG
import kr.co.bootpay.model.Item
import kr.co.bootpay.model.Request
import kr.co.bootpay.pref.UserInfo
import java.util.*

class PaymentDialog
/** @see Builder */
@Deprecated("")
constructor()// not allowed
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
        bootpay?.setData(result)
                ?.setDialog(dialog)
                ?.setOnResponseListener(listener)
    }

    private fun setData(request: Request?): PaymentDialog {
        result = request
        return this
    }

    override fun onDestroyView() {
        UserInfo.update()
        super.onDestroyView()
    }

    //    private fun setTrace(trace: Trace?): PaymentDialog {
//        PaymentDialog.trace = trace
//        return this
//    }
//
//    private fun setUserInfo(data: UserData?): PaymentDialog {
//        PaymentDialog.user = data
//        return this
//    }

    //    override fun onConfigurationChanged(newConfig: Configuration) {
//        super.onConfigurationChanged(newConfig)
//        bootpay?.setData(result)
//    }
//
    private fun transactionConfirm(data: String?) {
        bootpay?.transactionConfirm(data)
    }

    class Builder {
        private var fm: FragmentManager? = null
        private var result: Request = Request()
        //        private var trace: Trace? = null
//        private var user: UserData? = null
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

        fun setPrice(@IntRange(from = 0) price: Int): Builder {
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
            }
            return this
        }

        fun setName(name: String): Builder {
            result.name = name
            return this
        }

        fun addItem(name: String, @IntRange(from = 1) quantity: Int, primaryKey: String, @IntRange(from = 0) price: Int): Builder {
            result.addItem(Item(name, quantity, primaryKey, price.toDouble()))
            return this
        }

        fun addItem(item: Item): Builder {
            result.addItem(item)
            return this
        }

        fun addItems(items: Collection<Item>?): Builder {
            if (items != null) for (i in items) addItem(i)
            return this
        }

        fun setItems(items: MutableList<Item>): Builder {
            result.setItems(items)
            return this
        }

        fun setOrderId(orderId: String): Builder {
            result.setOrder_id(orderId)
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

//        fun setPageType(pageType: String): Builder {
//            if (trace == null) trace = Trace()
//            trace!!.setPageType(pageType)
//            return this
//        }
//
//        fun setMainCategory(category: String): Builder {
//            if (trace == null) trace = Trace()
//            trace!!.setMainCategory(category)
//            return this
//        }
//
//        fun setMiddleCategory(category: String): Builder {
//            if (trace == null) trace = Trace()
//            trace!!.setMiddleCategory(category)
//            return this
//        }
//
//        fun setSubCategory(category: String): Builder {
//            if (trace == null) trace = Trace()
//            trace!!.setSubCategory(category)
//            return this
//        }
//
//        fun setItemImage(imageUrl: String): Builder {
//            if (trace == null) trace = Trace()
//            trace!!.setItemImage(imageUrl)
//            return this
//        }
//
//        fun setItemName(itemName: String): Builder {
//            if (trace == null) trace = Trace()
//            trace!!.setItemName(itemName)
//            return this
//        }
//
//        fun setUniqueKey(uniqueKey: String): Builder {
//            if (trace == null) trace = Trace()
//            trace!!.setItemUnique(uniqueKey)
//            return this
//        }
//
//        fun setUserID(userID: String): Builder {
//            if (user == null) user = UserData()
//            user!!.setUserID(userID)
//            return this
//        }
//
//        fun setUserName(userName: String): Builder {
//            if (user == null) user = UserData()
//            user!!.setUserName(userName)
//            return this
//        }
//
//        fun setUserBirth(userBirth: Date): Builder {
//            if (user == null) user = UserData()
//            user!!.setUserBirth(String.format(Locale.getDefault(), "%d-%d-%d", userBirth.year, userBirth.month + 1, userBirth.day))
//            return this
//        }
//
//        fun setUserBirth(@IntRange(from = 1900, to = 2100) year: Int, @IntRange(from = 1, to = 12) month: Int, @IntRange(from = 1, to = 31) day: Int): Builder {
//            if (user == null) user = UserData()
//            user!!.setUserBirth(String.format(Locale.getDefault(), "%d-%d-%d", year, month, day))
//            return this
//        }
//
//        fun setUserGender(userGender: Gender): Builder {
//            if (user == null) user = UserData()
//            user!!.setUserGender(if (userGender === Gender.MALE) 1 else 0)
//            return this
//        }
//
//        fun setUserArea(userArea: String): Builder {
//            if (user == null) user = UserData()
//            user!!.setUserArea(userArea)
//            return this
//        }
//
//        fun setUserArea(userArea: Area): Builder {
//            if (user == null) user = UserData()
//            user?.setUserArea(when (userArea) {
//                Area.SEOUL     -> "서울"
//                Area.INCHEON   -> "인천"
//                Area.DAEKU     -> "대구"
//                Area.DEAJEON   -> "대전"
//                Area.GWANGJU   -> "광주"
//                Area.BUSAN     -> "부산"
//                Area.ULSAN     -> "울산"
//                Area.GYEONGGI  -> "경기"
//                Area.GANGWON   -> "강원"
//                Area.CHUNGBUK  -> "충북"
//                Area.CHUNGNAM  -> "충남"
//                Area.JEONBUK   -> "전북"
//                Area.JEONNAM   -> "전남"
//                Area.GYEONGBUK -> "경북"
//                Area.GYEONGNAM -> "경남"
//                Area.JEJU      -> "제주"
//                Area.SEJONG    -> "세종"
//            })
//            return this
//        }

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

            if (result.application_id.isEmpty())
                error("Application id is not configured.")

            if (result.pg.isEmpty())
                error("PG is not configured.")

            if (result.price < 0)
                error("Price is not configured.")

            if (result.orderId.isEmpty())
                error("Order id is not configured.")

            if (listener == null && (error == null || cancel == null || confirm == null || done == null))
                error("Must to be required to handle events.")

            dialog = PaymentDialog()
                    .setData(result)
//                    .setTrace(trace)
//                    .setUserInfo(user)
                    .setOnResponseListener(listener ?: object : EventListener {
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
            dialog?.onCancel(object : DialogInterface {
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

//    companion object {
//        private var trace: Trace? = null
//        private var user: UserData? = null
//    }

}
