package com.vincendp.RedditClone.Config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vincendp.RedditClone.Utility.SuccessResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    private ObjectMapper objectMapper;

    public CustomLogoutSuccessHandler(ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setStatus(200);
        PrintWriter writer = response.getWriter();
        objectMapper.writeValue(writer, new SuccessResponse(200, "Success: Logged out", null));
    }
}
