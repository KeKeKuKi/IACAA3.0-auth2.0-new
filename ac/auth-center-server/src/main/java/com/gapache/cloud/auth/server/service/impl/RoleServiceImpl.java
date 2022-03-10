package com.gapache.cloud.auth.server.service.impl;

import com.gapache.cloud.auth.server.dao.entity.PermissionEntity;
import com.gapache.cloud.auth.server.dao.entity.ResourceEntity;
import com.gapache.cloud.auth.server.dao.entity.RoleEntity;
import com.gapache.cloud.auth.server.dao.entity.RolePermissionEntity;
import com.gapache.cloud.auth.server.dao.repository.resource.ResourceRepository;
import com.gapache.cloud.auth.server.dao.repository.user.PermissionRepository;
import com.gapache.cloud.auth.server.dao.repository.user.RolePermissionRepository;
import com.gapache.cloud.auth.server.dao.repository.user.RoleRepository;
import com.gapache.cloud.auth.server.service.RoleService;
import com.gapache.commons.model.IPageRequest;
import com.gapache.commons.model.PageResult;
import com.gapache.commons.model.ThrowUtils;
import com.gapache.jpa.FindUtils;
import com.gapache.security.entity.RoleScopesEntity;
import com.gapache.security.holder.AccessCardHolder;
import com.gapache.security.model.*;
import com.gapache.vertx.redis.support.SimpleRedisRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author HuSen
 * @since 2021/1/26 9:56 上午
 */
