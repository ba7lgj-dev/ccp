package org.ba7lgj.ccp.miniprogram.filter;

import org.ba7lgj.ccp.common.constants.CcpConstants;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 通过请求头标记客户端类型。
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MpClientTypeFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String client = request.getHeader("X-Client");
        if (client != null && CcpConstants.CLIENT_TYPE_MINIAPP.equalsIgnoreCase(client)) {
            request.setAttribute("clientType", CcpConstants.CLIENT_TYPE_MINIAPP);
        } else {
            request.setAttribute("clientType", CcpConstants.CLIENT_TYPE_ADMIN);
        }
        filterChain.doFilter(request, response);
    }
}
