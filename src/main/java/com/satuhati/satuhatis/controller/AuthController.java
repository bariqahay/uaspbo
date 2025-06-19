package com.satuhati.satuhatis.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String login() {
        return "login"; // View template: src/main/resources/templates/login.html
    }

    // GAK PERLU register kalo admin doang yg nambahin user dari dashboard/backend/db
}
