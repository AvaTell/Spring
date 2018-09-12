package POJO;

public class TaxRateByPostalCode {
    public double totalRate;
    public TaxCodeSummary[] summary;
    public String name;
    public String type;

    public TaxRateByPostalCode(double totalRate, TaxCodeSummary[] summary, String name, String type){
        this.totalRate=totalRate;
        this.summary=summary;
        this.name=name;
        this.type=type;
    }
}
