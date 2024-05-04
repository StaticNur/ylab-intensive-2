/*
package com.ylab.intensive.in.servlets;

import com.ylab.intensive.model.Pageable;
import com.ylab.intensive.model.dto.AuditDto;
import com.ylab.intensive.model.Authentication;
import com.ylab.intensive.service.UserService;
import com.ylab.intensive.util.Converter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;

import java.io.IOException;

*/
/**
 * The AuditServlet class is a servlet responsible for retrieving and displaying audit logs of user actions.
 * <p>
 * This servlet allows users to view audit logs of their actions by sending a GET request to the "training-diary/user/audit" endpoint.
 * The servlet retrieves the audit logs from the AuditService, converts them to DTOs, and returns them in JSON format.
 *//*

@WebServlet("/training-diary/user/audit")
@ApplicationScoped
@NoArgsConstructor
public class AuditServlet extends HttpServlet {

    private UserService userService;

    private Converter converter;

    @Inject
    public void inject(UserService userService, Converter converter) {
        this.userService = userService;
        this.converter = converter;
    }

    */
/**
     * Handles GET requests to show audit logs of user actions.
     *
     * @param req  the HTTP servlet request
     * @param resp the HTTP servlet response
     * @throws IOException if an I/O error occurs during request processing
     *//*

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int page = converter.getInteger(req, "page");
        int count = converter.getInteger(req, "count");
        if (count == 0) count = 10;
        Authentication authentication = (Authentication) (req.getServletContext()).getAttribute("authentication");
        AuditDto audit = userService.getAudit(authentication.getLogin(), new Pageable(page, count));
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter()
                .append(converter.convertObjectToJson(audit));
    }
}
*/
