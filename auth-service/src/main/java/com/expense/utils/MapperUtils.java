package com.expense.utils;

import org.keycloak.representations.idm.CredentialRepresentation;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Named("mapperUtils")
public class MapperUtils {

    @Named("passwordToCredentialRepresentation")
    public List<CredentialRepresentation> passwordToCredentialRepresentation(String password) throws Exception {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setTemporary(false);
        credential.setValue(password);
        return List.of(credential);
    }
}
