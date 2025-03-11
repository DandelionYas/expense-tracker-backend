package com.expense.mappers;

import com.expense.dtos.ExpenseCategoryDto;
import com.expense.dtos.ExpenseDto;
import com.expense.dtos.ExpenseResponseDto;
import com.expense.models.Expense;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {UserMapper.class, ExpenseCategoryDto.class})
public interface ExpenseMapper {
    ExpenseMapper INSTANCE = Mappers.getMapper(ExpenseMapper.class);

    ExpenseResponseDto entityToDto(Expense expense);

    Expense dtoToEntity(ExpenseDto dto);
}
