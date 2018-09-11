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
        if(sesh.getAttribute("password")!=null&&sesh.getAttribute("username")!=null&&sesh.getAttribute("isLoggedIn")!=null){
            return ("finder");
        }
        return ("redirect:/index");
    }
}
