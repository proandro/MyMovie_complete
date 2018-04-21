package com.example.lucky.myapplication;

public class ListItem {

    private String id;
    private String title;
    private String relase_date;
    private String image;
    private String backurl;
    private String desc;
    private String rating;


    public ListItem(String id, String title, String relase_date, String image, String backurl, String desc, String rating) {
        this.id = id;
        this.title = title;
        this.relase_date = relase_date;
        this.image = image;
        this.backurl = backurl;
        this.desc = desc;
        this.rating = rating;

    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getrelase_date() {
        return relase_date;
    }

    public String getImage() {
        return image;
    }

    public String getBackurl() {
        return backurl;
    }

    public String getDesc() {
        return desc;
    }

    public String getRating() {
        return rating;
    }


}
