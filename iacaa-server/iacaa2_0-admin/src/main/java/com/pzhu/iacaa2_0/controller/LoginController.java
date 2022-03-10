package com.pzhu.iacaa2_0.controller;

import com.pzhu.iacaa2_0.model.JsonResult;
import com.pzhu.iacaa2_0.model.UserInfo;
import com.pzhu.iacaa2_0.model.UserLoginDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.UUID;

/**
 * @author ZhaoZezhong
 * @version V1.0
 * @Title: LoginController
 * @Description: Company:成都平凡谷科技有限责任公司
 * @date 2021/3/1013:32
 */
@RestController
public class LoginController {
//
//    @PostMapping("/newLogin")
//    public JsonResult<UserInfo> newLogin(@RequestBody UserLoginDTO dto) {
//
//        // param
//        if (!StringUtils.equals(dto.getUsername(), "admin")) {
//            return JsonResult.of(500, "");
//        }
//
//        String passwordMd5 = DigestUtils.md5DigestAsHex(dto.getPassword().getBytes(StandardCharsets.UTF_8));
//        if (!StringUtils.equals(passwordMd5, DigestUtils.md5DigestAsHex("Pfg85191558".getBytes(StandardCharsets.UTF_8)))) {
//            return JsonResult.of(500, "");
//        }
//
//        String loginToken = UUID.randomUUID().toString();
//
//        UserInfo userInfo = new UserInfo();
//        userInfo.setToken(loginToken);
//        userInfo.setName(dto.getUsername());
//        userInfo.setAvatar("https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
//        userInfo.setIntroduction("超级管理员");
//        userInfo.setRoles(Collections.singletonList("adminExclusive"));
//        return JsonResult.of(userInfo);
//    }
}

