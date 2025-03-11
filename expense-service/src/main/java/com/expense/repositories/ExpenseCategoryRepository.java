package com.expense.repositories;

import com.expense.models.ExpenseCategory;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseCategoryRepository extends PagingAndSortingRepository<ExpenseCategory, Long> {
}
