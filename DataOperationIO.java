import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.text.ListFormat.Style;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;

public class DataOperationIO extends Application {

      public static TabPane DataOperationsTab() {
        TabPane dataOpsTab = new TabPane();
        dataOpsTab.setStyle(
            "-fx-font-family: 'Segoe UI', Helvetica, Arial, sans-serif; " +
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
            for (EmissionSource entry : GreenPrintCLI.tracker.getEntriesByUser(userNameToSearch)) {
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

    /**
     * Styles for passive UI elements
     */
    static String PassiveStyle = "-fx-font-family: 'Segoe UI', Helvetica, Arial, sans-serif; " +
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


    public static Label ErrorLabel(String message) {
        Label errorLabel = new Label(message);
        errorLabel.setStyle(InvalidStyle);
        return errorLabel;
    }

    public static Label SuccessLabel(String message) {
        Label successLabel = new Label(message);
        successLabel.setStyle(ValidStyle);
        return successLabel;
    }

    @Override
    public void start(Stage arg0) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'start'");
    }

  

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

    public static ComboBox<String> CreateComboBox(String prompt, String... options) {
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setStyle(PassiveStyle);
        comboBox.setMaxWidth(350);
        comboBox.setPrefHeight(45);
        comboBox.getItems().addAll(options);
        comboBox.setPromptText(prompt);
        return comboBox;
    }

   public static TextField CreateTextField(String prompt) {
        TextField textField = new TextField();
        textField.setStyle(PassiveStyle);
        textField.setMaxWidth(350);
        textField.setPrefHeight(45);
        textField.setPromptText(prompt);
        return textField;
    }

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
                GreenPrintCLI.tracker.addEntry(emission);
                successLabel.setText("✓ Transportation Emission added successfully!");

                // Refresh the dashboard to show the new data
                GreenPrintCLI.refreshDashboard();

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
            addButton
        );
        return transportationBox;
    }

    

    public static VBox EnergyEmissionsFields() {
        VBox energyBox = new VBox(10);
        energyBox.setAlignment(Pos.CENTER);

        // Create fields for energy emissions
        HBox CreateIDField = CreateEmissionTypeTextField("E");
        TextField kwhField = CreateTextField("Enter KWH Used");
        ComboBox<String> energySourceComboBox = CreateComboBox("Select Energy Source", "Grid", "Solar", "Wind", "Hydro");
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
                GreenPrintCLI.tracker.addEntry(emission);
                successLabel.setText("✓ Energy Emission added successfully!");

                // Refresh the dashboard to show the new data
                GreenPrintCLI.refreshDashboard();

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
            addButton
        );

        return energyBox;
    }

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
                GreenPrintCLI.tracker.addEntry(emission);

                GreenPrintCLI.refreshDashboard();

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
            addButton
        );

        return foodBox;
    }
    

    // Overload for backward compatibility
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