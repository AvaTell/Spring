package POJO;

public class TaxRateByTaxCode {
    public double totalRate;
    public TaxCodeSummary[] summary;
    public String name;
    public String type;

    public TaxRateByTaxCode(double totalRate, TaxCodeSummary[] summary, String name, String type){
        this.totalRate=totalRate;
        this.summary=summary;
        this.name=name;
        this.type=type;
    }
}

/*
Object we get back from PC0401000:
 {"id":0,
"code":"1c04cc48-2383-476e-b440-4f8212e807e6",
"companyId":0,
"date":"2018-09-12",
"paymentDate":"2018-09-12",
"status":"Temporary",
"type":"SalesOrder",
"currencyCode":"USD",
"customerVendorCode":"ABC",
"customerCode":"ABC",
"reconciled":false,
"totalAmount":0.0,
"totalExempt":0.0,
"totalDiscount":0.0,
"totalTax":0.0,
"totalTaxable":0.0,
"totalTaxCalculated":0.0,
"adjustmentReason":"NotAdjusted",
"locked":false,
"version":1,
"exchangeRateEffectiveDate":"2018-09-12",
"exchangeRate":1.0,
"modifiedDate":"2018-09-12T22:23:39.0800636Z",
"modifiedUserId":214135,
"taxDate":"2018-09-12T00:00:00",
 "lines":[{
    "id":0,"transactionId":0,
    "lineNumber":"1",
    "description":"clothing",
    "discountAmount":0.0,
    "exemptAmount":0.0,
    "exemptCertId":0,
    "isItemTaxable":true,
    "lineAmount":0.0,
    "quantity":1.0,
    "reportingDate":"2018-09-12",
    "tax":0.0,
    "taxableAmount":0.0,
    "taxCalculated":0.0,
    "taxCode":"P0000000",
    "taxCodeId":4316,
    "taxDate":"2018-09-12",
    "taxIncluded":false,
 "details":[{
    "id":0,
    "transactionLineId":0,
    "transactionId":0,
    "country":"US",
    "region":"WA",
    "exemptAmount":0.0,
    "jurisCode":"53",
    "jurisName":"WASHINGTON",
    "stateAssignedNo":"",
    "jurisType":"STA",
    "jurisdictionType":"State",
    "nonTaxableAmount":0.0,"rate":0.065000,
    "tax":0.0,
    "taxableAmount":0.0,
    "taxType":"Sales",
    "taxName":"WA STATE TAX",
    "taxAuthorityTypeId":45,
    "taxCalculated":0.0,
    "rateType":"General",
    "rateTypeCode":"G",
    "isNonPassThru":false},
    {"id":0,
    "transactionLineId":0,
    "transactionId":0,
    "country":"US",
    "region":"WA",
    "exemptAmount":0.0,
    "jurisCode":"033",
    "jurisName":"KING",
    "stateAssignedNo":"1700",
    "jurisType":"CTY",
    "jurisdictionType":"County",
    "nonTaxableAmount":0.0,
    "rate":0.000000,
    "tax":0.0,
    "taxableAmount":0.0,
    "taxType":"Sales",
    "taxName":"WA COUNTY TAX",
    "taxAuthorityTypeId":45,
    "taxCalculated":0.0,
    "rateType":"General",
    "rateTypeCode":"G",
    "isNonPassThru":false},
    {"id":0,
    "transactionLineId":0,
    "transactionId":0,
    "country":"US",
    "region":"WA",
    "exemptAmount":0.0,
    "jurisCode":"63000",
    "jurisName":"SEATTLE",
    "stateAssignedNo":"1726",
    "jurisType":"CIT",
    "jurisdictionType":"City",
    "nonTaxableAmount":0.0,
    "rate":0.036000,
    "tax":0.0,
    "taxableAmount":0.0,
    "taxType":"Sales",
    "taxName":"WA CITY TAX",
    "taxAuthorityTypeId":45,
    "taxCalculated":0.0,
    "rateType":"General",
    "rateTypeCode":"G",
    "isNonPassThru":false}],
    "nonPassthroughDetails":[],
    "hsCode":"",
    "costInsuranceFreight":0.0,
    "vatCode":"",
    "vatNumberTypeId":0}],
     "addresses":[{
        "id":0,
        "transactionId":0,
        "boundaryLevel":"Zip5",
        "line1":"",
        "line2":"",
        "line3":"",
        "city":"",
        "region":"WA",
        "postalCode":"98121",
        "country":"US",
        "taxRegionId":2109700,
        "latitude":"47.615501",
        "longitude":"-122.345082"}],
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