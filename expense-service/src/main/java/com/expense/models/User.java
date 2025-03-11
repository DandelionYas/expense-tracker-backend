package com.expense.models;


import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    private int id;

    @CreationTimestamp
    private Instant createdAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Expense> expenses;
}
