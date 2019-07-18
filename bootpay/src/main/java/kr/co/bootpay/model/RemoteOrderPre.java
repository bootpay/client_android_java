package kr.co.bootpay.model;

import com.google.gson.Gson;

public class RemoteOrderPre {
    private String e_p = ""; //예상 가격, 미입력시 안보여줌
    private Boolean is_r_n = false; //구매자명 받을지 말지, is_receive_name
    private Boolean is_sale = false; //세일기간 정할지 말지
    private Long s_at = System.currentTimeMillis(); //예약 시작일
    private Long e_at = 0l; //예약 종료일
    private String desc_html = ""; //상품설명 html
    private String n = ""; //상품명
    private String cn = ""; //보여질 업체명

    public RemoteOrderPre setExpectedPrice(String value) {
        this.e_p = value;
        return this;
    }

    public String getExpectedPrice() {
        return this.e_p;
    }

    public RemoteOrderPre setIsReceiveName(Boolean value) {
        this.is_r_n = value;
        return this;
    }

    public Boolean getIsReceiveName() {
        return this.is_r_n;
    }

    public RemoteOrderPre setIsSale(Boolean value) {
        this.is_sale = value;
        return this;
    }

    public Boolean getIsSale() {
        return this.is_sale;
    }

    public RemoteOrderPre setStartAt(Long value) {
        this.s_at = value;
        return this;
    }

    public Long getStartAt() {
        return this.s_at;
    }

    public RemoteOrderPre setEndAt(Long value) {
        this.e_at = value;
        return this;
    }

    public Long getEndAt() {
        return this.e_at;
    }

    public RemoteOrderPre setDescHtml(String value) {
        this.desc_html = value;
        return this;
    }

    public String getDescHtml() {
        return this.desc_html;
    }

    public RemoteOrderPre setName(String value) {
        this.n = value;
        return this;
    }

    public String getName() {
        return this.n;
    }

    public RemoteOrderPre setCompanyName(String value) {
        this.cn = value;
        return this;
    }

    public String getCompanyName() {
        return this.cn;
    }

    public final String toGson() {
        if(this == null) return "";
        return new Gson().toJson(this);
    }
}
