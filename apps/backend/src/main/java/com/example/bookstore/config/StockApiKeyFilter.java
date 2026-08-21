package com.example.bookstore.config;

import com.example.bookstore.exception.ApiError;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Protects the stock mutation endpoints with a static API key passed in the
 * {@code X-API-Key} header. The key grants a single STOCK_MANAGER scope.
 * Every other endpoint is public. See
 * docs/sources/decisions/2026-08-21-008-api-key-auth.md.
 */
@Component
public class StockApiKeyFilter extends OncePerRequestFilter {

    private static final String API_KEY_HEADER = "X-API-Key";
    private static final String SET_STOCK_PATH = "^/api/books/\\d+/stock$";
    private static final String ADJUST_STOCK_PATH = "^/api/books/\\d+/stock/adjust$";

    private final ObjectMapper objectMapper;
    private final String apiKey;

    public StockApiKeyFilter(ObjectMapper objectMapper, @Value("${bookstore.api-key:}") String apiKey) {
        this.objectMapper = objectMapper;
        this.apiKey = apiKey;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String method = request.getMethod();
        String path = request.getRequestURI();
        boolean protectedRoute = ("PUT".equals(method) && path.matches(SET_STOCK_PATH))
                || ("PATCH".equals(method) && path.matches(ADJUST_STOCK_PATH));
        return !protectedRoute;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String provided = request.getHeader(API_KEY_HEADER);
        if (apiKey != null && !apiKey.isBlank() && apiKey.equals(provided)) {
            filterChain.doFilter(request, response);
            return;
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ApiError error = ApiError.of(401, "Unauthorized", "Missing or invalid API key", request.getRequestURI());
        response.getWriter().write(objectMapper.writeValueAsString(error));
    }
}
