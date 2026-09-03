package com.project.hussainproject.drive.controller;


import com.project.hussainproject.drive.dto.CreateFolderRequest;
import com.project.hussainproject.drive.model.Folder;
import com.project.hussainproject.drive.model.User;
import com.project.hussainproject.drive.service.FolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/folders")
@RequiredArgsConstructor
public class FolderController {

    private final FolderService folderService;

    @PostMapping
    public ResponseEntity<Folder> createFolder(
            @RequestBody CreateFolderRequest request,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(folderService.createFolder(request, user));
    }

    @GetMapping
    public ResponseEntity<List<Folder>> getFolders(
            @RequestParam(required = false) UUID parentId,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(folderService.getFolders(parentId, user));
    }
}

