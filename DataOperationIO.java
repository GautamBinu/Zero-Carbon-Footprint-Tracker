/**
 * DataOperationIO.java
 * This class is responsible for handling the user interface and logic for the "Data Operations" tab in the GreenPrint CLI application. It provides functionality for adding new users and their associated emission entries, as well as searching for existing users and their emissions. The class utilizes JavaFX components to create an interactive and user-friendly interface that allows users to input their data, receive validation feedback, and view their emissions in an organized manner. It also includes methods for creating styled UI elements such as buttons, text fields, combo boxes, and labels to maintain a consistent aesthetic throughout the application.
 * 
 */


import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;

/**
 * 
 * This class is responsible for handling the user interface and logic for the "Data Operations" tab in the GreenPrint CLI application. It provides functionality for adding new users and their associated emission entries, as well as searching for existing users and their emissions. The class utilizes JavaFX components to create an interactive and user-friendly interface that allows users to input their data, receive validation feedback, and view their emissions in an organized manner. It also includes methods for creating styled UI elements such as buttons, text fields, combo boxes, and labels to maintain a consistent aesthetic throughout the application.
 */

public class DataOperationIO {


     /**
     * Styles for passive UI elements
     */
    static String PassiveStyle = GreenPrintGUI.FontFamily +
        "-fx-font-size: 12px; " +
        "-fx-font-weight: bold;" +
        "-fx-background-color: #ffffff; " +
        "-fx-border-color: #004d27; " +
        "-fx-border-radius: 8px; " + // Added curved border
        "-fx-background-radius: 8px;"; // Adjust this number to increase/decrease the gap


    static String ValidStyle = "-fx-text-fill: green; -fx-font-weight: bold; -fx-font-size: 14px;";
    static String InvalidStyle = "-fx-text-fill: red; -fx-font-weight: bold; -fx-font-size: 14px;";
    static String InvalidBorderStyle = PassiveStyle + "-fx-border-color: red; -fx-border-width: 2px;";

    // Store shared data from InitialBox
    static String sharedUserName = "";
    static String sharedDate = "";
    static Tab AddUserTab;

    /**
     * Creates a styled error label with a red text color and bold font weight, designed to provide clear and visually distinct feedback to users when an error occurs. This method is used throughout the "Data Operations" tab to display error messages in a consistent and attention-grabbing manner, helping users quickly identify issues with their input or actions.
     * @param message
     * @return a Label object styled to indicate an error, containing the provided message text.
     */

    

      public static TabPane DataOperationsTab() {
        TabPane dataOpsTab = new TabPane();
        dataOpsTab.setStyle(
            GreenPrintGUI.FontFamily +
            "-fx-font-size: 12px; " + 
            "-fx-font-weight: bold;"
        );

        dataOpsTab.setTabMinHeight(40);
        dataOpsTab.setTabMinWidth(150);

        dataOpsTab.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        dataOpsTab.setSide(Side.TOP);

        AddUserTab = new Tab();
        AddUserTab.setText("Add User");
        AddUserTab.setContent(InitialBox());

        Tab SearchUserTab = new Tab();
        SearchUserTab.setText("Search User");
        SearchUserTab.setContent(SearchUserTab());

        dataOpsTab.getTabs().addAll(AddUserTab, SearchUserTab);

        return dataOpsTab;
    }

    /**
     * Creates the user interface for searching users and their associated emission entries in the "Data Operations" tab. The interface includes a text field for entering the user name to search, a search button, and a status label for displaying success or error messages. When the search button is clicked, the method validates the input, searches for entries matching the user name, and dynamically generates labels for each found entry. The results are displayed in a dedicated container below the search controls, providing users with an organized view of their emissions based on the searched user name.
     * @return a VBox containing the search controls and the dynamically generated results for the searched user, styled and organized for a user-friendly experience.
     */

