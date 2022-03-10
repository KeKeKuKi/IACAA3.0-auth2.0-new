package com.gapache.security.config;

import com.gapache.commons.security.RSAUtils;
import com.gapache.security.checker.AsyncSecurityChecker;
import com.gapache.security.checker.SecurityChecker;
import com.gapache.security.checker.impl.DefaultSignChecker;
import com.gapache.security.checker.impl.LocalAsyncSecurityChecker;
import com.gapache.security.checker.impl.LocalSecurityChecker;
import com.gapache.security.core.AsyncRedisAuthorizeInfoManager;
import com.gapache.security.core.RedisAuthorizeInfoManager;
import com.gapache.security.interfaces.AsyncAuthorizeInfoManager;
import com.gapache.security.interfaces.AuthorizeInfoManager;
import com.gapache.security.properties.SecurityProperties;
import com.gapache.vertx.redis.support.SimpleRedisRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;

/**
 * @author HuSen
 * @since 2020/7/31 1:14 下午
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(SecurityProperties.class)
public class SecurityAutoConfiguration {

    private final SecurityProperties securityProperties;

    public SecurityAutoConfiguration(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    @ConditionalOnProperty("com.gapache.security.public-key")
    @Bean("localAsyncSecurityChecker")
    public AsyncSecurityChecker asyncSecurityChecker(SimpleRedisRepository simpleRedisRepository) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        log.info("启用公钥解密############AsyncSecurityChecker");
        return new LocalAsyncSecurityChecker(
                RSAUtils.getPublicKey(securityProperties.getPublicKey().trim().replaceAll(" ", "")),
                asyncAuthorizeInfoManager(simpleRedisRepository), simpleRedisRepository);
    }

    @Bean("localSecurityChecker")
    public SecurityChecker localSecurityChecker(SimpleRedisRepository simpleRedisRepository) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        return new LocalSecurityChecker(asyncSecurityChecker(simpleRedisRepository));
    }

    @Bean
    @ConditionalOnProperty("com.gapache.security.private-key")
    public PrivateKey privateKey() throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        log.info("启用私钥进行签名############");
        return RSAUtils.getPrivateKey(securityProperties.getPrivateKey().trim().replaceAll(" ", ""));
    }

    @Bean
    public DefaultSignChecker defaultSignChecker(StringRedisTemplate stringRedisTemplate) {
        return new DefaultSignChecker(stringRedisTemplate);
    }

    @Bean
    public AsyncAuthorizeInfoManager asyncAuthorizeInfoManager(SimpleRedisRepository simpleRedisRepository) {
        return new AsyncRedisAuthorizeInfoManager(simpleRedisRepository);
    }

    @Bean
    public AuthorizeInfoManager authorizeInfoManager(SimpleRedisRepository simpleRedisRepository) {
        return new RedisAuthorizeInfoManager(asyncAuthorizeInfoManager(simpleRedisRepository), simpleRedisRepository);
    }
}
