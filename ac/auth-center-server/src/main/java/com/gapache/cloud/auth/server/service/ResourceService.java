package com.gapache.cloud.auth.server.service;

import com.gapache.security.model.ResourceDTO;

import java.util.List;

/**
 * @author HuSen
 * @since 2021/3/30 11:52 上午
 */
public interface ResourceService {

    List<ResourceDTO> findAll();
}