    public static VBox SearchUserTab() {
        VBox searchBox = new VBox(10);
        searchBox.setAlignment(Pos.CENTER);

        TextField searchField = CreateTextField("Enter User Name to Search");
        Button searchButton = CreateButton("Search");
        
        // This label is now just for showing error/success messages
        Label statusLabel = new Label();
        

        // Create a dedicated VBox to hold the dynamically generated labels
        VBox resultsContainer = new VBox(10); 
        resultsContainer.setAlignment(Pos.CENTER);

        searchButton.setOnAction(event -> {
            String userNameToSearch = searchField.getText().trim().toLowerCase();
            
            // 1. Clear out any labels from a previous search
            resultsContainer.getChildren().clear();

            if (userNameToSearch.isEmpty()) {
                statusLabel.setText("✗ Please enter a user name to search.");
                statusLabel.setStyle(InvalidStyle);
                return;
            }

            // 2. Search for entries matching the user name
            for (EmissionSource entry : GreenPrintGUI.tracker.getEntriesByUser(userNameToSearch)) {
                if (entry.getUserName().equalsIgnoreCase(userNameToSearch)) {
                    // Create a label for this specific entry and add it to the container
                    resultsContainer.getChildren().add(Dashboard.makeLabel(entry.toString()));
                }
            }

            // 3. Update the status label based on whether our container has anything in it
            if (!resultsContainer.getChildren().isEmpty()) {
                statusLabel.setText("✓ Found " + resultsContainer.getChildren().size() + " entries.");
                statusLabel.setStyle(ValidStyle);
            } else {
                statusLabel.setText("✗ No entries found for user: " + userNameToSearch);
                statusLabel.setStyle(InvalidStyle);
            }
        });

        // Add the new resultsContainer to the main view at the very bottom
        searchBox.getChildren().addAll(
            Dashboard.MakeTitleLabel("SEARCH USERS"), 
            searchField, 
            searchButton, 
            statusLabel, 
            resultsContainer
        );
        
        return searchBox;
}

   


    public static Label ErrorLabel(String message) {
        Label errorLabel = new Label(message);
        errorLabel.setStyle(InvalidStyle);
        return errorLabel;
    }

    /**
     * Creates a styled success label with a green text color and bold font weight, designed to provide clear and visually distinct feedback to users when an action is successful. This method is used throughout the "Data Operations" tab to display success messages in a consistent and positive manner, helping users quickly recognize when their input or actions have been processed correctly.
     * @param message
     * @return a Label object styled to indicate success, containing the provided message text.
     */
    public static Label SuccessLabel(String message) {
        Label successLabel = new Label(message);
        successLabel.setStyle(ValidStyle);
        return successLabel;
    }

    /**
     * Creates the initial user interface for adding a new user and their associated emission entries in the "Data Operations" tab. The interface includes fields for entering the user's name and the date of the emission entries, as well as buttons to navigate to specific forms for adding energy, food, or transportation emissions. The method also handles the shared data (user name and date) that will be used across the different emission entry forms, ensuring that this information is accessible when users navigate between them. This initial box serves as the starting point for users to input their data and access the various forms for adding their emissions.
     */

   

  
    /**
     * Creates a styled button with a consistent design, including a specific font size, text color, border color, and dimensions to enhance the user interface of the "Data Operations" tab. This method is used to generate buttons for various actions such as searching for users or adding emission entries, ensuring that all buttons across the application maintain a cohesive and visually appealing style.
     * @param text
     * @return a Button object styled with the defined design parameters, containing the provided text as its label.
     */

    public static Button CreateButton(String text) {
        Button button = new Button(text);
        button.setStyle(
            PassiveStyle + // Base style for consistency
            "-fx-font-size: 14px; " + // Increased size for better visibility
            "-fx-text-fill: #004d27; " + // White text for contrast
            "-fx-border-color: #004d27; " 
        );
        button.setPrefHeight(45); // Increased height for better clickability
        button.setMaxWidth(200); // Set a max width for consistency
        return button;
    }

