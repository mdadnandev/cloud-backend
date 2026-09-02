package com.project.hussainproject.drive.controller;

import com.project.hussainproject.drive.model.User;

import com.project.hussainproject.drive.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.UUID;



@RestController
@RequestMapping("/api/trash")
@RequiredArgsConstructor
public class TrashController {

    private final FileService fileService;

    @PostMapping("/files/{id}")
    public ResponseEntity<Void> trashFile(
            @PathVariable UUID id,
            @AuthenticationPrincipal User user
            ){
        fileService.trashFile(id, user);
        return ResponseEntity.ok().build();
    }
}
