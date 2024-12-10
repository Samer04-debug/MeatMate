package com.example.meatmateapplication.Activity.adminFoodPanel;

public class UpdateMeatModel {
    String Meats,RandomUID,Description,Quantity,Price,ImageURL,AdminId;


    public UpdateMeatModel()
    {

    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
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

    public String getMeats()
    {
        return Meats;
    }

    public void setMeats(String meats) {

        Meats = meats;
    }

    public String getAdminId() {
        return AdminId;
    }

    public void setAdminId(String adminId) {
        AdminId = adminId;
    }
}