    /**
     * Creates a styled ComboBox with a consistent design, including a specific font size, text color, border color, and dimensions to enhance the user interface of the "Data Operations" tab. This method is used to generate combo boxes for selecting options such as energy sources or vehicle types, ensuring that all combo boxes across the application maintain a cohesive and visually appealing style.
     * @param prompt
     * @param options
     * @return a ComboBox<String> object styled with the defined design parameters, containing the provided options and displaying the specified prompt text when no selection has been made.
     */
    public static ComboBox<String> CreateComboBox(String prompt, String... options) {
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setStyle(PassiveStyle);
        comboBox.setMaxWidth(350);
        comboBox.setPrefHeight(45);
        comboBox.getItems().addAll(options);
        comboBox.setPromptText(prompt);
        return comboBox;
    }

    /**
     * Creates a styled TextField with a consistent design, including a specific font size, text color, border color, and dimensions to enhance the user interface of the "Data Operations" tab. This method is used to generate text fields for inputting data such as user names, dates, or emission values, ensuring that all text fields across the application maintain a cohesive and visually appealing style.
     * @param prompt
     * @return a TextField object styled with the defined design parameters, displaying the specified prompt text when the field is empty and ready for user input.
     */
   public static TextField CreateTextField(String prompt) {
        TextField textField = new TextField();
        textField.setStyle(PassiveStyle);
        textField.setMaxWidth(350);
        textField.setPrefHeight(45);
        textField.setPromptText(prompt);
        return textField;
    }

    /**
     * Creates a "Back" button that, when clicked, navigates the user back to the initial box for adding users and their associated emission entries in the "Data Operations" tab. This button is styled consistently with other buttons in the application and is designed to provide a clear and easy way for users to return to the main input screen after navigating to specific forms for adding energy, food, or transportation emissions.
     * @return a Button object styled as a "Back" button, with an event handler that sets the content of the AddUserTab back to the initial box when clicked.
     */

    public static Button CreateBackButtonToInitialBox() {
        Button backButton = CreateButton("Back");
        backButton.setMaxWidth(100);
        backButton.setOnAction(e -> {
            AddUserTab.setContent(InitialBox());
        });
        return backButton;
    }

  
    /**
     * Creates a styled HBox containing a label and a text field for entering an emission ID, along with a validation label that provides real-time feedback on the validity of the entered ID. The method takes a string parameter that represents the beginning character of the emission ID (e.g., "E" for energy, "F" for food, "T" for transportation) and constructs the full ID format for validation. The validation logic checks the entered ID against predefined rules and updates the validation label to indicate whether the ID is valid or invalid, providing users with immediate feedback as they input their data.
     * @param BeginningCharacter
     * @return an HBox containing the prefix label, the text field for ID input, and the validation label, all styled and organized to provide a user-friendly interface for entering and validating emission IDs in the "Data Operations" tab.
     */
    public static HBox CreateEmissionTypeTextField(String BeginningCharacter) {
    HBox hbox = new HBox(10);
    
    Label validationLabel = new Label();
    
    // 1. Create the Label
    Label prefixLabel = new Label(BeginningCharacter + " -");
    
    
    prefixLabel.setStyle(
        
        "-fx-font-size: 18px; " +  
        "-fx-font-weight: 900; " +  
        "-fx-text-fill: #004d27;"   
    );
    
    // 2. Create the TextField
    TextField textField = CreateTextField("Enter ID Number (3 digits)");
    
    
    textField.textProperty().addListener((observable, oldValue, newValue) -> {
        
        String stringToValidate = BeginningCharacter + "-" + newValue.trim();
        
        try {
            int validationResult = EmissionIDValidator.ValidateFinal(stringToValidate);
            
            if (validationResult == 0) {
                validationLabel.setText("✓ Valid");
                validationLabel.setStyle(ValidStyle);
            } else {
                validationLabel.setText("✗ Invalid");
                validationLabel.setStyle(InvalidStyle);
            }
        } catch (IOException e) {
            validationLabel.setText("✗ Error");
            validationLabel.setStyle(InvalidStyle);
            e.printStackTrace(); // Helpful for debugging if the file read fails
        }
    });

    // 3. Add all elements to the HBox
    hbox.getChildren().addAll(prefixLabel, textField, validationLabel);
    hbox.setAlignment(Pos.CENTER); // Center the HBox contents
    
    return hbox;

}

