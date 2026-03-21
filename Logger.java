/**
 * Logger.java
 * This class is responsible for logging operations performed within the GreenPrint CLI application, as well as
 * saving and loading the application state. It provides methods to log specific operations with details, save the current state of emissions to a file, and load the state from a file when the application starts. The logging functionality helps track user actions and changes made to the emissions data, while the state management ensures that users can persist their data across sessions.
 */


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Logger {


    private static String LOG_FILE = "Zero-Carbon-Footprint-Tracker/greenprint_log.txt";
    private static String STATE_FILE = "Zero-Carbon-Footprint-Tracker/greenprint_save_state.txt";

  

 
    /**
     * Logs an operation with its details to the log file. Each log entry includes the operation name, details, and a timestamp of when the operation was performed. The log is appended to the existing log file, allowing for a historical record of actions taken within the application. This method is used throughout the application to track significant events and changes to the emissions data, providing a way to review past actions and debug issues if necessary.
     * @param operation
     * @param details
     */

    public static void log(String operation, String details) {
        try (FileWriter writer = new FileWriter(LOG_FILE, true)) {

            String date = LocalDate.now().toString();
            String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            writer.write(String.format("{%s} : %s : [%s | %s]\n", operation, details, date, time));
            writer.flush();

        } catch (IOException e) {
            System.out.println("Error writing to log file greenprint: " + e.getMessage());
        }
    }

     /**
     * Saves all emission entries to greenprint_state.txt file.
     * Format: TYPE|sourceID|category|date|userName|field1|field2|...
     */
    public static void saveState() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(STATE_FILE))) {
            for (EmissionSource entry : GreenPrintGUI.tracker.getEmissions()) {
                String line = "";
                if (entry instanceof EnergyEmission energy) {
                    line = String.format("ENERGY|%s|%s|%s|%s|%.2f|%s",
                        energy.getSourceID(), energy.getCategory(), energy.getDate(),
                        energy.getUserName(), energy.getKwhused(), energy.getEnergySource());
                } else if (entry instanceof FoodEmission food) {
                    line = String.format("FOOD|%s|%s|%s|%s|%s|%d",
                        food.getSourceID(), food.getCategory(), food.getDate(),
                        food.getUserName(), food.getMealType(), food.getNumberOfMeals());
                } else if (entry instanceof TransportationEmission transport) {
                    line = String.format("TRANSPORT|%s|%s|%s|%s|%.2f|%s",
                        transport.getSourceID(), transport.getCategory(), transport.getDate(),
                        transport.getUserName(), transport.getDistanceKM(), transport.getVehicleType());
                }
                if (!line.isEmpty()) {
                    writer.write(line);
                    log("STATE_SAVED", line);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Error saving state: " + e.getMessage());
        }
    }

    /**
     * Loads emission entries from greenprint_state.txt file if it exists.
     */
    public static void loadState() {
        File file = new File(STATE_FILE);
        if (!file.exists() || file.length() == 0) {
            return;
        }

        ArrayList<EmissionSource> emissions = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(STATE_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length < 6) continue;

                String type = parts[0];
                String sourceID = parts[1];
                String category = parts[2];
                String date = parts[3];
                String userName = parts[4];

                try {
                    if (type.equals("ENERGY")) {
                        double kwh = Double.parseDouble(parts[5]);
                        String energySource = parts[6];
                        emissions.add(new EnergyEmission(sourceID, category, date, userName, kwh, energySource));

                    } else if (type.equals("FOOD")) {
                        String mealType = parts[5];
                        int meals = Integer.parseInt(parts[6]);
                        emissions.add(new FoodEmission(sourceID, category, date, userName, mealType, meals));

                    } else if (type.equals("TRANSPORT")) {
                        double distance = Double.parseDouble(parts[5]);
                        String vehicleType = parts[6];
                        emissions.add(new TransportationEmission(sourceID, category, date, userName, distance, vehicleType));
                    }

                } catch (Exception e) {
                    System.err.println("Error parsing line: " + line + " - " + e.getMessage());
                }
            }

            GreenPrintGUI.tracker.setEmissions(emissions);

        

        } catch (IOException e) {
            System.err.println("Error loading state: " + e.getMessage());
        }


        
    }

    /**
     * Filters log entries based on a specific operation.
     * @param operation the operation to filter by
     * @return a list of filtered log entries
     */
    public static ArrayList<String> filterOperation(String operation) {
            ArrayList<String> filtered = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(LOG_FILE))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.contains("{" + operation + "}")) {
                        filtered.add(line);
                    }
                }
            } catch (IOException e) {
                System.err.println("Error reading log file: " + e.getMessage());
            }
            return filtered;
        }

}
