package com.project.hussainproject.drive.service;

import com.project.hussainproject.drive.dto.CreateFolderRequest;
import com.project.hussainproject.drive.model.Folder;
import com.project.hussainproject.drive.model.User;
import com.project.hussainproject.drive.repository.FolderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FolderService {
    private final FolderRepository folderRepository;

    public Folder createFolder(CreateFolderRequest request, User user) {
        Folder parent = null;

        if(request.getParentId() != null) {
            parent = folderRepository.findById(UUID.fromString(request.getParentId()))
                    .filter(f->f.getOwner().getId().equals(user.getId()))
                    .orElseThrow(()->new RuntimeException("Parent folder not fund or accesss denied"));
        }

        Folder folder = Folder.builder()
                .name(request.getName())
                .parentFolder(parent)
                .owner(user)
                .isTrashed(false)
                .build();

        return folderRepository.save(folder);
    }

    public List<Folder> getFolders(UUID parentId, User user) {
        if (parentId == null) {
            return folderRepository.findByOwnerAndParentFolderIsNullAndIsTrashedFalse(user);
        } else {
            return folderRepository.findByOwnerAndParentFolderIdAndIsTrashedFalse(user, parentId);
        }
    }
}

