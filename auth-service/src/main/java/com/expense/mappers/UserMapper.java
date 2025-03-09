package com.expense.mappers;

import com.expense.dtos.UserRequestDto;
import com.expense.dtos.UserResponseDto;
import com.expense.utils.MapperUtils;
import org.keycloak.representations.idm.UserRepresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {MapperUtils.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "password", target = "credentials", qualifiedByName = "mapperUtils" )
    UserRepresentation dtoToEntity(UserRequestDto userRequestDto);

    UserResponseDto entityToDto(UserRepresentation entity);
}
