package com.vincendp.RedditClone.Exception;

public class StorageException extends RuntimeException {
    public StorageException() {
    }

    public StorageException(String message) {
        super(message);
    }
}
