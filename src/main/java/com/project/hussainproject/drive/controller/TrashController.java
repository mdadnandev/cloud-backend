package com.project.hussainproject.drive.controller;

import com.project.hussainproject.drive.model.FileMetadata;
import com.project.hussainproject.drive.model.User;
import com.project.hussainproject.drive.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/trash")
@RequiredArgsConstructor
public class TrashController {

    private final FileService fileService;

    @GetMapping("/files")
    public ResponseEntity<List<FileMetadata>> getTrashedFiles(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(fileService.getTrashedFiles(user));
    }

    @PostMapping("/files/{id}")
    public ResponseEntity<Void> trashFile(
            @PathVariable UUID id,
            @AuthenticationPrincipal User user
            ){
        fileService.trashFile(id, user);
        return ResponseEntity.ok().build();
    }
}

