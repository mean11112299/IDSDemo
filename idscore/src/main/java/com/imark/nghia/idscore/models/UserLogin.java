package com.imark.nghia.idscore.models;

/**
 * Created by Imark-N on 10/24/2015.
 * Lớp login
 */
public class UserLogin {
    private String username;
    private String password;

    public UserLogin() {
    }

    public UserLogin(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