    public static DatePicker AddUserDatePicker() {
        DatePicker datePicker = new DatePicker();
        datePicker.setStyle(PassiveStyle);
        
        // Use setMaxWidth to match the TextField and prevent stretching
        datePicker.setMaxWidth(350); 
        datePicker.setPrefHeight(45); // Matched height with TextField
        return datePicker;
    }

    /**
     * Creates the initial user interface for adding a new user and their associated emission entries in the "Data Operations" tab. The interface includes fields for entering the user's name and the date of the emission entries, as well as buttons to navigate to specific forms for adding energy, food, or transportation emissions. The method also handles the shared data (user name and date) that will be used across the different emission entry forms, ensuring that this information is accessible when users navigate between them. This initial box serves as the starting point for users to input their data and access the various forms for adding their emissions.
     * @return a VBox containing the input fields for user name and date, as well as buttons to navigate to the specific emission entry forms, all styled and organized to provide a user-friendly interface for adding new users and their emissions in the "Data Operations" tab.
     */

    public static VBox TransportationEmissionsFields() {
        VBox transportationBox = new VBox(10);
        transportationBox.setAlignment(Pos.CENTER);

        // Create fields for transportation emissions
        HBox CreateIDField = CreateEmissionTypeTextField("T");
        TextField distanceField = CreateTextField("Enter Distance Travelled in KM");
        ComboBox<String> vehicleTypeComboBox = CreateComboBox("Select Vehicle Type", "Car", "Bus", "Train", "Cycle");
        Button addButton = CreateButton("ADD Transportation Emissions");
        addButton.setMaxWidth(350);
        
       
        Label errorLabel =  ErrorLabel("");
        Label successLabel = SuccessLabel("");

        // Setup ADD button action
        addButton.setOnAction(event -> {
            boolean isValid = true;
            StringBuilder errorMsg = new StringBuilder();
            

            // Get validation label from CreateIDField HBox (it's the 3rd child)
            Label validationLabel = (Label) CreateIDField.getChildren().get(2);
            TextField idTextField = (TextField) CreateIDField.getChildren().get(1);
            Label prefixLabel = (Label) CreateIDField.getChildren().get(0);

            // Validate ID field
            if (!validationLabel.getText().equals("✓ Valid")) {
                errorMsg.append("✗ Valid Emission ID is required\n");
                idTextField.setStyle(InvalidBorderStyle);
                isValid = false;
            } else {
                idTextField.setStyle(PassiveStyle);
            }

            // Validate distance field (must be positive double)
            String distanceText = distanceField.getText().trim();
            double distance = 0.0;
            try {
                distance = Double.parseDouble(distanceText);
                if (distance < 0) {
                    errorMsg.append("✗ Distance cannot be negative\n");
                    distanceField.setStyle(InvalidBorderStyle);
                    isValid = false;
                } else {
                    distanceField.setStyle(PassiveStyle);
                }
            } catch (NumberFormatException e) {
                errorMsg.append("✗ Distance must be a valid number\n");
                distanceField.setStyle(InvalidBorderStyle);
                isValid = false;
            }

            // Validate vehicle type
            String vehicleType = vehicleTypeComboBox.getValue();
            if (vehicleType == null) {
                errorMsg.append("✗ Vehicle Type must be selected\n");
                vehicleTypeComboBox.setStyle(InvalidBorderStyle);
                isValid = false;
            } else {
                vehicleTypeComboBox.setStyle(PassiveStyle);
            }

            if (!isValid) {
                errorLabel.setText(errorMsg.toString());
                return;
            }

            // All valid - add to tracker
            try {
                String fullEmissionID = prefixLabel.getText().replace(" -", "") + "-" + idTextField.getText().trim();
                TransportationEmission emission = new TransportationEmission(
                    fullEmissionID,
                    "Transportation",
                    sharedDate,
                    sharedUserName,
                    distance,
                    vehicleType
                );
                GreenPrintGUI.tracker.addEntry(emission);
                successLabel.setText("✓ Transportation Emission added successfully!");

                // Refresh the dashboard to show the new data
                GreenPrintGUI.refreshDashboard();

                // Clear fields for next entry
                AddUserTab.setContent(InitialBox("✓ Transportation Emission added successfully!"));
            } catch (Exception e) {
                errorLabel.setText("✗ Error adding emission: " + e.getMessage());
            }
        });

        transportationBox.getChildren().addAll(
            Dashboard.MakeTitleLabel("ADD TRANSPORTATION EMISSIONS"),
            CreateIDField,
            distanceField,
            vehicleTypeComboBox,
            errorLabel,
            successLabel,
            addButton,
            CreateBackButtonToInitialBox()
        );
        return transportationBox;
    }

    
    /**
     * Creates the user interface for adding energy emissions in the "Data Operations" tab. This interface includes fields for entering the emission ID, the amount of energy used in KWH, and the energy source. It also includes validation logic to ensure that the input data is correct before allowing the user to add the emission entry to the tracker. Upon successful addition, the dashboard is refreshed to reflect the new entry, and feedback is provided to the user through success or error messages. This method provides a structured and user-friendly way for users to input their energy emissions data into the application.
     * @return a VBox containing the input fields for energy emissions, along with validation and feedback mechanisms, all styled and organized to provide a user-friendly interface for adding energy emissions in the "Data Operations" tab.
     */
    public static VBox EnergyEmissionsFields() {
        VBox energyBox = new VBox(10);
        energyBox.setAlignment(Pos.CENTER);

        // Create fields for energy emissions
        HBox CreateIDField = CreateEmissionTypeTextField("E");
        TextField kwhField = CreateTextField("Enter KWH Used");
        ComboBox<String> energySourceComboBox = CreateComboBox("Select Energy Source", "Grid", "Solar", "Wind");
        Button addButton = CreateButton("ADD Energy Emissions");
        Label errorLabel =  ErrorLabel("");
        Label successLabel = SuccessLabel("");

        // Setup ADD button action
        addButton.setOnAction(event -> {
            boolean isValid = true;
            StringBuilder errorMsg = new StringBuilder();
            

            // Get validation label from CreateIDField HBox (it's the 3rd child)
            Label validationLabel = (Label) CreateIDField.getChildren().get(2);
            TextField idTextField = (TextField) CreateIDField.getChildren().get(1);
            Label prefixLabel = (Label) CreateIDField.getChildren().get(0);

            // Validate ID field
            if (!validationLabel.getText().equals("✓ Valid")) {
                errorMsg.append("✗ Valid Emission ID is required\n");
                idTextField.setStyle(InvalidBorderStyle);
                isValid = false;
            } else {
                idTextField.setStyle(PassiveStyle);
            }

            // Validate KWH field (must be positive double)
            String kwhText = kwhField.getText().trim();
            double kwh = 0.0;
            try {
                kwh = Double.parseDouble(kwhText);
                if (kwh < 0) {
                    errorMsg.append("✗ KWH cannot be negative\n");
                    kwhField.setStyle(InvalidBorderStyle);
                    isValid = false;
                } else {
                    kwhField.setStyle(PassiveStyle);
                }
            } catch (NumberFormatException e) {
                errorMsg.append("✗ KWH must be a valid number\n");
                kwhField.setStyle(InvalidBorderStyle);
                isValid = false;
            }

            // Validate energy source
            String energySource = energySourceComboBox.getValue();
            if (energySource == null) {
                errorMsg.append("✗ Energy Source must be selected\n");
                energySourceComboBox.setStyle(InvalidBorderStyle);
                isValid = false;
            } else {
                energySourceComboBox.setStyle(PassiveStyle);
            }

            if (!isValid) {
                errorLabel.setText(errorMsg.toString());
                return;
            }

            // All valid - add to tracker
            try {
                String fullEmissionID = prefixLabel.getText().replace(" -", "") + "-" + idTextField.getText().trim();
                EnergyEmission emission = new EnergyEmission(
                    fullEmissionID,
                    "Energy",
                    sharedDate,
                    sharedUserName,
                    kwh,
                    energySource
                );
                GreenPrintGUI.tracker.addEntry(emission);
                successLabel.setText("✓ Energy Emission added successfully!");

                // Refresh the dashboard to show the new data
                GreenPrintGUI.refreshDashboard();

                // Clear fields for next entry
                AddUserTab.setContent(InitialBox("✓ Energy Emission added successfully!"));
                
            } catch (Exception e) {
                errorLabel.setText("✗ Error adding emission: " + e.getMessage());
            }
        });

        energyBox.getChildren().addAll(
            Dashboard.MakeTitleLabel("ADD ENERGY EMISSIONS"),
            CreateIDField,
            kwhField,
            energySourceComboBox,
            errorLabel,
            successLabel,
            addButton,
            CreateBackButtonToInitialBox()
        );

        return energyBox;
    }

