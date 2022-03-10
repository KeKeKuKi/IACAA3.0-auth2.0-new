package com.gapache.cloud.auth.server.service.impl;

import com.gapache.cloud.auth.server.dao.entity.CompanyEntity;
import com.gapache.cloud.auth.server.dao.entity.PositionEntity;
import com.gapache.cloud.auth.server.dao.entity.UserPositionEntity;
import com.gapache.cloud.auth.server.dao.repository.company.CompanyRepository;
import com.gapache.cloud.auth.server.dao.repository.position.PositionRepository;
import com.gapache.cloud.auth.server.dao.repository.position.UserPositionRepository;
import com.gapache.cloud.auth.server.service.PositionService;
import com.gapache.commons.model.ThrowUtils;
import com.gapache.security.holder.AccessCardHolder;
import com.gapache.security.model.AccessCard;
import com.gapache.security.model.ElmUiTreeNode;
import com.gapache.security.model.PositionDTO;
import com.gapache.security.model.SecurityError;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author HuSen
 * @since 2021/3/25 5:02 下午
 */
@Service
public class PositionServiceImpl implements PositionService {

    private final PositionRepository positionRepository;
    private final UserPositionRepository userPositionRepository;
    private final CompanyRepository companyRepository;

    public PositionServiceImpl(PositionRepository positionRepository, UserPositionRepository userPositionRepository, CompanyRepository companyRepository) {
        this.positionRepository = positionRepository;
        this.userPositionRepository = userPositionRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long create(PositionDTO dto) {
        ThrowUtils.throwIfTrue(positionRepository.existsByName(dto.getName()), SecurityError.POSITION_NAME_EXISTED);

        PositionEntity entity = new PositionEntity();
        BeanUtils.copyProperties(dto, entity);

        positionRepository.save(entity);
        return entity.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean update(PositionDTO dto) {
        Optional<PositionEntity> byId = positionRepository.findById(dto.getId());
        ThrowUtils.throwIfTrue(!byId.isPresent(), SecurityError.POSITION_NOT_FOUND);

        byId.ifPresent(entity -> {
            ThrowUtils.throwIfTrue(!StringUtils.equals(entity.getName(), dto.getName())
                    && positionRepository.existsByName(dto.getName()), SecurityError.POSITION_NAME_EXISTED);

            entity.setName(dto.getName());

            positionRepository.save(entity);
        });
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean delete(Long id) {
        positionRepository.deleteById(id);
        List<PositionEntity> byAboveId = positionRepository.findAllByAboveId(id);
        if (CollectionUtils.isEmpty(byAboveId)) {
            return true;
        }

        for (PositionEntity entity : byAboveId) {
            // 递归删除
            delete(entity.getId());
        }
        return true;
    }

    @Override
    public List<ElmUiTreeNode> findTree(Long companyId) {
        ElmUiTreeNode root = new ElmUiTreeNode();
        root.setId("0");
        root.setLabel("组织架构");
        List<PositionEntity> all = positionRepository.findAllByCompanyId(companyId);
        Map<Long, List<PositionEntity>> aboveIdMap = all.stream().collect(Collectors.groupingBy(PositionEntity::getAboveId));

        List<PositionEntity> rootNodes = aboveIdMap.get(0L);
        List<ElmUiTreeNode> list = new ArrayList<>();
        if (rootNodes != null) {
            for (PositionEntity rootNode : rootNodes) {
                deepScanPosition(list, rootNode, aboveIdMap);
            }
        }

        root.setChildren(list);
        return Lists.newArrayList(root);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateAll(List<ElmUiTreeNode> nodes) {
        for (ElmUiTreeNode node : nodes) {
            Optional<PositionEntity> byId = positionRepository.findById(Long.parseLong(node.getId()));
            byId.ifPresent(entity -> {
                if (entity.getAboveId() != null) {
                    entity.setAboveId(0L);
                    positionRepository.save(entity);
                }
                deepScanUpdate(node.getChildren(), entity.getId());
            });
        }
        return true;
    }

    @Override
    public List<PositionDTO> findAll(boolean includingSelf, Long lowerId) {
        AccessCard accessCard = AccessCardHolder.getContext();
        Long userId = accessCard.getUserId();
        // 我是哪个公司的
        // 在指定lowerId的时候也就是指定了哪个公司了
        // 超级管理员
        Map<Long, CompanyEntity> companyEntityMap;
        List<PositionEntity> result = new ArrayList<>();
        CompanyEntity companyEntity = null;
        if (lowerId != null) {
            Optional<PositionEntity> byId = positionRepository.findById(lowerId);
            if (byId.isPresent()) {
                companyEntity = companyRepository.findById(byId.get().getCompanyId())
                        .orElse(null);
            }
        }
        List<CompanyEntity> companyEntities;
        if (userId == 0) {
            companyEntities = companyEntity == null
                    ? companyRepository.findAllByClientId(accessCard.checkClientId())
                    : Lists.newArrayList(companyEntity);

            List<PositionEntity> all = positionRepository.findAllByCompanyIdIn(companyEntities.stream().map(CompanyEntity::getId).collect(Collectors.toList()));
            result.addAll(all);
        } else {
            List<PositionEntity> positionEntities;
            if (companyEntity == null) {
                positionEntities = positionRepository
                        .findAllById(userPositionRepository.findAllByUserId(userId)
                                .stream()
                                .map(UserPositionEntity::getPositionId)
                                .collect(Collectors.toList()));

                companyEntities = companyRepository.findAllById(positionEntities.stream().map(PositionEntity::getCompanyId).collect(Collectors.toList()));
            } else {
                companyEntities = Lists.newArrayList(companyEntity);
                positionEntities = positionRepository.findAllByCompanyIdAndUserPosition(companyEntity.getId(), userId);
            }

            // includingSelf
            if (includingSelf) {
                result.addAll(positionEntities);
            }
            for (PositionEntity positionEntity : positionEntities) {
                deepFindLower(result, true, positionEntity.getId(), lowerId);
            }
        }

        companyEntityMap = companyEntities
                .stream()
                .collect(Collectors.toMap(CompanyEntity::getId, c -> c));

        return result
                .stream()
                .map(entity -> {
                    PositionDTO positionDTO = new PositionDTO();
                    positionDTO.setId(entity.getId());
                    positionDTO.setCompanyId(entity.getCompanyId());
                    positionDTO.setAboveId(entity.getAboveId());
                    positionDTO.setName(entity.getName());
                    CompanyEntity company = companyEntityMap.get(entity.getCompanyId());
                    if (company != null) {
                        positionDTO.setName(company.getName().concat(":").concat(positionDTO.getName()));
                    }
                    return positionDTO;
                }).collect(Collectors.toList());
    }

    private void deepFindLower(List<PositionEntity> entities, boolean init, Long rootId, Long lowerId) {
        if (rootId.equals(lowerId)) {
            return;
        }
        Optional<PositionEntity> byId = positionRepository.findById(rootId);
        byId.ifPresent(root -> {
            if (!init) {
                entities.add(root);
            }
            List<PositionEntity> allByAboveId = positionRepository.findAllByAboveId(rootId);
            if (CollectionUtils.isEmpty(allByAboveId)) {
                return;
            }
            for (PositionEntity entity : allByAboveId) {
                deepFindLower(entities, false, entity.getId(), lowerId);
            }
        });
    }

    private void deepScanUpdate(List<ElmUiTreeNode> list, Long aboveId) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }

        for (ElmUiTreeNode node : list) {
            positionRepository.findById(Long.parseLong(node.getId()))
                    .ifPresent(entity -> {
                        entity.setAboveId(aboveId);
                        positionRepository.save(entity);

                        deepScanUpdate(node.getChildren(), Long.parseLong(node.getId()));
                    });
        }
    }

    private void deepScanPosition(List<ElmUiTreeNode> list, PositionEntity positionEntity, Map<Long, List<PositionEntity>> aboveIdMap) {
        List<PositionEntity> nodes = aboveIdMap.get(positionEntity.getId());
        ElmUiTreeNode node = new ElmUiTreeNode();
        node.setId(positionEntity.getId().toString());
        node.setLabel(positionEntity.getName());
        list.add(node);
        if (nodes == null) {
            return;
        }

        node.setChildren(new ArrayList<>());

        for (PositionEntity p : nodes) {
            deepScanPosition(node.getChildren(), p, aboveIdMap);
        }
    }
}
