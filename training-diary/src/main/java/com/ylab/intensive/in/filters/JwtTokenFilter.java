package com.ylab.intensive.in.filters;

import com.ylab.intensive.model.dto.ExceptionResponse;
import com.ylab.intensive.model.Authentication;
import com.ylab.intensive.security.JwtTokenService;
import com.ylab.intensive.util.Converter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.annotation.WebInitParam;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Set;

import static com.ylab.intensive.model.enums.Endpoints.LOGIN;
import static com.ylab.intensive.model.enums.Endpoints.REGISTRATION;

/**
 * Filter for validating JWT tokens and handling authentication.
 * <p>
 * This filter intercepts requests and checks for a valid JWT token in the Authorization header.
 * If a valid token is found, it extracts the authentication details and sets them in the servlet context.
 * Requests to public paths are allowed without authentication.
 * </p>
 *
 * @since 1.0
 */
@WebFilter(urlPatterns = "/*", initParams = @WebInitParam(name = "order", value = "1"))
@ApplicationScoped
public class JwtTokenFilter implements Filter {

    /**
     * Set of public paths that do not require authentication.
     */
    private static final Set<String> PUBLIC_PATH = Set.of(LOGIN.getPath(), REGISTRATION.getPath());

    /**
     * Service for handling JWT tokens.
     */
    private JwtTokenService jwtTokenService;

    /**
     * Converter for converting objects to JSON.
     */
    private Converter converter;

    /**
     * Servlet context for accessing the servlet container's context attributes.
     */
    private ServletContext servletContext;


    @Inject
    public void init(JwtTokenService jwtTokenService, Converter converter) {
        this.jwtTokenService = jwtTokenService;
        this.converter = converter;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        servletContext = filterConfig.getServletContext();
    }

    /**
     * Filters requests to validate JWT tokens and handle authentication.
     * <p>
     * This method intercepts incoming requests, checks for a valid JWT token in the Authorization header,
     * and validates it. If the token is valid, the authentication details are extracted and set in the servlet context.
     * Requests to public paths are allowed without authentication.
     * </p>
     *
     * @param servletRequest  the request to be filtered.
     * @param servletResponse the response to be filtered.
     * @param filterChain     the filter chain for invoking the next filter in the chain.
     * @throws IOException      if an I/O error occurs during the filtering process.
     * @throws ServletException if a servlet exception occurs during the filtering process.
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        if (isPublicPath(httpRequest.getRequestURI())) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        String bearerToken = httpRequest.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")
            && jwtTokenService.validateToken(bearerToken.substring(7))) {

            Authentication authentication = jwtTokenService.authentication(bearerToken.substring(7));
            servletContext.setAttribute("authentication", authentication);

            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            httpResponse.getWriter()
                    .append(converter.convertObjectToJson(
                            new ExceptionResponse("Authentication was denied for this request.")));
        }
    }

    /**
     * Checks if a request path is a public path.
     * <p>
     * This method determines whether a given request path corresponds to a public path,
     * where authentication is not required.
     * </p>
     *
     * @param uri the request path to be checked.
     * @return {@code true} if the path is a public path, {@code false} otherwise.
     */
    private boolean isPublicPath(String uri) {
        return PUBLIC_PATH.stream().anyMatch(uri::startsWith);
    }
}
