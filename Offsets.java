import java.time.LocalDateTime;

public class Offsets {
    static double offset_rate_per_kg = 0.015;
    String date;

    public static double getOffsetRatePerKg() {
        return offset_rate_per_kg;
    }

    public static Double calculateOffsetCost(double EmissionsKg) {
        return EmissionsKg * offset_rate_per_kg;
    }

   

    public static String getOffsetReceipt(double EmissionsKg, String paymentMethod, String userName) {

        return String.format(

            "<- RECEIPT ->\n" +
                "Date %s\n" +
                "Time %s\n" +
                "User: %s\n" +
                "Emission Type: %s\n" +
                "Weigh offset: %.2f kg CO2\n" +
                "Amount Offset per Kg CO2: %f AED\n" +
                "Total Cost: $%.2f AED\n" +
                "Payment Method: %s\n" +
                "Status: Confirmation - Transaction Successful!" +
                "\nThank you for your contribution to a greener planet!\n" +
                "<- RECEIPT END ->\n",

                java.time.LocalDate.now(), java.time.LocalTime.now(), userName, "All Emission Types", EmissionsKg, offset_rate_per_kg, calculateOffsetCost(EmissionsKg), paymentMethod
        );
    }

}