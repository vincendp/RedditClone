package com.vincendp.RedditClone.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileSystemStorageService implements StorageService {

    private Path path;

    public FileSystemStorageService(String path){
        this.path = Paths.get(path);
    }

    @Override
    public void init() {
        try{
            Files.createDirectory(path);
        }
        catch(Exception e){
            System.out.println(e);
        }
    }

    @Override
    public void store(MultipartFile file) {

    }

    @Override
    public Path load(String filename) {
        return null;
    }
}
