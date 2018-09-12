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
