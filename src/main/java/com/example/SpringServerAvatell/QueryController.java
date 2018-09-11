package com.example.SpringServerAvatell;

import com.google.gson.Gson;
import net.avalara.avatax.rest.client.AvaTaxClient;
import net.avalara.avatax.rest.client.enums.AvaTaxEnvironment;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Base64;

@Controller
@SessionAttributes({"username","password"})
@ResponseBody()
public class QueryController {
    @GetMapping("/query/byzipcode")
    public String queryByZipCode(HttpServletRequest request, Model model, @RequestParam String country, @RequestParam String zipCode){
        String result = null;

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
//            URL requestURL = new URL("https://rest.avataxcom/api/v2/taxrates/bypostalcode?country="+country+"&postalCode="+zipCode);
//
//            String authorized = Base64.getEncoder().encodeToString((user+":"+pass).getBytes());
//
//            HttpClient client2 = HttpClients.custom().build();
//            HttpUriRequest request2 = RequestBuilder.get()
//                    .setUri("https://rest.avatax.com/api/v2/taxratesbyzipcode/download/2018-09-09")
//                    .setHeader(HttpHeaders.ACCEPT, "application/json")
//                    .setHeader(HttpHeaders.AUTHORIZATION, ("Basic " + Base64.getEncoder().encodeToString((user + ":" + pass).getBytes())))
//                    .build();
//            HttpResponse yadayada = client2.execute(request2);
//            System.out.println(client2);
//            System.out.println(request2);
//            System.out.println(yadayada);
//
        }catch(Exception e){
            System.out.println("Bad");
        }

        try {

            String authorized = Base64.getEncoder().encodeToString((user+":"+pass).getBytes());
            String requestURL = "https://rest.avatax.com/api/v2/taxrates/bypostalcode?country="+country+"&postalCode="+zipCode;

/*
RESOURCES: https://hc.apache.org/httpcomponents-client-ga/quickstart.html
 */
            CloseableHttpClient httpclient = HttpClients.createDefault();
//            HttpGet getRequest = new HttpGet(
//                    "https://rest.avatax.com/api/v2/taxratesbyzipcode/download/2018-09-10");
            HttpGet getRequest = new HttpGet(requestURL);
            getRequest.addHeader("accept", "application/json");
            getRequest.addHeader("authorization", "Basic " + authorized);

            HttpResponse response = httpclient.execute(getRequest);

            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatusLine().getStatusCode());
            }

            BufferedReader br = new BufferedReader(
                    new InputStreamReader((response.getEntity().getContent())));

            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                if (output.contains(zipCode))
                System.out.println(output);
                result = output;
            }

            return result;
        } catch (ClientProtocolException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();
        }


        return result;
    }


    @PostMapping("/query/bytaxcode")
    public String queryByTaxCode(HttpServletRequest request, Model model, @RequestParam String taxcode, @RequestParam String description, @RequestParam String taxzipcode){
        String result = null;

        if(model.asMap().get("username")==null||model.asMap().get("password")==null){
            return"redirect:/index";
        }
        String user=model.asMap().get("username").toString();
        String pass=model.asMap().get("password").toString();

        RestTemplate rTemp = new RestTemplate();

        try {

            String authorized = Base64.getEncoder().encodeToString((user+":"+pass).getBytes());
            String requestURL = "https://rest.avatax.com/api/v2/transactions/create";

/*
RESOURCES: https://hc.apache.org/httpcomponents-client-ga/quickstart.html
 */
            CloseableHttpClient httpclient = HttpClients.createDefault();
//            HttpGet getRequest = new HttpGet(
//                    "https://rest.avatax.com/api/v2/taxratesbyzipcode/download/2018-09-10");
            HttpPost postRequest = new HttpPost(requestURL);
            postRequest.addHeader("accept", "application/json");
            postRequest.addHeader("authorization", "Basic " + authorized);
            postRequest.addHeader("-d ", "\"{ \\\"lines\\\": [ { \\\"taxCode\\\": \\\"PS081282\\\", \\\"description\\\": \\\"Yarn\\\" } ], \\\"type\\\": \\\"SalesOrder\\\", \\\"companyCode\\\": \\\"DEFAULT\\\", \\\"date\\\": \\\"2018-09-11\\\", \\\"customerCode\\\": \\\"ABC\\\", \\\"purchaseOrderNo\\\": \\\"2018-09-11-001\\\", \\\"addresses\\\": { \\\"singleLocation\\\": { \\\"line1\\\": \\\"110 180th Street Court East\\\", \\\"city\\\": \\\"Spanaway\\\", \\\"region\\\": \\\"WA\\\", \\\"country\\\": \\\"US\\\", \\\"postalCode\\\": \\\"98387\\\" } }, \\\"commit\\\": false, \\\"currencyCode\\\": \\\"USD\\\",}");

            HttpResponse response = httpclient.execute(postRequest);

            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatusLine().getStatusCode());
            }

            BufferedReader br = new BufferedReader(
                    new InputStreamReader((response.getEntity().getContent())));

            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                if (output.contains(taxzipcode))
                    System.out.println(output);
                result = output;
            }

            return result;
        } catch (ClientProtocolException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();
        }


        return result;
    }
}
