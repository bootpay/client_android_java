package kr.co.bootpay.model;

public class StatItem {
    public String item_name;
    public String item_img;
    public String unique;
    public String cat1;
    public String cat2;
    public String cat3;

    public StatItem(String item_name, String item_img, String unique, String cat1, String cat2, String cat3) {
        this.item_name = item_name;
        this.item_img = item_img;
        this.unique = unique;
        this.cat1 = cat1;
        this.cat2 = cat2;
        this.cat3 = cat3;
    }
}
