/*
package com.ylab.intensive.in.servlets;

import com.ylab.intensive.model.Pageable;
import com.ylab.intensive.model.dto.AuditDto;
import com.ylab.intensive.model.Authentication;
import com.ylab.intensive.service.UserService;
import com.ylab.intensive.util.Converter;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("Audit Service Tests")
@ExtendWith(MockitoExtension.class)
class AuditServletTest {

    @Mock
    private UserService userService;

    @Mock
    private Converter converter;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private ServletContext servletContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuditServlet auditServlet;

    private StringWriter stringWriter;

    @BeforeEach
    void setUp() throws IOException {
        stringWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));
        when(request.getServletContext()).thenReturn(servletContext);
        when(servletContext.getAttribute("authentication")).thenReturn(authentication);
    }

    @Test
    void testDoGet_shouldReturnAuditDto() throws IOException {
        String login = "testUser";
        when(authentication.getLogin()).thenReturn(login);
        AuditDto auditDto = new AuditDto();
        when(userService.getAudit(login, new Pageable(0,10))).thenReturn(auditDto);
        when(converter.convertObjectToJson(auditDto)).thenReturn("{\"audit\":\"data\"}");

        auditServlet.doGet(request, response);

        verify(userService).getAudit(login, new Pageable(0,10));
        verify(converter).convertObjectToJson(auditDto);
        verify(response).setStatus(HttpServletResponse.SC_OK);
        assert stringWriter.toString().equals("{\"audit\":\"data\"}");
    }
}*/
