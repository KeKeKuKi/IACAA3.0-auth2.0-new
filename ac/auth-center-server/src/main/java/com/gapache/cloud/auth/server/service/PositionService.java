package com.gapache.cloud.auth.server.service;

import com.gapache.security.model.ElmUiTreeNode;
import com.gapache.security.model.PositionDTO;

import java.util.List;

/**
 * @author HuSen
 * @since 2021/3/25 5:02 下午
 */
public interface PositionService {

    Long create(PositionDTO dto);

    Boolean update(PositionDTO dto);

    Boolean delete(Long id);

    List<ElmUiTreeNode> findTree(Long companyId);

    Boolean updateAll(List<ElmUiTreeNode> nodes);

    List<PositionDTO> findAll(boolean includingSelf, Long lowerId);
}
