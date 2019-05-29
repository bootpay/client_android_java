package kr.co.bootpay.model;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kr.co.bootpay.enums.PushType;

public class SMSPayload {
    private final int REMOTE_ORDER_TYPE_FORM = 1;
    private final int REMOTE_ORDER_TYPE_PRE = 2;

    private String o_id = "";
    private int o_t = REMOTE_ORDER_TYPE_FORM;

    private String sj = "";
    private String msg = "";
    private int pt = PushType.SMS;
    private String sp = "";

    private List<String> rps = new ArrayList<>();
    private Long rq_at = System.currentTimeMillis();
    private Long s_at = System.currentTimeMillis();

    private String k_tp_id = ""; // 템플릿 코드
    private String k_msg = ""; // 알림톡 메시지
    private HashMap<String, Object> k_vals = new HashMap<>();
    private List<HashMap<String, String>> k_btns = new ArrayList<>();
    private int k_sms_t = PushType.SMS;

    private String img_url = "";
    private String img_link = "";
    private int ad = 1; //1: 표기함, 0: 표기안함, default 1

    private List<String> files = new ArrayList<>();

    public SMSPayload setOrderObjectId(String value) {
        this.o_id = value;
        return this;
    }

    public String getOrderObjectId() {
        return this.o_id;
    }

    public SMSPayload setOrderType(int value) {
        this.o_t = value;
        return this;
    }

    public int getOrderType() {
        return this.o_t;
    }

    public SMSPayload setSubject(String value) {
        this.sj = value;
        return this;
    }

    public String getSubject() {
        return this.sj;
    }

    public SMSPayload setMessage(String value) {
        this.msg = value;
        return this;
    }

    public String getMessage() {
        return this.msg;
    }

    public SMSPayload setPushType(int value) {
        this.pt = value;
        return this;
    }

    public int getPushType() {
        return this.pt;
    }

    public SMSPayload setSenderPhone(String value) {
        this.sp = value;
        return this;
    }

    public String getSenderPhone() {
        return this.sp;
    }

    public SMSPayload setReceieverPhones(List<String> value) {
        this.rps = value;
        return this;
    }

    public List<String> getReceieverPhones() {
        return this.rps;
    }

    public SMSPayload setStartAt(Long value) {
        this.s_at = value;
        return this;
    }

    public Long getStartAt() {
        return this.s_at;
    }

    public SMSPayload setRequestAt(Long value) {
        this.rq_at = value;
        return this;
    }

    public Long getRequestAt() {
        return this.rq_at;
    }

    public SMSPayload setKakaoTemplateId(String value) {
        this.k_tp_id = value;
        return this;
    }

    public String getKakaoTemplateId() {
        return this.k_tp_id;
    }


    public SMSPayload setKakaoMessage(String value) {
        this.k_msg = value;
        return this;
    }

    public String getKakaoMessage() {
        return this.k_msg;
    }

    public SMSPayload setKakaoValues(HashMap<String, Object> value) {
        this.k_vals = value;
        return this;
    }

    public HashMap<String, Object> getKakaoValues() {
        return this.k_vals;
    }

    public SMSPayload setKakaoMessage(List<HashMap<String, String>> value) {
        this.k_btns = value;
        return this;
    }

    public List<HashMap<String, String>> getKakaoButtons() {
        return this.k_btns;
    }

    public SMSPayload setKakaoSMSType(int value) {
        this.k_sms_t = value;
        return this;
    }

    public int getKakaoSMSType() {
        return this.k_sms_t;
    }

    public SMSPayload setImageUrl(String value) {
        this.img_url = value;
        return this;
    }

    public String getImageUrl() {
        return this.img_url;
    }

    public SMSPayload setImageLink(String value) {
        this.img_link = value;
        return this;
    }

    public String getImageLink() {
        return this.img_link;
    }

    public SMSPayload setAd(int value) {
        this.ad = value;
        return this;
    }

    public int getAdValue() {
        return this.ad;
    }

    public final String toGson() {
        if(this == null) return "";
        return new Gson().toJson(this);
    }
}
