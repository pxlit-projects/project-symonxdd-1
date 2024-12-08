// package be.pxl.services.controller;

// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;
// import org.springframework.web.multipart.MultipartFile;
// import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

// import java.io.File;
// import java.io.IOException;
// import java.nio.file.Files;
// import java.nio.file.Path;
// import java.nio.file.Paths;

// @RestController
// @RequestMapping("/api/images")
// public class ImageController {

// private static final String IMAGE_DIR =
// "src/main/resources/static/user_uploads/";

// @PostMapping("/upload")
// public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile
// file) {
// if (file.isEmpty()) {
// return new ResponseEntity<>("File is empty", HttpStatus.BAD_REQUEST);
// }

// try {
// String fileName = file.getOriginalFilename();
// Path path = Paths.get(IMAGE_DIR, fileName);
// Files.write(path, file.getBytes());

// String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
// .path("/images/")
// .path(fileName)
// .toUriString();

// return new ResponseEntity<>(imageUrl, HttpStatus.OK);
// } catch (IOException e) {
// return new ResponseEntity<>(e.getMessage(),
// HttpStatus.INTERNAL_SERVER_ERROR);
// }
// }
// }
