package com.tastytown.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.NoSuchElementException;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/images")
public class ImageController {

    private static final String IMAGE_DIR = "images"; // relative to root

    // @GetMapping("/{filename:.+}")
    // public ResponseEntity<Resource> serveImage(@PathVariable String filename) {
    //     try {
    //         Path imagePath = Paths.get(IMAGE_DIR).resolve(filename).normalize().toAbsolutePath();

    //         if (!Files.exists(imagePath) || !Files.isReadable(imagePath)) {
    //             return ResponseEntity.notFound().build();
    //         }

    //         Resource resource = new UrlResource(imagePath.toUri());
    //         String contentType = Files.probeContentType(imagePath);

    //         return ResponseEntity.ok()
    //                 .contentType(MediaType.parseMediaType(contentType != null ? contentType : "application/octet-stream"))
    //                 .body(resource);

    //     } catch (IOException e) {
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    //     }
    // }

    @GetMapping(value = "/{filename}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public ResponseEntity<byte[]> serveImage(@PathVariable String filename) throws Exception{
        if(filename == null)  {
            throw new NoSuchElementException("Image not present");
        }

        FileInputStream fis = new FileInputStream(IMAGE_DIR + File.separator + filename);
        byte[] image = fis.readAllBytes();
        fis.close();

        return ResponseEntity.ok(image);
    }
}