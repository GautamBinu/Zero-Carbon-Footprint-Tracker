/**
 * EmissionIDValidator.java
 * This class is responsible for validating emission IDs entered by the user in the GreenPrint CLI application. It ensures that the IDs follow a specific format and are unique within the system.
 * The validation process includes checking for uppercase letters, matching a specific pattern (EFT-XXX), and ensuring that the ID is not a duplicate of existing IDs in the system. The class provides two overloaded methods for validation: one that takes an input string and a list of existing IDs, and another that retrieves the existing IDs from the FootprintTracker before performing validation.
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



/**
 * The EmissionIDValidator class provides methods to validate emission IDs based on specific criteria. It checks if the input ID is in uppercase, matches the required pattern, and is not a duplicate of existing IDs in the system. The validation results are returned as integer codes, where "0" indicates no error, "1" indicates that the input is not in uppercase, "2" indicates that the pattern does not match, and "3" indicates that the ID is a duplicate.
 * The class relies on the FootprintTracker to retrieve existing IDs for validation purposes. It is designed to be used within the GreenPrint CLI application to ensure that users enter valid emission IDs when adding new entries to the system.
 * 
 */
public class EmissionIDValidator extends GreenPrintGUI {
    
    /**
     * Validates the input emission ID against a provided list of existing IDs. This method checks if the input is in uppercase, matches the required pattern (EFT-XXX), and is not a duplicate of any ID in the provided list. It returns an integer code representing the validation result, which can be used to display appropriate error messages to the user in the GUI.
     * @param input
     * @param id_list
     * @return integer code representing the validation result, where "0" indicates no error, "1" indicates that the input is not in uppercase, "2" indicates that the pattern does not match, and "3" indicates that the ID is a duplicate.
     */
    public static int ValidateFinal(String input,ArrayList<String> id_list){
        //String input will be replaced with textfield input from GUI
        if (!input.equals(input.toUpperCase())){
            return 1;    // "1" refers to entered letter not being uppercase error
        }
        if(!input.matches("[EFT]-\\d{3}")){
            return 2;    // "2" refers to pattern not matching error
        }
        if(id_list.contains(input)){
            return 3; // "3" refers to duplicate value error
        }
        return 0;   //"0" refers to no error
        // Idea is to implement the "method/thing" in the GUI class itself, for error type, and display the error as text to the user in the GUI, all in the same class
    }

    /**
     * Overloaded method that retrieves existing IDs from the FootprintTracker and validates the input ID against them. This method is useful for validating IDs without needing to manually pass the list of existing IDs, as it directly interacts with the FootprintTracker to obtain the necessary data for validation.
     * @param input
     * @return integer code representing the validation result, where "0" indicates no error, "1" indicates that the input is not in uppercase, "2" indicates that the pattern does not match, and "3" indicates that the ID is a duplicate.
     * @throws IOException
     */
    public static int ValidateFinal(String input) throws IOException {
        
        ArrayList<String> id_list = new ArrayList<>(tracker.extractID());
        return ValidateFinal(input, id_list);
    }
}

