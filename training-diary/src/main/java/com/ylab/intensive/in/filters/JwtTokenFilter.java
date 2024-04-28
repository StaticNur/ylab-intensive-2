package com.ylab.intensive.in.filters;

import com.ylab.intensive.model.dto.ExceptionResponse;
import com.ylab.intensive.security.Authentication;
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

@WebFilter(urlPatterns = "/*", initParams = @WebInitParam(name = "order", value = "1"))
@ApplicationScoped
public class JwtTokenFilter implements Filter {

    private static final Set<String> PUBLIC_PATH = Set.of(LOGIN.getPath(), REGISTRATION.getPath());

    private JwtTokenService jwtTokenService;

    private Converter converter;

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
     * Checks whether a given URI is a public path.
     * Public paths are paths that do not require authentication, such as login and registration.
     *
     * @param uri the URI to check
     * @return true if the URI is a public path, false otherwise
     */

    private boolean isPublicPath(String uri) {
        return PUBLIC_PATH.stream().anyMatch(uri::startsWith);
    }
}
