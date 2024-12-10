package com.example.meatmateapplication.Activity.customerFoodPanel;

public class CustomerPendingOrders {

    private String AdminId, MeatID, MeatName, MeatQuantity, Price, TotalPrice;

    public CustomerPendingOrders(String adminId, String meatID, String meatName, String meatQuantity, String price, String totalPrice) {
        AdminId = adminId;
        MeatID = meatID;
        MeatName = meatName;
        MeatQuantity = meatQuantity;
        Price = price;
        TotalPrice = totalPrice;

    }

    public CustomerPendingOrders() {

    }

    public String getAdminId() {
        return AdminId;
    }

    public void setAdminId(String adminId) {
        AdminId = adminId;
    }

    public String getMeatID() {
        return MeatID;
    }

    public void setMeatID(String meatID) {
        MeatID = meatID;
    }

    public String getMeatName() {
        return MeatName;
    }

    public void setMeatName(String meatName) {
        MeatName = meatName;
    }

    public String getMeatQuantity() {
        return MeatQuantity;
    }

    public void setMeatQuantity(String meatQuantity) {
        MeatQuantity = meatQuantity;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        TotalPrice = totalPrice;
    }


}
