package POJO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Transaction {
    public String type;
    public String companyCode;
    public String date;
    public String customerCode;
    public String purchaseOrderNo;
    public Boolean commit;
    public String currencyCode;

    public ArrayList<Map<String,Object>> lines;

    Transaction (
            String type,
            String companyCode,
            String date,
            String customerCode,
            String purchaseOrderNo,
            Boolean commit,
            String currencyCode,
            String number,
            int quantity,
            int amount,
            String taxCode,
            String itemCode,
            String description){
        this.type = type;
        this.companyCode = companyCode;
        this.date=date;
        this.customerCode=customerCode;
        this.purchaseOrderNo=purchaseOrderNo;
        this.commit=commit;
        this.currencyCode=currencyCode;

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("number", number);
        map.put("quantity", quantity);
        map.put("amount", amount);
        map.put("taxCode", taxCode);
        map.put("itemCode", itemCode);
        map.put("description", description);
        this.lines= new ArrayList<Map<String,Object>>();
        this.lines.add(map);
    }
}