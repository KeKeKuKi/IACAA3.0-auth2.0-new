package com.gapache.cloud.auth.server.service.impl;

import com.gapache.cloud.auth.server.dao.entity.UserClientRelationEntity;
import com.gapache.cloud.auth.server.dao.repository.user.UserClientRelationRepository;
import com.gapache.cloud.auth.server.model.UserClientRelationDTO;
import com.gapache.cloud.auth.server.service.UserClientRelationService;
import com.gapache.commons.model.ThrowUtils;
import com.gapache.security.model.SecurityError;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author HuSen
 * @since 2020/8/3 9:11 上午
 */
@Service
public class UserClientRelationServiceImpl implements UserClientRelationService {

    private final UserClientRelationRepository userClientRelationRepository;

    public UserClientRelationServiceImpl(UserClientRelationRepository userClientRelationRepository) {
        this.userClientRelationRepository = userClientRelationRepository;
    }

    @Override
    public UserClientRelationDTO findByUserIdAndClientId(Long userId, Long clientId) {
        UserClientRelationEntity entity = userClientRelationRepository.findByUserIdAndClientId(userId, clientId);
        if (entity == null) {
            return null;
        }
        UserClientRelationDTO dto = new UserClientRelationDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean create(UserClientRelationDTO userClientRelationDTO) {
        Boolean exists = userClientRelationRepository.existsByUserIdAndClientId(userClientRelationDTO.getUserId(), userClientRelationDTO.getClientId());
        ThrowUtils.throwIfTrue(exists, SecurityError.USER_CLIENT_RELATION_EXISTED);

        UserClientRelationEntity entity = new UserClientRelationEntity();
        BeanUtils.copyProperties(userClientRelationDTO, entity);
        return userClientRelationRepository.save(entity).getId() > 0;
    }
}
