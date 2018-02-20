package com.unwheeze.beans;

public class AuthClient {

    private String id;

    public AuthClient(String id) {
        this.id = id;
    }

    public AuthClient() {
    }

    public String getId() {

        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
