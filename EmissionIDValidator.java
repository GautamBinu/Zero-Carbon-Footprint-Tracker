import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class EmissionIDValidator extends GreenPrintCLI {
    

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
    public static int ValidateFinal(String input) throws IOException {
        
        ArrayList<String> id_list = new ArrayList<>(tracker.extractID());
        return ValidateFinal(input, id_list);
    }
}

