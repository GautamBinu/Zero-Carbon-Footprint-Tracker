/**
 * Data class representing the result of a discount request from the server.
 * Contains the original emission value, discount percentage, and discounted emission value.
 */
public class DiscountResult {
    private final double originalEmissionValue;
    private final int discountPercentage;
    private final double discountedEmissionValue;

    /**
     * Creates a new DiscountResult with the given values.
     * @param originalEmissionValue the original CO2 emission amount in kg
     * @param discountPercentage the discount percentage (1-30)
     * @param discountedEmissionValue the discounted CO2 emission amount in kg
     */
    public DiscountResult(double originalEmissionValue, int discountPercentage, double discountedEmissionValue) {
        this.originalEmissionValue = originalEmissionValue;
        this.discountPercentage = discountPercentage;
        this.discountedEmissionValue = discountedEmissionValue;
    }

    public double getOriginalEmissionValue() {
        return originalEmissionValue;
    }

    public int getDiscountPercentage() {
        return discountPercentage;
    }

    public double getDiscountedEmissionValue() {
        return discountedEmissionValue;
    }

    /**
     * Formats a user-friendly message describing the discount applied.
     * @return formatted message string
     */
    public String getFormattedMessage() {
        return String.format(
            "Server reward applied: %d%% discount! (%.2f AED) You are offsetting %.2f kg CO₂e for the impact of only %.2f kg CO₂e.",
            
            discountPercentage,
            calculateSavings(),
            originalEmissionValue,
            discountedEmissionValue
        );
    }

    /**
     * Calculates the monetary savings based on the offset rate.
     * @return savings amount in currency units
     */
    public double calculateSavings() {
        double savings = (originalEmissionValue - discountedEmissionValue) * Offsets.getOffsetRatePerKg();
        return Math.round(savings * 100.0) / 100.0; // Round to 2 decimals
    }
}
