package com.example.miulostandfound;
public class imageData {
    public String imageName;
    public String imageURL;

    public imageData() {
    }

    public String imageCaption;
    public imageData(String imageName, String imageURL, String imageCaption) {
        this.imageName = imageName;
        this.imageURL = imageURL;
        this.imageCaption = imageCaption;
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
