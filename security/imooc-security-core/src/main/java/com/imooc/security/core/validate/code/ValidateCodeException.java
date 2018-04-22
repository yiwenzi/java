package com.imooc.security.core.validate.code;

import org.springframework.security.core.AuthenticationException;

/**
 * Created by hunter on 2018/2/13.
 */
public class ValidateCodeException extends AuthenticationException {

    /**
     *
     */
    private static final long serialVersionUID = -7285211528095468156L;

    public ValidateCodeException(String msg) {
        super(msg);
    }

}
