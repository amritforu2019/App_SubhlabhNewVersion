package com.vastu.shubhlabhvastu.Task;

public class Fun {

    public String convertNumber_bystring(String num_string) {
        try {
           /* Format format = com.ibm.icu.text.NumberFormat.getCurrencyInstance(new Locale("en", "in"));
            return format.format(new BigDecimal(num_string)) ;*/
            return "0.00";
             
        } catch (Exception es) {
            return "0.00";
        }
    }



}
