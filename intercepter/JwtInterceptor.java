package com.example.system.intercepter;

import com.example.system.context.BaseContext;
import com.example.system.preporties.JwtProperties;
import com.example.system.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 校验jwt
     *
     * @param request  请求对象
     * @param response 响应对象
     * @param handler  处理器
     * @return boolean 返回true表示继续执行，false表示拦截
     * @throws Exception 异常
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 判断当前拦截到的是 Controller 的方法还是其他资源
        if (!(handler instanceof HandlerMethod)) {
            // 当前拦截到的不是动态方法，直接放行
            return true;
        }

        // 1、从请求头中获取令牌
        String token = request.getHeader(jwtProperties.getAdminTokenName());

        if (token == null || token.isEmpty()) {
            // 如果没有提供 token，跳转到登录页面
            response.sendRedirect("/admin/login.html");
            return false;
        }

        // 2、校验令牌
        try {
            log.info("jwt 校验: {}", token);
            Claims claims = JwtUtil.parseJWT(jwtProperties.getAdminSecretKey(), token);
            Long empId = Long.valueOf(claims.get("管理员ID").toString());
            log.info("当前员工 ID: {}", empId);
            BaseContext.setCurrentId(empId);  // 设置当前用户 ID

            // 3、校验通过，放行
            return true;
        } catch (Exception ex) {
            // 4、校验失败，跳转到登录页面
            log.error("JWT 校验失败", ex);
            response.sendRedirect("/admin/login.html");
            return false;
        }
    }
}
