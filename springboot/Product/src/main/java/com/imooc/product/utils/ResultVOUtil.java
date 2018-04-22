package com.imooc.product.utils;

import com.imooc.product.vo.ResultVO;

/**
 * Created by hunter on 2018/4/16.
 */
public class ResultVOUtil {
    public static ResultVO success(Object object) {
        ResultVO resultVO = new ResultVO();
        resultVO.setData(object);
        resultVO.setCode(0);
        resultVO.setMsg("成功");
        return resultVO;
    }
}
