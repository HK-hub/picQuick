package com.cqut.picquick.filter;

import com.cqut.picquick.common.ResponseResult;
import com.cqut.picquick.enums.ResultCode;
import com.cqut.picquick.util.JwtToken;
import lombok.Data;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author : HK意境
 * @ClassName : JwtFilter
 * @date : 2021/9/14 15:37
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Component
public class JwtFilter /*extends BasicHttpAuthenticationFilter implements Filter*/ {

   /* @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        // 获取请求header中的token
        String token = httpServletRequest.getHeader("token");
        JwtToken jwtToken = new JwtToken(token);
        // 提交给realm进行登入，如果错误他会抛出异常并被捕获
        try {
            // 使用jwt token登录Shiro
            getSubject(request, response).login(jwtToken);
            return true;
        } catch (Exception e) {
            ResponseResult responseResult = new ResponseResult(ResultCode.UNAUTHORIZED);
            response.setContentType("application/json; charset=utf-8");
            response.getWriter().write(responseResult.toString());
            return false;
        }
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        try {
            return executeLogin(request, response);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setStatus(200);
        return false;
    }

    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        return super.preHandle(request, response);
    }*/
}