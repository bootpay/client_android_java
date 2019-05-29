package kr.co.bootpay.model;

import java.util.List;

public class StatCall {
    public String ver;
    public String application_id;
    public String uuid;
    public String url;
    public String page_type;
    public List<StatItem> items;
    public String sk;
    public String user_id;
    public String referer;

    public StatCall(String ver, String application_id, String uuid, String url, String page_type, List<StatItem> items, String sk, String user_id, String referer) {
        this.ver = ver;
        this.application_id = application_id;
        this.uuid = uuid;
        this.url = url;
        this.page_type = page_type;
        this.items = items;
        this.sk = sk;
        this.user_id = user_id;
        this.referer = referer;
    }
}
