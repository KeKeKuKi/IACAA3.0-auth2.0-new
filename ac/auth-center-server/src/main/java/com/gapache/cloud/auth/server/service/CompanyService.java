package com.gapache.cloud.auth.server.service;

import com.gapache.security.model.CompanyDTO;
import com.gapache.security.model.ElmUiTreeNode;

import java.util.List;

/**
 * @author HuSen
 * @since 2021/3/26 11:12 上午
 */
public interface CompanyService {

    Long create(CompanyDTO dto);

    Boolean update(CompanyDTO dto);

    Boolean delete(Long id);

    List<ElmUiTreeNode> findTree();

    Boolean updateAll(List<ElmUiTreeNode> nodes);

    List<CompanyDTO> findAll();
}
