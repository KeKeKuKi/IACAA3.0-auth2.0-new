package com.gapache.cloud.auth.server;

import com.gapache.cloud.auth.server.dao.entity.ClientEntity;
import com.gapache.cloud.auth.server.dao.entity.UserClientRelationEntity;
import com.gapache.cloud.auth.server.dao.repository.client.ClientRepository;
import com.gapache.cloud.auth.server.dao.repository.user.UserClientRelationRepository;
import com.gapache.cloud.auth.server.utils.DynamicPropertyUtils;
import com.gapache.commons.utils.ContextUtils;
import com.gapache.redis.lock.DistributedLock;
import com.gapache.security.annotation.EnableAuthResourceServer;
import com.gapache.user.sdk.annotation.EnableUserServerFeign;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.SmartLifecycle;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

import java.util.concurrent.TimeUnit;

/**
 * 首先当某个应用要接入此认证中心时，我们为其颁发一个clientId和clientSecret，如果需要签名的会为其颁发公私钥
 * 签名规则见签名规则
 * 颁发的client会自动拥有应用的所有scope，当然这需要我们标识出这个应用是谁
 * 通过配置com.gapache.security.client-id即可实现
 *
 * TODO 画一个认证中心架构和流程图，判断设计是否合理
 * TODO 出一个认证中心的说明书
 *
 * @author HuSen
 * @since 2020/7/30 10:15 下午
 */
@Slf4j
@SpringBootApplication
@EnableDiscoveryClient
@EnableAuthResourceServer("AuthCenter")
@EnableUserServerFeign
public class AuthCenterServer implements SmartLifecycle {

    private static final String ACS_CREATE_SELF_LOCK = "ACS_CREATE_SELF_LOCK";

    private boolean running;

    public static void main(String[] args) {
        SpringApplication.run(AuthCenterServer.class, args);
    }

    @Override
    public void start() {
        this.running = true;
        DistributedLock lock = DistributedLock.getLock(ACS_CREATE_SELF_LOCK);

        boolean tryLock = false;
        try {
            tryLock = lock.tryLock(300, TimeUnit.SECONDS);
            if (tryLock) {
                String authCenterClientId = DynamicPropertyUtils.getString("auth.center.client-id");
                ApplicationContext applicationContext = ContextUtils.getApplicationContext();

                createSelf(authCenterClientId, applicationContext);

                PasswordEncoder passwordEncoder = applicationContext.getBean(PasswordEncoder.class);
                String adminUsername = DynamicPropertyUtils.getString("admin.username");
                String adminPassword = DynamicPropertyUtils.getString("admin.password");
                String adminIntroduction = DynamicPropertyUtils.getString("admin.introduction");
                String adminAvatar = DynamicPropertyUtils.getString("admin.avatar");

                log.info("{}.{}.{}.{}", adminUsername, passwordEncoder.encode(adminPassword), adminIntroduction, adminAvatar);

                cacheClientPrivateKey(applicationContext);
            }
        } catch (InterruptedException e) {
            log.error("start error.", e);
        } finally {
            if (tryLock) {
                lock.unlock();
            }
        }
    }

    private void cacheClientPrivateKey(ApplicationContext applicationContext) {
        ClientRepository clientRepository = applicationContext.getBean(ClientRepository.class);
        StringRedisTemplate stringRedisTemplate = applicationContext.getBean(StringRedisTemplate.class);
        clientRepository.findAll().forEach(c -> {
            if (StringUtils.isNotBlank(c.getPrivateKey())) {
                stringRedisTemplate.opsForValue().set("CLIENT_PRIVATE_KEY:" + c.getClientId(), c.getPrivateKey());
            }
        });
    }

    private void createSelf(String authCenterClientId, ApplicationContext applicationContext) {
        String authCenterId = DynamicPropertyUtils.getString("auth.center.id");
        String authCenterName = DynamicPropertyUtils.getString("auth.center.name");
        log.info("{}.{}", authCenterId, authCenterName);
        PasswordEncoder passwordEncoder = applicationContext.getBean(PasswordEncoder.class);
        ClientRepository clientRepository = applicationContext.getBean(ClientRepository.class);
        UserClientRelationRepository userClientRelationRepository = applicationContext.getBean(UserClientRelationRepository.class);
        Assert.notNull(clientRepository, "clientRepository required");
        ClientEntity clientEntity = clientRepository.findByClientId(authCenterClientId);
        if (clientEntity == null) {
            clientEntity = new ClientEntity();
        }
        clientEntity.setClientId(authCenterClientId);
        clientEntity.setAutoGrant(false);
        clientEntity.setRefreshTokenTimeout(-1L);
        clientEntity.setTimeout(1000L * 60 * 60 * 24 * 30);
        clientEntity.setRefreshTokenTimeout(1000L * 60 * 60 * 24 * 60);
        clientEntity.setSecret(passwordEncoder.encode("12345678"));
        clientRepository.save(clientEntity);

        UserClientRelationEntity userClientRelationEntity = userClientRelationRepository.findByUserIdAndClientId(0L, clientEntity.getId());
        if (userClientRelationEntity == null) {
            userClientRelationEntity = new UserClientRelationEntity();
            userClientRelationEntity.setUserId(0L);
            userClientRelationEntity.setClientId(clientEntity.getId());
            userClientRelationRepository.save(userClientRelationEntity);
        }
    }

    @Override
    public void stop() {
        this.running = false;
    }

    @Override
    public boolean isRunning() {
        return running;
    }
}
