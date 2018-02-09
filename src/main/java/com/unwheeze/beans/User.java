package com.unwheeze.beans;

public class User {

    private String ID;
    private String firstName;
    private String lastName;
    private String ImgUrl;
    private String email;
    private String location;
    private String city;
    private String country;
    private String salt;
    private String pwd;


    public User(String ID, String pwd, String salt,String firstName, String lastName, String imgUrl, String email, String location, String city, String country) {
        this.ID = ID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.ImgUrl = imgUrl;
        this.email = email;
        this.location = location;
        this.city = city;
        this.country = country;
        this.pwd = pwd;
        this.salt = salt;
    }

    public User(String ID, String pwd, String salt, String firstName, String lastName, String email) {
        this.ID = ID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.pwd = pwd;
        this.salt = salt;
    }

    public User(String ID, String pwd, String salt, String firstName, String lastName, String email, String location) {
        this.ID = ID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.location = location;
        this.pwd = pwd;
        this.salt = salt;
    }

    public User() {
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getImgUrl() {
        return ImgUrl;
    }

    public void setImgUrl(String imgUrl) {
        ImgUrl = imgUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
