package com.vincendp.RedditClone.Utility;

public class ResponseWrapper {

    private int status;
    private String message;
    private String data;

    public ResponseWrapper(int status, String message, String data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}
