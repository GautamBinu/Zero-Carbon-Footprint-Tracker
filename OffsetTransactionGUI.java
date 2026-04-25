import javafx.scene.control.*;
import javafx.geometry.Insets;
import javafx.animation.PauseTransition;
import javafx.event.EventHandler;
import javafx.util.Duration;
import java.util.ArrayList;
import javafx.scene.layout.VBox;
import javafx.geometry.Side;
import javafx.geometry.Pos;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.event.Event;

public class OffsetTransactionGUI {  // No longer extends Application

    static Tab currentOffsetTab;
    static Tab currentOffsetLogTab;
    static String PassiveStyle = DataOperationIO.PassiveStyle;
    static String ValidStyle = DataOperationIO.ValidStyle;
    static String InvalidStyle = DataOperationIO.InvalidStyle;
    static String InvalidBorderStyle = DataOperationIO.InvalidBorderStyle;
    static String ScrollPaneStyle = "-fx-background: white; -fx-background-color: white;";

    /**
     * Rounds a double value to 2 decimal places. This method is used to ensure that the emission amounts and costs displayed in the GUI are consistent with the precision shown to the user, avoiding issues with floating-point representation that could lead to validation errors when comparing user input against total emissions.
     * @param value
     * @return the input value rounded to 2 decimal places
     */

