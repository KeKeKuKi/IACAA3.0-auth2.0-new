package com.gapache.security.model;

import com.dyuproject.protostuff.Tag;
import com.gapache.commons.model.AuthConstants;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * @author HuSen
 * @since 2020/8/5 11:07 上午
 */
@Data
public class AccessCard {
    @Tag(1)
    private Long userId;
    @Tag(2)
    private String username;
    @Tag(3)
    private CustomerInfo customerInfo;
    @Tag(4)
    private Set<String> authorities;
    @Tag(5)
    private String clientId;
    @Tag(6)
    private Boolean sign;

    public static AccessCard createEmpty() {
        AccessCard accessCard = new AccessCard();
        accessCard.setCustomerInfo(new CustomerInfo(0));
        accessCard.setAuthorities(new HashSet<>(0));
        accessCard.setSign(false);
        return accessCard;
    }

    public String checkClientId() {
        return StringUtils.isBlank(clientId) ? AuthConstants.VEA : clientId;
    }
}
