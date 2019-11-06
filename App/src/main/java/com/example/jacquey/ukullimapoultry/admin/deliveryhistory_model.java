package com.example.jacquey.ukullimapoultry.admin;

public class deliveryhistory_model {

    private String orderid, shippingaddress, user, date,delivery_status;


    public deliveryhistory_model(String orderid, String shippingaddress, String user, String date,String delivery_status) {
        this.orderid = orderid;
        this.shippingaddress = shippingaddress;
        this.user = user;
        this.delivery_status = delivery_status;
        this.date = date;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getShippingaddress() {
        return shippingaddress;
    }

    public void setShippingaddress(String shippingaddress) {
        this.shippingaddress = shippingaddress;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDelivery_status() {
        return delivery_status;
    }

    public void setDelivery_status(String delivery_status) {
        this.delivery_status = delivery_status;
    }
}