    public static double roundTo2Decimals(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    /**
     * Creates the input box for offset transactions, which includes a user selection dropdown, a label to display the user's total emissions, a text field for entering the amount of emissions to offset, a payment method dropdown, a label to display the calculated price, and a submit button. The method also includes validation for user input and updates the price label dynamically as the user enters the emission amount. When the submit button is clicked, it validates the input, shows a processing message, simulates a delay, logs the purchase, refreshes the offset log tab, and displays a receipt page with the transaction details.
     * @return a VBox containing the input elements for offset transactions
     */
    public static VBox createOffsetInputBox() {
        VBox inputBox = new VBox(10);
        inputBox.setPadding(new Insets(20));
        inputBox.setAlignment(Pos.CENTER);
        inputBox.setSpacing(10);
        ArrayList<String> users = GreenPrintGUI.tracker.getUniqueUsers();

        // Create UI elements
        ComboBox<String> Users_ComboBox = DataOperationIO.CreateComboBox("Select User", users.toArray(new String[users.size()]));
        
        // Refresh user list when dropdown is opened to show newly added users
        Users_ComboBox.setOnShowing(new EventHandler<Event>() { 
        @Override
        public void handle(Event event) {
        ArrayList<String> updatedUsers = GreenPrintGUI.tracker.getUniqueUsers();
        Users_ComboBox.getItems().setAll(updatedUsers);
        }
        });
        
        Label userEmissionsLabel = Dashboard.makeLabel("Select a user to view emissions");
        TextField EmissionInput = DataOperationIO.CreateTextField("Enter Emission Amount to Offset (kg CO2)");
        ComboBox<String> Payment_ComboBox = DataOperationIO.CreateComboBox("Payment Method", "Credit Card", "Digital Wallet", "Campus Card");
        Label priceLabel = Dashboard.UserEntryLabel("Calculated Price: $0.00");
        Label discountMessageLabel = DataOperationIO.ErrorLabel(""); // Reuse ErrorLabel styling, will be set to ValidStyle or InvalidStyle
        Label errorLabel = DataOperationIO.ErrorLabel("");
        Button submitButton = DataOperationIO.CreateButton("Purchase Offset");
        Button requestDiscountButton = DataOperationIO.CreateButton("Request Discount");

        // Update label when user is selected
        Users_ComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != null && !oldValue.equals(newValue)
                && discountMessageLabel.getText() != null
                && (discountMessageLabel.getText().startsWith("Server reward applied:")
                    || discountMessageLabel.getText().equals("You can request a discount for the new value!")
                    || discountMessageLabel.getText().equals("You can request a discount for the new user!"))) {
                discountMessageLabel.setText("You can request a discount for the new user!");
                discountMessageLabel.setStyle(DataOperationIO.ValidStyle);
            }

            if (newValue != null) {
                double totalEmissions = GreenPrintGUI.tracker.GetTotalEmissionsForUser(newValue);
                userEmissionsLabel.setText(String.format("%s Total Emissions: \n\n %.2f kg CO2", newValue, roundTo2Decimals(totalEmissions)));
                requestDiscountButton.setDisable(false);
            } else {
                userEmissionsLabel.setText("Select a user to view emissions");
                requestDiscountButton.setDisable(true);
            }
        });

        // Update price label when emission amount changes
        EmissionInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != null && !oldValue.equals(newValue)
                && discountMessageLabel.getText() != null
                && discountMessageLabel.getText().startsWith("Server reward applied:")) {
                discountMessageLabel.setText("You can request a discount for the new value!");
                discountMessageLabel.setStyle(DataOperationIO.ValidStyle);
            }

            try {
                if (newValue.trim().isEmpty()) {
                    priceLabel.setText("Calculated Price: $0.00");
                    requestDiscountButton.setDisable(true);
                } else {
                    double amount = Double.parseDouble(newValue.trim());
                    double cost = Offsets.calculateOffsetCost(amount);
                    priceLabel.setText(String.format("Calculated Price: $%.2f", roundTo2Decimals(cost)));
                    requestDiscountButton.setDisable(amount <= 0);
                }
            } catch (NumberFormatException e) {
                priceLabel.setText("Calculated Price: $0.00");
                requestDiscountButton.setDisable(true);
            }
        });

        // Request Discount button action
        requestDiscountButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String emissionText = EmissionInput.getText().trim();

                // Validate emission input
                if (emissionText.isEmpty()) {
                    discountMessageLabel.setText("Enter an emission amount first");
                    discountMessageLabel.setStyle(DataOperationIO.InvalidStyle);
                    return;
                }

                double emissionAmount = 0.0;
                try {
                    emissionAmount = Double.parseDouble(emissionText);
                    if (emissionAmount <= 0) {
                        discountMessageLabel.setText("Emission amount must be greater than 0");
                        discountMessageLabel.setStyle(DataOperationIO.InvalidStyle);
                        return;
                    }
                } catch (NumberFormatException e) {
                    discountMessageLabel.setText("Enter a valid emission amount");
                    discountMessageLabel.setStyle(DataOperationIO.InvalidStyle);
                    return;
                }

                // Disable button and show processing message
                requestDiscountButton.setDisable(true);
                discountMessageLabel.setText("Requesting discount from server...");
                discountMessageLabel.setStyle(DataOperationIO.ValidStyle);

                final double finalEmissionAmount = emissionAmount;

                // Call client to request discount (runs on background thread)
                FootprintDiscountClient.requestDiscount(emissionAmount,
                    new FootprintDiscountClient.DiscountSuccessHandler() {
                        @Override
                        public void handle(DiscountResult result) {
                            // Success callback
                            discountMessageLabel.setText(result.getFormattedMessage());
                            discountMessageLabel.setStyle(DataOperationIO.ValidStyle);

                            // Update price label with discounted cost
                            double discountedCost = Offsets.calculateOffsetCost(result.getDiscountedEmissionValue());
                            priceLabel.setText(String.format("Discounted Price: $%.2f", roundTo2Decimals(discountedCost)));

                            // Get the selected user for logging
                            String selectedUser = Users_ComboBox.getValue();
                            if (selectedUser != null) {
                                double savings = result.calculateSavings();
                                String logDetails = String.format("User: %s | Original: %.2f kg CO2 | Discount: %d%% | Discounted: %.2f kg CO2 | Savings: $%.2f",
                                    selectedUser, finalEmissionAmount, result.getDiscountPercentage(), result.getDiscountedEmissionValue(), savings);
                                Logger.log("DISCOUNT_APPLIED", logDetails);
                            }

                            // Keep disabled until the user changes inputs.
                            requestDiscountButton.setDisable(true);
                        }
                    },
                    new FootprintDiscountClient.DiscountErrorHandler() {
                        @Override
                        public void handle(String error) {
                            // Error callback
                            discountMessageLabel.setText(error);
                            discountMessageLabel.setStyle(DataOperationIO.InvalidStyle);
                            requestDiscountButton.setDisable(false);
                        }
                    }
                );
            }
        });

        // Purchase Offset button action with validation
        submitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event)  {
            boolean isValid = true;
            String errorMsg = "";

            // Validate user selection
            String selectedUser = Users_ComboBox.getValue();
            if (selectedUser == null) {
                errorMsg += "✗ User must be selected\n";
                Users_ComboBox.setStyle(InvalidBorderStyle);
                isValid = false;
            } else {
                Users_ComboBox.setStyle(PassiveStyle);
            }

            // Validate emission input
            String emissionText = EmissionInput.getText().trim();
            double emissionAmount = 0.0;
            try {
                emissionAmount = Double.parseDouble(emissionText);
                if (emissionAmount < 0) {
                    errorMsg += "✗ Emission Amount cannot be negative\n";
                    EmissionInput.setStyle(InvalidBorderStyle);
                    isValid = false;
                } else if (selectedUser != null) {
                    double userTotalEmissions = GreenPrintGUI.tracker.GetTotalEmissionsForUser(selectedUser);
                    // Compare against the same precision shown in the UI to avoid floating-point mismatch.
                    double allowedMax = roundTo2Decimals(userTotalEmissions);
                    if (emissionAmount > allowedMax) {
                        errorMsg += "✗ Emission Amount cannot exceed Total Emissions\n";
                        EmissionInput.setStyle(InvalidBorderStyle);
                        isValid = false;
                    } else {
                        EmissionInput.setStyle(PassiveStyle);
                    }
                }
            } catch (NumberFormatException e) {
                errorMsg += "✗ Emission Amount must be a valid number\n";
                EmissionInput.setStyle(InvalidBorderStyle);
                isValid = false;
            }

            // Validate payment method
            String paymentMethod = Payment_ComboBox.getValue();
            if (paymentMethod == null) {
                errorMsg += "✗ Payment Method must be selected\n";
                Payment_ComboBox.setStyle(InvalidBorderStyle);
                isValid = false;
            } else {
                Payment_ComboBox.setStyle(PassiveStyle);
            }

            if (!isValid) {
                errorLabel.setText(errorMsg);
                return;
            }

            // All valid - show processing message and simulate delay
            errorLabel.setText("Processing purchase...");
            errorLabel.setStyle(ValidStyle);
            submitButton.setDisable(true);

            // Create 2-second pause transition
            double finalEmissionAmount = emissionAmount;
            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            pause.setOnFinished(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {

                String displayedPriceText = priceLabel.getText();
                boolean isDiscounted = displayedPriceText.startsWith("Discounted Price:");
                double finalCost;
                try {
                    finalCost = Double.parseDouble(displayedPriceText.replaceAll("[^0-9.]", ""));
                } catch (NumberFormatException e) {
                    finalCost = Offsets.calculateOffsetCost(finalEmissionAmount);
                }

                double regularCost = Offsets.calculateOffsetCost(finalEmissionAmount);

                double savings = roundTo2Decimals(regularCost - finalCost);

                if (savings < 0) {
                    savings = 0.0;
                }

                // Log the purchase
                if (isDiscounted) {
                     String logDetails = String.format("User: %s | Amount: %.2f kg CO2 | Cost: $%.2f (Discounted) | Payment: %s",
                selectedUser, finalEmissionAmount, finalCost, paymentMethod);
                Logger.log("OFFSET_PURCHASED", logDetails);
                    
                } else {
                String logDetails = String.format("User: %s | Amount: %.2f kg CO2 | Cost: $%.2f | Payment: %s",
                selectedUser, finalEmissionAmount, finalCost, paymentMethod);
                Logger.log("OFFSET_PURCHASED", logDetails);
                }

                // Refresh the Offset Log tab to show the new purchase
                if (currentOffsetLogTab != null) {
                    currentOffsetLogTab.setContent(CreateOffsetLogTab());
                }

                GreenPrintGUI.refreshDashboard();

                // Show receipt page
                String receipt = Offsets.getOffsetReceipt(finalEmissionAmount, paymentMethod, selectedUser, isDiscounted, finalCost, savings);
                showReceiptPage(receipt);
            }
            });
            pause.play();
        }
    });

        inputBox.getChildren().addAll(Users_ComboBox, userEmissionsLabel, EmissionInput, Payment_ComboBox, priceLabel, requestDiscountButton, discountMessageLabel, errorLabel, submitButton);
        return inputBox;
    }

    /**
     * Displays the receipt page after a successful offset transaction. The receipt includes details of the transaction such as the date, time, user name, emission type, weight of emissions offset, amount offset per kilogram of CO2, total cost of the offset, payment method, and a confirmation status. The receipt is displayed in a styled TextArea within a ScrollPane, and includes a button to navigate back to the offset purchase page for users who wish to purchase more offsets.
     * @param receipt
     */

    public static void showReceiptPage(String receipt) {
        VBox receiptBox = new VBox(20);
        receiptBox.setPadding(new Insets(40));
        receiptBox.setAlignment(Pos.CENTER);

        // Create receipt display
        TextArea receiptArea = new TextArea(receipt);
        receiptArea.setEditable(false);
        receiptArea.setWrapText(true);
        receiptArea.setStyle(
            "-fx-font-family: 'Courier New', monospace; " +
            "-fx-font-size: 14px; " +
            "-fx-background-color: white; " +
            "-fx-border-color: green; " +
            "-fx-border-width: 3px; " +
            "-fx-border-radius: 10px; " +
            "-fx-background-radius: 10px;"
        );
        receiptArea.setPrefRowCount(15);

        // Create back button
        Button backButton = DataOperationIO.CreateButton("Back to Purchase More Offsets"); backButton.setMaxWidth(350);
        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                currentOffsetTab.setContent(createOffsetGUI());
            }
        });

        receiptBox.getChildren().addAll(
            Dashboard.MakeTitleLabel("Transaction Successful!"),
            receiptArea,
            backButton
        );

        ScrollPane scrollPane = new ScrollPane(receiptBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle(ScrollPaneStyle);

        if (currentOffsetTab != null) {
            currentOffsetTab.setContent(scrollPane);
        }
    }

    // Main factory method: Returns ScrollPane ready for TabPane tab.setContent(OffsetTransactionGUI.createOffsetGUI());
    public static ScrollPane createOffsetGUI() {
        VBox content = new VBox(10);
        content.getChildren().addAll(
                Dashboard.MakeTitleLabel("Offset Emissions Transactions"),
                Dashboard.makeLabel(String.format("Total Emissions: \n\n %.2f kg CO2", roundTo2Decimals(GreenPrintGUI.tracker.GetTotalEmissions()))),
                createOffsetInputBox()
                
        );
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle(ScrollPaneStyle);
        return scrollPane;
    }


    /**
     * Creates the offset log tab, which displays a list of all offset purchase transactions logged in the system. The method retrieves log entries related to offset purchases, formats them for display, and adds them to a VBox container. If there are no offset purchases logged, it displays a message indicating that no offsets have been purchased yet. The content is wrapped in a ScrollPane to allow for easy navigation through the log entries.
     * @return a ScrollPane containing the offset purchase log
     */
    public static ScrollPane CreateOffsetLogTab() {
        VBox OffsetsContainer = new VBox(10);
        OffsetsContainer.setPadding(new Insets(20));
        OffsetsContainer.setAlignment(Pos.CENTER);
        ArrayList<String> offsetEntries = Logger.filterOperation("OFFSET_PURCHASED");

        OffsetsContainer.getChildren().add(Dashboard.MakeTitleLabel("Offset Purchase Log"));
        OffsetsContainer.getChildren().add(Dashboard.UserEntryLabel(String.format("%d offsets Purchased", offsetEntries.size())));
        OffsetsContainer.getChildren().add(Dashboard.UserEntryLabel(String.format("Total Offsets Purchased: %.2f KG CO2", roundTo2Decimals(Logger.CalculateTotalOffsetAmount()))));
        

        // Get and display offset purchase logs
        
        if (offsetEntries.isEmpty()) {
            OffsetsContainer.getChildren().add(Dashboard.makeLabel("no Offsets Purchased Yet"));
        } else {
            for (String entry : offsetEntries) {
                OffsetsContainer.getChildren().add(Dashboard.makeLabel(entry));
            }
        }

        ScrollPane scrollPane = new ScrollPane(OffsetsContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle(ScrollPaneStyle);
        return scrollPane;
    }   
    

    /**
     * Creates the main content for the Offset Transactions tab, which includes a title, a label displaying the total emissions, and the input box for purchasing offsets. The content is wrapped in a ScrollPane to allow for easy navigation, especially when the input box contains multiple elements. This method serves as the factory for generating the complete GUI for the Offset Transactions tab in the application.
     * @return a ScrollPane containing the main content for the Offset Transactions tab
     */

    public static TabPane createOffsetTabPane() {
        TabPane OffsetTabPane = new TabPane();
        OffsetTabPane.setStyle(
            GreenPrintGUI.FontFamily +
            "-fx-font-size: 12px; " +
            "-fx-font-weight: bold;"
        );

        OffsetTabPane.setTabMinHeight(40);
        OffsetTabPane.setTabMinWidth(150);

        OffsetTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        OffsetTabPane.setSide(Side.TOP);

        Tab OffsetTab = new Tab();
        OffsetTab.setText("Offset Transactions");
        OffsetTab.setContent(createOffsetGUI());

        // Store reference for navigation
        currentOffsetTab = OffsetTab;


        Tab OffsetLog = new Tab();
        OffsetLog.setText("Offset Log");
        OffsetLog.setContent(CreateOffsetLogTab());

        // Store reference for refreshing
        currentOffsetLogTab = OffsetLog;

        OffsetTabPane.getTabs().addAll(OffsetTab, OffsetLog);

        return OffsetTabPane;

    }
    

    
}