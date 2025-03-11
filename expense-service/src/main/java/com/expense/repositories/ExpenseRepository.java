package com.expense.repositories;

import com.expense.models.Expense;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseRepository extends PagingAndSortingRepository<Expense, String> {
}
