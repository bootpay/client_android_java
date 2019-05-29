package kr.co.bootpay.model;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class RemoteOrderForm {
    private String m_id = "";
    private String pg = "";
    private List<String> fm = new ArrayList<>();
    private Double tfp = 0.0;
    private String n = "";
    private String cn = "";
    private Double ip = 0.0;
    private Double dp = 0.0;
    private Double dap = 0.0;

    private boolean is_r_n = false;
    private boolean is_r_p = false;
    private boolean is_addr = false;
    private boolean is_da = false;
    private boolean is_memo = false;
    private String desc_html = "";
    private Double dap_jj = 0.0;
    private Double dap_njj = 0.0;
    private String o_key = "";

    public RemoteOrderForm setTaxFreePrice(Double value) {
        this.tfp = value;
        return this;
    }

    public Double getTaxFreePrice() {
        return this.tfp;
    }

    public RemoteOrderForm setPG(String value) {
        this.pg = value;
        return this;
    }

    public String getPG() {
        return this.pg;
    }

    public RemoteOrderForm setMethods(List<String> value) {
        this.fm = value;
        return this;
    }

    public List<String> getMethods() {
        return this.fm;
    }

    public RemoteOrderForm setName(String value) {
        this.n = value;
        return this;
    }

    public String getName() {
        return this.n;
    }

    public RemoteOrderForm setCompanyName(String value) {
        this.cn = value;
        return this;
    }

    public String getCompanyName() {
        return this.cn;
    }

    public RemoteOrderForm setItemPrice(int value) {
        return setItemPrice(new Double(value));
    }

    public RemoteOrderForm setItemPrice(Double value) {
        this.ip = value;
        return this;
    }

    public Double getItemPrice() {
        return this.ip;
    }

    public RemoteOrderForm setDisplayPrice(Double value) {
        this.dp = value;
        return this;
    }

    public Double getDisplayPrice() {
        return this.dp;
    }


    public RemoteOrderForm setDeliveryAreaPrice(int value) {
        return setDeliveryAreaPrice(new Double(value));
    }

    public RemoteOrderForm setDeliveryAreaPrice(Double value) {
        this.dap = value;
        return this;
    }

    public Double getDeliveryAreaPrice() {
        return this.dap;
    }

    public RemoteOrderForm setIsReceiverName(Boolean value) {
        this.is_r_n = value;
        return this;
    }

    public Boolean getIsReceiverName() {
        return this.is_r_n;
    }

    public RemoteOrderForm setIsReceiverPrice(Boolean value) {
        this.is_r_p = value;
        return this;
    }

    public Boolean getIsReceiverPrice() {
        return this.is_r_p;
    }

    public RemoteOrderForm setIsAddr(Boolean value) {
        this.is_addr = value;
        return this;
    }

    public Boolean getIsAddr() {
        return this.is_addr;
    }

    public RemoteOrderForm setIsDeliveryArea(Boolean value) {
        this.is_da = value;
        return this;
    }

    public Boolean getIsDeliveryArea() {
        return this.is_da;
    }

    public RemoteOrderForm setIsMemo(Boolean value) {
        this.is_memo = value;
        return this;
    }

    public Boolean getIsMemo() {
        return this.is_memo;
    }


    public RemoteOrderForm setDescHtml(String value) {
        this.desc_html = value;
        return this;
    }

    public String getDescHtml() {
        return this.desc_html;
    }

    public RemoteOrderForm setDapJeju(int value) {
        return setDapJeju(new Double(value));
    }

    public RemoteOrderForm setDapJeju(Double value) {
        this.dap_jj = value;
        return this;
    }

    public Double getDapJeju() {
        return this.dap_jj;
    }

    public RemoteOrderForm setDapNonJeju(int value) {
        return setDapNonJeju(new Double(value));
    }

    public RemoteOrderForm setDapNonJeju(Double value) {
        this.dap_njj = value;
        return this;
    }

    public Double getDapNonJeju() {
        return this.dap_njj;
    }

    public RemoteOrderForm setOrderFormKey(String value) {
        this.o_key = value;
        return this;
    }

    public String getOrderFormKey() {
        return this.o_key;
    }

    public final String toGson() {
        if(this == null) return "";
        return new Gson().toJson(this);
    }
}
