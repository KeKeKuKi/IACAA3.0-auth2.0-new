package com.gapache.cloud.auth.server.service;

import com.gapache.commons.model.IPageRequest;
import com.gapache.commons.model.PageResult;
import com.gapache.security.model.RoleCreateDTO;
import com.gapache.security.model.RoleDTO;
import com.gapache.security.model.RolePermissionDTO;
import com.gapache.security.model.RoleUpdateDTO;

import java.util.List;

/**
 * @author HuSen
 * @since 2021/1/26 9:56 上午
 */
public interface RoleService {

    Boolean create(RoleCreateDTO dto);

    Boolean update(RoleUpdateDTO dto);

    Boolean delete(Long id);

    PageResult<RoleDTO> page(IPageRequest<RoleDTO> iPageRequest);

    RolePermissionDTO findRoleAndPermissions(Long id);

    List<RoleDTO> findAllByName(String name);

    Boolean isGroup();
}
