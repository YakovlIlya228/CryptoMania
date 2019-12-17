package com.example.cryptosampleproject.API;

public class Currency {
    private int id;
    private String name;
    private String codename;
    private String price;
    private String change;

    public Currency(String name, String codename, String price, String change) {
        this.name = name;
        this.codename = codename;
        this.price = price;
        this.change = change;
    }

    public Currency(String name, String codename) {
        this.name = name;
        this.codename = codename;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCodename() {
        return codename;
    }

    public void setCodename(String codename) {
        this.codename = codename;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getChange() { return change; }

    public void setChange(String change) { this.change = change; }
}
