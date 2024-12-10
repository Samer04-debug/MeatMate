package com.example.meatmateapplication.Activity.adminFoodPanel;

public class FoodSupplyDetails {
    public String Meats,Quantity,Price,Description,ImageURL,RandomUID,AdminId;

    public FoodSupplyDetails(String meats, String quantity, String price, String description, String imageURL,String randomUID,String adminId) {
        Meats = meats;
        Quantity = quantity;
        Price = price;
        Description = description;
        ImageURL = imageURL;
        RandomUID = randomUID;
        AdminId = adminId;
    }

}