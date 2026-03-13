import java.time.LocalDateTime;

public class Offsets {

    
private double offset_rate_per_kg;
private String date;    


public Offsets(){

    this.offset_rate_per_kg = 0.75;
    

}

public double getOffsetRatePerKg() {
    return offset_rate_per_kg;
}

public Double calculateOffsetCost(double EmissionsKg) {
    return EmissionsKg * offset_rate_per_kg;
}

public String getOffsetReceipt(double EmissionsKg, String paymentMethod) {
    
    return String.format(

        "<- RECEIPT ->\n" +
            "Date %s\n" +
            "Time %s\n" +
            "Weigh offset: %.2f kg CO2\n" +
            "Amount Offset per Kg CO2: %.2f AED\n" +
            "Total Cost: $%.2f AED\n" +
            "Payment Method: %s\n" +
            "Status: Confirmation - Transaction Successful!" +
            "\nThank you for your contribution to a greener planet!" +
            "<- RECEIPT END ->\n",

            java.time.LocalDate.now(), java.time.LocalTime.now(), EmissionsKg, offset_rate_per_kg, calculateOffsetCost(EmissionsKg), paymentMethod
    );
}

}