import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;

/**
 * Offsets Panel - Manages carbon offset calculations and purchases
 */
public class OffsetsPanel {
    private FootprintTracker tracker;
    private GreenPrintGUI mainApp;
    private VBox panel;
    private Label offsetRateLabel;
    private Label totalCostLabel;
    private Label netEmissionsLabel;
    private Label totalValueLabel;
    private VBox historyListContainer;
    // Note: totalOffsetsAdded is now stored in GreenPrintGUI as a static field

    public OffsetsPanel(FootprintTracker tracker, GreenPrintGUI mainApp) {
        this.tracker = tracker;
        this.mainApp = mainApp;
        this.panel = new VBox();
        initializePanel();
    }

    private void initializePanel() {
        panel.setStyle("-fx-background-color: #f5f5f5;");
        panel.setPadding(new Insets(20));
        panel.setSpacing(15);

        // Title
        Label title = new Label("Carbon Offset Calculator");
        title.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Separator separator = new Separator();
        separator.setPrefHeight(2);
        separator.setStyle("-fx-padding: 10; -fx-background-color: #2ecc71;");

        ScrollPane scrollPane = new ScrollPane();
        VBox content = new VBox(20);
        content.setPadding(new Insets(10));
        content.setStyle("-fx-background-color: #f5f5f5;");
        content.getChildren().addAll(
            title,
            separator,
            createOffsetInfoSection(),
            createOffsetCalculatorSection(),
            createOffsetHistorySection()
        );

        scrollPane.setContent(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: #f5f5f5;");

        panel.getChildren().add(scrollPane);

        // Set up continuous auto-refresh (every 1 second)
        Timeline refreshTimer = new Timeline(
            new KeyFrame(Duration.millis(1000), event -> refresh())
        );
        refreshTimer.setCycleCount(Timeline.INDEFINITE);
        refreshTimer.play();
    }

    /**
     * Creates the offset information section
     */
    private VBox createOffsetInfoSection() {
        VBox section = new VBox(12);
        section.setPadding(new Insets(15));
        section.setStyle("-fx-background-color: white; -fx-border-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");

        Label title = new Label("ℹ️ What are Carbon Offsets?");
        title.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        TextArea infoText = new TextArea(
            "Carbon offsets are reductions in greenhouse gas emissions made to compensate for emissions produced elsewhere. " +
            "By purchasing offsets, you can neutralize your carbon footprint.\n\n" +
            "How it works:\n" +
            "1. Calculate your total carbon emissions\n" +
            "2. Purchase offsets to neutralize those emissions\n" +
            "3. Support renewable energy and conservation projects\n\n" +
            "Current Offset Rate: " + String.format("$%.3f per kg CO2", Offsets.getOffsetRatePerKg())
        );
        infoText.setEditable(false);
        infoText.setWrapText(true);
        infoText.setPrefHeight(150);
        infoText.setStyle("-fx-font-size: 11; -fx-padding: 10; -fx-control-inner-background: #f9f9f9;");

        section.getChildren().addAll(title, infoText);
        return section;
    }

    /**
     * Creates the offset calculator section
     */
    private VBox createOffsetCalculatorSection() {
        VBox section = new VBox(12);
        section.setPadding(new Insets(15));
        section.setStyle("-fx-background-color: white; -fx-border-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");

        Label title = new Label("🔢 Calculate Offsets");
        title.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        VBox calculatorForm = new VBox(12);
        calculatorForm.setPadding(new Insets(10));

        // Total Emissions
        HBox totalEmissionsBox = new HBox(15);
        totalEmissionsBox.setAlignment(Pos.CENTER_LEFT);

        Label totalLabel = new Label("Total Emissions:");
        totalLabel.setStyle("-fx-font-size: 12; -fx-min-width: 150;");

        totalValueLabel = new Label(String.format("%.2f kg CO2", tracker.GetTotalEmissions()));
        totalValueLabel.setStyle("-fx-font-size: 14; -fx-font-weight: bold; -fx-text-fill: #e74c3c;");

        totalEmissionsBox.getChildren().addAll(totalLabel, totalValueLabel);
        HBox.setHgrow(totalEmissionsBox, Priority.ALWAYS);

        // Net Emissions (after offsets)
        HBox netEmissionsBox = new HBox(15);
        netEmissionsBox.setAlignment(Pos.CENTER_LEFT);

        Label netLabel = new Label("Net Emissions:");
        netLabel.setStyle("-fx-font-size: 12; -fx-min-width: 150;");

        netEmissionsLabel = new Label(String.format("%.2f kg CO2", tracker.GetTotalEmissions() - GreenPrintGUI.getTotalOffsetsAdded()));
        netEmissionsLabel.setStyle("-fx-font-size: 14; -fx-font-weight: bold; -fx-text-fill: #2ecc71;");

        netEmissionsBox.getChildren().addAll(netLabel, netEmissionsLabel);
        HBox.setHgrow(netEmissionsBox, Priority.ALWAYS);

        // Offset Amount Input
        HBox offsetBox = new HBox(15);
        offsetBox.setAlignment(Pos.CENTER_LEFT);

        Label offsetLabel = new Label("Offset Amount (kg):");
        offsetLabel.setStyle("-fx-font-size: 12; -fx-min-width: 150;");

        TextField offsetField = new TextField();
        offsetField.setPrefWidth(200);
        offsetField.setStyle("-fx-padding: 8; -fx-font-size: 12; -fx-border-color: #bdc3c7; -fx-border-radius: 4;");
        offsetField.setPromptText("Enter amount in kg");

        offsetBox.getChildren().addAll(offsetLabel, offsetField);

        // Offset Rate
        HBox rateBox = new HBox(15);
        rateBox.setAlignment(Pos.CENTER_LEFT);

        Label rateLabel = new Label("Offset Rate:");
        rateLabel.setStyle("-fx-font-size: 12; -fx-min-width: 150;");

        offsetRateLabel = new Label(String.format("$%.3f per kg", Offsets.getOffsetRatePerKg()));
        offsetRateLabel.setStyle("-fx-font-size: 12; -fx-font-weight: bold; -fx-text-fill: #2ecc71;");

        rateBox.getChildren().addAll(rateLabel, offsetRateLabel);

        // Calculate Button
        Button calculateBtn = new Button("🧮 Calculate Cost");
        calculateBtn.setStyle("-fx-padding: 10 20 10 20; -fx-font-size: 12; -fx-font-weight: bold; -fx-background-color: #3498db; -fx-text-fill: white; -fx-cursor: hand;");
        calculateBtn.setPrefWidth(200);

        // Cost Result
        totalCostLabel = new Label("Total Cost: $0.00");
        totalCostLabel.setStyle("-fx-font-size: 14; -fx-font-weight: bold; -fx-text-fill: #2ecc71;");

        calculateBtn.setOnAction(e -> {
            try {
                double amount = Double.parseDouble(offsetField.getText());
                if (amount < 0) {
                    showError("Amount must be positive!");
                    return;
                }
                double cost = Offsets.calculateOffsetCost(amount);
                totalCostLabel.setText(String.format("Total Cost: $%.2f", cost));
            } catch (NumberFormatException ex) {
                showError("Please enter a valid number!");
            }
        });

        // Payment Method Selection
        HBox paymentBox = new HBox(15);
        paymentBox.setAlignment(Pos.CENTER_LEFT);

        Label paymentLabel = new Label("Payment Method:");
        paymentLabel.setStyle("-fx-font-size: 12; -fx-min-width: 150;");

        ComboBox<String> paymentCombo = new ComboBox<>();
        paymentCombo.getItems().addAll("Credit Card", "Digital Wallet", "Bank Transfer");
        paymentCombo.setValue("Credit Card");
        paymentCombo.setPrefWidth(200);

        paymentBox.getChildren().addAll(paymentLabel, paymentCombo);

        // Confirm Purchase Button
        Button purchaseBtn = new Button("💰 Purchase Offsets");
        purchaseBtn.setStyle("-fx-padding: 12 30 12 30; -fx-font-size: 13; -fx-font-weight: bold; -fx-background-color: #2ecc71; -fx-text-fill: white; -fx-cursor: hand;");
        purchaseBtn.setPrefWidth(200);

        purchaseBtn.setOnAction(e -> {
            try {
                double amount = Double.parseDouble(offsetField.getText());
                if (amount <= 0) {
                    showError("Amount must be greater than zero!");
                    return;
                }

                String user = getUserName();
                String paymentMethod = paymentCombo.getValue();
                double cost = Offsets.calculateOffsetCost(amount);

                // Log the offset purchase
                String logMessage = String.format("User: %s | Amount: %.2f kg CO2 | Cost: $%.2f | Payment: %s",
                        user, amount, cost, paymentMethod);
                GreenPrintCLI.getLogger().log("OFFSET_PURCHASED", logMessage);

                // Add offset to global total
                GreenPrintGUI.addToTotalOffsets(amount);

                // Update net emissions display
                double netEmissions = Math.max(0, tracker.GetTotalEmissions() - GreenPrintGUI.getTotalOffsetsAdded());
                netEmissionsLabel.setText(String.format("%.2f kg CO2", netEmissions));

                // Show receipt
                String receipt = Offsets.getOffsetReceipt(amount, paymentMethod, user);
                showReceipt(receipt);

                // Clear form
                offsetField.clear();
                totalCostLabel.setText("Total Cost: $0.00");

                // Refresh history display
                updateHistoryDisplay();

            } catch (NumberFormatException ex) {
                showError("Please enter a valid amount!");
            }
        });

        calculatorForm.getChildren().addAll(
            totalEmissionsBox,
            netEmissionsBox,
            new Separator(),
            offsetBox,
            rateBox,
            calculateBtn,
            totalCostLabel,
            new Separator(),
            paymentBox,
            new VBox(),
            purchaseBtn
        );

        VBox.setVgrow(calculatorForm.getChildren().get(calculatorForm.getChildren().size() - 2), Priority.ALWAYS);

        section.getChildren().addAll(title, calculatorForm);
        return section;
    }

    /**
     * Creates the offset history section
     */
    private VBox createOffsetHistorySection() {
        VBox section = new VBox(12);
        section.setPadding(new Insets(15));
        section.setStyle("-fx-background-color: white; -fx-border-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");

        Label title = new Label("📊 Offset Purchase History");
        title.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        historyListContainer = new VBox(8);
        historyListContainer.setPadding(new Insets(10));

        // Get offset purchase logs
        updateHistoryDisplay();

        ScrollPane scrollPane = new ScrollPane(historyListContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(200);
        scrollPane.setStyle("-fx-background-color: #f9f9f9;");

        section.getChildren().addAll(title, scrollPane);
        return section;
    }
    
    private void updateHistoryDisplay() {
        historyListContainer.getChildren().clear();
        
        java.util.ArrayList<String> offsetLogs = GreenPrintCLI.getLogger().filterOperation("OFFSET_PURCHASED");

        if (offsetLogs.isEmpty()) {
            Label noData = new Label("No offset purchases yet");
            noData.setStyle("-fx-text-fill: #999; -fx-font-style: italic;");
            historyListContainer.getChildren().add(noData);
        } else {
            for (String log : offsetLogs) {
                HBox logRow = createLogRow(log);
                historyListContainer.getChildren().add(logRow);
            }
        }
    }

    /**
     * Creates a log entry row
     */
    private HBox createLogRow(String logEntry) {
        HBox row = new HBox(10);
        row.setPadding(new Insets(12));
        row.setStyle("-fx-border-color: #ecf0f1; -fx-border-width: 0 0 1 0; -fx-background-color: #fafafa;");
        row.setAlignment(Pos.TOP_LEFT);

        try {
            // Log format: {OFFSET_PURCHASED} : User: X | Amount: Y kg CO2 | Cost: $Z | Payment: M : [timestamp]
            // Extract the content between first ':' after tag and last ':' before timestamp
            
            String content = logEntry;
            
            // Remove the {OFFSET_PURCHASED} tag if present
            if (content.contains("{OFFSET_PURCHASED}")) {
                content = content.substring(content.indexOf("{OFFSET_PURCHASED}") + "{OFFSET_PURCHASED}".length()).trim();
                if (content.startsWith(":")) {
                    content = content.substring(1).trim();
                }
            }
            
            // Remove the timestamp part (last colon and everything after it)
            if (content.lastIndexOf(":") > 0) {
                // Check if the last part looks like a timestamp
                String lastPart = content.substring(content.lastIndexOf(":") + 1).trim();
                if (lastPart.startsWith("[") && lastPart.endsWith("]")) {
                    content = content.substring(0, content.lastIndexOf(":")).trim();
                }
            }
            
            // Now parse the pipe-separated values
            String[] details = content.split("\\|");
            
            VBox detailsBox = new VBox(3);
            detailsBox.setStyle("-fx-padding: 5;");
            
            for (String detail : details) {
                String trimmed = detail.trim();
                if (!trimmed.isEmpty()) {
                    Label detailLabel = new Label(trimmed);
                    detailLabel.setStyle("-fx-font-size: 11; -fx-text-fill: #333; -fx-wrap-text: true;");
                    detailsBox.getChildren().add(detailLabel);
                }
            }
            
            row.getChildren().add(detailsBox);
            HBox.setHgrow(detailsBox, Priority.ALWAYS);
        } catch (Exception e) {
            // Fallback: just display the raw entry with better formatting
            Label logLabel = new Label(logEntry);
            logLabel.setStyle("-fx-font-size: 11; -fx-text-fill: #555; -fx-wrap-text: true;");
            row.getChildren().add(logLabel);
            HBox.setHgrow(logLabel, Priority.ALWAYS);
        }

        return row;
    }

    /**
     * Gets the user name - in a real app this would come from the user session
     */
    private String getUserName() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Enter User Name");
        dialog.setHeaderText(null);
        dialog.setContentText("User Name:");
        dialog.getDialogPane().setStyle("-fx-font-size: 12;");

        java.util.Optional<String> result = dialog.showAndWait();
        return result.orElse("anonymous").toLowerCase();
    }

    private void showReceipt(String receipt) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Offset Receipt");
        alert.setHeaderText(null);
        alert.getDialogPane().setPrefWidth(500);

        TextArea textArea = new TextArea(receipt);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 11;");

        alert.getDialogPane().setContent(textArea);
        alert.showAndWait();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public VBox getPanel() {
        return panel;
    }

    public void refresh() {
        // Update the total and net emissions labels
        if (totalValueLabel != null) {
            totalValueLabel.setText(String.format("%.2f kg CO2", tracker.GetTotalEmissions()));
        }
        if (netEmissionsLabel != null) {
            double netEmissions = Math.max(0, tracker.GetTotalEmissions() - GreenPrintGUI.getTotalOffsetsAdded());
            netEmissionsLabel.setText(String.format("%.2f kg CO2", netEmissions));
        }
        
        // Update history display
        if (historyListContainer != null) {
            updateHistoryDisplay();
        }
    }
}
