package com.imooc.order.utils;

import com.imooc.order.vo.ResultVO;

/**
 * Created by hunter on 2018/4/17.
 */
public class ResultVOUtil {
    public static ResultVO success(Object object) {
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(0);
        resultVO.setMsg("成功");
        resultVO.setData(object);
        return resultVO;
    }
}
