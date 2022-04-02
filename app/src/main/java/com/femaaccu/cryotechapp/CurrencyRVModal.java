package com.femaaccu.cryotechapp;

public class CurrencyRVModal {
    private String name;
    private String symbol;
    private double price;
    private String id;

    public CurrencyRVModal(String name, String symbol, double price, String id) {
        this.name = name;
        this.symbol = symbol;
        this.price = price;
        this.id = id;
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
