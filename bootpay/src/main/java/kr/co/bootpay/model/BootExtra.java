package kr.co.bootpay.model;

import com.google.gson.Gson;

import java.util.Locale;


public final class BootExtra {
    private String start_at; //정기 결제 시작일 - 지정하지 않을 경우 - 그 날 당일로부터 결제가 가능한 Billing key 지급, "2016-10-14"
    private String end_at; // 정기결제 만료일 - 기간 없음 - 무제한, "2016-10-14"
    private Integer expire_month; //정기결제가 적용되는 개월 수 (정기결제 사용시), 미지정일시 PG사 기본값에 따름
    private boolean vbank_result; //가상계좌 결과창을 볼지(1), 말지(0)
    private int[] quotas; //결제금액이 5만원 이상시 할부개월 허용범위를 설정할 수 있음, [0(일시불), 2개월, 3개월] 허용, 미설정시 12개월까지 허용

    private String app_scheme; //app2app 결제시 return 받을 intent scheme
    private String app_scheme_host; //app2app 결제시 return 받을 intent scheme host
    private String ux; //다양한 결제시나리오를 지원하기 위한 용도로 사용됨
    private String disp_cash_result = "Y"; // 현금영수증 보일지 말지.. 가상계좌 KCP 옵션
    private int escrow = 0; // 에스크로 쓸지 안쓸지


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

    private String dispCashResult() {
        return String.format(Locale.KOREA, "disp_cash_result: '%s'", this.disp_cash_result);
    }

    private String escrow() {
        return String.format(Locale.KOREA, "escrow: %d", this.escrow);
    }

    public final String toJson() {
        return extra(
                startAt(),
                endAt(),
                expireMonth(),
                vbankResult(),
                quotas(),
                ux(),
                appScheme(),
                dispCashResult(),
                escrow()
        );
    }

    public final String toGson() {
        if(this == null) return "";
        return new Gson().toJson(this);
    }

    public String getDisp_cash_result() {
        return disp_cash_result;
    }

    public void setDisp_cash_result(String disp_cash_result) {
        this.disp_cash_result = disp_cash_result;
    }

    public int getEscrow() {
        return escrow;
    }

    public void setEscrow(int escrow) {
        this.escrow = escrow;
    }
}