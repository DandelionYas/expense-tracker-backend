package com.expense.models;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    private int id;

    @CreationTimestamp
    private Instant createdAt;

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
    private List<Expense> expenses;
}
