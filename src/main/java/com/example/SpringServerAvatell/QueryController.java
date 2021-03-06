package com.example.SpringServerAvatell;

import POJO.TaxCodeSummary;
import POJO.TaxRateByTaxCode;
import POJO.ZipCodeQuery;
import POJO.TaxRateByPostalCode;
import com.google.gson.Gson;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.google.gson.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

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

            BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));

            String output = null;
            while ((output = br.readLine()) != null) {
                if (output.contains(zipCodeQuery.zipCode)) {
                    System.out.println("Output only = " + output);
                    result = output;
                }
            }

            Gson gson = new Gson();
            TaxRateByPostalCode rate = gson.fromJson(result, TaxRateByPostalCode.class);

            model.addAttribute("rateInfo",rate);
            model.addAttribute("totalTax",rate.totalRate);

            return "result-from-zipcode";

        } catch (ClientProtocolException e) {

            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "result-from-zipcode";
    }


    @PostMapping("/query/bytaxcode")
    public String queryByTaxCode(HttpServletRequest request,
                                Model model,
                                 @RequestParam String companyCode,
                                 @RequestParam String customerCode,
                                 @RequestParam BigDecimal amount,
                                 @RequestParam BigDecimal quantity,
                                 @RequestParam String taxcode,
                                 @RequestParam String description,
                                 @RequestParam String taxzipcode)
                                {
        String result = null;

        HttpSession sesh = request.getSession();

        if(sesh.getAttribute("username")==null||sesh.getAttribute("password")==null){
            return"redirect:/index";
        }


        String user=model.asMap().get("username").toString();
        String pass=model.asMap().get("password").toString();

        RestTemplate rTemp = new RestTemplate();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        CreateTransactionModel ctm = new CreateTransactionModel();
        Date date = new Date();

        LineItemModel line = new LineItemModel();
        line.setTaxCode(taxcode);
        line.setDescription(description);
        line.setAmount(amount);
        line.setQuantity(quantity);

        ArrayList<LineItemModel> lines = new ArrayList<>();
        lines.add(line);

        AddressesModel adModel = new AddressesModel();
        AddressLocationInfo adInfo = new AddressLocationInfo();
        adInfo.setPostalCode(taxzipcode);

        adModel.setSingleLocation(adInfo);

        ctm.setDate(date);
        ctm.setCompanyCode(companyCode);
        ctm.setCustomerCode(customerCode);
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
            HttpPost postRequest = new HttpPost(requestURL);
            postRequest.addHeader("accept", "application/json");
            postRequest.addHeader("authorization", "Basic " + authorized);
            postRequest.setEntity(requestEntity);

            HttpResponse response = httpclient.execute(postRequest);

            if (response.getStatusLine().getStatusCode() != 201 && (response.getStatusLine().getStatusCode() != 200)) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatusLine().getStatusCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));

            try {
                String output = br.readLine();
                result = output;
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            Gson gson1 = new Gson();
            TaxRateByTaxCode summaries = gson1.fromJson(result, TaxRateByTaxCode.class);

            Double totalTax = 0.0;
            for (TaxCodeSummary rate : summaries.summary) {
                totalTax +=rate.rate;
            }

            model.addAttribute("mainObject", summaries);
            model.addAttribute("summaries",summaries.summary);
            model.addAttribute("totalTax", totalTax);

            return "result-from-taxcode";

        } catch (ClientProtocolException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "result-from-taxcode";
    }
}
