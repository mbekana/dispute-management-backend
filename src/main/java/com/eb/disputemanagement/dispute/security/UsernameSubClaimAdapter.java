package com.eb.disputemanagement.dispute.security;


import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.jwt.MappedJwtClaimSetConverter;

import java.util.Collections;
import java.util.Map;

class UsernameSubClaimAdapter implements Converter<Map<String, Object>, Map<String, Object>> {

    private final MappedJwtClaimSetConverter delegate = MappedJwtClaimSetConverter.withDefaults(Collections.emptyMap());

    @Override
    public Map<String, Object> convert(@NotNull Map<String, Object> claims) {
        Map<String, Object> convertedClaims = this.delegate.convert(claims);
        String username = (String) convertedClaims.get("preferred_username");
        convertedClaims.put("sub", username);
        return convertedClaims;
    }

}
