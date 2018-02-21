package com.unwheeze.beans;

public class AuthClient {

    private String key;

    public AuthClient(String key) {
        this.key = key;
    }

    public AuthClient() {
    }

    public String getId() {

        return key;
    }

    public void setId(String key) {
        this.key = key;
    }
}
