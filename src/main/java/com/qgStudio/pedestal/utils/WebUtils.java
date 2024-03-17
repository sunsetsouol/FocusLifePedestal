package com.qgStudio.pedestal.utils;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author yinjunbiao
 * @version 1.0
 * @date 2023/11/15
 */
@Slf4j
public class WebUtils {
    public static void sendJson(HttpServletResponse httpServletResponse, Object object) {
        try {
            httpServletResponse.setCharacterEncoding("UTF-8");
            httpServletResponse.setContentType("application/json;charset=utf-8");

            PrintWriter writer = httpServletResponse.getWriter();
            writer.println(JSON.toJSONString(object));
            writer.flush();
        } catch (IOException e) {
            log.debug("没有权限登录返回错误失败");
        }
    }
}
