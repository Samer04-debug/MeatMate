package com.example.meatmateapplication.Activity.customerFoodPanel;

public class CustomerFinalOrders {

    private String AdminId,MeatId,MeatName,MeatPrice,MeatQuantity,RandomUID,TotalPrice,UserId;

    public CustomerFinalOrders(String adminId, String meatId, String meatName, String meatPrice, String meatQuantity, String randomUID, String totalPrice, String userId) {
        AdminId = adminId;
        MeatId = meatId;
        MeatName = meatName;
        MeatPrice = meatPrice;
        MeatQuantity = meatQuantity;
        RandomUID = randomUID;
        TotalPrice = totalPrice;
        UserId = userId;
    }

    public CustomerFinalOrders()
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

    public String getMeatPrice() {
        return MeatPrice;
    }

    public void setMeatPrice(String meatPrice) {
        MeatPrice = meatPrice;
    }

    public String getMeatQuantity() {
        return MeatQuantity;
    }

    public void setMeatQuantity(String meatQuantity) {
        MeatQuantity = meatQuantity;
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
