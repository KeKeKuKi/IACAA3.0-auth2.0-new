package com.gapache.security.interfaces;

import com.gapache.security.model.ClientDTO;

/**
 * @author HuSen
 * @since 2021/1/25 5:28 下午
 */
public interface ClientLoader {

    /**
     * 加载PublicKey
     *
     * @param clientId clientId
     * @return 公钥
     */
    ClientDTO load(String clientId);
}
