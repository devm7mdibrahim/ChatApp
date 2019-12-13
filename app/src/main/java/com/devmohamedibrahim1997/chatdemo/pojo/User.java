package com.devmohamedibrahim1997.chatdemo.pojo;

public class User {

    private String name;
    private String email;
    private String passWord;
    private String passWord2;
    private String id;

    public User(String name, String email, String passWord, String passWord2) {
        this.name = name;
        this.email = email;
        this.passWord = passWord;
        this.passWord2 = passWord2;
    }

    public String getUserName() {return name;}

    public String getEmail() {return email;}

    public String getPassWord() {return passWord;}

    public String getPassWord2() {return passWord2;}

    public String getId(){return id;}

    public void setId(String id) {this.id = id;}
}
