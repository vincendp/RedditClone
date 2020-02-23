package com.vincendp.RedditClone.Utility;

public class ErrorResponse extends ResponseWrapper {

    public ErrorResponse(int status, String message, String data){
        super(status, message, data);
    }
}
