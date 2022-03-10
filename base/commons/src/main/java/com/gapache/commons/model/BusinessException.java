package com.gapache.commons.model;

import lombok.Getter;

/**
 * @author HuSen
 * create on 2020/1/14 14:25
 */
@Getter
public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = -8570405796076634812L;

    private final Error error;

    public BusinessException(Error error) {
        super(error.getError());
        this.error = error;
    }
}
