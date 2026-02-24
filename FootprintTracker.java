import java.util.ArrayList;

/**
 * The FootprintTracker class is responsible for managing and tracking the carbon footprint entries for different users. It allows adding new emission entries, calculating total emissions for all users or specific users, and generating a daily report that groups emissions by user and provides subtotals and a grand total of emissions.
 */


public class FootprintTracker {
    private String trackerName;
    private ArrayList<EmissionSource> emissions;

    /**
    * constructs a new FootprintTracker with the specified tracker name and initializes an empty list of emissions.
    * @param trackerName the name of the footprint tracker, which can be used to identify the specific tracking instance (e.g., "RIT GreenPrint 2026").
    * @param emissions the list of emission entries that will be tracked by this FootprintTracker instance. This list is initialized as an empty ArrayList and will store instances of EmissionSource or its subclasses (e.g., EnergyEmission, FoodEmission, TransportationEmission) as they are added to the tracker.
     */

    public FootprintTracker(String trackerName) {
        this.trackerName = "RIT GreenPrint 2026";
        this.emissions = new ArrayList<>();
    }

    /**
     * Adds a new emission entry to the list of emissions being tracked. The entry must be an instance of EmissionSource or its subclasses, which contain the necessary information to calculate emissions (e.g., sourceID, category, date, userName) and implement the calculateEmission() method to compute the specific emissions based on their attributes.
     * @param entry
     */
    public void addEntry(EmissionSource entry) {
        try {
        emissions.add(entry);
        } catch (Exception e) {
            System.err.println("An Error Occurred while adding an entry: " + e.getMessage());
        }
    }

    /**
     * Calculates the total emissions by iterating through all the entries in the emissions list and summing up the calculated emissions for each entry using the calculateEmission() method defined in the EmissionSource class and implemented by its subclasses. This method returns the total emissions in kg CO2 for all entries currently tracked by this FootprintTracker instance.
     * @return the total emissions in kg CO2 for all entries currently tracked by this FootprintTracker instance, calculated by summing the emissions from each entry in the emissions list using their respective calculateEmission() methods.
     */
    public double GetTotalEmissions() {
        try {
            double total = 0.0;
            for (EmissionSource entry : emissions) {
                total += entry.calculateEmission();
            }
            return total;
        } catch (Exception e) {
            System.err.println("An Error Occurred while calculating total emissions: " + e.getMessage());
            return 0.0; // Return 0.0 in case of an error to indicate that the total emissions could not be calculated.
        }
    }

    /**
     * Calculates the total emissions for a specific user by iterating through all the entries in the emissions list, checking if the userName of each entry matches the specified userName, and summing up the calculated emissions for those entries that belong to the specified user. This method returns the total emissions in kg CO2 for the specified user based on their tracked entries.
     * @param userName
     * @return the total emissions in kg CO2 for the specified user.
     */
    public double GetTotalEmissionsForUser(String userName) {
        try {

        double total = 0.0;
        for (EmissionSource entry : emissions) {
            if (entry.getUserName().equals(userName)) {
                total += entry.calculateEmission();
            }
        }
        return total;

        } catch (Exception e) {
            System.err.println("An Error Occurred while calculating total emissions for user: " + e.getMessage());
            return 0.0; // Return 0.0 in case of an error to indicate that the total emissions could not be calculated for the specified user.
        }
    }

    /**
     * Generates a daily report that groups emissions by user, prints each entry for each user, calculates and prints the subtotal of emissions for each user, and finally calculates and prints the grand total of emissions for all users. The report is formatted to display the details of each emission entry (e.g., sourceID, category, date) along with the calculated emissions in kg CO2, and it provides a clear summary of the emissions for each user as well as the overall total.
     * prints a formatted daily report that groups emissions by user, includes subtotals for each user, and a grand total for all users, displaying the details of each emission entry and the calculated emissions in kg CO2.
     */
    public void generateDailyReport() {

        try{

        System.out.println("=== " + trackerName + " Daily Report ===\n");
        
        // Get unique users
        ArrayList<String> users = new ArrayList<>();
        for (EmissionSource entry : emissions) {
            String userName = entry.getUserName();
            if (!users.contains(userName)) {
                users.add(userName);
            }
        }
        
        double grandTotal = 0.0;
        
        // Print emissions grouped by user
        for (String user : users) {
            System.out.println("\nUser: " + user);
            double userTotal = 0.0;
            
            // Print all entries for this user
            for (EmissionSource entry : emissions) {
                if (entry.getUserName().equals(user)) {
                    System.out.println(entry.toString());
                    userTotal += entry.calculateEmission();
                }
            }
            
            // Print user subtotal
            System.out.printf("Subtotal: %.2f kg CO2\n", userTotal);
            grandTotal += userTotal;
        }
        
        // Print grand total
        System.out.printf("\n\nGrand Total: %.2f kg CO2", grandTotal);

    } catch (Exception e) {
        System.err.println("An Error Occurred while generating the report: " + e.getMessage());
    }

}

}
