package com.project.hussainproject.drive.controller;

import com.project.hussainproject.drive.model.FileMetadata;
import com.project.hussainproject.drive.model.User;
import com.project.hussainproject.drive.repository.FileRepository;
import com.project.hussainproject.drive.service.FileService;
import com.project.hussainproject.drive.service.StorageService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final StorageService storageService;
    private final FileRepository fileRepository;
    private final FileService fileService;


    // GET FILES
    @GetMapping
    public ResponseEntity<List<FileMetadata>> getFiles(
            @RequestParam(required = false) UUID folderId,
            @AuthenticationPrincipal User user) {

        return ResponseEntity.ok(
                fileService.getFiles(folderId, user)
        );
    }


    // INIT UPLOAD
    @PostMapping("/init-upload")
    public ResponseEntity<Map<String, String>> initUpload(
            @RequestParam String fileName,
            @RequestParam String mimeType) {

        String storageKey =
                UUID.randomUUID().toString() + "-" + fileName;

        String uploadUrl =
                storageService.generateUploadUrl(
                        storageKey,
                        mimeType
                );

        return ResponseEntity.ok(
                Map.of(
                        "uploadUrl", uploadUrl,
                        "storageKey", storageKey
                )
        );
    }


    // COMPLETE UPLOAD
    @PostMapping("/complete-upload")
    public ResponseEntity<FileMetadata> completeUpload(
            @AuthenticationPrincipal User user,
            @RequestParam String fileName,
            @RequestParam String storageKey,
            @RequestParam Long size,
            @RequestParam String mimeType) {

        FileMetadata fileMetadata =
                FileMetadata.builder()
                        .originalName(fileName)
                        .storageKey(storageKey)
                        .size(size)
                        .mimeType(mimeType)
                        .owner(user)
                        .isTrashed(false)
                        .build();

        FileMetadata savedFile =
                fileRepository.save(fileMetadata);

        return ResponseEntity.ok(savedFile);
    }


    // DOWNLOAD / VIEW FILE
    @GetMapping("/{id}/download-url")
    public ResponseEntity<Map<String, String>> getDownloadUrl(
            @PathVariable UUID id,
            @AuthenticationPrincipal User user) {

        FileMetadata file = fileRepository.findById(id)
                .filter(f ->
                        f.getOwner()
                                .getId()
                                .equals(user.getId())
                )
                .orElseThrow(() ->
                        new RuntimeException(
                                "File not found or access denied"
                        )
                );

        String downloadUrl =
                storageService.generateDownloadUrl(
                        file.getStorageKey()
                );

        return ResponseEntity.ok(
                Map.of("downloadUrl", downloadUrl)
        );
    }


    // RENAME
    @PutMapping("/{id}/rename")
    public ResponseEntity<FileMetadata> renameFile(
            @PathVariable UUID id,
            @RequestParam String newName,
            @AuthenticationPrincipal User user) {

        return ResponseEntity.ok(
                fileService.renameFile(
                        id,
                        newName,
                        user
                )
        );
    }


    // MOVE
    @PutMapping("/{id}/move")
    public ResponseEntity<FileMetadata> moveFile(
            @PathVariable UUID id,
            @RequestParam(required = false) UUID folderId,
            @AuthenticationPrincipal User user) {

        return ResponseEntity.ok(
                fileService.moveFile(
                        id,
                        folderId,
                        user
                )
        );
    }
}