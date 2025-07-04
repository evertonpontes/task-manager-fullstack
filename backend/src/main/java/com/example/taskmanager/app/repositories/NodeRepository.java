package com.example.taskmanager.app.repositories;

import com.example.taskmanager.app.entities.Node;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface NodeRepository extends JpaRepository<Node, UUID> {
    @Query("SELECT n FROM Node n WHERE n.parentNode IS NULL AND n.user.id = :id")
    List<Node> findAllByUserId(UUID id);
    
    @Query("SELECT MAX(n.rank) FROM Node n WHERE n.parentNode.id = :parentNodeId")
    Optional<Long> findMaxRankByParentNodeId(@Param("parentNodeId") UUID parentNodeId);

    @Query("SELECT MAX(n.rank) FROM Node n WHERE n.parentNode IS NULL AND n.user.id = :userId")
    Optional<Long> findMaxRankForRootNodes(@Param("userId") UUID userId);
}
