package kr.co.bootpay.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public final class Request {
    private boolean test_mode = true;
    private int price = 0;
    private String application_id = "";
    private String name = "";
    private String pg = "";
    private String order_id = "";
    private List<Item> items = new LinkedList<>();
    private String method = "";
    private String unit = "";
    private String feedback_url = "";
    private String params = "";

    public final boolean isTest_mode() {
        return test_mode;
    }

    public final void setTest_mode(boolean test_mode) {
        this.test_mode = test_mode;
    }

    public final int getPrice() {
        return price;
    }

    public final void setPrice(int price) {
        this.price = price;
    }

    public final String getApplication_id() {
        return application_id;
    }

    public final void setApplication_id(@NonNull String application_id) {
        this.application_id = application_id;
    }

    public final String getName() {
        return name;
    }

    public final void setName(@NonNull String name) {
        this.name = name;
    }

    public final String getPg() {
        return pg;
    }

    public final void setPg(@NonNull String pg) {
        this.pg = pg;
    }

    public final String getOrderId() {
        return order_id;
    }

    public final void setOrder_id(@NonNull String order_id) {
        this.order_id = order_id;
    }

    public final List<Item> getItems() {
        return items;
    }

    public final void addItem(Item item) {
        items.add(item);
    }

    public final void addItems(Collection<Item> items) {
        this.items.addAll(items);
    }

    public final void setItems(@NonNull List<Item> items) {
        this.items = items;
    }

    public final String getMethod() {
        return method;
    }

    public final void setMethod(@NonNull String method) {
        this.method = method;
    }

    public final String getUnit() {
        return unit;
    }

    public final void setUnit(@NonNull String unit) {
        this.unit = unit;
    }

    public final String getFeedback_url() {
        return feedback_url;
    }

    public final void setFeedback_url(@NonNull String feedback_url) {
        this.feedback_url = feedback_url;
    }

    @Nullable
    public final String getParams() {
        return params;
    }

    public final JSONObject getParamsOfJson() throws JSONException {
        return new JSONObject(params);
    }

    public final <T> T getParamsOfObject(Class<T> cls) throws JsonSyntaxException {
        return new Gson().fromJson(params, cls);
    }

    public final void setParams(String json) {
        this.params = json;
    }

    public final void setParams(JSONObject json) {
        this.params = json.toString();
    }

    public final void setParams(Object object) {
        this.params = new Gson().toJson(object);
    }

}
