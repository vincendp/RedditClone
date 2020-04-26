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
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "inline").body(resource);
    }
}
