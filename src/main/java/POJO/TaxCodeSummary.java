package POJO;

public class TaxCodeSummary {
    public String taxType;
    public String taxName;
    public String rateType;
    public String taxable;
    public double rate;
    public double tax;
    public double taxCalculated;
    public double nonTaxable;
    public double exemption;

}

/*
"summary":[{
    "country":"US",
    "region":"WA",
    "jurisType":"State",
    "jurisCode":"53",
    "jurisName":"WASHINGTON",
    "taxAuthorityType":45,
    "stateAssignedNo":"",
    "taxType":"Sales",
    "taxName":"WA STATE TAX",
    "rateType":"General",
    "taxable":0.0,
    "rate":0.065000,
    "tax":0.0,
    "taxCalculated":0.0,
    "nonTaxable":0.0,
    "exemption":0.0},

    {"country":"US",
    "region":"WA",
    "jurisType":"County",
    "jurisCode":"053",
    "jurisName":"PIERCE",
    "taxAuthorityType":45,
    "stateAssignedNo":"2700",
    "taxType":"Sales",
    "taxName":"WA COUNTY TAX",
    "rateType":"General",
    "taxable":0.0,
    "rate":0.000000,
    "tax":0.0,
    "taxCalculated":0.0,
    "nonTaxable":0.0,
    "exemption":0.0},

    {"country":"US",
    "region":"WA",
    "jurisType":"Special",
    "jurisCode":"BWDN",
    "jurisName":"PIERCE CO TR",
    "taxAuthorityType":45,
    "stateAssignedNo":"2700",
    "taxType":"Sales",
    "taxName":"WA SPECIAL TAX",
    "rateType":"General",
    "taxable":0.0,
    "rate":0.028000,
    "tax":0.0,
    "taxCalculated":0.0,
    "nonTaxable":0.0,
    "exemption":0.0}]}
 */