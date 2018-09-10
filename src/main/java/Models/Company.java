package Models;
public class Company {
    int id;
    int accountId;
    int parentCompanyId;
    String sstPid;
    String companyCode;
    String name;
    boolean isDefault;
    int defaultLocationId;
    Boolean isActive;
    int taxpayerIdNumber;
    Boolean hasProfile;
    boolean isReportingEntity;
    String defaultCountry;
    String defaultCurrencyCode;
    String roundingLevelId;
    Boolean isTest;
    String taxDependencyLevelId;
    Company[] contacts;
    //Item[] items;
    //Location[] locations;
    //Nexus[] nexus;
    //Setting[] settings;
    //TaxCode[] taxCodes;
    //upc[]
}
