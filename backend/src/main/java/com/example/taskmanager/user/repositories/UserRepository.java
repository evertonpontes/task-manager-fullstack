package com.example.taskmanager.user.repositories;

import com.example.taskmanager.user.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.accounts WHERE u.email = :email")
    Optional<User> findByEmailWithAccounts(@Param("email") String email);
}
