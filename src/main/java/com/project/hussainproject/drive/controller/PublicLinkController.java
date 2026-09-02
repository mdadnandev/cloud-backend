package com.project.hussainproject.drive.controller;


import com.project.hussainproject.drive.dto.PublicLinkRequest;
import com.project.hussainproject.drive.model.PublicLink;
import com.project.hussainproject.drive.model.User;
import com.project.hussainproject.drive.service.PublicLinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/public-links")
@RequiredArgsConstructor
public class PublicLinkController {
    private final PublicLinkService publicLinkService;

    @PostMapping
    public ResponseEntity<PublicLink> createLink
            (@RequestBody PublicLinkRequest request,
             @AuthenticationPrincipal User user
            ) {
        return ResponseEntity.ok(publicLinkService.createLink(request, user));
    }
}
