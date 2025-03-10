package com.expense.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Spring has a default prefix for roles: ROLE_
 * @see SecurityExpressionRoot
 * For smooth working with keycloak the spring's prefix should be added to Role name:
 * @see com.expense.configs.JwtAuthConverter#SPRING_ROLE_PREFIX
 */
@Component
@RequiredArgsConstructor
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    private final static String SPRING_ROLE_PREFIX = "ROLE_";
    private final static String CLAIM_KEY = "resource_access";
    private final static String ROLES_KEY = "roles";
    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
    private final KeycloakProperties keycloakProperties;

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Set<GrantedAuthority> authorities =
                Stream.concat(jwtGrantedAuthoritiesConverter.convert(jwt).stream(),
                                extractResourceRoles(jwt).stream())
                        .collect(Collectors.toSet());

        return new JwtAuthenticationToken(jwt,
                authorities,
                Optional.ofNullable(keycloakProperties.getPrincipalAttribute()).orElse(JwtClaimNames.SUB));
    }

    // returns ResourceRoles containing converted RoleNames
    private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt jwt) {
        if (jwt.getClaim(CLAIM_KEY) == null) {
            return Set.of();
        }

        Map<String, Object> resourceAccess = jwt.getClaim(CLAIM_KEY);
        if (resourceAccess.get(keycloakProperties.getClientId()) == null) {
            return Set.of();
        }

        Map<String, Object> resource = (Map<String, Object>) resourceAccess.get(keycloakProperties.getClientId());
        Collection<String> resourceRoles = (Collection<String>) resource.get(ROLES_KEY);

        return resourceRoles.stream()
                .map(role -> new SimpleGrantedAuthority(SPRING_ROLE_PREFIX + role))
                .collect(Collectors.toSet());
    }
}
