package com.gapache.user.sdk.fallback;

import com.gapache.commons.model.JsonResult;
import com.gapache.commons.model.SystemError;
import com.gapache.user.common.model.vo.SaveUserRelationVO;
import com.gapache.user.common.model.vo.UserVO;
import com.gapache.user.sdk.feign.UserServerFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author HuSen
 * @since 2020/8/26 9:38 上午
 */
@Slf4j
@Component
public class UserServerFeignFallback implements UserServerFeign {

    @Override
    public JsonResult<UserVO> create(UserVO vo) {
        return JsonResult.of(SystemError.SERVER_EXCEPTION);
    }

    @Override
    public JsonResult<Boolean> userIsExisted(Long id) {
        return JsonResult.of(SystemError.SERVER_EXCEPTION);
    }

    @Override
    public JsonResult<UserVO> findByUsername(String username, String clientId) {
        return JsonResult.of(SystemError.SERVER_DEGRADE);
    }

    @Override
    public JsonResult<Boolean> saveUserRelation(SaveUserRelationVO vo) {
        return JsonResult.of(SystemError.SERVER_DEGRADE);
    }

    @Override
    public JsonResult<List<UserVO>> findAllByIdIn(List<Long> userIds) {
        return JsonResult.of(SystemError.SERVER_DEGRADE);
    }

    @Override
    public JsonResult<String> degrade2(int a) {
        return JsonResult.of(SystemError.SERVER_DEGRADE);
    }

    @Override
    public JsonResult<Object> findValue(Long userId, String clientId, String key) {
        return JsonResult.of(SystemError.SERVER_DEGRADE);
    }
}
