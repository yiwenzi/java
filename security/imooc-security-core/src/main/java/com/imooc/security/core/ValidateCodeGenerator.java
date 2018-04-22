package com.imooc.security.core;

import com.imooc.security.core.validate.code.ValidateCode;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * Created by hunter on 2018/2/13.
 */
public interface ValidateCodeGenerator {
    ValidateCode generate(ServletWebRequest request);
}
