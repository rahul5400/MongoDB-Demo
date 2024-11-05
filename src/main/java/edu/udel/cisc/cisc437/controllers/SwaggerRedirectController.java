package edu.udel.cisc.cisc437.controllers;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@RestController
public class SwaggerRedirectController {
    @GetMapping("/")
    public void handleMain(HttpServletResponse res) throws IOException {
        res.sendRedirect("/swagger-ui.html");
    }
}
