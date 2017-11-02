package kr.co.bootpay.model

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class Request {
    var price = 0
    var application_id = ""
    var name = ""
    var pg = ""
    var orderId = ""
        private set
    private var items: MutableList<Item> = LinkedList()
    var method = ""
    var unit = ""
    var feedback_url = ""
    var params: String? = ""

    fun setOrder_id(order_id: String) {
        this.orderId = order_id
    }

    fun getItems(): List<Item> = items

    fun addItem(item: Item) {
        items.add(item)
    }

    fun addItems(items: Collection<Item>) {
        this.items.addAll(items)
    }

    fun setItems(items: MutableList<Item>) {
        this.items = items
    }

    val paramsOfJson: JSONObject
        @Throws(JSONException::class)
        get() = JSONObject(params)

    @Throws(JsonSyntaxException::class)
    fun <T> getParamsOfObject(cls: Class<T>): T = Gson().fromJson(params, cls)

    fun setParams(json: JSONObject) {
        this.params = json.toString()
    }

    fun setParams(`object`: Any) {
        this.params = Gson().toJson(`object`)
    }

}
