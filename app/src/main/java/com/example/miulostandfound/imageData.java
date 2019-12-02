package com.example.miulostandfound;

import android.widget.EditText;

import com.google.firebase.database.Exclude;

public class imageData {
    public String imageName;
    public String imageURL;
    public String imageCaption;
    public boolean isFound;
    public String FoundAt;
    public String user;

    public imageData(String imageName, String imageURL, String imageCaption, boolean isFound, String foundAt, String user) {
        this.imageName = imageName;
        this.imageURL = imageURL;
        this.imageCaption = imageCaption;
        this.isFound = isFound;
        FoundAt = foundAt;
        this.user = user;
    }

    public imageData() {
    }

    @Exclude
    public String  getFoundAt() {
        return FoundAt;
    }
    @Exclude
    public void setFoundAt(String foundAt) {
        this.FoundAt = foundAt;
    }



    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public boolean getIsFound() {
        return isFound;
    }

    public void setIsFound(boolean isFound) {
        this.isFound = isFound;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getImageCaption() {
        return imageCaption;
    }

    public void setImageCaption(String imageCaption) {
        this.imageCaption = imageCaption;
    }
}
