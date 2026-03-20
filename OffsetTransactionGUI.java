import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.*;
import java.util.PriorityQueue;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.logging.Handler;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.TextAlignment;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.input.MouseEvent;
import javafx.geometry.Side;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

public class OffsetTransactionGUI {  // No longer extends Application

    static Tab currentOffsetTab;
    static Tab currentOffsetLogTab;
    static String PassiveStyle = DataOperationIO.PassiveStyle;
    static String ValidStyle = DataOperationIO.ValidStyle;
    static String InvalidStyle = DataOperationIO.InvalidStyle;
    static String InvalidBorderStyle = DataOperationIO.InvalidBorderStyle;
    static String ScrollPaneStyle = "-fx-background: white; -fx-background-color: white;";

   


    public static VBox createOffsetInputBox() {
        VBox inputBox = new VBox(10);
        inputBox.setPadding(new Insets(20));
        inputBox.setAlignment(Pos.CENTER);
        inputBox.setSpacing(10);
        ArrayList<String> users = GreenPrintCLI.tracker.getUniqueUsers();

        // Create UI elements
        ComboBox<String> Users_ComboBox = DataOperationIO.CreateComboBox("Select User", users.toArray(new String[0]));
        Label userEmissionsLabel = Dashboard.makeLabel("Select a user to view emissions");
        TextField EmissionInput = DataOperationIO.CreateTextField("Enter Emission Amount to Offset (kg CO2)");
        ComboBox<String> Payment_ComboBox = DataOperationIO.CreateComboBox("Payment Method", "Credit Card", "Digital Wallet", "Campus Card");
        Label priceLabel = Dashboard.UserEntryLabel("Calculated Price: $0.00");
        Label errorLabel = DataOperationIO.ErrorLabel("");
        Button submitButton = DataOperationIO.CreateButton("Purchase Offset");

        // Update label when user is selected
        Users_ComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                double totalEmissions = GreenPrintCLI.tracker.GetTotalEmissionsForUser(newValue);
                userEmissionsLabel.setText(String.format("%s Total Emissions: \n\n %.2f kg CO2", newValue, totalEmissions));
            } else {
                userEmissionsLabel.setText("Select a user to view emissions");
            }
        });

        // Update price label when emission amount changes
        EmissionInput.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if (newValue.trim().isEmpty()) {
                    priceLabel.setText("Calculated Price: $0.00");
                } else {
                    double amount = Double.parseDouble(newValue.trim());
                    double cost = Offsets.calculateOffsetCost(amount);
                    priceLabel.setText(String.format("Calculated Price: $%.2f", cost));
                }
            } catch (NumberFormatException e) {
                priceLabel.setText("Calculated Price: $0.00");
            }
        });

        // Purchase Offset button action with validation
        submitButton.setOnAction(event -> {
            boolean isValid = true;
            StringBuilder errorMsg = new StringBuilder();

            // Validate user selection
            String selectedUser = Users_ComboBox.getValue();
            if (selectedUser == null) {
                errorMsg.append("✗ User must be selected\n");
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
                    errorMsg.append("✗ Emission Amount cannot be negative\n");
                    EmissionInput.setStyle(InvalidBorderStyle);
                    isValid = false;
                } else if (selectedUser != null) {
                    double userTotalEmissions = GreenPrintCLI.tracker.GetTotalEmissionsForUser(selectedUser);
                    if (emissionAmount > userTotalEmissions) {
                        errorMsg.append("✗ Emission Amount cannot exceed Total Emissions\n");
                        EmissionInput.setStyle(InvalidBorderStyle);
                        isValid = false;
                    } else {
                        EmissionInput.setStyle(PassiveStyle);
                    }
                }
            } catch (NumberFormatException e) {
                errorMsg.append("✗ Emission Amount must be a valid number\n");
                EmissionInput.setStyle(InvalidBorderStyle);
                isValid = false;
            }

            // Validate payment method
            String paymentMethod = Payment_ComboBox.getValue();
            if (paymentMethod == null) {
                errorMsg.append("✗ Payment Method must be selected\n");
                Payment_ComboBox.setStyle(InvalidBorderStyle);
                isValid = false;
            } else {
                Payment_ComboBox.setStyle(PassiveStyle);
            }

            if (!isValid) {
                errorLabel.setText(errorMsg.toString());
                return;
            }

            // All valid - show processing message and simulate delay
            errorLabel.setText("Processing purchase...");
            errorLabel.setStyle(ValidStyle);
            submitButton.setDisable(true);

            // Create 2-second pause transition
            double finalEmissionAmount = emissionAmount;
            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            pause.setOnFinished(e -> {
                // Log the purchase
                Logger logger = new Logger();
                String logDetails = String.format("User: %s | Amount: %.2f kg CO2 | Cost: $%.2f | Payment: %s",
                    selectedUser, finalEmissionAmount, Offsets.calculateOffsetCost(finalEmissionAmount), paymentMethod);
                logger.log("OFFSET_PURCHASED", logDetails);

                // Refresh the Offset Log tab to show the new purchase
                if (currentOffsetLogTab != null) {
                    currentOffsetLogTab.setContent(CreateOffsetLogTab());
                }

                // Show receipt page
                String receipt = Offsets.getOffsetReceipt(finalEmissionAmount, paymentMethod, selectedUser);
                showReceiptPage(receipt);
            });
            pause.play();
        });

        inputBox.getChildren().addAll(Users_ComboBox, userEmissionsLabel, EmissionInput, Payment_ComboBox, priceLabel, errorLabel, submitButton);
        return inputBox;
    }

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
        backButton.setOnAction(e -> {
            currentOffsetTab.setContent(createOffsetGUI());
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
                Dashboard.makeLabel(String.format("Total Emissions: \n\n %.2f kg CO2", GreenPrintCLI.tracker.GetTotalEmissions())),
                createOffsetInputBox()
                
        );
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle(ScrollPaneStyle);
        return scrollPane;
    }



    public static ScrollPane CreateOffsetLogTab() {
        VBox OffsetsContainer = new VBox(10);
        OffsetsContainer.setPadding(new Insets(20));
        OffsetsContainer.setAlignment(Pos.CENTER);

        OffsetsContainer.getChildren().add(Dashboard.MakeTitleLabel("Offset Purchase Log"));

        // Get and display offset purchase logs
        for (String entry : Logger.filterOperation("OFFSET_PURCHASED")) {
            OffsetsContainer.getChildren().add(Dashboard.makeLabel(entry));
        }

        ScrollPane scrollPane = new ScrollPane(OffsetsContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle(ScrollPaneStyle);
        return scrollPane;
    }   
    



    public static TabPane createOffsetTabPane() {
        TabPane OffsetTabPane = new TabPane();
        OffsetTabPane.setStyle(
            "-fx-font-family: 'Segoe UI', Helvetica, Arial, sans-serif; " +
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
