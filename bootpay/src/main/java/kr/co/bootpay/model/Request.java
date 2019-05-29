package kr.co.bootpay.model;

import java.util.ArrayList;
import java.util.List;

import kr.co.bootpay.enums.UX;

public class Request {
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
    private String unit = "";
    private Boolean is_show_agree = false;
    private String boot_key = "";
    private List<Item> items = new ArrayList<>();

    private UX ux = UX.PG_DIALOG;
    private BootUser bootUser;
    private BootExtra bootExtra;
    private RemoteOrderForm remoteOrderForm;
    private RemoteOrderPre remoteOrderPre;

    private SMSPayload smsPayload;
    private Boolean smsUse = false;

    public Request setApplicationId(String value) {
        this.application_id = value;
        return this;
    }

    public String getApplicationId() {
        return this.application_id;
    }

    public Request setPG(String value) {
        this.pg = value;
        return this;
    }

    public String getPG() {
        return this.pg;
    }

    public Request setMethod(String value) {
        this.method = value;
        return this;
    }

    public String getMethod() {
        return this.method;
    }

    public Request setMethods(List<String> value) {
        this.methods = value;
        return this;
    }

    public List<String> getMethods() {
        return this.methods;
    }

    public Request setName(String value) {
        this.name = value;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Request setPrice(Double value) {
        this.price = value;
        return this;
    }

    public Double getPrice() {
        return this.price;
    }

    public Request setTaxFree(Double value) {
        this.tax_free = value;
        return this;
    }

    public Double getTaxFree() {
        return this.tax_free;
    }

    public Request setOrderId(String value) {
        this.order_id = value;
        return this;
    }

    public String getOrderId() {
        return this.order_id;
    }

    public Request setUseOrderId(Boolean value) {
        this.use_order_id = value;
        return this;
    }

    public Boolean getUseOrderId() {
        return this.use_order_id;
    }

    public Request setParams(String value) {
        this.params = value;
        return this;
    }

    public String getParams() {
        return this.params;
    }

    public Request setAccountExpireAt(String value) {
        this.account_expire_at = value;
        return this;
    }

    public String getAccountExpireAt() {
        return this.account_expire_at;
    }

    public Request setUnit(String value) {
        this.unit = value;
        return this;
    }

    public String getUnit() {
        return this.unit;
    }

    public Request setIsShowAgree(Boolean value) {
        this.is_show_agree = value;
        return this;
    }

    public Boolean getIsShowAgree() {
        return this.is_show_agree;
    }

    public String getBootKey() {
        return boot_key;
    }

    public void setBootKey(String boot_key) {
        this.boot_key = boot_key;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public UX getUX() {
        return ux;
    }

    public void setUX(UX ux) {
        this.ux = ux;
    }

    public BootUser getBootUser() {
        return bootUser;
    }

    public void setBootUser(BootUser bootUser) {
        this.bootUser = bootUser;
    }

    public BootExtra getBootExtra() {
        return bootExtra;
    }

    public void setBootExtra(BootExtra bootExtra) {
        this.bootExtra = bootExtra;
    }

    public RemoteOrderForm getRemoteOrderForm() {
        return remoteOrderForm;
    }

    public void setRemoteOrderForm(RemoteOrderForm remoteOrderForm) {
        this.remoteOrderForm = remoteOrderForm;
    }

    public RemoteOrderPre getRemoteOrderPre() {
        return remoteOrderPre;
    }

    public void setRemoteOrderPre(RemoteOrderPre remoteOrderPre) {
        this.remoteOrderPre = remoteOrderPre;
    }

    public SMSPayload getSmsPayload() {
        return smsPayload;
    }

    public void setSmsPayload(SMSPayload smsPayload) {
        this.smsPayload = smsPayload;
    }

    public Boolean getSmsUse() {
        return smsUse;
    }

    public void setSmsUse(Boolean smsUse) {
        this.smsUse = smsUse;
    }

    public void addItem(Item item) {
        this.items.add(item);
    }

    public void addItems(List<Item> items) {
        this.items.addAll(items);
    }
}
