package com.docaccess.repository;


import com.docaccess.entity.Permission;
import com.docaccess.enums.PermissionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {


    @Query("""
    SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END
    FROM Permission p
    WHERE p.user.username = :username
      AND p.document.id = :documentId
      AND p.permission = :permission
      AND p.document.status = 'ACTIVE'
""")
    boolean existsPermission(@Param("username") String username,
                             @Param("documentId") Long documentId,
                             @Param("permission") PermissionType permission);

    @Query("""
    SELECT p.document.id
    FROM Permission p
    WHERE p.user.username = :username
      AND p.permission = :permission
      AND p.document.id IN :documentIds
      AND  p.document.status = 'ACTIVE'
""")
    Set<Long> findDocumentIdsByUsernameAndPermissionInDocumentList(
            @Param("username") String username,
            @Param("permission") PermissionType permission,
            @Param("documentIds") List<Long> documentIds
    );



}
