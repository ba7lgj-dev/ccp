package org.ba7lgj.ccp.miniprogram.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.ba7lgj.ccp.miniprogram.context.UserContextHolder;
import org.ba7lgj.ccp.miniprogram.util.JwtTokenUtil;
import org.ba7lgj.ccp.miniprogram.vo.Result;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthTokenInterceptor implements HandlerInterceptor {
    private static final Set<String> WHITE_LIST = new HashSet<>(Arrays.asList(
            "/auth/wxLogin",
            "/auth/wxPhoneBind",
            "/mp/school/list",
            "/mp/campus/listBySchool",
            "/mp/gate/listByCampus"
    ));

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getRequestURI();
        if (WHITE_LIST.contains(path)) {
            return true;
        }
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            writeInvalidToken(response);
            return false;
        }
        String token = authHeader.substring("Bearer ".length());
        Long userId = JwtTokenUtil.parseUserId(token);
        if (userId == null) {
            writeInvalidToken(response);
            return false;
        }
        UserContextHolder.setUserId(userId);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        UserContextHolder.clear();
    }

    private void writeInvalidToken(HttpServletResponse response) throws Exception {
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        Result<Void> result = Result.error(4001, "token invalid");
        ObjectMapper objectMapper = new ObjectMapper();
        try (PrintWriter writer = response.getWriter()) {
            writer.write(objectMapper.writeValueAsString(result));
        }
    }
}
