package ru.otus.spring.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@ConfigurationProperties(prefix = "jwt")
public record RsaKeyRecord(RSAPublicKey rsaPublicKey, RSAPrivateKey rsaPrivateKey) {

}