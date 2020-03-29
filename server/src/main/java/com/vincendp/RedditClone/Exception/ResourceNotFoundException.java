package com.vincendp.RedditClone.Exception;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException() {

    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}