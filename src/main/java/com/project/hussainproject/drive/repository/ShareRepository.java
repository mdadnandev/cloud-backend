package com.project.hussainproject.drive.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.hussainproject.drive.model.Share;
import com.project.hussainproject.drive.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ShareRepository extends JpaRepository<Share, UUID> {
    @Query("SELECT s FROM Share s JOIN FETCH s.file f JOIN FETCH s.sharedBy u WHERE s.sharedWith = :user AND f.isTrashed = false")
    List<Share> findBySharedWith(@Param("user") User user);

    Optional<Share> findByFileIdAndSharedWithId(UUID fileId, UUID userId);
}
