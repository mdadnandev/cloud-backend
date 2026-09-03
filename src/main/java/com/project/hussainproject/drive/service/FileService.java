package com.project.hussainproject.drive.service;


import com.project.hussainproject.drive.model.FileMetadata;
import com.project.hussainproject.drive.model.Folder;
import com.project.hussainproject.drive.model.User;
import com.project.hussainproject.drive.repository.FileRepository;
import com.project.hussainproject.drive.repository.FolderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {


    private final FileRepository fileRepository;
    private final FolderRepository folderRepository;

    public List<FileMetadata> getFiles(UUID folderId, User user) {
        if (folderId == null) {
            return fileRepository.findByOwnerAndFolderIsNullAndIsTrashedFalse(user);
        } else {
            return fileRepository.findByOwnerAndFolderIdAndIsTrashedFalse(user, folderId);
        }
    }

    public List<FileMetadata> getTrashedFiles(User user) {
        return fileRepository.findByOwnerAndIsTrashedTrue(user);
    }

    public FileMetadata renameFile(UUID fileId, String newName, User user) {
        FileMetadata  file = getFileOwnedByUser(fileId, user);
        file.setOriginalName(newName);
        return fileRepository.save(file);

    }

    public FileMetadata moveFile(UUID fileId, UUID targetFolderId, User user) {
          FileMetadata  file = getFileOwnedByUser(fileId, user);
          Folder targetFolder = null;

          if(targetFolderId != null) {
              targetFolder = folderRepository.findById(targetFolderId)
                      .filter(f->f.getOwner().getId().equals(user.getId()))
                      .orElseThrow(()->new RuntimeException("Target folder not found"));

          }

          file.setFolder(targetFolder);
          return fileRepository.save(file);

    }

    public void trashFile(UUID fileId, User user) {
        FileMetadata  file = getFileOwnedByUser(fileId, user);
        file.setTrashed(true);
        fileRepository.save(file);

    }





    private FileMetadata getFileOwnedByUser(UUID fileId, User user) {
        return fileRepository.findById(fileId)
                .filter(f->f.getOwner().getId().equals(user.getId()))
                .orElseThrow(()->new RuntimeException("File not found or access denied"));
    }

}

