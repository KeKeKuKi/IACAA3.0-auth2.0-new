package com.gapache.cloud.auth.server.controller;

import com.gapache.cloud.auth.server.service.UserService;
import com.gapache.commons.model.JsonResult;
import com.gapache.security.annotation.AuthResource;
import com.gapache.security.annotation.NeedAuth;
import com.gapache.security.model.SetUserRoleDTO;
import com.gapache.user.common.model.vo.UserVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author HuSen
 * @since 2020/8/3 11:42 上午
 */
@RestController
@RequestMapping("/api/user")
@NeedAuth("user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping
    @AuthResource(scope = "create", name = "创建用户")
    public JsonResult<Boolean> create(@RequestBody UserVO vo) {
        return JsonResult.of(userService.create(vo));
    }

    @PostMapping("/setUserRole")
    @AuthResource(scope = "setUserRole", name = "设置用户角色")
    public JsonResult<Boolean> setUserRole(@RequestBody SetUserRoleDTO dto) {
        return JsonResult.of(userService.setUserRole(dto, true));
    }

    @GetMapping("/findUserRoleId/{userId}")
    @AuthResource(scope = "findUserRoleId", name = "查询用户的角色")
    public JsonResult<Long> findUserRoleId(@PathVariable Long userId) {
        return JsonResult.of(userService.findUserRoleId(userId));
    }

    @GetMapping("/isEnabled/{userId}")
    public JsonResult<Boolean> isEnabled(@PathVariable Long userId) {
        return JsonResult.of(userService.isEnabled(userId));
    }

    @GetMapping("/findAllByPositionId/{positionId}")
    @AuthResource(scope = "findAllByPositionId", name = "根据职位查询用户")
    public JsonResult<List<UserVO>> findAllByPositionId(@PathVariable Long positionId) {
        return JsonResult.of(userService.findAllByPositionId(positionId));
    }

    @GetMapping("/findAllByPositionIdBetween/{lowPositionId}")
    @AuthResource(scope = "findAllByPositionIdBetween", name = "查询低于等于我并且高于指定职位的用户")
    public JsonResult<List<UserVO>> findAllByPositionIdBetween(@PathVariable Long lowPositionId) {
        return JsonResult.of(userService.findAllByPositionIdBetween(lowPositionId));
    }

    @GetMapping("/findSuperior/{userId}")
    @AuthResource(scope = "findSuperior", name = "查询上级是谁")
    public JsonResult<UserVO> findSuperior(@PathVariable Long userId) {
        return JsonResult.of(userService.findSuperior(userId));
    }
}
