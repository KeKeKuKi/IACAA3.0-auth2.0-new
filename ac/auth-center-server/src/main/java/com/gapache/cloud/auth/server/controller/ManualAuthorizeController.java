package com.gapache.cloud.auth.server.controller;

import com.gapache.cloud.auth.server.model.ClientDetailsImpl;
import com.gapache.cloud.auth.server.model.UserDetailsImpl;
import com.gapache.cloud.auth.server.security.SecurityContextHelper;
import com.gapache.cloud.auth.server.service.ClientService;
import com.gapache.commons.model.ThrowUtils;
import com.gapache.security.model.SecurityError;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

/**
 * @author HuSen
 * @since 2020/8/4 11:36 上午
 */
@Slf4j
@Controller
@RequestMapping("/oauth")
public class ManualAuthorizeController {

    @Resource
    private ClientService clientService;

    /**
     * 1：指定授予某个权限
     * 2：给定全部的权限，用户选择权限授予
     * 3：自动授予所有权限
     *
     * @return 手动授权页面
     */
    @GetMapping("/manualAuthorize")
    public ModelAndView manualAuthorize(@RequestParam(required = false) Integer type, @RequestParam(required = false) String scopes, @RequestParam(required = false) String redirectUrl) {
        UserDetailsImpl userDetails = SecurityContextHelper.getUserDetails();
        ThrowUtils.throwIfTrue(userDetails == null, SecurityError.PLEASE_LOGIN);

        ClientDetailsImpl clientDetails = clientService.findByClientId(userDetails.getClientId());
        ThrowUtils.throwIfTrue(clientDetails == null, SecurityError.CLIENT_ERROR);

        redirectUrl = StringUtils.isNotBlank(redirectUrl) ? redirectUrl : clientDetails.getRedirectUrl();

        ModelAndView modelAndView = new ModelAndView("manualAuthorize")
                .addObject("redirectUrl", redirectUrl);
        switch (type) {
            case 1:
                ThrowUtils.throwIfTrue(StringUtils.isBlank(scopes), SecurityError.INVALID_PARAMS);
                String[] scopeArr = StringUtils.split(scopes, ",");
                // 过滤出合法的scope
                Set<String> validSet = new HashSet<>(scopeArr.length);
                Set<String> clientDetailsScopes = clientDetails.getScopes();
                for (String scope : scopeArr) {
                    if (clientDetailsScopes.contains(scope)) {
                        validSet.add(scope);
                    }
                }
                // 存储合法的scope
                modelAndView.addObject("scopes", validSet);
                break;
            case 2:
                modelAndView.addObject("scopes", clientDetails.getScopes());
                break;
            default:
                ThrowUtils.throwError(SecurityError.NOT_SUPPORT);
        }
        return modelAndView;
    }
}
