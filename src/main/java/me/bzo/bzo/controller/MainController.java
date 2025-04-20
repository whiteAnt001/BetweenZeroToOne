package me.bzo.bzo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class MainController {

    //플랫폼 메인화면
    @GetMapping("/")
    private String home(Model model) {
        return "main";
    }
}
