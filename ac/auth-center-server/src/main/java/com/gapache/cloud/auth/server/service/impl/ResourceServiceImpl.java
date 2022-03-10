package com.gapache.cloud.auth.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.gapache.cloud.auth.server.dao.entity.ClientEntity;
import com.gapache.cloud.auth.server.dao.entity.PermissionEntity;
import com.gapache.cloud.auth.server.dao.entity.ResourceEntity;
import com.gapache.cloud.auth.server.dao.entity.ResourceServerEntity;
import com.gapache.cloud.auth.server.dao.repository.client.ClientRepository;
import com.gapache.cloud.auth.server.dao.repository.user.PermissionRepository;
import com.gapache.cloud.auth.server.dao.repository.resource.ResourceRepository;
import com.gapache.cloud.auth.server.dao.repository.resource.ResourceServerRepository;
import com.gapache.cloud.auth.server.service.ResourceService;
import com.gapache.security.interfaces.ResourceReceiver;
import com.gapache.security.model.AuthResourceDTO;
import com.gapache.security.model.ResourceDTO;
import com.gapache.security.model.ResourceReportDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author HuSen
 * @since 2020/8/6 6:04 下午
 */
@Slf4j
@Service
public class ResourceServiceImpl implements ResourceReceiver, ResourceService {

    private final ResourceRepository resourceRepository;

    private final ResourceServerRepository resourceServerRepository;

    private final ClientRepository clientRepository;

    private final PermissionRepository permissionRepository;

    public ResourceServiceImpl(ResourceRepository resourceRepository, ResourceServerRepository resourceServerRepository, ClientRepository clientRepository, PermissionRepository permissionRepository) {
        this.resourceRepository = resourceRepository;
        this.resourceServerRepository = resourceServerRepository;
        this.clientRepository = clientRepository;
        this.permissionRepository = permissionRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean receiveReportData(ResourceReportDTO reportData) {
        log.info("receiveReportData:{}", JSON.toJSONString(reportData));
        ClientEntity client = clientRepository.findByClientId(reportData.getClientId());
        if (client == null) {
            log.info("client {} not existed", reportData.getClientId());
            return false;
        }

        // 判断ResourceServer是否存在，不存在则创建
        String resourceServerName = reportData.getResourceServerName();
        ResourceServerEntity resourceServer = resourceServerRepository.findByName(resourceServerName);
        if (resourceServer == null) {
            resourceServer = new ResourceServerEntity();
            resourceServer.setClientId(reportData.getClientId());
            resourceServer.setName(resourceServerName);
            resourceServerRepository.save(resourceServer);
        }

        Long resourceServerId = resourceServer.getId();

        List<AuthResourceDTO> authResources = reportData.getAuthResources();
        Set<String> newScope = authResources.stream().map(AuthResourceDTO::getScope).collect(Collectors.toSet());
        List<ResourceEntity> old = resourceRepository.findAllByResourceServerId(resourceServerId);
        List<Long> oldResourceIds = old.stream().map(ResourceEntity::getId).collect(Collectors.toList());
        List<PermissionEntity> oldPermission = permissionRepository.findAllByResourceIdIn(oldResourceIds);
        Map<Long, PermissionEntity> resourceIdPermissionMap = oldPermission.stream().collect(Collectors.toMap(PermissionEntity::getResourceId, p -> p));

        // 待删除
        List<ResourceEntity> delete = new ArrayList<>();
        List<PermissionEntity> deletePermission = new ArrayList<>();
        // 待更新
        List<ResourceEntity> update = new ArrayList<>();
        // 待添加
        List<ResourceEntity> add = new ArrayList<>();
        List<PermissionEntity> addPermission = new ArrayList<>();

        Map<String, ResourceEntity> oldScopeMap = old.stream().peek(r -> {
            if (!newScope.contains(r.getScope())) {
                delete.add(r);
                deletePermission.add(resourceIdPermissionMap.remove(r.getId()));
            }
        }).collect(Collectors.toMap(ResourceEntity::getScope, e -> e));

        authResources.forEach(dto -> {
            if (oldScopeMap.containsKey(dto.getScope())) {
                ResourceEntity resourceEntity = oldScopeMap.get(dto.getScope());
                if (!StringUtils.equals(resourceEntity.getName(), dto.getName())) {
                    resourceEntity.setName(dto.getName());
                    update.add(resourceEntity);
                }
            }  else {
                ResourceEntity entity = new ResourceEntity();
                entity.setResourceServerId(resourceServerId);
                entity.setResourceServerName(resourceServerName);
                entity.setScope(dto.getScope());
                entity.setName(dto.getName());
                add.add(entity);
            }
        });
        log.info("receiveReportData delete:{}", JSON.toJSONString(delete));
        resourceRepository.deleteInBatch(delete);
        permissionRepository.deleteInBatch(deletePermission);
        log.info("receiveReportData update:{}", JSON.toJSONString(update));
        resourceRepository.saveAll(update);
        log.info("receiveReportData add:{}", JSON.toJSONString(add));
        resourceRepository.saveAll(add).forEach(p -> {
            PermissionEntity permissionEntity = new PermissionEntity();
            permissionEntity.setResourceId(p.getId());
            addPermission.add(permissionEntity);
        });

        permissionRepository.saveAll(addPermission);

        Set<String> updateScopes = update.stream().map(ResourceEntity::getScope).collect(Collectors.toSet());
        Set<String> addScopes = add.stream().map(ResourceEntity::getScope).collect(Collectors.toSet());
        updateScopes.addAll(addScopes);
        clientRepository.save(client);
        return true;
    }

    @Override
    public List<ResourceDTO> findAll() {
        return resourceRepository.findAll()
                .stream()
                .map(resourceEntity -> {
                    ResourceDTO dto = new ResourceDTO();
                    BeanUtils.copyProperties(resourceEntity, dto);
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
