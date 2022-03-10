package com.gapache.cloud.auth.server.model;

import com.gapache.security.model.CustomerInfo;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * @author HuSen
 * @since 2020/7/31 10:15 上午
 */
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = -7492932138645849421L;
    /**
     * userId
     */
    private Long id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 授予的权限
     */
    private List<GrantedAuthority> authorities;
    /**
     * 自定义信息
     */
    private CustomerInfo customerInfo;
    /**
     * 当前正在进行授权的clientId
     */
    private String clientId;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public CustomerInfo getCustomerInfo() {
        if (customerInfo != null) {
            return customerInfo.copy();
        }
        return new CustomerInfo();
    }

    public Long getId() {
        return id;
    }

    public String getClientId() {
        return clientId;
    }
}