@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final ResourceRepository resourceRepository;
    private final PermissionRepository permissionRepository;
    private final SimpleRedisRepository simpleRedisRepository;

    public RoleServiceImpl(RoleRepository roleRepository, RolePermissionRepository rolePermissionEntity, ResourceRepository resourceRepository, PermissionRepository permissionRepository, SimpleRedisRepository simpleRedisRepository) {
        this.roleRepository = roleRepository;
        this.rolePermissionRepository = rolePermissionEntity;
        this.resourceRepository = resourceRepository;
        this.permissionRepository = permissionRepository;
        this.simpleRedisRepository = simpleRedisRepository;
    }

    @PostConstruct
    public void init() {
        List<RoleEntity> all = roleRepository.findAll();
        all.forEach(role -> simpleRedisRepository.findById(role.getId().toString(), RoleScopesEntity.class)
                .onSuccess(x -> {
                    // 没有才缓存
                    if (x.getRoleId() == null) {
                        cacheRoleScopes(role);
                    }
                }));
    }

    private void cacheRoleScopes(RoleEntity role) {
        RoleScopesEntity entity = new RoleScopesEntity();
        entity.setRoleId(role.getId());
        List<ResourceEntity> resources = resourceRepository.findCustomizeAllResourceFromRid(role.getId());
        String scopes = resources.isEmpty() ? "" : resources.stream().map(ResourceEntity::fullScopeName).collect(Collectors.joining(","));
        entity.setScopes(scopes);
        simpleRedisRepository.save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean create(RoleCreateDTO dto) {
        AccessCard accessCard = AccessCardHolder.getContext();
        RoleEntity creatorRole = roleRepository.findByUserId(accessCard.getUserId());
        // 组管理员不允许再创建主角色了
        ThrowUtils.throwIfTrue(creatorRole != null && creatorRole.getGroupId() != null && dto.getIsGroup(), SecurityError.ROLE_PERMISSION_DENY);
        // 是一个角色组的成员，但不是管理员，禁止创建
        ThrowUtils.throwIfTrue(creatorRole != null && creatorRole.getGroupId() != null && !creatorRole.getIsManager(), SecurityError.ROLE_PERMISSION_DENY);

        RoleEntity entity = roleRepository.findByName(dto.getName());
        ThrowUtils.throwIfTrue(entity != null, SecurityError.ROLE_EXISTED);

        entity = new RoleEntity();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());

        // 组长创建的就为组员
        boolean createGroupManager = dto.getIsGroup() && (creatorRole == null || creatorRole.getGroupId() == null);
        if (creatorRole != null && creatorRole.getIsManager()) {
            entity.setIsManager(false);
            entity.setGroupId(creatorRole.getGroupId());
            roleRepository.save(entity);
        } else if (createGroupManager) {
            // 如果不是组长创建的，就为组长
            entity.setIsManager(true);
            roleRepository.save(entity);
            entity.setGroupId(entity.getId());
            roleRepository.save(entity);
        } else {
            roleRepository.save(entity);
        }

        Long roleId = entity.getId();

        Set<Long> permissionList = dto.getPermissionList();
        saveOrUpdatePermission(roleId, permissionList);

        cacheRoleScopes(entity);
        return true;
    }

    private void saveOrUpdatePermission(Long roleId, Set<Long> permissionList) {
        if (CollectionUtils.isNotEmpty(permissionList)) {
            List<RolePermissionEntity> rolePermissionEntities = rolePermissionRepository.findAllByRoleId(roleId);
            Set<Long> permissionIdOld = rolePermissionEntities.stream().map(RolePermissionEntity::getPermissionId).collect(Collectors.toSet());
            List<RolePermissionEntity> newAdd = permissionList.stream().filter(permissionId -> !permissionIdOld.contains(permissionId)).map(permissionId -> {
                RolePermissionEntity rolePermissionEntity = new RolePermissionEntity();
                rolePermissionEntity.setRoleId(roleId);
                rolePermissionEntity.setPermissionId(permissionId);
                return rolePermissionEntity;
            }).collect(Collectors.toList());
            rolePermissionRepository.saveAll(newAdd);

            List<RolePermissionEntity> deleteAll = rolePermissionEntities.stream().filter(old -> !permissionList.contains(old.getPermissionId())).collect(Collectors.toList());
            rolePermissionRepository.deleteAll(deleteAll);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean update(RoleUpdateDTO dto) {

        Optional<RoleEntity> byId = roleRepository.findById(dto.getId());
        ThrowUtils.throwIfTrue(!byId.isPresent(), SecurityError.ROLE_NOT_FOUND);
        if (!StringUtils.equals(byId.get().getName(), dto.getName())) {
            ThrowUtils.throwIfTrue(roleRepository.findByName(dto.getName()) != null, SecurityError.ROLE_EXISTED);
        }

        byId.ifPresent(roleEntity -> {
            checkRoleGroup(roleEntity);
            roleEntity.setName(dto.getName());
            roleEntity.setDescription(dto.getDescription());
            roleRepository.save(roleEntity);
            saveOrUpdatePermission(roleEntity.getId(), dto.getPermissionList());
            cacheRoleScopes(roleEntity);
        });

        return true;
    }

    private void checkRoleGroup(RoleEntity roleEntity) {
        AccessCard accessCard = AccessCardHolder.getContext();
        RoleEntity role = roleRepository.findByUserId(accessCard.getUserId());
        // 如果自己属于组的角色则必须是管理员才能操作
        ThrowUtils.throwIfTrue(role != null && role.getGroupId() != null && !role.getIsManager(), SecurityError.ROLE_PERMISSION_DENY);
        // 只能操作自己组的角色
        boolean groupLimit = role != null && role.getGroupId() != null;
        ThrowUtils.throwIfTrue(groupLimit && !role.getGroupId().equals(roleEntity.getGroupId()), SecurityError.ROLE_PERMISSION_DENY);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean delete(Long id) {
        roleRepository.findById(id)
                .ifPresent(member -> {
                    checkRoleGroup(member);

                    roleRepository.delete(member);
                    rolePermissionRepository.deleteAllByRoleId(id);
                    RoleScopesEntity delete = new RoleScopesEntity();
                    delete.setRoleId(id);
                    simpleRedisRepository.delete(delete);
                });
        return true;
    }

    @Override
    public PageResult<RoleDTO> page(IPageRequest<RoleDTO> iPageRequest) {
        AccessCard accessCard = AccessCardHolder.getContext();
        RoleEntity role = roleRepository.findByUserId(accessCard.getUserId());
        // 主角色可以只能看到属于自己组的角色
        boolean groupLimit = role != null && role.getGroupId() != null;
        // 组成员不具有这个权限
        ThrowUtils.throwIfTrue(groupLimit && !role.getIsManager(), SecurityError.ROLE_PERMISSION_DENY);

        PageRequest pageRequest = PageRequest.of(iPageRequest.getPage() - 1, iPageRequest.getNumber());
        Page<RoleEntity> all = groupLimit ? roleRepository.findAllByGroupIdAndIdNot(role.getGroupId(), role.getId(), pageRequest) : roleRepository.findAll(pageRequest);
        return PageResult.of(all.getTotalElements(), x -> {
            RoleDTO roleDTO = new RoleDTO();
            BeanUtils.copyProperties(x, roleDTO);
            return roleDTO;
        }, all.getContent());
    }

    @Override
    public RolePermissionDTO findRoleAndPermissions(Long id) {
        AccessCard accessCard = AccessCardHolder.getContext();
        RoleEntity role = roleRepository.findByUserId(accessCard.getUserId());
        // 主角色可以分配自己所有的权限给角色
        // 这里进行组的限制，如果没有组限制的默认所有的权限
        boolean groupLimit = role != null && role.getGroupId() != null;
        // 组成员不具有这个权限
        ThrowUtils.throwIfTrue(groupLimit && !role.getIsManager(), SecurityError.ROLE_PERMISSION_DENY);

        RolePermissionDTO rolePermissionDTO = new RolePermissionDTO();
        if (id != 0) {
            Optional<RoleEntity> optional = roleRepository.findById(id);
            ThrowUtils.throwIfTrue(!optional.isPresent(), SecurityError.ROLE_NOT_FOUND);
            // 我要修改的角色是不是我所管理的
            RoleEntity roleEntity = optional.get();
            ThrowUtils.throwIfTrue(groupLimit && (roleEntity.getId().equals(role.getId()) || !roleEntity.getGroupId().equals(role.getGroupId())), SecurityError.ROLE_PERMISSION_DENY);

            RoleDTO roleDTO = new RoleDTO();
            BeanUtils.copyProperties(roleEntity, roleDTO);
            rolePermissionDTO.setRole(roleDTO);
        }

        List<ElmUiTreeNode> elmUiTreeNodes = new ArrayList<>();
        List<ResourceEntity> resources = groupLimit ?
                resourceRepository.findCustomizeAllResourceFromRid(role.getId()) : resourceRepository.findAll();

        if (groupLimit) {
            resources.removeIf(resourceEntity -> StringUtils.startsWithIgnoreCase(resourceEntity.getScope(), "User")
                    || StringUtils.startsWithIgnoreCase(resourceEntity.getScope(), "Role")
                    || StringUtils.startsWithIgnoreCase(resourceEntity.getScope(), "Position"));
        }

        // 可分配的所有权限
        List<PermissionEntity> permissions = permissionRepository.findAll();
        rolePermissionDTO.setDefaultExpandedKeys(new HashSet<>(resources.size()));
        rolePermissionDTO.setDefaultCheckedKeys(new HashSet<>(resources.size()));
        Map<Long, PermissionEntity> permissionMap = permissions.stream().collect(Collectors.toMap(PermissionEntity::getResourceId, p -> p));
        // 自己可访问的资源
        List<ResourceEntity> myResources = id != 0 ? resourceRepository.findCustomizeAllResourceFromRid(id) : new ArrayList<>(0);

        Set<Long> myResourceIds = myResources.stream().map(ResourceEntity::getId).collect(Collectors.toSet());
        resources.stream().collect(Collectors.groupingBy(ResourceEntity::getResourceServerId))
                .forEach((resourceServerId, rs) -> {
                    ElmUiTreeNode root = new ElmUiTreeNode();
                    root.setId(resourceServerId.toString());
                    root.setLabel(rs.get(0).getResourceServerName());
                    root.setChildren(new ArrayList<>());
                    rolePermissionDTO.getDefaultExpandedKeys().add(root.getId());
                    elmUiTreeNodes.add(root);

                    rs.stream().collect(Collectors.groupingBy(resourceEntity -> resourceEntity.getScope().split(":")[0]))
                            .forEach((group, members) -> {
                                ElmUiTreeNode model = new ElmUiTreeNode();
                                // 预留10万个权限
                                model.setId(resourceServerId + "-" + (resourceServerId * 100000 + root.getChildren().size()));
                                model.setLabel(group);
                                model.setChildren(new ArrayList<>());
                                root.getChildren().add(model);
                                rolePermissionDTO.getDefaultExpandedKeys().add(model.getId());

                                members.forEach(member -> {
                                    ElmUiTreeNode node = new ElmUiTreeNode();
                                    PermissionEntity permissionEntity = permissionMap.get(member.getId());
                                    if (permissionEntity != null) {
                                        // 这里应该使用权限的Id
                                        node.setId(model.getId() + "-" + permissionEntity.getId());
                                        node.setLabel(member.getName());
                                        model.getChildren().add(node);

                                        if (myResourceIds.contains(member.getId())) {
                                            rolePermissionDTO.getDefaultCheckedKeys().add(node.getId());
                                            rolePermissionDTO.getDefaultCheckedKeys().add(model.getId());
                                            rolePermissionDTO.getDefaultCheckedKeys().add(root.getId());
                                        }
                                    }
                                });
                            });
                });

        rolePermissionDTO.setPermissions(elmUiTreeNodes);

        return rolePermissionDTO;
    }

    @Override
    public List<RoleDTO> findAllByName(String name) {
        AccessCard accessCard = AccessCardHolder.getContext();
        RoleEntity role = roleRepository.findByUserId(accessCard.getUserId());
        // 主角色可以只能看到属于自己组的角色
        boolean groupLimit = role != null && role.getGroupId() != null;
        // 组成员不具有这个权限
        ThrowUtils.throwIfTrue(groupLimit && !role.getIsManager(), SecurityError.ROLE_PERMISSION_DENY);

        List<RoleEntity> entities = groupLimit
                ? StringUtils.isNotBlank(name) ? roleRepository.findAllByGroupIdAndIdNotAndNameLike(role.getGroupId(), role.getId(), FindUtils.allMatch(name)) : roleRepository.findAllByGroupIdAndIdIsNot(role.getGroupId(), role.getId())
                : StringUtils.isNotBlank(name) ? roleRepository.findAllByNameLike(FindUtils.allMatch(name)) : roleRepository.findAll();

        return entities.stream().map(entity -> {
            RoleDTO roleDTO = new RoleDTO();
            BeanUtils.copyProperties(entity, roleDTO);
            return roleDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public Boolean isGroup() {
        AccessCard accessCard = AccessCardHolder.getContext();
        if (accessCard.getUserId() == 0L) {
            return false;
        }

        RoleEntity roleEntity = roleRepository.findByUserId(accessCard.getUserId());
        return roleEntity != null && roleEntity.getGroupId() != null;
    }
}
