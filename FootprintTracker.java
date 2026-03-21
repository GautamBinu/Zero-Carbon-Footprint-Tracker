
import java.util.ArrayList;
import java.util.List;



/**
 * The FootprintTracker class is responsible for managing and tracking the carbon footprint entries for different users. It allows adding new emission entries, calculating total emissions for all users or specific users, and generating a daily report that groups emissions by user and provides subtotals and a grand total of emissions.
 */


public class FootprintTracker {
    private String trackerName;
    private ArrayList<EmissionSource> emissions;
    Logger log = new Logger();
    
   

    /**
    * constructs a new FootprintTracker with the specified tracker name and initializes an empty list of emissions.
    * @param trackerName the name of the footprint tracker, which can be used to identify the specific tracking instance (e.g., "RIT GreenPrint 2026").
    * @param emissions the list of emission entries that will be tracked by this FootprintTracker instance. This list is initialized as an empty ArrayList and will store instances of EmissionSource or its subclasses (e.g., EnergyEmission, FoodEmission, TransportationEmission) as they are added to the tracker.
     */

    public FootprintTracker() {
        this.trackerName = "RIT GreenPrint 2026";
        this.emissions = new ArrayList<>();
    }

    /**
     * Returns the list of emission entries currently being tracked by this FootprintTracker instance.
     * @return the list of emission entries currently being tracked by this FootprintTracker instance.
     */
    public ArrayList<EmissionSource> getEmissions() {
    return this.emissions;
}

    /**
     * Sets the list of emission entries for this FootprintTracker instance to the provided list. This method allows for updating the emissions being tracked, such as when loading saved data or replacing the current list with a new set of entries.
     * @param loadedEmissions the new list of emission entries that will replace the current list of emissions being tracked by this FootprintTracker instance.
     */
    public void setEmissions(ArrayList<EmissionSource> loadedEmissions) {
    this.emissions = loadedEmissions;
    }

    /**
     * Adds a new emission entry to the list of emissions being tracked. The entry must be an instance of EmissionSource or its subclasses, which contain the necessary information to calculate emissions (e.g., sourceID, category, date, userName) and implement the calculateEmission() method to compute the specific emissions based on their attributes.
     * @param entry
     */
    public void addEntry(EmissionSource entry) {
        try {
            

             emissions.add(entry);
             log.log("ENTRY_ADDED", entry.toString());
        } catch (Exception e) {
            System.err.println("An Error Occurred while adding an entry: " + e.getMessage());
        }
    }

        /**
     *   Returns the total number of emission entries currently being tracked by this FootprintTracker instance, which is determined by the size of the emissions list. This method provides a way to quickly assess how many entries have been added to the tracker without needing to access the list directly.
     * @return the total number of emission entries currently being tracked by this FootprintTracker instance, which is determined by the size of the emissions list.
     */

    public Integer getTotalEntries() {
        return emissions.size();
    }

    /**
     * Returns a list of emission entries that belong to a specific user by iterating through the emissions list and checking if the userName of each entry matches the specified userName. This method allows users to retrieve all their tracked emission entries for review or analysis.
     * @param userName
     * @return a list of emission entries that belong to the specified user, which is determined by matching the userName of each entry in the emissions list with the provided userName parameter. The returned list contains all entries that are associated with the specified user, allowing for easy access and review of their tracked emissions.
     */

