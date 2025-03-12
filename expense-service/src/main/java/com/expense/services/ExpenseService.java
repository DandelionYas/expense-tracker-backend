package com.expense.services;

import com.expense.dtos.ExpenseDto;
import com.expense.dtos.ExpenseResponseDto;
import com.expense.exceptions.ExpenseNotFoundException;
import com.expense.mappers.ExpenseMapper;
import com.expense.models.Expense;
import com.expense.models.ExpenseCategory;
import com.expense.models.User;
import com.expense.repositories.ExpenseCategoryRepository;
import com.expense.repositories.ExpenseRepository;
import com.expense.repositories.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

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
        expense.setUser(existingUser.orElseGet(() -> usersRepository.save(expense.getUser())));

        Optional<ExpenseCategory> existingCategory = categoryRepository.findByName(expenseDto.category().name());
        expense.setCategory(existingCategory.orElseGet(() -> categoryRepository.save(expense.getCategory())));

        Expense saved = expenseRepository.save(expense);
        return ExpenseMapper.INSTANCE.entityToDto(saved);
    }

    @Transactional
    public void deleteExpense(UUID expenseId) {
        Optional<Expense> existingExpense = expenseRepository.findById(expenseId);
        if (existingExpense.isEmpty()) {
            throw new ExpenseNotFoundException("Expense not found. id: %s".formatted(expenseId));
        }

        expenseRepository.deleteById(expenseId);
    }
}