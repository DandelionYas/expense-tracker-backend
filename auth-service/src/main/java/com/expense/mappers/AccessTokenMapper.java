package com.expense.mappers;

import com.expense.dtos.AccessTokenDto;
import org.keycloak.representations.AccessTokenResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AccessTokenMapper {
    AccessTokenMapper INSTANCE = Mappers.getMapper(AccessTokenMapper.class);
    @Mapping(source = "token", target = "accessToken")
    AccessTokenDto entityToDto(AccessTokenResponse entity);
}
