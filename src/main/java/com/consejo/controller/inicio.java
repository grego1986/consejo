package com.consejo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class inicio {

    @GetMapping("/")
    public String index() {
        return "index"; // Retorna la vista `index.html`
    }
}
