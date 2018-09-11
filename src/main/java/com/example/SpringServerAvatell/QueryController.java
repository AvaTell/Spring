package com.example.SpringServerAvatell;

import POJO.ZipCodeQuery;
import POJO.TaxRateByPostalCode;
import POJO.TaxRate;
import com.google.gson.Gson;
import jdk.nashorn.internal.parser.JSONParser;
import jdk.nashorn.internal.runtime.regexp.joni.constants.StringType;
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
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.Arrays;
import java.util.Base64;
import java.util.stream.Stream;

@Controller
@SessionAttributes({"username","password"})
@ResponseBody()
public class QueryController {

    @GetMapping("/query/byzipcode")
    public String queryByZipCode(HttpServletRequest request, Model model, @RequestParam String country, @RequestParam String zipCode){

        ZipCodeQuery zipCodeQuery = new ZipCodeQuery(country, zipCode);
        String result = null;


        HttpSession sesh = request.getSession();

        if(sesh.getAttribute("username")==null||sesh.getAttribute("password")==null||sesh.getAttribute("isLoggedIn")==null){

            return"redirect:/index";
        }
        String user=model.asMap().get("username").toString();
        String pass=model.asMap().get("password").toString();

//        RestTemplate rTemp = new RestTemplate();

        try {

            String authorized = Base64.getEncoder().encodeToString((user+":"+pass).getBytes());
            String requestURL = "https://rest.avatax.com/api/v2/taxrates/bypostalcode?country="+country+"&postalCode="+zipCode;

            /*
            RESOURCES: https://hc.apache.org/httpcomponents-client-ga/quickstart.html
            NOTE: Where I got the info for using the HttpClient
             */

            CloseableHttpClient httpclient = HttpClients.createDefault();
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

            String output = null;
            System.out.println("Output from Server .... \n");

            /*
            Example Data Coming Back:
            {
            "totalRate":0.100000,"rates":
            [{
            "rate":0.065000,
            "name":"WA STATE TAX",
            "type":"State"
            },
            {
            "rate":0.000000,
            "name":"WA COUNTY TAX",
            "type":"County"
            },
            {
            "rate":0.035000,
            "name":"WA CITY TAX",
            "type":"City"
            }]}
             */


            while ((output = br.readLine()) != null) {
                String[] outputArray = output.split("[^a-zA-Z0-9.\\s]");
                System.out.println("Output Array = " + Arrays.deepToString(outputArray));



                if (output.contains(zipCodeQuery.zipCode))
                    System.out.println("Output only = " + output);
                    result = output;
            }
            Gson gson = new Gson();

            TaxRateByPostalCode rate = gson.fromJson(result, TaxRateByPostalCode.class);

            System.out.println(rate);

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
