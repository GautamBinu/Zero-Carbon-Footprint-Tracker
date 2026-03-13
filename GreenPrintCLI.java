public class GreenPrintCLI {

    /**
     * The main method serves as the entry point for the GreenPrintCLI application. It creates an instance of the FootprintTracker class.
     * @param args
     */

    public static void main(String[] args) {

        /**
         * The main method serves as the entry point for the GreenPrintCLI application. It creates an instance of the FootprintTracker class, adds sample emission entries for different users (Alice, Bob, Charlie) across various categories (Energy, Food, Transportation), 
         * and demonstrates the functionality of calculating total emissions for a specific user and for all users. Finally, it generates a daily report that summarizes the emissions by user and provides subtotals and a grand total of emissions in kg CO2. 
         * This method effectively showcases how the FootprintTracker can be used to manage and track carbon footprint data for multiple users and categories.
         */

        try {
            // Create an instance of FootprintTracker

        FootprintTracker tracker = new FootprintTracker("RIT GreenPrint 2026");

        // Sample data entries
        tracker.addEntry(new EnergyEmission("E001", "Energy", "2024-06-01", "Alice", 15.0, "Grid"));
        tracker.addEntry(new FoodEmission("F002", "Food", "2024-06-01", "Alice", "Vegetarian", 2));
        tracker.addEntry(new FoodEmission("F001", "Food", "2024-06-01", "Bob", "Vegan", 3));
        tracker.addEntry(new TransportationEmission("T002", "Transportation", "2024-06-01", "Bob", 15.0, "Bus"));
        tracker.addEntry(new TransportationEmission("T001", "Transportation", "2024-06-01", "Charlie", 10.0, "Car"));
        tracker.addEntry(new EnergyEmission("E002", "Energy", "2024-06-01", "Charlie", 25.0, "solar"));

        System.out.printf("Total Emissions for Alice: %.2f kg CO2\n\n", tracker.GetTotalEmissionsForUser("Alice"));

        System.out.printf("Total Emissions: %.2f kg CO2\n\n", tracker.GetTotalEmissions());

        // Generate the daily report
        tracker.generateDailyReport();

        } catch (Exception e) {
            System.err.println("An Error Occurred: " + e.getMessage());
        }
    }    
}
