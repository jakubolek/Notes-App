package com.jakubolek.notesapp.infrastructure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtProperties {

    public String secret;

    public long accessTokenExpireTime;

    public long refreshTokenExpireTime;
}
