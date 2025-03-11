package com.expense.repositories;

import com.expense.models.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UsersRepository extends PagingAndSortingRepository<User, UUID> {

}
