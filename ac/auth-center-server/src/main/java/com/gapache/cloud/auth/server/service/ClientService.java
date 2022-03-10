package com.gapache.cloud.auth.server.service;

import com.gapache.cloud.auth.server.model.ClientDetailsImpl;
import com.gapache.commons.model.IPageRequest;
import com.gapache.commons.model.PageResult;
import com.gapache.security.model.ClientDTO;

/**
 * @author HuSen
 * @since 2020/7/31 5:24 下午
 */
public interface ClientService {

    ClientDetailsImpl findByClientId(String clientId);

    Boolean create(ClientDTO clientDTO);

    Boolean update(ClientDTO clientDTO);

    ClientDetailsImpl findById(Long id);

    ClientDTO findDtoByClientId(String clientId);

    PageResult<ClientDTO> page(IPageRequest<ClientDTO> iPageRequest);
}
