package kr.co.bootpay.model;

public class Item {
    private String item_name; //아이템 이름
    private int qty; //상품 판매된 수량
    private String unique; //상품의 고유 PK
    private Double price; //상품 하나당 판매 가격
    private String cat1; //카테고리 상
    private String cat2; //카테고리 중
    private String cat3; //카테고리 하

    public Item(String item_name, int qty, String unique, Double price, String cat1, String cat2, String cat3) {
        this.item_name = item_name;
        this.qty = qty;
        this.unique = unique;
        this.price = price;
        this.cat1 = cat1;
        this.cat2 = cat2;
        this.cat3 = cat3;
    }

    public String getItemName() {
        return item_name;
    }

    public void setItemName(String item_name) {
        this.item_name = item_name;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getUnique() {
        return unique;
    }

    public void setUnique(String unique) {
        this.unique = unique;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCat1() {
        return cat1;
    }

    public void setCat1(String cat1) {
        this.cat1 = cat1;
    }

    public String getCat2() {
        return cat2;
    }

    public void setCat2(String cat2) {
        this.cat2 = cat2;
    }

    public String getCat3() {
        return cat3;
    }

    public void setCat3(String cat3) {
        this.cat3 = cat3;
    }
}
