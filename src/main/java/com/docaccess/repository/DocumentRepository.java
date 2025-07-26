package com.docaccess.repository;


import com.docaccess.entity.Document;
import com.docaccess.entity.User;
import com.docaccess.enums.PermissionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

@Query("""
        SELECT d FROM Document d
        JOIN d.permissions p
        WHERE p.user.username = :username
         AND p.permission = :permission
         AND  p.document.status = 'ACTIVE'
    """)
List<Document> findDocumentsByUserWithPermission(
        @Param("username") String username,
        @Param("permission") PermissionType permission
);




}