package com.femaaccu.cryotechapp;

public class CurrencyRVModal {
    private String name;
    private String symbol;
    private double price;
    private String id;
    private String image;
    private double rate;

    public CurrencyRVModal(String name, String symbol, double price, String id, String image, double rate) {
        this.name = name;
        this.symbol = symbol;
        this.price = price;
        this.id = id;
        this.image = image;
        this.rate = rate;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getPrice() {
        return price;
    }

    public String getId(){ return id; }

    public String getImage(){return image;}

    public double getRate(){return rate;}

    public void setName(String name) {
        this.name = name;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setPrice(double price) {
        this.price = price;
    }


}
