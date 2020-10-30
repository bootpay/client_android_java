package kr.co.bootpay.model;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import kr.co.bootpay.enums.UX;
import kr.co.bootpay.model.bio.BioPayload;
import kr.co.bootpay.pref.UserInfo;

public class Request {
    private String application_id = "";
    private String pg = "";
    private String method = "";
    private List<String> methods = new ArrayList<>();
    private String name = "";

    private Double price = 0.0;
    private Double tax_free = 0.0;
    private String order_id = "";
    private int use_order_id = 0;
    private String params = "";



    private String account_expire_at = "";
    private String unit = "";
    private Boolean is_show_agree = false;
    private String boot_key = "";
    private List<Item> items = new ArrayList<>();

    private UX ux = UX.PG_DIALOG;
    private BootUser boot_user;
    private BootExtra boot_extra;
    private RemoteOrderForm remote_order_form;
    private RemoteOrderPre remote_order_pre;
    private String easyPayUserToken;

//    private SMSPayload sms_payload = new SMSPayload();
    private BioPayload bioPayload = new BioPayload();
//    private Boolean sms_use = false;

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

    public Request setUseOrderId(int value) {
        this.use_order_id = value;
        return this;
    }

    public int getUseOrderId() {
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

    public BootUser getBoot_user() {
        return boot_user;
    }

    public void setBoot_user(BootUser boot_user) {
        this.boot_user = boot_user;
    }

    //getBootpayExtra() 로 대체됨. 2020. 03. 31
    @Deprecated
    public BootExtra getBoot_extra() {
        return boot_extra;
    }

    public BootExtra getBootExtra(Context context) {
        if(UserInfo.getInstance(context).getEnableOneStore()) {
            BootpayOneStore oneStore = new BootpayOneStore();
            oneStore.ad_id = UserInfo.getInstance(context).getAdId();
            oneStore.sim_operator = UserInfo.getInstance(context).getSimOperator();
            oneStore.installer_package_name = UserInfo.getInstance(context).getInstallPackageMarket();

            if(boot_extra == null) { boot_extra = new BootExtra(); }
            if(boot_extra.getOnestore() == null) { boot_extra.setOnestore(oneStore); }
        }

        return boot_extra;
    }



    public void setBoot_extra(BootExtra boot_extra) {
        this.boot_extra = boot_extra;
    }

    public RemoteOrderForm getRemote_order_form() {
        return remote_order_form;
    }

    public void setRemote_order_form(RemoteOrderForm remote_order_form) {
        this.remote_order_form = remote_order_form;
    }

    public RemoteOrderPre getRemote_order_pre() {
        return remote_order_pre;
    }

    public void setRemote_order_pre(RemoteOrderPre remote_order_pre) {
        this.remote_order_pre = remote_order_pre;
    }

    public BioPayload getBioPayload() {
        return bioPayload;
    }

    public void setBioPayload(BioPayload bioPayload) {
        this.bioPayload = bioPayload;
        if(pg.length() == 0) pg = bioPayload.pg;
        if(price == 0.0) price = bioPayload.price;
        if(tax_free == 0.0) tax_free = bioPayload.tax_free;
        if(params.length() == 0) params = bioPayload.params;
        if(name.length() == 0) name = bioPayload.name;
        if(easyPayUserToken == null || easyPayUserToken.length() == 0) easyPayUserToken = bioPayload.easy_pay_user_token;
    }

    public Payload getPayload() {
        return new Payload(this);
    }

//    public SMSPayload getSms_payload() {
//        return sms_payload;
//    }
//
//    public void setSms_payload(SMSPayload sms_payload) {
//        this.sms_payload = sms_payload;
//    }

//    public Boolean getSms_use() {
//        return sms_use;
//    }

//    public void setSms_use(Boolean sms_use) {
//        this.sms_use = sms_use;
//    }

    public void addItem(Item item) {
        this.items.add(item);
    }

    public void addItems(List<Item> items) {
        this.items.addAll(items);
    }

    public String getEasyPayUserToken() {
        return easyPayUserToken;
    }

    public void setEasyPayUserToken(String easyPayUserToken) {
        this.easyPayUserToken = easyPayUserToken;
    }
}
