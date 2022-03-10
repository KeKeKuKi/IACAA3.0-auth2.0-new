package com.gapache.user.server.service.impl;

import com.gapache.commons.model.AuthConstants;
import com.gapache.commons.model.IPageRequest;
import com.gapache.commons.model.PageResult;
import com.gapache.commons.model.ThrowUtils;
import com.gapache.jpa.FindUtils;
import com.gapache.jpa.PageHelper;
import com.gapache.jpa.SpecificationFactory;
import com.gapache.security.holder.AccessCardHolder;
import com.gapache.security.model.AccessCard;
import com.gapache.user.common.model.UserError;
import com.gapache.user.common.model.vo.SaveUserRelationVO;
import com.gapache.user.common.model.vo.UserCustomizeInfoVO;
import com.gapache.user.common.model.vo.UserVO;
import com.gapache.user.server.dao.entity.UserCustomizeInfoEntity;
import com.gapache.user.server.dao.entity.UserEntity;
import com.gapache.user.server.dao.entity.UserRelationEntity;
import com.gapache.user.server.dao.repository.UserCustomizeInfoRepository;
import com.gapache.user.server.dao.repository.UserRelationRepository;
import com.gapache.user.server.dao.repository.UserRepository;
import com.gapache.user.server.service.UserCustomizeInfoService;
import com.gapache.user.server.service.UserService;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author HuSen
 * @since 2021/1/25 1:08 下午
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserCustomizeInfoRepository userCustomizeInfoRepository;
    private final UserCustomizeInfoService userCustomizeInfoService;
    private final UserRelationRepository userRelationRepository;

    public UserServiceImpl(UserRepository userRepository, UserCustomizeInfoRepository userCustomizeInfoRepository, UserCustomizeInfoService userCustomizeInfoService, UserRelationRepository userRelationRepository) {
        this.userRepository = userRepository;
        this.userCustomizeInfoRepository = userCustomizeInfoRepository;
        this.userCustomizeInfoService = userCustomizeInfoService;
        this.userRelationRepository = userRelationRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserVO create(UserVO vo) {
        ThrowUtils.throwIfTrue(userRepository.existsByUsername(vo.getUsername()), UserError.USERNAME_EXISTED);

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(vo.getUsername());
        userEntity.setPassword(vo.getPassword());
        userEntity.setIsDelete(false);

        userRepository.save(userEntity);

        String customizeInfo = vo.getCustomizeInfo();
        if (StringUtils.isNotBlank(vo.getClient()) && StringUtils.isNotBlank(customizeInfo)) {
            UserCustomizeInfoEntity userCustomizeInfoEntity = new UserCustomizeInfoEntity();
            userCustomizeInfoEntity.setInfo(customizeInfo);
            userCustomizeInfoEntity.setClientId(vo.getClient());
            userCustomizeInfoEntity.setUserId(userEntity.getId());
            userCustomizeInfoRepository.save(userCustomizeInfoEntity);
        }

        vo.setId(userEntity.getId());
        vo.setCreateTime(userEntity.getCreateTime());
        vo.setCreateBy(userEntity.getCreateBy());
        return vo;
    }

    @Override
    public Boolean userIsExisted(Long id) {
        return userRepository.existsByIdAndIsDelete(id, false);
    }

    @Override
    public UserVO findByUsername(String username, String clientId) {
        UserEntity userEntity = userRepository.findByUsernameAndIsDeleteIsNot(username, true);
        ThrowUtils.throwIfTrue(userEntity == null, UserError.USER_NOT_FOUND);

        UserVO vo = new UserVO();
        vo.setUsername(userEntity.getUsername());
        vo.setPassword(userEntity.getPassword());
        vo.setId(userEntity.getId());
        vo.setClient(clientId);
        vo.setCreateTime(userEntity.getCreateTime());
        vo.setCreateBy(userEntity.getCreateBy());
        vo.setLastModifiedBy(userEntity.getLastModifiedBy());
        vo.setLastModifiedTime(userEntity.getLastModifiedTime());

        getCustomerInfo(clientId, userEntity, vo);
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Long id) {
        // 删除用户 这里做逻辑删除
        userRepository.findById(id)
                .ifPresent(userEntity -> {
                    userEntity.setIsDelete(true);
                    userRepository.save(userEntity);
                });
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(UserVO vo) {
        Optional<UserEntity> optional = userRepository.findById(vo.getId());
        ThrowUtils.throwIfTrue(!optional.isPresent(), UserError.USER_NOT_FOUND);

        optional.ifPresent(entity -> {
            UserCustomizeInfoVO userCustomizeInfoVO = new UserCustomizeInfoVO();
            userCustomizeInfoVO.setUserId(vo.getId());
            userCustomizeInfoVO.setClientId(vo.getClient());
            userCustomizeInfoVO.setInfo(vo.getCustomizeInfo());
            userCustomizeInfoService.update(userCustomizeInfoVO);
        });

        return true;
    }

    @Override
    public PageResult<UserVO> page(IPageRequest<UserVO> iPageRequest) {
        AccessCard accessCard = AccessCardHolder.getContext();
        Pageable pageable = PageHelper.of(iPageRequest);
        UserVO params = iPageRequest.getCustomParams();
        Page<UserEntity> page;
        if (accessCard.getCustomerInfo().containsKey(AuthConstants.POSITION_ID)
                || accessCard.getCustomerInfo().containsKey(AuthConstants.SUPERIOR_ID)) {
            if (params != null && StringUtils.isNotBlank(params.getUsername())) {
                page = userRepository.findAllByOwnerIdAndUsernameLike(FindUtils.allMatch(params.getUsername()), accessCard.getUserId(), pageable);
            } else {
                page = userRepository.findAllByOwnerId(accessCard.getUserId(), pageable);
            }
        } else {
            page = userRepository.findAll(SpecificationFactory.produce((predicates, root, criteriaBuilder) -> {
                if (params != null) {
                    if (StringUtils.isNotBlank(params.getUsername())) {
                        predicates.add(criteriaBuilder.like(root.get("username").as(String.class), FindUtils.allMatch(params.getUsername())));
                    }
                    predicates.add(criteriaBuilder.notEqual(root.get("isDelete").as(Boolean.class), true));
                }
            }), pageable);
        }

        Map<Long, UserCustomizeInfoEntity> map = new HashMap<>(iPageRequest.getNumber());
        if (params != null && StringUtils.isBlank(params.getClient())) {
            map.putAll(userCustomizeInfoRepository.findAllByUserIdInAndClientId(page.getContent().stream().map(UserEntity::getId).collect(Collectors.toList()), accessCard.checkClientId())
                    .stream().collect(Collectors.toMap(UserCustomizeInfoEntity::getUserId, u -> u)));
        }
        PageResult<UserVO> pageResult = PageResult.of(page.getTotalElements(), this::entity2Vo, page.getContent());
        if (MapUtils.isNotEmpty(map)) {
            pageResult.getItems().forEach(vo -> {
                if (map.containsKey(vo.getId())) {
                    vo.setCustomizeInfo(map.get(vo.getId()).getInfo());
                }
            });
        }
        return pageResult;
    }

    @Override
    public UserVO get(Long id, String clientId) {
        Optional<UserEntity> optional = userRepository.findById(id);
        ThrowUtils.throwIfTrue(!optional.isPresent(), UserError.USER_NOT_FOUND);

        UserEntity userEntity = optional.get();
        UserVO vo = entity2Vo(userEntity);

        getCustomerInfo(clientId, userEntity, vo);
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveUserRelation(SaveUserRelationVO vo) {
        switch (vo.getType()) {
            // 新增
            case 0: {
                List<UserRelationEntity> addList = new ArrayList<>(vo.getOwnerIdList().size());
                for (Long ownerId : vo.getOwnerIdList()) {
                    if (userRelationRepository.existsByOwnerIdAndUserId(ownerId, vo.getUserId())) {
                        continue;
                    }
                    UserRelationEntity entity = new UserRelationEntity();
                    entity.setOwnerId(ownerId);
                    entity.setUserId(vo.getUserId());
                    addList.add(entity);
                }
                userRelationRepository.saveAll(addList);
                break;
            }
            // 删除
            case 1: {
                userRelationRepository.deleteAllByOwnerIdInAndUserId(vo.getOwnerIdList(), vo.getUserId());
                break;
            }
            default:
        }
        return true;
    }

    @Override
    public List<UserVO> findAllByIdIn(List<Long> userIds) {
        return userRepository.findAllByIdInAndIsDeleteIsNot(userIds, true)
                .stream()
                .map(this::entity2Vo)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserVO> list(UserVO userVO) {
        List<UserEntity> all = userRepository.findAll();
        List<UserVO> userVOS = new ArrayList<>();
        all.forEach(i -> {
            UserVO newUserVo = new UserVO();
            BeanUtils.copyProperties(i,newUserVo);
            newUserVo.setPassword("加密不可见");
            userVOS.add(newUserVo);
        });
        return userVOS;
    }

    private void getCustomerInfo(String clientId, UserEntity userEntity, UserVO vo) {
        if (StringUtils.isNotBlank(clientId)) {
            UserCustomizeInfoEntity customizeInfoEntity = userCustomizeInfoRepository.findByUserIdAndClientId(userEntity.getId(), clientId);
            if (customizeInfoEntity != null) {
                vo.setCustomizeInfo(customizeInfoEntity.getInfo());
            }
        } else {
            UserCustomizeInfoEntity customizeInfoEntity = userCustomizeInfoRepository.findByUserIdAndClientId(userEntity.getId(), AuthConstants.VEA);
            if (customizeInfoEntity != null) {
                vo.setCustomizeInfo(customizeInfoEntity.getInfo());
            }
        }
    }

    private UserVO entity2Vo(UserEntity userEntity) {
        UserVO vo = new UserVO();
        vo.setId(userEntity.getId());
        vo.setUsername(userEntity.getUsername());
        vo.setCreateTime(userEntity.getCreateTime());
        vo.setLastModifiedTime(userEntity.getLastModifiedTime());
        vo.setCreateBy(userEntity.getCreateBy());
        vo.setLastModifiedBy(userEntity.getLastModifiedBy());
        vo.setIsDelete(userEntity.getIsDelete());
        return vo;
    }
}
