package com.unwheeze.utils;

public enum ClientScopes {

    LAMBDA(0),
    USER(1),
    ADMIN(3);

    private final int scope;

    ClientScopes(int scope) {
        this.scope = scope;
    }

    public int getScope() {
        return scope;
    }
}
