package com.gapache.user.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gapache.commons.model.AuthConstants;
import com.gapache.security.event.EventSender;
import com.gapache.user.common.model.vo.UserCustomizeInfoVO;
import com.gapache.user.server.dao.entity.UserCustomizeInfoEntity;
import com.gapache.user.server.dao.repository.UserCustomizeInfoRepository;
import com.gapache.user.server.service.UserCustomizeInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author HuSen
 * @since 2021/1/25 1:09 下午
 */
@Service
public class UserCustomizeInfoServiceImpl implements UserCustomizeInfoService {

    private final UserCustomizeInfoRepository userCustomizeInfoRepository;
    private final EventSender eventSender;

    public UserCustomizeInfoServiceImpl(UserCustomizeInfoRepository userCustomizeInfoRepository, EventSender eventSender) {
        this.userCustomizeInfoRepository = userCustomizeInfoRepository;
        this.eventSender = eventSender;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserCustomizeInfoVO create(UserCustomizeInfoVO vo) {
        UserCustomizeInfoEntity entity = userCustomizeInfoRepository.findByUserIdAndClientId(vo.getUserId(), vo.getClientId());
        if (entity == null) {
            entity = new UserCustomizeInfoEntity();
            entity.setUserId(vo.getUserId());
            entity.setClientId(vo.getClientId());
        }
        entity.setInfo(vo.getInfo());
        userCustomizeInfoRepository.save(entity);
        vo.setId(entity.getId());
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(UserCustomizeInfoVO vo) {
        UserCustomizeInfoEntity entity = userCustomizeInfoRepository.findByUserIdAndClientId(vo.getUserId(), vo.getClientId());
        if (entity != null && StringUtils.isNotBlank(vo.getInfo())) {
            if (StringUtils.isBlank(entity.getInfo())) {
                entity.setInfo(vo.getInfo());
            } else {
                JSONObject newValues = JSON.parseObject(vo.getInfo());
                JSONObject oldValues = JSON.parseObject(entity.getInfo());
                newValues.forEach((key, value) -> {
                    if (AuthConstants.filterCustomizeInfo(key)) {
                        return;
                    }
                    oldValues.put(key, value);
                });
                entity.setInfo(oldValues.toJSONString());

                // 发布事件
                eventSender.send(vo.getUserId(), newValues.toJSONString(), null);
            }
            userCustomizeInfoRepository.save(entity);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Long id) {
        userCustomizeInfoRepository.deleteById(id);
        return true;
    }

    @Override
    public UserCustomizeInfoVO get(Long id) {
        return userCustomizeInfoRepository.findById(id)
                .map(entity -> {
                    UserCustomizeInfoVO vo = new UserCustomizeInfoVO();
                    BeanUtils.copyProperties(entity, vo);
                    return vo;
                })
                .orElse(null);
    }

    @Override
    public Object findValue(Long userId, String clientId, String key) {
        clientId = StringUtils.isNotBlank(clientId) ? clientId : AuthConstants.VEA;
        UserCustomizeInfoEntity userCustomizeInfoEntity = userCustomizeInfoRepository.findByUserIdAndClientId(userId, clientId);
        if (userCustomizeInfoEntity == null || StringUtils.isBlank(userCustomizeInfoEntity.getInfo())) {
            return null;
        }

        JSONObject jsonObject = JSON.parseObject(userCustomizeInfoEntity.getInfo());
        return jsonObject.get(key);
    }
}
