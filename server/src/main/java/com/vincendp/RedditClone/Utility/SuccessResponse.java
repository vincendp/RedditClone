package com.vincendp.RedditClone.Utility;

public class SuccessResponse extends ResponseWrapper {

    public SuccessResponse(){

    }

    public SuccessResponse(int status, String message, Object data){
        super(status, message, data);
    }
}
