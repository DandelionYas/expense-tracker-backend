package com.expense.mappers;

import com.expense.dtos.AccessTokenDto;
import org.keycloak.representations.AccessTokenResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AccessTokenMapper {
    public AccessTokenMapper INSTANCE = Mappers.getMapper(AccessTokenMapper.class);
    AccessTokenDto entityToDto(AccessTokenResponse entity);
}
