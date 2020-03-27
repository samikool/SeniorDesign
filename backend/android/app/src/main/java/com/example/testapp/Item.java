package com.example.testapp;

public class Item {
    private int id;
    private double price;
    private String name;

    public Item(int id, String name, double price){
        this.name = name;
        this.id = id;
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object object){
        Item item = (Item) object;
        if(this.getId() == (item).getId()
                && this.getPrice() == item.getPrice()
                && this.getName().equals(item.getName())){
            return true;
        }
        return false;
    }


    @Override
    public String toString(){
        return "ID: " + id + " Name: " + name + " Price: " + price;
    }
}
