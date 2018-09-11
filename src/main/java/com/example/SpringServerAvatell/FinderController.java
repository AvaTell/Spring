package com.example.SpringServerAvatell;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class FinderController {
    @RequestMapping("/finder")
    public String finderPage(HttpServletRequest request, Model model) {
        HttpSession sesh = request.getSession();
        if((model.asMap().get("username")==null||model.asMap().get("password")==null||model.asMap().get("isLoggedIn")==null)&&(sesh.getAttribute("username")==null||sesh.getAttribute("password")==null)){
            return ("finder");
        }
        return ("redirect:/index");
    }
}
