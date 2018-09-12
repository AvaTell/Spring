package com.example.SpringServerAvatell;

import POJO.Transaction;
import POJO.ZipCodeQuery;
import POJO.TaxRateByPostalCode;
import POJO.TaxRate;
import com.google.gson.Gson;
import jdk.nashorn.internal.parser.JSONParser;
import jdk.nashorn.internal.runtime.regexp.joni.constants.StringType;
import net.avalara.avatax.rest.client.AvaTaxClient;
import net.avalara.avatax.rest.client.enums.AvaTaxEnvironment;
import net.avalara.avatax.rest.client.enums.DocumentType;
import net.avalara.avatax.rest.client.models.AddressLocationInfo;
import net.avalara.avatax.rest.client.models.AddressesModel;
import net.avalara.avatax.rest.client.models.CreateTransactionModel;
import net.avalara.avatax.rest.client.models.LineItemModel;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.google.gson.*;
import sun.jvm.hotspot.debugger.Address;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sound.sampled.Line;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.*;
import java.util.stream.Stream;

@Controller
@SessionAttributes({"username","password"})
public class QueryController {

    @GetMapping("/query/byzipcode")
    public String queryByZipCode(HttpServletRequest request, Model model, @RequestParam String country, @RequestParam String zipCode){

        ZipCodeQuery zipCodeQuery = new ZipCodeQuery(country, zipCode);
        String result = null;

        HttpSession sesh = request.getSession();

        if(sesh.getAttribute("username")==null||sesh.getAttribute("password")==null){

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

            model.addAttribute("rateInfo",rate);

            model.addAttribute("totalTax",rate.totalRate);

            return "result";
        } catch (ClientProtocolException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();
        }


        return "result";
    }


    @PostMapping("/query/bytaxcode")
    @ResponseBody
    public String queryByTaxCode(HttpServletRequest request, Model model, @RequestParam String taxcode, @RequestParam String description, @RequestParam String taxzipcode){
        String result = null;

        HttpSession sesh = request.getSession();

        if(sesh.getAttribute("username")==null||sesh.getAttribute("password")==null){
            return"redirect:/index";
        }
        String user=model.asMap().get("username").toString();
        String pass=model.asMap().get("password").toString();

        RestTemplate rTemp = new RestTemplate();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");

        //Transaction sendVal = new Transaction(type,companyCode,dtf.format(LocalDateTime.now()), "ABC",,false,"USD",1,1,1,taxcode,,description);

        CreateTransactionModel ctm = new CreateTransactionModel();

        Date date = new Date();

        LineItemModel line = new LineItemModel();

        line.setTaxCode(taxcode);
        line.setDescription(description);

        ArrayList<LineItemModel> lines = new ArrayList<>();
        lines.add(line);

        AddressesModel adModel = new AddressesModel();
        AddressLocationInfo adInfo = new AddressLocationInfo();
        adInfo.setPostalCode(taxzipcode);

        adModel.setSingleLocation(adInfo);

        ctm.setDate(date);
        ctm.setCompanyCode("DEFAULT");
        ctm.setCustomerCode("ABC");
        ctm.setType(DocumentType.SalesOrder);
        ctm.setCurrencyCode("USD");
        ctm.setCommit(false);
        ctm.setAddresses(adModel);
        ctm.setLines(lines);

        Gson gson = new Gson();

        try {

            String authorized = Base64.getEncoder().encodeToString((user+":"+pass).getBytes());
            String requestURL = "https://rest.avatax.com/api/v2/transactions/create";

/*
RESOURCES FOR HTTPPOST: https://hc.apache.org/httpcomponents-client-ga/quickstart.html

RESOURCES FOR STRING ENTITY: https://stackoverflow.com/questions/12059278/how-to-post-json-request-using-apache-httpclient
 */

            StringEntity requestEntity = new StringEntity(
                    gson.toJson(ctm),
                    ContentType.APPLICATION_JSON);

            CloseableHttpClient httpclient = HttpClients.createDefault();
//            HttpGet getRequest = new HttpGet(
//                    "https://rest.avatax.com/api/v2/taxratesbyzipcode/download/2018-09-10");
            HttpPost postRequest = new HttpPost(requestURL);
            postRequest.addHeader("accept", "application/json");
            postRequest.addHeader("authorization", "Basic " + authorized);
//            postRequest.addHeader("-d ", gson.toJson(ctm));
            postRequest.setEntity(requestEntity);

            HttpResponse response = httpclient.execute(postRequest);

            if (response.getStatusLine().getStatusCode() != 201 && (response.getStatusLine().getStatusCode() != 200)) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatusLine().getStatusCode());
            }

            BufferedReader br = new BufferedReader(
                    new InputStreamReader((response.getEntity().getContent())));

            CreateTransactionModel model3 = new CreateTransactionModel();

            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
//                if (output.contains(taxzipcode))
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
