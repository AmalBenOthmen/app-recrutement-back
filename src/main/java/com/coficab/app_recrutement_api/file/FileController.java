package com.coficab.app_recrutement_api.file;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/files")
public class FileController {

    private final Path fileStorageLocation = Paths.get("C:\\Users\\lenovo\\Desktop\\app recrutement\\uploads")
            .toAbsolutePath().normalize();
    // Update with your actual storage location

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            Path targetLocation = fileStorageLocation.resolve(fileName).normalize();

            // Create directories if they don't exist
            Files.createDirectories(targetLocation.getParent());

            Files.copy(file.getInputStream(), targetLocation);
            return ResponseEntity.ok(fileName);
        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not upload file.");
        }
    }

    @GetMapping("/cv/{fileName:.+}")
    public ResponseEntity<Resource> downloadCV(@PathVariable String fileName) {
        Path filePath = fileStorageLocation.resolve("cv").resolve(fileName).normalize();
        return downloadFile(filePath);
    }

    @GetMapping("/additionalDocuments/{fileName:.+}")
    public ResponseEntity<Resource> downloadAdditionalDocuments(@PathVariable String fileName) {
        Path filePath = fileStorageLocation.resolve("additionalDocuments").resolve(fileName).normalize();
        return downloadFile(filePath);
    }

    private ResponseEntity<Resource> downloadFile(Path filePath) {
        try {
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_PDF)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
