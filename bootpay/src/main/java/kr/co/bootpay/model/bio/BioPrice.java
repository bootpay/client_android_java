package kr.co.bootpay.model.bio;

public final class BioPrice {
    private String name;
    private Double price = 0.0;
    private Double price_stroke = 0.0;

    public BioPrice(String name, Double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getPrice_stroke() {
        return price_stroke;
    }

    public void setPrice_stroke(Double price_stroke) {
        this.price_stroke = price_stroke;
    }
}