package kr.co.bootpay.model.bio;

import java.util.ArrayList;
import java.util.List;

import kr.co.bootpay.Bootpay;
import kr.co.bootpay.enums.PG;
import kr.co.bootpay.model.Payload;
import kr.co.bootpay.model.Request;

public class BioPayload extends Payload {

    private List<String> names = new ArrayList<>();
    private List<BioPrice> prices = new ArrayList<>();
    private int imageResources = -1;

    public BioPayload() {}

    public BioPayload(Request request) {
        super(request);
    }
//    private List<Integer> quotas = new ArrayList<>();

    public List<String> getNames() {
        return names;
    }

    public BioPayload setNames(List<String> names) {
        this.names = names;
        return this;
    }

    public List<BioPrice> getPrices() {
        return prices;
    }

    public BioPayload setPrices(List<BioPrice> prices) {
        this.prices = prices;
        return this;
    }

    public int getImageResources() {
        return imageResources;
    }

    public BioPayload setImageResources(int imageResources) {
        this.imageResources = imageResources;
        return this;
    }

    public BioPayload setApplication_id(String application_id) {
        this.application_id = application_id;
        return this;
    }

    public BioPayload setPg(String pg) {
        this.pg = pg;
        return this;
    }

    public BioPayload setPg(PG pg) {
        this.pg = Bootpay.getPG(pg);
        return this;
    }

    public BioPayload setMethod(String method) {
        this.method = method;
        return this;
    }

    public BioPayload setMethods(List<String> methods) {
        this.methods = methods;
        return this;
    }

    public BioPayload setName(String name) {
        this.name = name;
        return this;
    }

    public BioPayload setPrice(Double price) {
        this.price = price;
        return this;
    }

    public BioPayload setTax_free(Double tax_free) {
        this.tax_free = tax_free;
        return this;
    }


    public BioPayload setOrder_id(String order_id) {
        this.order_id = order_id;
        return this;
    }

    public BioPayload setUse_order_id(int use_order_id) {
        this.use_order_id = use_order_id;
        return this;
    }


    public BioPayload setParams(String params) {
        this.params = params;
        return this;
    }


    public BioPayload setAccount_expire_at(String account_expire_at) {
        this.account_expire_at = account_expire_at;
        return this;
    }


    public BioPayload setShow_agree_window(Boolean show_agree_window) {
        this.show_agree_window = show_agree_window;
        return this;
    }


    public BioPayload setBoot_key(String boot_key) {
        this.boot_key = boot_key;
        return this;
    }


    public BioPayload setUx(String ux) {
        this.ux = ux;
        return this;
    }

    public BioPayload setEasyPayUserToken(String easyPayUserToken) {
        this.easy_pay_user_token = easyPayUserToken;
        return this;
    }
}
