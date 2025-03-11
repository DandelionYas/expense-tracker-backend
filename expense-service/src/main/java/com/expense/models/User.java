package com.expense.models;


import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * User id is the Keycloak userId
 * User details is saved in Keycloak
 * No extra information is saved in here for now
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    private UUID id;

    @CreationTimestamp
    private Instant createdAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Expense> expenses;
}
