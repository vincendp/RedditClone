package com.vincendp.RedditClone.Controller;

import com.vincendp.RedditClone.Service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.apache.commons.io.FilenameUtils;

@RestController
@RequestMapping("image")
public class ImageController {

    private StorageService storageService;

    @Autowired
    public ImageController(StorageService storageService){
        this.storageService = storageService;
    }

    @GetMapping("/{path}")
    ResponseEntity loadImage(@PathVariable String path){
        Resource resource = storageService.loadAsResource(path);
        String contentType;

        switch(FilenameUtils.getExtension(path)){
            case "jpg":
                contentType = "image/jpg";
                break;
            case "jpeg":
                contentType = "image/jpeg";
                break;
            case "png":
                contentType = "image/png";
                break;
            default:
                throw new IllegalArgumentException("Error: Not an image");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline");
        headers.set(HttpHeaders.CONTENT_TYPE, contentType);
        return ResponseEntity.ok().headers(headers).body(resource);
    }
}
