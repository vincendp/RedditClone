package com.vincendp.RedditClone.Exception;

public class ResourceAlreadyExistsException extends RuntimeException {
    public ResourceAlreadyExistsException() {

    }

    public ResourceAlreadyExistsException(String message) {
        super(message);
    }
}
