package com.expense.services;

import com.expense.dtos.ExpenseDto;
import com.expense.dtos.ExpenseResponseDto;
import com.expense.mappers.ExpenseMapper;
import com.expense.models.Expense;
import com.expense.models.ExpenseCategory;
import com.expense.models.User;
import com.expense.repositories.ExpenseCategoryRepository;
import com.expense.repositories.ExpenseRepository;
import com.expense.repositories.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UsersRepository usersRepository;
    private final ExpenseCategoryRepository categoryRepository;

    @Transactional
    public ExpenseResponseDto addExpense(ExpenseDto expenseDto) {
        Expense expense = ExpenseMapper.INSTANCE.dtoToEntity(expenseDto);

        Optional<User> existingUser = usersRepository.findById(expense.getUser().getId());
        if (existingUser.isEmpty()) {
            expense.setUser(usersRepository.save(expense.getUser()));
        }

        Optional<ExpenseCategory> existingCategory = categoryRepository.findByName(expenseDto.category().name());
        if (existingCategory.isEmpty()) {
            expense.setCategory(categoryRepository.save(expense.getCategory()));
        }

        Expense saved = expenseRepository.save(expense);
        return ExpenseMapper.INSTANCE.entityToDto(saved);
    }
}