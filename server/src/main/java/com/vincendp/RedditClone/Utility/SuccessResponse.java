package com.vincendp.RedditClone.Utility;

public class SuccessResponse extends ResponseWrapper {

    public SuccessResponse(){

    }

    public SuccessResponse(int status, String message, Object result){
        super(status, message, result);
    }
}
