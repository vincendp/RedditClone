package com.vincendp.RedditClone.Dto;

public class CreateUserRequest {

    private String username;
    private String password;
    private String verify_password;

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

    public String getVerify_password() {
        return verify_password;
    }

    public void setVerify_password(String verify_password) {
        this.verify_password = verify_password;
    }
}