    /**
     * Creates the user interface for adding food emissions in the "Data Operations" tab.
     * @return a VBox containing the input fields for food emissions, along with validation and feedback mechanisms, all styled and organized to provide a user-friendly interface for adding food emissions in the "Data Operations" tab.
     */
    public static VBox FoodEmissionsFields() {
        VBox foodBox = new VBox(10);
        foodBox.setAlignment(Pos.CENTER);

        // Create fields for food emissions
        HBox CreateIDField = CreateEmissionTypeTextField("F");
        TextField mealsField = CreateTextField("Enter Number of Meals");
        ComboBox<String> mealTypeComboBox = CreateComboBox("Select Meal Type", "Vegan", "Vegetarian", "Poultry", "Beef");
        Button addButton = CreateButton("ADD Food Emissions");
        Label errorLabel =  ErrorLabel("");
        Label successLabel = SuccessLabel("");

        // Setup ADD button action
        addButton.setOnAction(event -> {
            boolean isValid = true;
            StringBuilder errorMsg = new StringBuilder();
            

            // Get validation label from CreateIDField HBox (it's the 3rd child)
            Label validationLabel = (Label) CreateIDField.getChildren().get(2);
            TextField idTextField = (TextField) CreateIDField.getChildren().get(1);
            Label prefixLabel = (Label) CreateIDField.getChildren().get(0);

            // Validate ID field
            if (!validationLabel.getText().equals("✓ Valid")) {
                errorMsg.append("✗ Valid Emission ID is required\n");
                idTextField.setStyle(InvalidBorderStyle);
                isValid = false;
            } else {
                idTextField.setStyle(PassiveStyle);
            }

            // Validate meals field (must be positive integer)
            String mealsText = mealsField.getText().trim();
            int meals = 0;
            try {
                meals = Integer.parseInt(mealsText);
                if (meals < 0) {
                    errorMsg.append("✗ Number of Meals cannot be negative\n");
                    mealsField.setStyle(InvalidBorderStyle);
                    isValid = false;
                } else {
                    mealsField.setStyle(PassiveStyle);
                }
            } catch (NumberFormatException e) {
                errorMsg.append("✗ Number of Meals must be a valid integer\n");
                mealsField.setStyle(InvalidBorderStyle);
                isValid = false;
            }

            // Validate meal type
            String mealType = mealTypeComboBox.getValue();
            if (mealType == null) {
                errorMsg.append("✗ Meal Type must be selected\n");
                mealTypeComboBox.setStyle(InvalidBorderStyle);
                isValid = false;
            } else {
                mealTypeComboBox.setStyle(PassiveStyle);
            }

            if (!isValid) {
                errorLabel.setText(errorMsg.toString());
                return;
            }

            // All valid - add to tracker
            try {
                String fullEmissionID = prefixLabel.getText().replace(" -", "") + "-" + idTextField.getText().trim();
                FoodEmission emission = new FoodEmission(
                    fullEmissionID,
                    "Food",
                    sharedDate,
                    sharedUserName,
                    mealType,
                    meals
                );
                GreenPrintGUI.tracker.addEntry(emission);

                GreenPrintGUI.refreshDashboard();

                // Clear fields for next entry
                AddUserTab.setContent(InitialBox("✓ Food Emission added successfully!"));
                
                
            } catch (Exception e) {
                errorLabel.setText("✗ Error adding emission: " + e.getMessage());
            }
        });

        foodBox.getChildren().addAll(
            Dashboard.MakeTitleLabel("ADD FOOD EMISSIONS"),
            CreateIDField,
            mealsField,
            mealTypeComboBox,
            errorLabel,
            successLabel,
            addButton,
            CreateBackButtonToInitialBox()
        );

        return foodBox;
    }
    

