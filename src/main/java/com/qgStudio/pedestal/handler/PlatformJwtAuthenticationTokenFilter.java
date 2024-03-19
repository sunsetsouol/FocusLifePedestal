package com.qgStudio.pedestal.handler;

import com.alibaba.fastjson2.JSON;
import com.qgStudio.pedestal.constant.RedisConstants;
import com.qgStudio.pedestal.entity.bo.UserDetailsImpl;
import com.qgStudio.pedestal.entity.vo.Result;
import com.qgStudio.pedestal.entity.vo.ResultStatusEnum;
import com.qgStudio.pedestal.utils.JwtUtils;
import com.qgStudio.pedestal.utils.RedisCache;
import com.qgStudio.pedestal.utils.WebUtils;
import io.jsonwebtoken.Claims;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author yinjunbiao
 * @version 1.0
 * @date 2023/11/16
 */
@Data
@Component
public class PlatformJwtAuthenticationTokenFilter extends OncePerRequestFilter {

    private final RedisCache redisCache;

    @Autowired
    public PlatformJwtAuthenticationTokenFilter(RedisCache redisCache) {
        this.redisCache = redisCache;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = httpServletRequest.getRequestURI();
        if (requestURI.startsWith("/user/login") ||
                requestURI.startsWith("/user/register") ||
                requestURI.startsWith("/swagger") ||
                requestURI.startsWith("/v2/api-docs") ||
                requestURI.startsWith("/webjars") ||
                requestURI.startsWith("/doc.html") ||
                requestURI.startsWith("/test") ||
                requestURI.startsWith("/configuration")) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }
        try {

            String token = httpServletRequest.getHeader("Authorization");
            Claims claims = JwtUtils.parseJWT(token);
            String id = claims.get("id").toString();
            UserDetailsImpl userDetails = JSON.parseObject(redisCache.getCacheObject(RedisConstants.USER_LOGIN + id), UserDetailsImpl.class);
//             将用户信息存入SecurityContextHolder中
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        } catch (Exception e) {
            WebUtils.sendJson(httpServletResponse, Result.fail(ResultStatusEnum.NOT_AUTHORIZATION));
            return;
        }

        // 放行
        filterChain.doFilter(httpServletRequest, httpServletResponse);

    }
}
