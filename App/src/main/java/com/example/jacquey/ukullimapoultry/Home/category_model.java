package com.example.jacquey.ukullimapoultry.Home;

public class category_model {

    private String prod_id, prod_name, img_ulr,prod_price;

    public category_model(String prod_price) {
        this.prod_price = prod_price;
    }

    public category_model(String prod_id, String prod_name, String img_ulr) {
        this.prod_id = prod_id;
        this.prod_name = prod_name;
        this.img_ulr = img_ulr;
    }

    public String getProd_id(){ return prod_id;}
    public void setProd_id(String id){ this.prod_id = id;}

    public String getProd_name(){ return prod_name;}
    public void setProd_name(String name){ this.prod_name = name;}

    public String getProd_price() {
        return prod_price;
    }

    public String getImg_ulr(){ return img_ulr;}
    public void setImg_ulr(String img_ulr){ this.img_ulr = img_ulr;}

}
