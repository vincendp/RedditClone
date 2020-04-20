package com.vincendp.RedditClone.Utility;

public class ErrorResponse extends ResponseWrapper {

    public ErrorResponse(){

    }

    public ErrorResponse(int status, String message, Object result){
        super(status, message, result);
    }
}
