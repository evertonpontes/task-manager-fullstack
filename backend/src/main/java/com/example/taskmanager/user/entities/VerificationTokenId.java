package com.example.taskmanager.user.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class VerificationTokenId implements Serializable {
    @Column(nullable = false)
    private String identifier;
    @Column(nullable = false)
    private String code;
}
