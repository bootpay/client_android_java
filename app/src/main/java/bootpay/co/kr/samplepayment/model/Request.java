package bootpay.co.kr.samplepayment.model;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class Request {
    private boolean test_mode = false;
    private double price = 0.0;
    private String application_id = "";
    private String name = "";
    private String pg = "";
    private List<Item> items = new LinkedList<>();
    private String method = "";
    private String unit = "";
    private String feedback_url = "";

    public boolean isTest_mode() {
        return test_mode;
    }

    public void setTest_mode(boolean test_mode) {
        this.test_mode = test_mode;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getApplication_id() {
        return application_id;
    }

    public void setApplication_id(String application_id) {
        this.application_id = application_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPg() {
        return pg;
    }

    public void setPg(String pg) {
        this.pg = pg;
    }

    public List<Item> getItems() {
        return items;
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public void addItems(Collection<Item> items) {
        this.items.addAll(items);
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getFeedback_url() {
        return feedback_url;
    }

    public void setFeedback_url(String feedback_url) {
        this.feedback_url = feedback_url;
    }


}
