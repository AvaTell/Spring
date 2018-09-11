package com.example.SpringServerAvatell;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class FinderController {
    @RequestMapping("/finder")
    public String finderPage(HttpServletRequest request, Model model) {
        return ("finder");
    }
}
