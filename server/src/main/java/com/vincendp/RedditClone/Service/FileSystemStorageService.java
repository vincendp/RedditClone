package com.vincendp.RedditClone.Service;

import com.vincendp.RedditClone.Config.StorageProperties;
import com.vincendp.RedditClone.Exception.ResourceNotFoundException;
import com.vincendp.RedditClone.Exception.StorageException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileSystemStorageService implements StorageService {

    private Path location;

    public FileSystemStorageService(StorageProperties properties){
        this.location = Paths.get(properties.getLocation());
    }

    @Override
    @PostConstruct
    public void init() {
        try{
            if(!Files.isDirectory(Paths.get(location.toString()))){
                Files.createDirectory(location);
            }
        }
        catch(Exception e){
            throw new StorageException("Error: Could not initialize directory");
        }
    }

    @Override
    public String store(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFilename = UUID.randomUUID().toString() + extension;
        try{
            if(file.isEmpty()){
                throw new StorageException("Error: File is empty");
            }
            try(InputStream inputStream = file.getInputStream()){
                Files.copy(inputStream, location.resolve(newFilename), StandardCopyOption.REPLACE_EXISTING);
            }
        }
        catch (IOException e) {
            throw new StorageException("Error: Failed to store file");
        }

        return newFilename;
    }

    @Override
    public Path load(String filename) {
        return location.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new ResourceNotFoundException("Error: Could not read file " + filename);
            }
        }
        catch (MalformedURLException e) {
            throw new ResourceNotFoundException("Error: Could not read file: " + filename);
        }
    }
}
