package com.gapache.cloud.auth.server.controller;

import com.gapache.cloud.auth.server.constant.GrantType;
import com.gapache.cloud.auth.server.constant.ResponseType;
import com.gapache.cloud.auth.server.model.*;
import com.gapache.cloud.auth.server.security.BaseGenerateTokenLogic;
import com.gapache.cloud.auth.server.security.CodeStrategy;
import com.gapache.cloud.auth.server.security.ScopeManager;
import com.gapache.cloud.auth.server.security.SecurityContextHelper;
import com.gapache.cloud.auth.server.service.ClientService;
import com.gapache.cloud.auth.server.service.UserClientRelationService;
import com.gapache.commons.model.Error;
import com.gapache.commons.model.*;
import com.gapache.security.checker.SecurityChecker;
import com.gapache.commons.model.SecurityException;
import com.gapache.security.model.AccessCard;
import com.gapache.security.model.SecurityError;
import com.gapache.security.model.TokenInfoDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author HuSen
 * @since 2020/7/31 5:20 下午
 */
@Slf4j
@Controller
@RequestMapping("/oauth")
public class Oauth2Controller {

    public Oauth2Controller(Map<String, BaseGenerateTokenLogic> generateTokenLogicMap, CodeStrategy codeStrategy, ClientService clientService, UserClientRelationService userClientRelationService, ScopeManager scopeManager, SecurityChecker securityChecker) {
        this.codeStrategy = codeStrategy;
        this.clientService = clientService;
        this.userClientRelationService = userClientRelationService;
        this.scopeManager = scopeManager;
        this.securityChecker = securityChecker;
        log.info("Oauth2Controller init:{}", generateTokenLogicMap);
        this.generateTokenLogicMap = generateTokenLogicMap;
    }

    @ExceptionHandler
    @ResponseBody
    public JsonResult<Object> exceptionHandler(Exception e) {
        if (e instanceof SecurityException) {
            SecurityException securityException = (SecurityException) e;
            Error error = securityException.getError();
            log.error("SecurityException:{}, {}", error.getCode(), error.getError());
            return JsonResult.of(error);
        } else if (e instanceof BusinessException) {
            BusinessException businessException = (BusinessException) e;
            Error error = businessException.getError();
            log.error("BusinessException:{}, {}", error.getCode(), error.getError());
            return JsonResult.of(error);
        } else {
            log.error("Oauth2Controller error ", e);
            return JsonResult.of(SystemError.SERVER_EXCEPTION);
        }
    }

    private final Map<String, BaseGenerateTokenLogic> generateTokenLogicMap;
    private final CodeStrategy codeStrategy;
    private final ClientService clientService;
    private final UserClientRelationService userClientRelationService;
    private final ScopeManager scopeManager;
    private final SecurityChecker securityChecker;

    @PostMapping("/userAuthorize")
    public void userAuthorize(UserAuthorizeDTO userAuthorizeDTO, HttpServletResponse response) {
        // 进到这里用户肯定是已经登陆了的
        UserDetailsImpl userDetails = SecurityContextHelper.getUserDetails();
        if (userDetails == null) {
            log.info("please login");
            callback("login", response);
            return;
        }

        String clientId = userDetails.getClientId();
        ClientDetailsImpl clientDetails = clientService.findByClientId(clientId);
        if (clientDetails == null) {
            log.info("clientDetails is null:{}", clientId);
            callback(userAuthorizeDTO.getRedirectUrl(), response);
            return;
        }
        // 不含有该权限
        List<String> scopeList = Arrays.stream(userAuthorizeDTO.getScopes().split(",")).collect(Collectors.toList());
        boolean notHasThisPermission = scopeList.stream()
                .anyMatch(scope -> !userDetails.getAuthorities().contains(new SimpleGrantedAuthority(scope)) ||
                !clientDetails.getScopes().contains(scope));
        ThrowUtils.throwIfTrue(notHasThisPermission, SecurityError.FORBIDDEN);

        // 临时缓存scopes
        scopeManager.save(clientId, userDetails.getId(), clientDetails.getRefreshTokenTimeout(), new HashSet<>(scopeList));

        String redirectUrl = userAuthorizeDTO.getRedirectUrl();
        if (StringUtils.isBlank(redirectUrl)) {
            redirectUrl = clientDetails.getRedirectUrl();
        }

        // 生成code并响应
        String code = codeStrategy.generate(userDetails.getId(), userDetails.getUsername(), userDetails.getCustomerInfo());
        callback(redirectUrl + "?code=" + code, response);
    }

