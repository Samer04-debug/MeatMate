package com.example.meatmateapplication.Activity.adminFoodPanel;

public class AdminPendingOrders {
    private String AdminId,MeatId,MeatName,MeatQuantity,Price,RandomUID,TotalPrice,UserId;

    public AdminPendingOrders(String adminId, String meatId, String meatName, String meatQuantity, String price,String randomUID, String totalPrice, String userId) {
        AdminId = adminId;
        MeatId = meatId;
        MeatName = meatName;
        MeatQuantity = meatQuantity;
        Price = price;
        RandomUID=randomUID;
        TotalPrice = totalPrice;
        UserId = userId;
    }

    public AdminPendingOrders()
    {

    }

    public String getAdminId() {
        return AdminId;
    }

    public void setAdminId(String adminId) {
        AdminId = adminId;
    }

    public String getMeatId() {
        return MeatId;
    }

    public void setMeatId(String meatId) {
        MeatId = meatId;
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

    public String getRandomUID() {
        return RandomUID;
    }

    public void setRandomUID(String randomUID) {
        RandomUID = randomUID;
    }

    public String getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        TotalPrice = totalPrice;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }
}