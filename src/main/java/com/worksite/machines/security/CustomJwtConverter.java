package com.worksite.machines.security;

import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.ArrayList;
import java.util.List;

public class CustomJwtConverter implements Converter<Jwt, CustomJwt> {

    @Override
    public CustomJwt convert(@NonNull Jwt source) {
        List<GrantedAuthority> grantedAuthorities = extractAuthorities(source);
        CustomJwt customJwt = new CustomJwt(source, grantedAuthorities) ;
        customJwt.setFirstname(source.getClaimAsString("given_name"));
        customJwt.setLastname(source.getClaimAsString("family_name"));
        return customJwt;
    }

    private List<GrantedAuthority> extractAuthorities(Jwt source) {
        var result = new ArrayList<GrantedAuthority>();
        var realmAccess = source.getClaimAsMap("realm_access");
        if (realmAccess != null && realmAccess.get("roles") != null) {
            var roles = realmAccess.get("roles");
            if (roles instanceof  List<?> l) {
                for (Object role : l) {
                    result.add(new SimpleGrantedAuthority("ROLE_" + role));
                }
            }
        }
        return result;
    }
}
