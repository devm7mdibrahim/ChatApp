package com.devmohamedibrahim1997.chatdemo.pojo;

public class Contact {

    private String userName;
    private String id;
    private String imageURL;

    public Contact(String userName, String imageURL) {
        this.userName = userName;
        this.imageURL = imageURL;
    }
    public Contact(){
    }

    public String getUserName() {return userName;}

    public String getImgURL() {return imageURL;}

    public void setImgURL(String imgURL) {this.imageURL = imgURL;}

    public String getId(){return id;}

    public void setId(String id) {this.id = id;}
}
