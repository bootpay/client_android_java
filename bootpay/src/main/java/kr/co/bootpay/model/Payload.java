package kr.co.bootpay.model;

import java.util.ArrayList;
import java.util.List;

import kr.co.bootpay.Bootpay;
import kr.co.bootpay.enums.PG;

public class Payload {

    protected String application_id = "";
    protected String pg = "";
    protected String method = "";
    protected List<String> methods = new ArrayList<>();
    protected String name = "";
    protected Double price = 0.0;
    protected Double tax_free = 0.0;
    protected String order_id = "";
    protected int use_order_id = 0; // 1: 사용, 0: 미사용
    protected String params = "";

    protected String account_expire_at = "";
    protected Boolean show_agree_window = false;
    protected String boot_key = "";
    protected String ux = "";
//    protected Boolean sms_use = false;
    protected String easy_pay_user_token;

    public Payload() {}

    public Payload(Request request) {
        this.application_id = request.getApplicationId();
        this.pg = request.getPG();
        this.method = request.getMethod();
        this.methods = request.getMethods();
        this.name = request.getName();
        this.price = request.getPrice();
        this.tax_free = request.getTaxFree();
        this.order_id = request.getOrderId();
        this.use_order_id = request.getUseOrderId();
        this.params = request.getParams();
        this.account_expire_at = request.getAccountExpireAt();
        this.show_agree_window = request.getIsShowAgree();
        this.boot_key = request.getBootKey();
//        this.ux = request.getUX();
        this.easy_pay_user_token = request.getEasyPayUserToken();
    }

    public String getApplication_id() {
        return application_id;
    }

    public Payload setApplication_id(String application_id) {
        this.application_id = application_id;
        return this;
    }

    public String getPg() {
        return pg;
    }

    public Payload setPg(String pg) {
        this.pg = pg;
        return this;
    }

    public Payload setPg(PG pg) {
        this.pg = Bootpay.getPG(pg);
        return this;
    }

    public String getMethod() {
        return method;
    }

    public Payload setMethod(String method) {
        this.method = method;
        return this;
    }

    public List<String> getMethods() {
        return methods;
    }

    public Payload setMethods(List<String> methods) {
        this.methods = methods;
        return this;
    }

    public String getName() {
        return name;
    }

    public Payload setName(String name) {
        this.name = name;
        return this;
    }

    public Double getPrice() {
        return price;
    }

    public Payload setPrice(Double price) {
        this.price = price;
        return this;
    }

    public Double getTax_free() {
        return tax_free;
    }

    public Payload setTax_free(Double tax_free) {
        this.tax_free = tax_free;
        return this;
    }

    public String getOrder_id() {
        return order_id;
    }

    public Payload setOrder_id(String order_id) {
        this.order_id = order_id;
        return this;
    }

    public int getUse_order_id() {
        return use_order_id;
    }

    public Payload setUse_order_id(int use_order_id) {
        this.use_order_id = use_order_id;
        return this;
    }

    public String getParams() {
        return params;
    }

    public Payload setParams(String params) {
        this.params = params;
        return this;
    }

    public String getAccount_expire_at() {
        return account_expire_at;
    }

    public Payload setAccount_expire_at(String account_expire_at) {
        this.account_expire_at = account_expire_at;
        return this;
    }

    public Boolean getShow_agree_window() {
        return show_agree_window;
    }

    public Payload setShow_agree_window(Boolean show_agree_window) {
        this.show_agree_window = show_agree_window;
        return this;
    }

    public String getBoot_key() {
        return boot_key;
    }

    public Payload setBoot_key(String boot_key) {
        this.boot_key = boot_key;
        return this;
    }

    public String getUx() {
        return ux;
    }

    public Payload setUx(String ux) {
        this.ux = ux;
        return this;
    }

//    public Boolean getSms_use() {
//        return sms_use;
//    }
//
//    public Payload setSms_use(Boolean sms_use) {
//        this.sms_use = sms_use;
//        return this;
//    }

    public String getEasyPayUserToken() {
        return easy_pay_user_token;
    }

    public Payload setEasyPayUserToken(String easyPayUserToken) {
        this.easy_pay_user_token = easyPayUserToken;
        return this;
    }
}
