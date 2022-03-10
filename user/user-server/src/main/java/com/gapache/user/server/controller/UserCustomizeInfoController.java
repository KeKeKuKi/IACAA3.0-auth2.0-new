package com.gapache.user.server.controller;

import com.gapache.commons.model.JsonResult;
import com.gapache.security.annotation.AuthResource;
import com.gapache.security.annotation.NeedAuth;
import com.gapache.user.common.model.vo.UserCustomizeInfoVO;
import com.gapache.user.server.service.UserCustomizeInfoService;
//import com.gapache.web.Check;
//import com.gapache.web.Validating;
import org.springframework.web.bind.annotation.*;

/**
 * @author HuSen
 * @since 2021/1/25 1:12 下午
 */
//@Validating
@RestController
@RequestMapping("/api/userCustomizeInfo")
@NeedAuth("UserCustomizeInfo")
public class UserCustomizeInfoController {

    private final UserCustomizeInfoService userCustomizeInfoService;

    public UserCustomizeInfoController(UserCustomizeInfoService userCustomizeInfoService) {
        this.userCustomizeInfoService = userCustomizeInfoService;
    }

    @PostMapping
    @AuthResource(scope = "create", name = "创建用户自定义信息")
    public JsonResult<UserCustomizeInfoVO> create(@RequestBody UserCustomizeInfoVO vo) {
        return JsonResult.of(userCustomizeInfoService.create(vo));
    }

    @PutMapping
    @AuthResource(scope = "update", name = "更新用户自定义信息")
    public JsonResult<Boolean> update(@RequestBody UserCustomizeInfoVO vo) {
        return JsonResult.of(userCustomizeInfoService.update(vo));
    }

    @DeleteMapping("/{id}")
    @AuthResource(scope = "delete", name = "删除用户自定义信息")
    public JsonResult<Boolean> delete(@PathVariable Long id) {
        return JsonResult.of(userCustomizeInfoService.delete(id));
    }

    @GetMapping("/{id}")
    @AuthResource(scope = "get", name = "根据ID查询用户自定义信息")
    public JsonResult<UserCustomizeInfoVO> get(@PathVariable Long id) {
        return JsonResult.of(userCustomizeInfoService.get(id));
    }

    @GetMapping("/findValue/{userId}/{key}")
    @AuthResource(scope = "findValue", name = "根据用户Id和clientId查询单个值", checkEnabled = false)
    public JsonResult<Object> findValue(@PathVariable Long userId, @RequestParam(required = false) String clientId, @PathVariable String key) {
        return JsonResult.of(userCustomizeInfoService.findValue(userId, clientId, key));
    }
}
