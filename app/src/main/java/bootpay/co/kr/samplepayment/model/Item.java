package bootpay.co.kr.samplepayment.model;

public class Item {
    private Item() { /* ignore */ }

    public Item(String name, int quantity, String primaryKey, double price) {
        this.name = name;
        this.quantity = quantity;
        this.primaryKey = primaryKey;
        this.price = price;
    }

    String name = "";
    int quantity = 0;
    String primaryKey = "";
    double price = 0.0;
}
