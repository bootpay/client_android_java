package kr.co.bootpay.model;

import com.google.gson.Gson;

import java.util.Locale;


public final class BootExtra {
    private String start_at;
    private String end_at;
    private Integer expire_month;
    private boolean vbank_result;
    private int[] quotas;

    private String app_scheme;
    private String app_scheme_host;
    private String ux;


    public final String getApp_scheme() {
        return this.app_scheme;
    }

    public final void setApp_scheme(String value) {
        this.app_scheme = value;
    }


    public final String getApp_scheme_host() {
        return this.app_scheme_host;
    }

    public final void setApp_scheme_host(String value) {
        this.app_scheme_host = value;
    }

    public final String getUx() {
        return this.ux;
    }

    public final void setUx(String value) {
        this.ux = value;
    }

    public final BootExtra setStartAt(String value) {
        this.start_at = value;
        return this;
    }

    public final BootExtra setEndAt(String value) {
        this.end_at = value;
        return this;
    }

    public final BootExtra setExpireMonth(Integer value) {
        this.expire_month = value;
        return this;
    }
 
    public final BootExtra setVbankResult(boolean value) {
        this.vbank_result = value;
        return this;
    }
 
    public final BootExtra setQuotas(int[] value) {
        this.quotas = value;
        return this;
    }
 
    public final BootExtra setAppScheme(String value) {
        this.app_scheme = value;
        return this;
    }
 
    public final BootExtra setAppSchemeHost(String value) {
        this.app_scheme_host = value;
        return this;
    }

    public final BootExtra setUX(String value) {
        this.ux = value;
        return this;
    }

    private String extra(String... etcs) {
        StringBuilder sb = new StringBuilder();
        for(String str : etcs) {
            if(str.length() == 0) continue;
            if(sb.toString().length() > 0) sb.append(",");
            sb.append(str);
        }
        return String.format(Locale.KOREA,"{%s}", sb.toString());
    }

    private String startAt() {
        if(this.start_at == null) return "";
        return String.format(Locale.KOREA, "start_at: '%s'", this.start_at);
    }

    private String endAt() {
        if(this.end_at == null) return "";
        return String.format(Locale.KOREA, "end_at: '%s'", this.end_at);
    }

    private String expireMonth() {
        if(this.expire_month == null) return "";
        return String.format(Locale.KOREA, "expire_month: '%s'", this.expire_month);
    }

    private String vbankResult() {
        return String.format(Locale.KOREA, "vbank_result: '%b'", this.vbank_result);
    }


    private final String quotas() {
        if (quotas == null || quotas.length == 0) return "";
        StringBuilder sb = new StringBuilder();
        sb.append(quotas[0]);
        for (int i = 1; i < quotas.length; i++) sb.append(",").append(quotas[i]);
        return String.format(Locale.KOREA, "quotas: '%s'", sb.toString());
    }

    private final String appScheme() {
        if(this.app_scheme == null) return "";
        return String.format(Locale.KOREA, "app_scheme: '%s://%s'", this.app_scheme, this.appSchemeHost());
    }

    private final String appSchemeHost() {
        if(this.app_scheme_host == null) return "";
        return app_scheme_host;
    }

    private String ux() {
        if(this.ux == null) return "";
        return String.format(Locale.KOREA, "ux: '%s'", this.ux);
    }

    public final String toJson() {
        return extra(
                startAt(),
                endAt(),
                expireMonth(),
                vbankResult(),
                quotas(),
                ux(),
                appScheme()
        );
    }

    public final String toGson() {
        if(this == null) return "";
        return new Gson().toJson(this);
    }
}