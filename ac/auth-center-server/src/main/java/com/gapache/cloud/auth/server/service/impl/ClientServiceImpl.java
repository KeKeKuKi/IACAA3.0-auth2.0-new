package com.gapache.cloud.auth.server.service.impl;

import com.gapache.cloud.auth.server.dao.entity.ClientEntity;
import com.gapache.cloud.auth.server.dao.entity.ResourceEntity;
import com.gapache.cloud.auth.server.dao.repository.client.ClientRepository;
import com.gapache.cloud.auth.server.dao.repository.resource.ResourceRepository;
import com.gapache.cloud.auth.server.model.ClientDetailsImpl;
import com.gapache.cloud.auth.server.service.ClientService;
import com.gapache.cloud.auth.server.utils.DynamicPropertyUtils;
import com.gapache.commons.model.IPageRequest;
import com.gapache.commons.model.PageResult;
import com.gapache.commons.model.ThrowUtils;
import com.gapache.jpa.PageHelper;
import com.gapache.security.model.ClientDTO;
import com.gapache.security.model.SecurityError;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author HuSen
 * @since 2020/7/31 5:24 下午
 */
@Service
public class ClientServiceImpl implements ClientService {

    @Resource
    private ClientRepository clientRepository;

    @Resource
    private ResourceRepository resourceRepository;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public ClientDetailsImpl findByClientId(String clientId) {
        ClientEntity clientEntity = clientRepository.findByClientId(clientId);
        if (clientEntity == null) {
            return null;
        }
        ClientDetailsImpl clientDetails = new ClientDetailsImpl();
        BeanUtils.copyProperties(clientEntity, clientDetails);
        clientDetails.setId(clientEntity.getId());

        // 空字符串标识所有 null表示没有
        String authCenterClientId = DynamicPropertyUtils.getString("auth.center.client-id");
        if (StringUtils.equals(authCenterClientId, clientId) || "".equals(clientEntity.getScopes())) {
            List<ResourceEntity> allResource = resourceRepository.findAll();
            clientDetails.setScopes(allResource.stream().map(ResourceEntity::fullScopeName).collect(Collectors.joining(",")));
        }
        return clientDetails;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean create(ClientDTO clientDTO) {
        Boolean exists = clientRepository.existsByClientId(clientDTO.getClientId());
        ThrowUtils.throwIfTrue(exists, SecurityError.CLIENT_EXISTED);

        ClientEntity entity = new ClientEntity();
        BeanUtils.copyProperties(clientDTO, entity, "secret");
        boolean all = Arrays.stream(clientDTO.getScopes().split(",")).anyMatch(scope -> StringUtils.equals(scope, "全部"));
        if (all) {
            entity.setScopes("");
        } else if (StringUtils.isBlank(clientDTO.getScopes())) {
            entity.setScopes(null);
        }
        entity.setSecret(passwordEncoder.encode(clientDTO.getSecret()));
        if (StringUtils.isNotBlank(clientDTO.getPrivateKey())) {
            stringRedisTemplate.opsForValue().set("CLIENT_PRIVATE_KEY:" + clientDTO.getClientId(), clientDTO.getPrivateKey());
        }
        return clientRepository.save(entity).getId() != null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean update(ClientDTO clientDTO) {
        ClientEntity clientEntity = clientRepository.findByClientId(clientDTO.getClientId());
        ThrowUtils.throwIfTrue(clientEntity == null, SecurityError.CLIENT_NOT_FOUND);

        clientEntity.setTimeout(clientDTO.getTimeout());
        clientEntity.setRefreshTokenTimeout(clientDTO.getRefreshTokenTimeout());
        clientEntity.setAutoGrant(clientDTO.getAutoGrant());
        boolean all = Arrays.stream(clientDTO.getScopes().split(",")).anyMatch(scope -> StringUtils.equals(scope, "全部"));
        if (all) {
            clientEntity.setScopes("");
        } else if (StringUtils.isBlank(clientDTO.getScopes())) {
            clientEntity.setScopes(null);
        } else {
            clientEntity.setScopes(clientDTO.getScopes());
        }
        clientEntity.setGrantTypes(clientDTO.getGrantTypes());
        clientEntity.setPrivateKey(clientDTO.getPrivateKey());
        clientEntity.setRedirectUrl(clientDTO.getRedirectUrl());

        clientRepository.save(clientEntity);
        return true;
    }

    @Override
    public ClientDetailsImpl findById(Long id) {
        Optional<ClientEntity> optional = clientRepository.findById(id);
        return optional.map(clientEntity -> {
            ClientDetailsImpl clientDetails = new ClientDetailsImpl();
            BeanUtils.copyProperties(clientEntity, clientDetails);
            return clientDetails;
        }).orElse(null);
    }

    @Override
    public ClientDTO findDtoByClientId(String clientId) {
        ClientEntity clientEntity = clientRepository.findByClientId(clientId);
        if (clientEntity == null) {
            return null;
        }

        ClientDTO dto = new ClientDTO();
        BeanUtils.copyProperties(clientEntity, dto);
        dto.setId(clientEntity.getId());
        dto.setSecret(null);
        return dto;
    }

    @Override
    public PageResult<ClientDTO> page(IPageRequest<ClientDTO> iPageRequest) {
        Pageable pageable = PageHelper.of(iPageRequest);
        Page<ClientEntity> page = clientRepository.findAll(pageable);
        return PageResult.of(page.getTotalElements(), entity -> {
            ClientDTO dto = new ClientDTO();
            BeanUtils.copyProperties(entity, dto);
            dto.setSecret(null);
            return dto;
        }, page.getContent());
    }
}
