package com.example.taskmanager.user.repositories;

import com.example.taskmanager.user.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {
    Optional<Account> findByProviderAndProviderAccountId(String provider, String providerAccountId);
}
