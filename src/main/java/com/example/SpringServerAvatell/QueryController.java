package com.example.SpringServerAvatell;

import net.avalara.avatax.rest.client.AvaTaxClient;
import net.avalara.avatax.rest.client.enums.AvaTaxEnvironment;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.net.URL;
import java.util.Base64;

@Controller
@SessionAttributes({"username","password"})
@ResponseBody()
public class QueryController {
    @GetMapping("/query")
    public String queryPage(HttpServletRequest request, Model model, @RequestParam String country, @RequestParam String zipCode){
        if(model.asMap().get("username")==null||model.asMap().get("password")==null){
            return"redirect:/index";
        }
        String user=model.asMap().get("username").toString();
        String pass=model.asMap().get("password").toString();

        RestTemplate rTemp = new RestTemplate();
        //GET "https://rest.avatax.com/api/v2/taxrates/bypostalcode?country=USA&postalCode=98387"
        // -H "accept: application/json" -H
        // "Authorization: Basic Y29oZW5hbXl2QGdtYWlsLmNvbTpCaWdyZWRmaXNoNzIh" -H
        try{
            AvaTaxClient client = new AvaTaxClient("Test","1.0","localhost",AvaTaxEnvironment.Production).withSecurity(user,pass);
            URL requestURL = new URL("https://rest.avataxcom/api/v2/taxrates/bypostalcode?country="+country+"&postalCode="+zipCode);

            String authorized = Base64.getEncoder().encodeToString((user+":"+pass).getBytes());

            HttpClient client2 = HttpClients.custom().build();
            HttpUriRequest request2 = RequestBuilder.get()
                    .setUri("https://rest.avatax.com/api/v2/taxratesbyzipcode/download/2018-09-09")
                    .setHeader(HttpHeaders.ACCEPT, "application/json")
                    .setHeader(HttpHeaders.AUTHORIZATION, ("Basic " + Base64.getEncoder().encodeToString((user + ":" + pass).getBytes())))
                    .build();
            HttpResponse yadayada = client2.execute(request2);
            System.out.println(client2);
            System.out.println(request2);

        }catch(Exception e){
            System.out.println("Bad");
        }
        return "HEK HEK HEK";
    }
}
