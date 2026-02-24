public class GreenPrintCLI {

    public static void main(String[] args) {
        FootprintTracker tracker = new FootprintTracker("RIT GreenPrint 2026");

        // Sample data entries
        tracker.addEntry(new EnergyEmission("E001", "Energy", "2024-06-01", "Alice", 15.0, "Grid"));
        tracker.addEntry(new FoodEmission("F002", "Food", "2024-06-01", "Alice", "Vegetarian", 2));
        tracker.addEntry(new FoodEmission("F001", "Food", "2024-06-01", "Bob", "Vegan", 3));
        tracker.addEntry(new TransportationEmission("T002", "Transportation", "2024-06-01", "Bob", 15.0, "Bus"));
        tracker.addEntry(new TransportationEmission("T001", "Transportation", "2024-06-01", "Charlie", 10.0, "Car"));
        tracker.addEntry(new EnergyEmission("E002", "Energy", "2024-06-01", "Charlie", 25.0, "solar"));

        System.out.println("Total Emissions for Alice: " + tracker.GetTotalEmissionsForUser("Alice") + " kg CO2");

        System.out.println("Total Emissions: " + tracker.GetTotalEmissions() + " kg CO2");

        // Generate the daily report
        tracker.generateDailyReport();
    }    
}
