package com.vincendp.RedditClone.Utility;

public class SuccessResponse extends ResponseWrapper {

    public SuccessResponse(int status, String message, String data){
        super(status, message, data);
    }
}
