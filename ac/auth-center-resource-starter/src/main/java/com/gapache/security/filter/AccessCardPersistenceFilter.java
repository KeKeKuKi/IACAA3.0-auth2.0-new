package com.gapache.security.filter;

import com.gapache.commons.helper.AccessCardHeaderHolder;
import com.gapache.security.holder.AccessCardHolder;
import com.gapache.security.utils.AccessCardUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author HuSen
 * @since 2020/8/9 5:44 下午
 */
@Slf4j
@Order(0)
@WebFilter(filterName = "accessCardPersistenceFilter", urlPatterns = "/*")
public class AccessCardPersistenceFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            AccessCardUtils.checkAccessCard(request);
            filterChain.doFilter(request, response);
        } finally {
            AccessCardHolder.clearContext();
            AccessCardHeaderHolder.clear();
            AccessCardUtils.clearCurrentOpMan();
            request.removeAttribute(AccessCardUtils.FILTER_APPLIED);
            log.info("AccessCardHolder now cleared, as request processing completed");
        }
    }

    @Override
    public void destroy() {
        log.info("destroy accessCardPersistenceFilter");
    }
}
