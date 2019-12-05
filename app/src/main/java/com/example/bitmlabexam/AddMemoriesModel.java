package com.example.bitmlabexam;

public class AddMemoriesModel {


    private String memoryDescription;
    private String imageUrl;

    public AddMemoriesModel() {
    }

    public AddMemoriesModel(String memoryDescription, String imageUrl) {
        this.memoryDescription = memoryDescription;
        this.imageUrl = imageUrl;
    }

    public String getMemoryDescription() {
        return memoryDescription;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
