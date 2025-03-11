package com.expense.mappers;

import com.expense.dtos.ExpenseCategoryDto;
import com.expense.dtos.ExpenseCategoryResponseDto;
import com.expense.models.ExpenseCategory;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ExpenseCategoryMapper {
    ExpenseCategoryMapper INSTANCE = Mappers.getMapper(ExpenseCategoryMapper.class);

    ExpenseCategoryResponseDto entityToDto(ExpenseCategory entity);

    ExpenseCategory dtoToEntity(ExpenseCategoryDto dto);
}
