package com.ylab.intensive.in.filters;

import com.ylab.intensive.aspects.annotation.Loggable;
import com.ylab.intensive.exception.*;
import com.ylab.intensive.model.dto.ExceptionResponse;
import com.ylab.intensive.util.Converter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.annotation.WebInitParam;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ValidationException;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.DataFormatException;

/**
 * {@code ExceptionHandlerFilter} is a servlet filter responsible for handling exceptions
 * thrown during the processing of requests. It catches specified runtime exceptions and
 * customizes the HTTP response accordingly.
 *
 * <p>The filter is defined with the following annotations:
 * {@code @WebFilter(filterName = "a_exception_handler", value = "/*")} -
 * Specifies the filter name and URL pattern to which the filter should be applied.
 * {@code @ApplicationScoped} - Specifies that the filter instance is application-scoped.
 * {@code @Slf4j} - Lombok annotation for automatic generation of a logger field.
 *
 * <p>The filter initializes a map of exception classes and their corresponding HTTP status codes
 * in the {@code init} method. The exceptions and status codes are used during the filter processing.
 *
 * <p>The filter class is injected with a {@code Converter} instance using the {@code @Inject} annotation.
 *
 * <p>The main logic of the filter is in the {@code doFilter} method, where it invokes the
 * {@code doFilter} method of the next filter in the chain. If a runtime exception occurs during
 * the processing, it checks if the exception type is in the predefined map. If yes, it customizes
 * the HTTP response with the appropriate status code and error message. Otherwise, it logs the
 * exception and rethrows it.
 *
 * @author Eugene Kulik
 * @see Filter
 * @see WebFilter
 * @see ApplicationScoped
 * @see Slf4j
 * @see Loggable
 */
@WebFilter(filterName = "a_exception_handler", urlPatterns = "/*",
        initParams = @WebInitParam(name = "order", value = "2"))
@ApplicationScoped
@Log4j2
public class ExceptionHandlerFilter implements Filter {

    private Converter converter;
    private static final Map<Class<? extends Exception>, Integer> exceptions = new HashMap<>();

    /**
     * Injects the {@code Converter} instance into the filter.
     *
     * @param converter The {@code Converter} instance to be injected.
     */
    @Inject
    public void inject(Converter converter) {
        this.converter = converter;
    }

    /**
     * Default list of exception classes to catch.
     */
    static  {
        exceptions.put(RegistrationException.class, HttpServletResponse.SC_BAD_REQUEST);
        exceptions.put(AuthorizeException.class, HttpServletResponse.SC_BAD_REQUEST);
        exceptions.put(InvalidTokenException.class, HttpServletResponse.SC_BAD_REQUEST);
        exceptions.put(DataFormatException.class, HttpServletResponse.SC_BAD_REQUEST);
        exceptions.put(InvalidUUIDException.class, HttpServletResponse.SC_BAD_REQUEST);
        exceptions.put(TrainingTypeException.class, HttpServletResponse.SC_BAD_REQUEST);
        exceptions.put(WorkoutException.class, HttpServletResponse.SC_BAD_REQUEST);
        exceptions.put(WorkoutInfoException.class, HttpServletResponse.SC_BAD_REQUEST);
        exceptions.put(WorkoutTypeException.class, HttpServletResponse.SC_BAD_REQUEST);
        exceptions.put(NotFoundException.class, HttpServletResponse.SC_BAD_REQUEST);
        exceptions.put(InvalidInputException.class, HttpServletResponse.SC_BAD_REQUEST);

        exceptions.put(AccessDeniedException.class, HttpServletResponse.SC_FORBIDDEN);
        exceptions.put(ChangeUserPermissionsException.class, HttpServletResponse.SC_FORBIDDEN);

        exceptions.put(DaoException.class, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

    }

    /**
     * Processes the request by invoking the next filter in the chain. Catches runtime exceptions
     * and customizes the HTTP response accordingly.
     *
     * @param servletRequest  The request object.
     * @param servletResponse The response object.
     * @param filterChain     The filter chain.
     * @throws IOException      If an I/O error occurs.
     * @throws ServletException If a servlet-related exception occurs.
     */
    @Override
    @Loggable
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (RuntimeException exception) {
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            if (exceptions.containsKey(exception.getClass())) {
                response.setStatus(exceptions.get(exception.getClass()));
            } else {
                log.error(exception.getMessage(), exception);
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
            response.getWriter()
                    .append(converter.convertObjectToJson(new ExceptionResponse(exception.getMessage())));
        }
    }
}
