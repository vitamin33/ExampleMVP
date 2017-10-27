package com.mvp.example.model;

/**
 * Created by vitalii_serbyn on 10/25/17.
 */

public class FriendlyMessage {

    private String id;
    private String text;
    private String name;
    private String imageUrl;

    public FriendlyMessage() {
    }

    public FriendlyMessage(String text, String name, String imageUrl) {
        this.text = text;
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
