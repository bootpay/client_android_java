package kr.co.bootpay.model;

import java.util.ArrayList;
import java.util.List;

public class Payload {

    private String application_id = "";
    private String pg = "";
    private String method = "";
    private List<String> methods = new ArrayList<>();
    private String name = "";
    private Double price = 0.0;
    private Double tax_free = 0.0;
    private String order_id = "";
    private Boolean use_order_id = false;
    private String params = "";

    private String account_expire_at = "";
    private Boolean show_agree_window = false;
    private String boot_key = "";
    private String ux = "";
    private Boolean sms_use = false;
    private String easy_pay_user_token;

    public String getApplication_id() {
        return application_id;
    }

    public void setApplication_id(String application_id) {
        this.application_id = application_id;
    }

    public String getPg() {
        return pg;
    }

    public void setPg(String pg) {
        this.pg = pg;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public List<String> getMethods() {
        return methods;
    }

    public void setMethods(List<String> methods) {
        this.methods = methods;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getTax_free() {
        return tax_free;
    }

    public void setTax_free(Double tax_free) {
        this.tax_free = tax_free;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public Boolean getUse_order_id() {
        return use_order_id;
    }

    public void setUse_order_id(Boolean use_order_id) {
        this.use_order_id = use_order_id;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getAccount_expire_at() {
        return account_expire_at;
    }

    public void setAccount_expire_at(String account_expire_at) {
        this.account_expire_at = account_expire_at;
    }

    public Boolean getShow_agree_window() {
        return show_agree_window;
    }

    public void setShow_agree_window(Boolean show_agree_window) {
        this.show_agree_window = show_agree_window;
    }

    public String getBoot_key() {
        return boot_key;
    }

    public void setBoot_key(String boot_key) {
        this.boot_key = boot_key;
    }

    public String getUx() {
        return ux;
    }

    public void setUx(String ux) {
        this.ux = ux;
    }

    public Boolean getSms_use() {
        return sms_use;
    }

    public void setSms_use(Boolean sms_use) {
        this.sms_use = sms_use;
    }

    public String getEasyPayUserToken() {
        return easy_pay_user_token;
    }

    public void setEasyPayUserToken(String easyPayUserToken) {
        this.easy_pay_user_token = easyPayUserToken;
    }
}
