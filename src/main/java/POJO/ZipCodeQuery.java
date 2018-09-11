package POJO;

public class ZipCodeQuery {
    public String country;
    public String zipCode;

    public ZipCodeQuery(String country, String zipCode){
        this.country = country;
        this.zipCode = zipCode;
    }

    public String getCountry(){
        return this.country;
    }

    public String getZipCode(){
        return this.zipCode;
    }

    public void setCountry(){
        this.country = country;
    }

    public void setZipCode(){
        this.zipCode = zipCode;
    }

}
