package com.project.hussainproject.drive.controller;


import com.project.hussainproject.drive.dto.CreateFolderRequest;
import com.project.hussainproject.drive.model.Folder;
import com.project.hussainproject.drive.model.User;
import com.project.hussainproject.drive.repository.FolderRepository;
import com.project.hussainproject.drive.service.FolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/folders")
@RequiredArgsConstructor
public class FolderController {

    private final FolderService folderService;

    @PostMapping
    public ResponseEntity<Folder> createFolder
            (@RequestBody CreateFolderRequest request,
             @AuthenticationPrincipal User user)

    {
        return ResponseEntity.ok(folderService.createFolder(request,user));
    }
}
