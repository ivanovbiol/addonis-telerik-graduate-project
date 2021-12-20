package com.company.addonis.controllers.mvc;

import com.company.addonis.controllers.AuthenticationHelper;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpSession;

public class BaseAuthenticationController {

    private final AuthenticationHelper authenticationHelper;

    public BaseAuthenticationController(AuthenticationHelper authenticationHelper) {
        this.authenticationHelper = authenticationHelper;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @ModelAttribute("isUser")
    public boolean populateIsUser(HttpSession session) {
        return session.getAttribute("currentUser") != null &&
                authenticationHelper.tryGetUser(session).isUser();
    }

    @ModelAttribute("isEnabledUser")
    public boolean populateIsEnabled(HttpSession session) {
        return populateIsUser(session) &&
                authenticationHelper.tryGetUser(session).isEnabled();
    }

    @ModelAttribute("isAdmin")
    public boolean populateIAdmin(HttpSession session) {
        return session.getAttribute("currentUser") != null &&
                authenticationHelper.tryGetUser(session).isAdmin();
    }

    public AuthenticationHelper getAuthenticationHelper() {
        return authenticationHelper;
    }
}
