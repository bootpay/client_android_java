package bootpay.co.kr.samplepayment.model;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public final class Request {
    private boolean test_mode = false;
    private double price = 0.0;
    private String application_id = "";
    private String name = "";
    private String pg = "";
    private List<Item> items = new LinkedList<>();
    private String method = "";
    private String unit = "";
    private String feedback_url = "";

    public final boolean isTest_mode() {
        return test_mode;
    }

    public final void setTest_mode(boolean test_mode) {
        this.test_mode = test_mode;
    }

    public final double getPrice() {
        return price;
    }

    public final void setPrice(double price) {
        this.price = price;
    }

    public final String getApplication_id() {
        return application_id;
    }

    public final void setApplication_id(String application_id) {
        this.application_id = application_id;
    }

    public final String getName() {
        return name;
    }

    public final void setName(String name) {
        this.name = name;
    }

    public final String getPg() {
        return pg;
    }

    public final void setPg(String pg) {
        this.pg = pg;
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

    public final void setItems(List<Item> items) {
        this.items = items;
    }

    public final String getMethod() {
        return method;
    }

    public final void setMethod(String method) {
        this.method = method;
    }

    public final String getUnit() {
        return unit;
    }

    public final void setUnit(String unit) {
        this.unit = unit;
    }

    public final String getFeedback_url() {
        return feedback_url;
    }

    public final void setFeedback_url(String feedback_url) {
        this.feedback_url = feedback_url;
    }


}
