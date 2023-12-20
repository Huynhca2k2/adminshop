package com.chh.adminshop.Domain;

import java.io.Serializable;

public class User implements Serializable {
    private String id;
    private String address;
    private String email;
    private String name;
    private String role;
    private String picUrl;

    public User(){
    }

    public User(String id, String address, String email, String name, String role, String picUrl) {
        this.id = id;
        this.address = address;
        this.email = email;
        this.name = name;
        this.role = role;
        this.picUrl = picUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
}
