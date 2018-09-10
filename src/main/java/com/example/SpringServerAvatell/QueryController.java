package com.example.SpringServerAvatell;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.net.URL;

@Controller
//@SessionAttribute("password")
//@SessionAttribute("username")
@ResponseBody()
public class QueryController {
    @GetMapping("/query")
    public String queryPage(HttpServletRequest request, Model model, @RequestParam String companyID, @RequestParam String zipCode){
        RestTemplate rTemp = new RestTemplate();
        try{
            URL getCompURL = new URL("https://rest.avatax.com/api/v2/companies/"+companyID);
            rTemp.getForObject(getCompURL);
        }catch(Exception e){
            System.out.println("Bad");
        }
    }
}