    public ArrayList<EmissionSource> getEntriesByUser(String userName) {
        ArrayList<EmissionSource> userEntries = new ArrayList<>();
        for (EmissionSource entry : emissions) {
            if (entry.getUserName().equals(userName)) {
                userEntries.add(entry);
            }
        }
        return userEntries;
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
     * Extracts the source IDs from the emission entries being tracked and returns them as a list of strings. This method iterates through the emissions list, retrieves the sourceID from each entry, checks if it matches the required pattern (EFT-XXX), and adds it to the id_list if it does. The returned list contains all valid source IDs currently being tracked by this FootprintTracker instance, which can be used for validation purposes when adding new entries to ensure that IDs are unique and follow the correct format.
     * @return a list of valid source IDs currently being tracked by this FootprintTracker instance, extracted from the emission entries in the emissions list and filtered to include only those that match the required pattern (EFT-XXX).
     * 
     */

    public List<String> extractID() {
        List<String> id_list= new ArrayList<>();

        for (EmissionSource entry : emissions) {
            String id = entry.getSourceID();
            if (id.matches("[EFT]-\\d{3}")) {
                id_list.add(id);
            }
        }

        return id_list;

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
     * Determines the type of emission for a given entry by checking the instance type of the entry (e.g., EnergyEmission, FoodEmission, TransportationEmission) and returns a formatted string that includes specific details relevant to that type of emission (e.g., KWH used and energy source for EnergyEmission, meal type and number of meals for FoodEmission, distance and transportation mode for TransportationEmission). If the entry does not match any known emission types, it returns "Unknown". This method provides a way to extract and display specific information about each emission entry based on its type.
     * @param entry
     * @return a formatted string containing specific details about the emission entry based on its type, or "Unknown" if the entry type is not recognized.
     */

    public String TypeofEmission(EmissionSource entry) {
    if (entry instanceof EnergyEmission energyEmission) {
        return "KWH used: " + energyEmission.getKwhused() + "\n" + "Energy Source: " + energyEmission.getEnergySource();
    } else if (entry instanceof FoodEmission foodEmission) {
        return "Meal Type: " + foodEmission.getMealType() + "\n" + "Number of Meals: " + foodEmission.getNumberOfMeals();
    } else if (entry instanceof TransportationEmission transportationEmission) {
        return "Distance: " + transportationEmission.getDistanceKM() + "\n" + "Transportation Mode: " + transportationEmission.getVehicleType();
    } else {
        return "Unknown";
    }
}


    /**
     * Returns a list of unique user names from the emissions list.
     * @return an ArrayList of unique user names.
     */
    public ArrayList<String> getUniqueUsers() {
        ArrayList<String> uniqueUsers = new ArrayList<>();
        for (EmissionSource entry : emissions) {
        String userName = entry.getUserName();
        if (!uniqueUsers.contains(userName)) {
            uniqueUsers.add(userName);
        }
    }
         return uniqueUsers;
    }

    /**
     * Determines the user with the highest total emissions by iterating through the list of unique users, calculating the total emissions for each user using the GetTotalEmissionsForUser method, and comparing these totals to find the user with the highest emissions. This method returns the name of the user who has the highest total emissions based on their tracked entries.
     * @return the name of the user with the highest total emissions.
     */
    public String getHighestTotalEmissionUser() {
        String highestUser = "";
        double highestEmission = 0.0;
        ArrayList<String> users = getUniqueUsers();
        for (String user : users) {
            double userEmission = GetTotalEmissionsForUser(user);
            if (userEmission > highestEmission) { highestEmission = userEmission; highestUser = user; }
        }
        return String.format("%s (%.2f kg CO2)", highestUser, highestEmission);
    }


    /**
     * Generates a daily report that groups emissions by user, prints each entry for each user, calculates and prints the subtotal of emissions for each user, and finally calculates and prints the grand total of emissions for all users. The report is formatted to display the details of each emission entry (e.g., sourceID, category, date) along with the calculated emissions in kg CO2, and it provides a clear summary of the emissions for each user as well as the overall total.
     * prints a formatted daily report that groups emissions by user, includes subtotals for each user, and a grand total for all users, displaying the details of each emission entry and the calculated emissions in kg CO2.
     */
    public void generateDailyReport() {

        try{

        System.out.println("=== " + trackerName + " Daily Report ===\n");
        
        // Get unique users
        ArrayList<String> users = getUniqueUsers();
        
        
        
        // Print emissions grouped by user
        for (String user : users) {
            System.out.println("\nUser: " + user);
            
            
            // Print all entries for this user
            for (EmissionSource entry : emissions) {
                if (entry.getUserName().equals(user)) {
                    System.out.println(entry.toString());
                    
                }
            }
            
            // Print user subtotal
            System.out.printf("Subtotal: %.2f kg CO2\n", GetTotalEmissionsForUser(user));
           
        }
        
        // Print grand total
        System.out.printf("\n\nGrand Total: %.2f kg CO2\n\n", GetTotalEmissions());

    } catch (Exception e) {
        System.err.println("An Error Occurred while generating the report: " + e.getMessage());
    }



}

   
}
