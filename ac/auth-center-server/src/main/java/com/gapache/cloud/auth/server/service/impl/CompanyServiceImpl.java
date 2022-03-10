package com.gapache.cloud.auth.server.service.impl;

import com.gapache.cloud.auth.server.dao.entity.CompanyEntity;
import com.gapache.cloud.auth.server.dao.repository.company.CompanyRepository;
import com.gapache.cloud.auth.server.service.CompanyService;
import com.gapache.commons.model.ThrowUtils;
import com.gapache.security.holder.AccessCardHolder;
import com.gapache.security.model.AccessCard;
import com.gapache.security.model.CompanyDTO;
import com.gapache.security.model.ElmUiTreeNode;
import com.gapache.security.model.SecurityError;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author HuSen
 * @since 2021/3/26 11:12 上午
 */
@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long create(CompanyDTO dto) {
        AccessCard accessCard = AccessCardHolder.getContext();
        ThrowUtils.throwIfTrue(companyRepository.existsByNameAndClientId(dto.getName(), accessCard.checkClientId()), SecurityError.COMPANY_NAME_EXISTED);

        CompanyEntity companyEntity = new CompanyEntity();
        companyEntity.setName(dto.getName());
        companyEntity.setAboveId(dto.getAboveId());
        companyEntity.setClientId(accessCard.checkClientId());

        companyRepository.save(companyEntity);
        return companyEntity.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean update(CompanyDTO dto) {
        Optional<CompanyEntity> byId = companyRepository.findById(dto.getId());
        ThrowUtils.throwIfTrue(!byId.isPresent(), SecurityError.COMPANY_NOT_FOUND);

        byId.ifPresent(entity -> {
            AccessCard accessCard = AccessCardHolder.getContext();
            if (!StringUtils.equals(entity.getName(), dto.getName())) {
                ThrowUtils.throwIfTrue(companyRepository.existsByNameAndClientId(dto.getName(), accessCard.checkClientId()), SecurityError.COMPANY_NAME_EXISTED);
            }

            entity.setName(dto.getName());
            companyRepository.save(entity);
        });

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean delete(Long id) {
        companyRepository.deleteById(id);

        List<CompanyEntity> byAboveId = companyRepository.findAllByAboveId(id);
        if (CollectionUtils.isEmpty(byAboveId)) {
            return true;
        }

        for (CompanyEntity companyEntity : byAboveId) {
            delete(companyEntity.getId());
        }
        return true;
    }

    @Override
    public List<ElmUiTreeNode> findTree() {
        AccessCard accessCard = AccessCardHolder.getContext();
        String clientId = accessCard.checkClientId();

        ElmUiTreeNode root = new ElmUiTreeNode();
        root.setId("0");
        root.setLabel("公司架构");
        List<CompanyEntity> all = companyRepository.findAllByClientId(clientId);
        Map<Long, List<CompanyEntity>> aboveIdMap = all.stream().collect(Collectors.groupingBy(CompanyEntity::getAboveId));

        List<CompanyEntity> rootNodes = aboveIdMap.get(0L);
        List<ElmUiTreeNode> list = new ArrayList<>();
        if (rootNodes != null) {
            for (CompanyEntity rootNode : rootNodes) {
                deepScanPosition(list, rootNode, aboveIdMap);
            }
        }

        root.setChildren(list);
        return Lists.newArrayList(root);
    }

    @Override
    public Boolean updateAll(List<ElmUiTreeNode> nodes) {
        for (ElmUiTreeNode node : nodes) {
            Optional<CompanyEntity> byId = companyRepository.findById(Long.parseLong(node.getId()));
            byId.ifPresent(entity -> {
                if (entity.getAboveId() != null) {
                    entity.setAboveId(0L);
                    companyRepository.save(entity);
                }
                deepScanUpdate(node.getChildren(), entity.getId());
            });
        }
        return true;
    }

    @Override
    public List<CompanyDTO> findAll() {
        AccessCard accessCard = AccessCardHolder.getContext();
        return companyRepository.findAllByClientId(accessCard.checkClientId())
                .stream()
                .map(entity -> {
                    CompanyDTO dto = new CompanyDTO();
                    BeanUtils.copyProperties(entity, dto);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    private void deepScanUpdate(List<ElmUiTreeNode> list, Long aboveId) {
        if (CollectionUtils.isNotEmpty(list)) {
            for (ElmUiTreeNode node : list) {
                companyRepository.findById(Long.parseLong(node.getId()))
                        .ifPresent(entity -> {
                            entity.setAboveId(aboveId);
                            companyRepository.save(entity);

                            deepScanUpdate(node.getChildren(), Long.parseLong(node.getId()));
                        });
            }
        }
    }

    private void deepScanPosition(List<ElmUiTreeNode> list, CompanyEntity positionEntity, Map<Long, List<CompanyEntity>> aboveIdMap) {
        List<CompanyEntity> nodes = aboveIdMap.get(positionEntity.getId());
        ElmUiTreeNode node = new ElmUiTreeNode();
        node.setId(positionEntity.getId().toString());
        node.setLabel(positionEntity.getName());
        node.setData(positionEntity);
        list.add(node);
        if (nodes == null) {
            return;
        }

        node.setChildren(new ArrayList<>());

        for (CompanyEntity p : nodes) {
            deepScanPosition(node.getChildren(), p, aboveIdMap);
        }
    }
}
