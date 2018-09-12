package com.example.SpringServerAvatell;

import net.avalara.avatax.rest.client.AvaTaxClient;
import net.avalara.avatax.rest.client.enums.AvaTaxEnvironment;
import net.avalara.avatax.rest.client.models.PingResultModel;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@SessionAttributes({"username","password"})
public class AuthController {
    @RequestMapping("/logout")
    public String logout(HttpServletRequest request, Model model){
        HttpSession sesh = request.getSession();
        sesh.removeAttribute("username");
        sesh.removeAttribute("password");
        sesh.removeAttribute("isLoggedIn");
        sesh.invalidate();
        model.addAttribute("password",null);
        model.addAttribute("username",null);

        return "redirect:/index";
    }
    @GetMapping("/auth")
    public String authPage(HttpServletRequest request, Model model, @RequestParam String user, @RequestParam String pass){
        HttpSession sesh = request.getSession();
        AvaTaxClient client = new AvaTaxClient("Test","1.0","localhost",AvaTaxEnvironment.Production).withSecurity(user,pass);
        try{
            PingResultModel ping = client.ping();
            if(ping.getAuthenticated()){
                System.out.println("Authentication recieved!");
                sesh.setAttribute("username",user);
                sesh.setAttribute("password",pass);
                sesh.setAttribute("isLoggedIn", true);
                return "redirect:/finder";
            }else{
                System.out.println("Authentication rejected");
            }
        }catch(Exception e){
            System.out.println("inauthenticated");
            System.out.println(e);
        }
        return "redirect:/index";
    }
}