    /**
     * Creates the initial user interface for adding a new user and their associated emission entries in the "Data Operations" tab. The interface includes fields for entering the user's name and the date of the emission entries, as well as buttons to navigate to specific forms for adding energy, food, or transportation emissions. The method also handles the shared data (user name and date) that will be used across the different emission entry forms, ensuring that this information is accessible when users navigate between them. This initial box serves as the starting point for users to input their data and access the various forms for adding their emissions.
     * @return a VBox containing the input fields for user name and date, as well as buttons to navigate to the specific emission entry forms, all styled and organized to provide a user-friendly interface for adding new users and their emissions in the "Data Operations" tab.
     */
    public static VBox InitialBox() {
        return InitialBox(null);
    }

    public static VBox InitialBox(String successMessage) {
        VBox initialBox = new VBox(20);
        initialBox.setPadding(new javafx.geometry.Insets(40)); // Increased padding for breathing room
        initialBox.setAlignment(Pos.CENTER);

        // Create UI elements
        TextField userNameField = CreateTextField("Enter User Name");
        ComboBox<String> emissionTypeComboBox = CreateComboBox("Select Emission Type", "Transportation Emissions", "Energy Emissions", "Food Emissions");
        DatePicker datePicker = AddUserDatePicker();
        Button continueButton = CreateButton("Continue...");
        Label errorLabel =  ErrorLabel("");

        // Success label for displaying success messages
        Label successLabel = SuccessLabel("");


        // Setup Continue button action
        continueButton.setOnAction(event -> {
            String userName = userNameField.getText().trim();
            String emissionType = emissionTypeComboBox.getValue();

            // Validate inputs
            boolean isValid = true;
            StringBuilder errorMsg = new StringBuilder();

            if (userName.isEmpty()) {
                errorMsg.append("✗ User Name is required\n");
                userNameField.setStyle(InvalidBorderStyle);
                isValid = false;
            } else {
                userNameField.setStyle(PassiveStyle);
            }

            if (emissionType == null) {
                errorMsg.append("✗ Emission Type must be selected\n");
                emissionTypeComboBox.setStyle(InvalidBorderStyle);
                isValid = false;
            } else {
                emissionTypeComboBox.setStyle(PassiveStyle);
            }

            if (datePicker.getValue() == null) {
                errorMsg.append("✗ Date must be selected\n");
                datePicker.setStyle(InvalidBorderStyle);
                isValid = false;
            } else {
                datePicker.setStyle(PassiveStyle);
            }

            if (!isValid) {
                errorLabel.setText(errorMsg.toString());
                return;
            }

            // Store shared data
            sharedUserName = userName;
            sharedDate = datePicker.getValue().toString();

            // Switch to appropriate emission fields
            if (emissionType.equals("Transportation Emissions")) {
                AddUserTab.setContent(TransportationEmissionsFields());
            } else if (emissionType.equals("Energy Emissions")) {
                AddUserTab.setContent(EnergyEmissionsFields());
            } else if (emissionType.equals("Food Emissions")) {
                AddUserTab.setContent(FoodEmissionsFields());
            }
        });

        // Add initial content or instructions for the Data Operations tab
        // Include success label only if there's a success message
        if (successMessage != null && !successMessage.isEmpty()) {
            successLabel.setText(successMessage);
            initialBox.getChildren().addAll(Dashboard.MakeTitleLabel("ADD USERS"), successLabel, userNameField, emissionTypeComboBox, datePicker, errorLabel, continueButton);
        } else {
            initialBox.getChildren().addAll(Dashboard.MakeTitleLabel("ADD USERS"), userNameField, emissionTypeComboBox, datePicker, errorLabel, continueButton);
        }

        return initialBox;
    }

}