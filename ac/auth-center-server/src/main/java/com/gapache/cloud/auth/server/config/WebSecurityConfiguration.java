package com.gapache.cloud.auth.server.config;

import com.gapache.cloud.auth.server.model.UserDetailsImpl;
import com.gapache.cloud.auth.server.security.GenerateRefreshTokenStrategy;
import com.gapache.cloud.auth.server.security.impl.JwtGenerateTokenStrategy;
import com.gapache.cloud.auth.server.security.impl.UUIDGenerateRefreshTokenStrategy;
import com.gapache.security.holder.AccessCardHolder;
import com.gapache.security.interfaces.GenerateTokenStrategy;
import com.gapache.security.model.AccessCard;
import com.gapache.security.utils.AccessCardUtils;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.PrivateKey;
import java.util.stream.Collectors;

/**
 * 前后端分离的情况下有的数据可以让别人看无需加密
 * 前端做的加密其实都是不安全的
 * 特殊业务用上特殊的进行特殊的验证来解决，如进行手机短信验证等
 * <p>
 * 开放的接口需要进行签名校验
 * 加密由https来做（防止中间人攻击）
 * 由请求方生成公钥和私钥，请求方使用私钥进行签名，我们使用公钥进行验签
 *
 * @author HuSen
 * @since 2020/7/31 10:40 上午
 */
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    /**
     * 登录页面
     */
    private static final String LOGIN = "/login";

    /**
     * 登出地址
     */
    private static final String LOGOUT = "/logout";

    @Resource
    @Qualifier("userService")
    private UserDetailsService userDetailsService;

    @Resource
    private BeforeUsernamePasswordAuthenticationFilter beforeUsernamePasswordAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public GenerateTokenStrategy generateTokenStrategy(PrivateKey privateKey) {
        return new JwtGenerateTokenStrategy(privateKey);
    }

    @Bean
    public GenerateRefreshTokenStrategy generateRefreshTokenStrategy() {
        return new UUIDGenerateRefreshTokenStrategy();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.headers().cacheControl();

        http.authorizeRequests()
                .antMatchers(HttpMethod.POST, "/auth/login").permitAll()
                .antMatchers(HttpMethod.POST, "/auth/logout").permitAll()
                .antMatchers(HttpMethod.POST, "/api/user").permitAll()
                .antMatchers(HttpMethod.GET, "/api/user/isEnabled/*").permitAll()
                .antMatchers(HttpMethod.POST, "/api/client").permitAll()
                .antMatchers(HttpMethod.POST, "/api/client/bindUser").permitAll()
                .antMatchers(HttpMethod.POST, "/oauth/token").permitAll()
                .antMatchers(HttpMethod.POST, "/oauth/check").permitAll()
                .antMatchers(HttpMethod.POST, "/oauth/userAuthorize").permitAll()
                .antMatchers(HttpMethod.POST, "/api/resource/receiveReportData").permitAll()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .anyRequest().authenticated();

        http.headers().cacheControl();

        http.formLogin()
                .loginPage(LOGIN)
                .and()
                .logout().logoutUrl(LOGOUT).logoutSuccessUrl(LOGIN).permitAll();

        http.addFilterBefore(beforeUsernamePasswordAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

    private static final String CSS = "/css/**";
    private static final String JS = "/js/**";
    private static final String LIB = "/lib/**";
    private static final String IMAGES = "/images/**";
    private static final String FONTS = "/fonts/**";
    private static final String ELE_TREE = "/eleTree.js";
    private static final String FAVICON = "/favicon.ico";
    private static final String EXCEL = "/excel/**";
    private static final String LAY_EXT = "/layui_exts/**";
    private static final String LAY = "/layui/**";

    @Override
    public void configure(WebSecurity web) {
        // 不去拦截这些静态资源
        web.ignoring().antMatchers(IMAGES, LIB, CSS, FONTS, JS, ELE_TREE, FAVICON, EXCEL, LAY_EXT, LAY);
    }

    @Component
    @Order(1)
    static class BeforeUsernamePasswordAuthenticationFilter extends OncePerRequestFilter {

        @Override
        protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
            AccessCardUtils.checkAccessCard(request);

            AccessCard accessCard = AccessCardHolder.getContext();
            if (accessCard.getUserId() != null) {
                UserDetailsImpl userDetails = new UserDetailsImpl(
                        accessCard.getUserId(),
                        accessCard.getUsername(),
                        "123456",
                        CollectionUtils.isEmpty(accessCard.getAuthorities()) ? Lists.newArrayList() : accessCard.getAuthorities().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()),
                        accessCard.getCustomerInfo(),
                        accessCard.getClientId()
                );
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            filterChain.doFilter(request, response);
        }
    }
}
