package kr.co.bootpay.model

import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class Request {
    var application_id: String = ""
    var name: String = ""
    var price: Long = 0
    var pg: String = ""
    var order_id: String = ""
    var method: String = ""
    var unit: String = ""
    var feedback_url: String = ""
    var isShowAgree: Boolean = false
    var params: String? = null

    var items: MutableList<Item> = LinkedList()

    var userName: String? = null
    var userEmail: String? = null
    var userAddr: String? = null
    var userPhone: String? = null

    var extraExpireMonth: Int? = null
    var extraVbankResult: Int? = null
    var extraQuotas: IntArray? = null
//            intArrayOf(0,2,3,4,5,6,7,8,9,10,11,12)


    fun addItem(item: Item): MutableList<Item> {
        items.add(item)
        return items
    }

    fun addItems(item: Collection<Item>): MutableList<Item> {
        items.addAll(item)
        return items
    }

    @Throws(JSONException::class)
    fun paramOfJson(): JSONObject = JSONObject(params)

    @Throws(JSONException::class)
    fun <T> getParamsOfObject(cls: Class<T>): T = Gson().fromJson(params, cls)

    fun setParams(json: JSONObject) {
        this.params = json.toString()
    }

    fun setParams(`object`: Any) {
        this.params = Gson().toJson(`object`)
    }
}