    @GetMapping("/authorize")
    public void authorize(ResponseType responseType, String clientId, String redirectUrl, @RequestParam(required = false) String scope, HttpServletResponse response) {
        ClientDetailsImpl clientDetails = clientService.findByClientId(clientId);
        if (clientDetails == null) {
            log.info("client不存在:{}", clientId);
            callback(redirectUrl, response);
            return;
        }
        redirectUrl = StringUtils.isNotBlank(redirectUrl) ? redirectUrl : clientDetails.getRedirectUrl();
        switch (responseType) {
            // 授权码模式
            case code: {
                UserDetailsImpl userDetails  = SecurityContextHelper.getUserDetails();
                if (userDetails == null || !checkAuthorize(clientDetails, userDetails, scope)) {
                    if (userDetails == null) {
                        log.info("please login");
                    }
                    callback(redirectUrl, response);
                    return;
                }

                Long userId = userDetails.getId();
                String username = userDetails.getUsername();
                Collection<? extends GrantedAuthority> userDetailsAuthorities = userDetails.getAuthorities();

                userDetails.setClientId(clientId);
                // 自动选择scope
                if (clientDetails.getAutoGrant()) {
                    Set<String> allEnableScope = new HashSet<>();
                    if (StringUtils.isBlank(scope)) {
                        Set<String> clientScopes = clientDetails.getScopes();
                        allEnableScope = clientScopes.stream()
                                .filter(s -> userDetailsAuthorities.contains(new SimpleGrantedAuthority(s))).collect(Collectors.toSet());
                    } else {
                        allEnableScope.add(scope);
                    }
                    // 临时缓存scopes
                    scopeManager.save(clientId, userDetails.getId(), clientDetails.getRefreshTokenTimeout(), allEnableScope);
                    // 生成code并响应
                    String code = codeStrategy.generate(userDetails.getId(), username, userDetails.getCustomerInfo());
                    callback(redirectUrl + "?code=" + code, response);
                } else {
                    List<String> cacheScopes = scopeManager.get(clientId, userId);
                    if (CollectionUtils.isNotEmpty(cacheScopes) && cacheScopes.contains(scope)) {
                        // 生成code并响应
                        String code = codeStrategy.generate(userDetails.getId(), username, userDetails.getCustomerInfo());
                        callback(redirectUrl + "?code=" + code, response);
                    }
                    // 需要用户自己确认并选择
                    else {
                        if (StringUtils.isNotBlank(scope)) {
                            callback("/oauth/manualAuthorize?type=1&scopes=" + scope + "&redirectUrl=" + redirectUrl, response);
                        } else {
                            callback("/oauth/manualAuthorize?type=2&redirectUrl=" + redirectUrl, response);
                        }
                    }
                }
                break;
            }
            // 隐密模式
            case token: {
                break;
            }
            default: callback(redirectUrl, response);
        }
    }

    @PostMapping("/token")
    @ResponseBody
    public JsonResult<TokenInfoDTO> token(@RequestBody AuthorizeTokenDTO authorizeTokenDTO) {
        GrantType grantType = authorizeTokenDTO.getGrantType();
        BaseGenerateTokenLogic generateTokenLogic = generateTokenLogicMap.get(grantType.name());
        ThrowUtils.throwIfTrue(generateTokenLogic == null, SecurityError.NOT_SUPPORT);
        TokenInfoDTO dto = generateTokenLogicMap.get(grantType.name()).execute(authorizeTokenDTO);
        return JsonResult.of(dto);
    }

    @PostMapping("/check")
    @ResponseBody
    public JsonResult<AccessCard> check(@RequestBody Map<String, String> params) {
        String token = params.get("token");
        ThrowUtils.throwIfTrue(StringUtils.isBlank(token), SecurityError.INVALID_PARAMS);

        AccessCard accessCard = securityChecker.checking(token);
        ThrowUtils.throwIfTrue(accessCard == null, SecurityError.INVALID_TOKEN);

        return JsonResult.of(accessCard);
    }

    private void callback(String redirectUrl, HttpServletResponse response) {
        try {
            response.sendRedirect(redirectUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean checkAuthorize(ClientDetailsImpl clientDetails, UserDetailsImpl userDetails, String scope) {
        String clientId = clientDetails.getClientId();
        Long clientUid = clientDetails.getId();
        Long userId = userDetails.getId();
        String username = userDetails.getUsername();
        Collection<? extends GrantedAuthority> userDetailsAuthorities = userDetails.getAuthorities();

        if (!clientDetails.getGrantTypes().contains(GrantType.authorization_code)) {
            log.info("只支持授权码模式:{}", clientId);
            return false;
        }

        UserClientRelationDTO userClientRelationDTO = userClientRelationService.findByUserIdAndClientId(userId, clientUid);
        if (userClientRelationDTO == null) {
            log.info("用户不属于该client:{}, {}, {}", clientId, userId, username);
            return false;
        }

        if (StringUtils.isNotBlank(scope)) {
            if (!clientDetails.getScopes().contains(scope)) {
                log.info("该client不拥有该scope:{}, {}", clientId, scope);
                return false;
            }
            if (CollectionUtils.isEmpty(userDetailsAuthorities) || !userDetailsAuthorities.contains(new SimpleGrantedAuthority(scope))) {
                log.info("用户没有权限:{}, {}, {}, {}", clientId, userId, username, scope);
                return false;
            }
        }
        return true;
    }
}
