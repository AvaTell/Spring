package POJO;

public class TaxRateByPostalCode {
    public double totalRate;
    public TaxRate[] rates;
    public String name;
    public String type;

    public TaxRateByPostalCode(double totalRate, TaxRate[] rates, String name, String type){
        this.totalRate=totalRate;
        this.rates=rates;
        this.name=name;
        this.type=type;
    }
}
