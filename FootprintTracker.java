import java.util.ArrayList;

public class FootprintTracker {
    private String trackerName;
    private ArrayList<EmissionSource> emissions;

    public FootprintTracker(String trackerName) {
        this.trackerName = trackerName;
        this.emissions = new ArrayList<>();
    }

    public void addEntry(EmissionSource entry) {
        emissions.add(entry);
    }

    public double GetTotalEmissions() {
        double total = 0.0;
        for (EmissionSource entry : emissions) {
            total += entry.calculateEmission();
        }
        return total;
    }

    public double GetTotalEmissionsForUser(String userName) {
        double total = 0.0;
        for (EmissionSource entry : emissions) {
            if (entry.getUserName().equals(userName)) {
                total += entry.calculateEmission();
            }
        }
        return total;
    }

    public void generateDailyReport() {
        System.out.println("=== RIT GreenPrint 2026 — Daily Report ===\n");
        
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
    }

}
