import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Offsets {
    static double offset_rate_per_kg = 1.015;
    String date;

  

    /**
     * Returns the offset rate per kilogram of CO2.
     * @return the offset rate per kilogram
     */

    public static double getOffsetRatePerKg() {
        return offset_rate_per_kg;
    }

    /**
     * Calculates the total cost of offsetting a given amount of emissions in kilograms of CO2 by multiplying the emissions with the offset rate per kilogram. This method provides a way to determine the financial cost associated with offsetting a specific amount of carbon emissions, which can be useful for users looking to understand the impact of their actions and make informed decisions about offsetting their carbon footprint.
     * @param EmissionsKg
     * @return the total cost of offsetting the given amount of emissions in kilograms of CO2
     */

    public static Double calculateOffsetCost(double EmissionsKg) {
        return EmissionsKg * offset_rate_per_kg;
    }

   /**
    * Generates a receipt for an offset transaction, including details such as the date, time, user name, emission type, weight of emissions offset, amount offset per kilogram of CO2, total cost of the offset, payment method, and a confirmation status. The receipt is formatted as a string that can be displayed to the user after completing an offset transaction, providing a clear summary of the transaction details and confirming that the transaction was successful.
    * @param EmissionsKg
    * @param paymentMethod
    * @param userName
    * @return a formatted receipt string containing the details of the offset transaction
    */

    public static String getOffsetReceipt(double EmissionsKg, String paymentMethod, String userName, boolean isDiscounted, double finalCost, double savings) {

        String totalCostLine;
        if (isDiscounted) {
            totalCostLine = String.format("Total Cost: %.2f AED (Discounted, saved %.2f AED)", finalCost, savings);
        } else {
            totalCostLine = String.format("Total Cost: %.2f AED", finalCost);
        }

        return String.format(

            "<- RECEIPT ->\n" +
                "Date %s\n" +
                "Time %s\n" +
                "User: %s\n" +
                "Emission Type: %s\n" +
                "Weigh offset: %.2f kg CO2\n" +
                "Amount Offset per Kg CO2: %.3f AED\n" +
                "%s\n" +
                "Payment Method: %s\n" +
                "Status: Confirmation - Transaction Successful!" +
                "\nThank you for your contribution to a greener planet!\n" +
                "<- RECEIPT END ->",

                java.time.LocalDate.now(), LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")), userName, "All Emission Types", EmissionsKg, offset_rate_per_kg, totalCostLine, paymentMethod
        );
    }

}