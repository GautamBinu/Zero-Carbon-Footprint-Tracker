import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;


import java.util.ArrayList;

/**
 * Dashboard Panel - Displays key statistics and metrics
 */
public class DashboardPanel {
    private FootprintTracker tracker;
    private GreenPrintGUI mainApp;
    private VBox panel;

    // Statistics cards
    private Label totalEmissionsLabel;
    private Label netEmissionsLabel;
    private Label totalEntriesLabel;
    private Label uniqueUsersLabel;
    private Label highestEmitterLabel;

    // Live Emission Dashboard components
    private FlowPane emissionFlowPane;

    public DashboardPanel(FootprintTracker tracker, GreenPrintGUI mainApp) {
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
        Label title = new Label("Dashboard Overview");
        title.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Separator separator = new Separator();
        separator.setPrefHeight(2);
        separator.setStyle("-fx-padding: 10; -fx-background-color: #3498db;");

        // Statistics Grid
        HBox statsGrid = createStatisticsGrid();

        // Live Emission Dashboard
        VBox liveEmissionSection = createLiveEmissionDashboard();

        // Emissions Breakdown
        VBox breakdownSection = createBreakdownSection();

        // User Statistics
        VBox userStatsSection = createUserStatisticsSection();

        ScrollPane scrollPane = new ScrollPane();
        VBox content = new VBox(20);
        content.setPadding(new Insets(10));
        content.getChildren().addAll(
            title,
            separator,
            statsGrid,
            liveEmissionSection,
            breakdownSection,
            userStatsSection
        );
        content.setStyle("-fx-background-color: #f5f5f5;");
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
     * Creates the main statistics grid
     */
    private HBox createStatisticsGrid() {
        HBox grid = new HBox(15);
        grid.setPadding(new Insets(15));
        grid.setSpacing(15);

        // Combined Emissions Card (Total & Net)
        VBox card1 = createEmissionsComparisonCard();

        // Total Entries Card
        VBox card2 = createStatCard(
            "📋 Total Entries",
            () -> String.valueOf(tracker.getTotalEntries()),
            "#3498db"
        );

        // Unique Users Card
        VBox card3 = createStatCard(
            "👥 Unique Users",
            () -> String.valueOf(tracker.getUniqueUsers().size()),
            "#2ecc71"
        );

        // Highest Emitter Card
        VBox card4 = createStatCard(
            "⚠️ Top Emitter",
            () -> {
                String highest = tracker.getHighestTotalEmissionUser();
                return highest.isEmpty() ? "N/A" : highest.split(" ")[0];
            },
            "#f39c12"
        );

        totalEntriesLabel = (Label) card2.getChildren().get(1);
        uniqueUsersLabel = (Label) card3.getChildren().get(1);
        highestEmitterLabel = (Label) card4.getChildren().get(1);

        grid.getChildren().addAll(card1, card2, card3, card4);
        HBox.setHgrow(card1, Priority.ALWAYS);
        HBox.setHgrow(card2, Priority.ALWAYS);
        HBox.setHgrow(card3, Priority.ALWAYS);
        HBox.setHgrow(card4, Priority.ALWAYS);

        return grid;
    }

    /**
     * Creates a combined emissions card showing both total and net emissions
     */
    private VBox createEmissionsComparisonCard() {
        VBox card = new VBox(15);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: white; -fx-border-color: #2ecc71; -fx-border-width: 3; -fx-border-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        
        Label titleLabel = new Label("💨 Emissions Summary");
        titleLabel.setStyle("-fx-font-size: 14; -fx-font-weight: bold; -fx-text-fill: #555;");
        
        // Total Emissions Section
        VBox totalSection = new VBox(5);
        totalSection.setStyle("-fx-padding: 10; -fx-background-color: #f8f9fa; -fx-border-radius: 5;");
        Label totalTitleLabel = new Label("Total Emissions");
        totalTitleLabel.setStyle("-fx-font-size: 12; -fx-text-fill: #7f8c8d;");
        totalEmissionsLabel = new Label("0.00 kg CO2");
        totalEmissionsLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: #e74c3c;");
        totalSection.getChildren().addAll(totalTitleLabel, totalEmissionsLabel);
        
        // Net Emissions Section
        VBox netSection = new VBox(5);
        netSection.setStyle("-fx-padding: 10; -fx-background-color: #f0fdf4; -fx-border-radius: 5;");
        Label netTitleLabel = new Label("Net Emissions");
        netTitleLabel.setStyle("-fx-font-size: 12; -fx-text-fill: #7f8c8d;");
        netEmissionsLabel = new Label("0.00 kg CO2");
        netEmissionsLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: #2ecc71;");
        netSection.getChildren().addAll(netTitleLabel, netEmissionsLabel);
        
        // Create a horizontal layout for emissions sections
        HBox emissionsLayout = new HBox(10);
        emissionsLayout.getChildren().addAll(totalSection, netSection);
        HBox.setHgrow(totalSection, Priority.ALWAYS);
        HBox.setHgrow(netSection, Priority.ALWAYS);
        
        card.getChildren().addAll(titleLabel, emissionsLayout);
        return card;
    }

    /**
     * Creates a single statistics card
     */
    private VBox createStatCard(String title, java.util.function.Supplier<String> valueSupplier, String color) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: white; -fx-border-color: " + color + "; -fx-border-width: 3; -fx-border-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        card.setAlignment(Pos.CENTER);

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 14; -fx-font-weight: bold; -fx-text-fill: #555;");

        Label valueLabel = new Label(valueSupplier.get());
        valueLabel.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-text-fill: " + color + ";");

        card.getChildren().addAll(titleLabel, valueLabel);
        return card;
    }

    /**
     * Creates the Live Emission Dashboard with color-coded emission entries
     */
    private VBox createLiveEmissionDashboard() {
        VBox section = new VBox(10);
        section.setPadding(new Insets(15));
        section.setStyle("-fx-background-color: white; -fx-border-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");

        Label title = new Label("🔴 Live Emission Dashboard");
        title.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Create FlowPane for emission entries
        emissionFlowPane = new FlowPane();
        emissionFlowPane.setHgap(10);
        emissionFlowPane.setVgap(10);
        emissionFlowPane.setPadding(new Insets(10));
        emissionFlowPane.setStyle("-fx-background-color: #fafafa;");

        // Create legend
        HBox legend = createLegend();

        // Detail panel removed - details now shown in popup dialog when emission is clicked

        section.getChildren().addAll(title, legend, emissionFlowPane);
        return section;
    }

    /**
     * Creates the legend for color coding
     */
    private HBox createLegend() {
        HBox legend = new HBox(20);
        legend.setPadding(new Insets(10));
        legend.setAlignment(Pos.CENTER_LEFT);
        legend.setStyle("-fx-background-color: #f5f5f5; -fx-border-radius: 4;");

        // Green legend
        VBox greenBox = new VBox(3);
        greenBox.setAlignment(Pos.CENTER);
        Rectangle greenRect = new Rectangle(20, 20);
        greenRect.setFill(Color.web("#2ecc71"));
        Label greenLabel = new Label("Low\n(< 1.0 kg)");
        greenLabel.setStyle("-fx-font-size: 10; -fx-text-alignment: center;");
        greenBox.getChildren().addAll(greenRect, greenLabel);

        // Yellow legend
        VBox yellowBox = new VBox(3);
        yellowBox.setAlignment(Pos.CENTER);
        Rectangle yellowRect = new Rectangle(20, 20);
        yellowRect.setFill(Color.web("#f39c12"));
        Label yellowLabel = new Label("Medium\n(1.0-3.0 kg)");
        yellowLabel.setStyle("-fx-font-size: 10; -fx-text-alignment: center;");
        yellowBox.getChildren().addAll(yellowRect, yellowLabel);

        // Red legend
        VBox redBox = new VBox(3);
        redBox.setAlignment(Pos.CENTER);
        Rectangle redRect = new Rectangle(20, 20);
        redRect.setFill(Color.web("#e74c3c"));
        Label redLabel = new Label("High\n(> 3.0 kg)");
        redLabel.setStyle("-fx-font-size: 10; -fx-text-alignment: center;");
        redBox.getChildren().addAll(redRect, redLabel);

        legend.getChildren().addAll(greenBox, yellowBox, redBox);
        return legend;
    }

    /**
     * Creates a color-coded emission element
     */
    private VBox createEmissionElement(EmissionSource emission) {
        double emissionValue = emission.calculateEmission();
        String color;

        if (emissionValue < 1.0) {
            color = "#2ecc71"; // Green - Low
        } else if (emissionValue <= 3.0) {
            color = "#f39c12"; // Yellow - Medium
        } else {
            color = "#e74c3c"; // Red - High
        }

        VBox element = new VBox();
        element.setPrefSize(120, 120);
        element.setAlignment(Pos.CENTER);
        element.setStyle("-fx-background-color: " + color + "; -fx-border-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 2);");
        element.setCursor(javafx.scene.Cursor.HAND);

        Label idLabel = new Label(emission.getSourceID());
        idLabel.setStyle("-fx-font-size: 14; -fx-font-weight: bold; -fx-text-fill: white; -fx-text-alignment: center;");

        element.getChildren().add(idLabel);

        // Add hover and click event handlers
        element.setOnMouseEntered(event -> {
            element.setStyle("-fx-background-color: " + color + "; -fx-border-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 10, 0, 0, 4); -fx-scale-x: 1.05; -fx-scale-y: 1.05;");
        });

        element.setOnMouseExited(event -> {
            element.setStyle("-fx-background-color: " + color + "; -fx-border-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 2);");
        });

        element.setOnMouseClicked(event -> {
            showEmissionDetails(emission);
        });

        return element;
    }

    /**
     * Displays detailed information about an emission in a pop-up dialog
     */
    private void showEmissionDetails(EmissionSource emission) {
        StringBuilder details = new StringBuilder();
        details.append("📌 EMISSION DETAILS\n");
        details.append("═══════════════════════════════════════\n\n");
        
        // Basic Information
        details.append("🔹 BASIC INFORMATION\n");
        details.append("  ID: ").append(emission.getSourceID()).append("\n");
        details.append("  Category: ").append(emission.getCategory()).append("\n");
        details.append("  User: ").append(emission.getUserName()).append("\n");
        details.append("  Date: ").append(emission.getDate()).append("\n\n");
        
        // Emission Calculation
        double emissionAmount = emission.calculateEmission();
        details.append("🔹 EMISSION CALCULATION\n");
        details.append("  Total CO₂: ").append(String.format("%.4f kg", emissionAmount)).append("\n");
        details.append("  Percentage of Daily Average: ").append(String.format("%.2f%%", getPercentageOfDailyAverage(emissionAmount))).append("\n\n");

        // Add type-specific details
        if (emission instanceof EnergyEmission) {
            EnergyEmission energy = (EnergyEmission) emission;
            details.append("⚡ ENERGY DETAILS\n");
            details.append("  Energy Source: ").append(energy.getEnergySource()).append("\n");
            details.append("  kWh Used: ").append(String.format("%.2f", energy.getKwhused())).append("\n");
            details.append("  Carbon Coefficient: ").append(String.format("%.2f kg CO₂/kWh", getCarbonCoefficient(energy.getEnergySource()))).append("\n");
            details.append("  Calculation: ").append(String.format("%.2f kWh × %.2f = %.4f kg CO₂\n", 
                energy.getKwhused(), getCarbonCoefficient(energy.getEnergySource()), emissionAmount));
        } else if (emission instanceof FoodEmission) {
            FoodEmission food = (FoodEmission) emission;
            details.append("🍔 FOOD DETAILS\n");
            details.append("  Meal Type: ").append(food.getMealType()).append("\n");
            details.append("  Number of Meals: ").append(food.getNumberOfMeals()).append("\n");
            details.append("  Emission per Meal: ").append(String.format("%.4f kg CO₂", emissionAmount / food.getNumberOfMeals())).append("\n");
            details.append("  Calculation: ").append(String.format("%d meals × %.4f = %.4f kg CO₂\n", 
                food.getNumberOfMeals(), emissionAmount / food.getNumberOfMeals(), emissionAmount));
        } else if (emission instanceof TransportationEmission) {
            TransportationEmission transport = (TransportationEmission) emission;
            details.append("🚗 TRANSPORTATION DETAILS\n");
            details.append("  Vehicle Type: ").append(transport.getVehicleType()).append("\n");
            details.append("  Distance: ").append(String.format("%.2f km", transport.getDistanceKM())).append("\n");
            details.append("  Carbon Intensity: ").append(String.format("%.2f kg CO₂/km", getCarbonIntensity(transport.getVehicleType()))).append("\n");
            details.append("  Calculation: ").append(String.format("%.2f km × %.2f = %.4f kg CO₂\n", 
                transport.getDistanceKM(), getCarbonIntensity(transport.getVehicleType()), emissionAmount));
        }
        
        // Offset Impact Information
        details.append("\n🔹 OFFSET IMPACT\n");
        double totalOffsets = GreenPrintGUI.getTotalOffsetsAdded();
        if (totalOffsets > 0) {
            double userTotal = tracker.GetTotalEmissionsForUser(emission.getUserName());
            double userOffsetImpact = Math.min(totalOffsets, userTotal);
            double userNetEmissions = Math.max(0, userTotal - userOffsetImpact);
            details.append("  Total Offsets Purchased: ").append(String.format("%.2f kg CO₂\n", totalOffsets));
            details.append("  Your User's Total Emissions: ").append(String.format("%.2f kg CO₂\n", userTotal));
            details.append("  Your User's Net Emissions: ").append(String.format("%.2f kg CO₂\n", userNetEmissions));
            if (userNetEmissions == 0 && userTotal > 0) {
                details.append("  Status: ✅ CARBON NEUTRAL\n");
            } else if (userNetEmissions > 0) {
                details.append("  Status: 🟡 Partially offset\n");
            }
        } else {
            details.append("  No offsets purchased yet.\n");
            details.append("  Visit the Carbon Offsets tab to purchase offsets and reduce your carbon footprint!\n");
        }
        
        details.append("\n═══════════════════════════════════════");
        
        // Create and show popup dialog
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle("Emission Details - " + emission.getSourceID());
        dialog.setHeaderText(null);
        dialog.getDialogPane().setPrefWidth(600);
        dialog.getDialogPane().setPrefHeight(500);
        
        TextArea textArea = new TextArea(details.toString());
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 11;");
        textArea.setPrefRowCount(25);
        
        dialog.getDialogPane().setContent(textArea);
        dialog.showAndWait();
    }

    /**
     * Gets the carbon coefficient for energy sources
     */
    private double getCarbonCoefficient(String energySource) {
        switch (energySource.toLowerCase()) {
            case "coal": return 0.95;
            case "natural gas": return 0.45;
            case "nuclear": return 0.012;
            case "wind": return 0.015;
            case "solar": return 0.048;
            case "hydroelectric": return 0.024;
            default: return 0.5;  // Average grid mix
        }
    }
    
    /**
     * Gets the carbon intensity for transportation
     */
    private double getCarbonIntensity(String vehicleType) {
        switch (vehicleType.toLowerCase()) {
            case "car": return 0.20;
            case "suv": return 0.26;
            case "truck": return 0.35;
            case "bus": return 0.08;
            case "train": return 0.04;
            case "airplane": return 0.25;
            case "electric car": return 0.05;
            default: return 0.20;
        }
    }
    
    /**
     * Gets the percentage of daily average emissions
     */
    private double getPercentageOfDailyAverage(double emission) {
        double dailyAverage = tracker.GetTotalEmissions() / Math.max(1, tracker.getTotalEntries());
        return dailyAverage > 0 ? (emission / dailyAverage) * 100 : 0;
    }

    /**
     * Creates a rectangle shape for UI
     */
    private class Rectangle extends javafx.scene.shape.Rectangle {
        public Rectangle(double width, double height) {
            super(width, height);
        }
    }

    /**
     * Creates emissions breakdown by type
     */
    private VBox createBreakdownSection() {
        VBox section = new VBox(10);
        section.setPadding(new Insets(15));
        section.setStyle("-fx-background-color: white; -fx-border-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");

        Label title = new Label("Emissions by Type");
        title.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        HBox breakdown = new HBox(20);
        breakdown.setPadding(new Insets(10));
        breakdown.setSpacing(20);

        // Energy
        double energyTotal = calculateEmissionsByType(EnergyEmission.class);
        VBox energyBox = createTypeBox("⚡ Energy", String.format("%.2f kg CO2", energyTotal), "#f39c12");

        // Food
        double foodTotal = calculateEmissionsByType(FoodEmission.class);
        VBox foodBox = createTypeBox("🍔 Food", String.format("%.2f kg CO2", foodTotal), "#e74c3c");

        // Transportation
        double transportTotal = calculateEmissionsByType(TransportationEmission.class);
        VBox transportBox = createTypeBox("🚗 Transport", String.format("%.2f kg CO2", transportTotal), "#3498db");

        breakdown.getChildren().addAll(energyBox, foodBox, transportBox);
        HBox.setHgrow(energyBox, Priority.ALWAYS);
        HBox.setHgrow(foodBox, Priority.ALWAYS);
        HBox.setHgrow(transportBox, Priority.ALWAYS);

        section.getChildren().addAll(title, breakdown);
        return section;
    }

    /**
     * Creates user statistics section
     */
    private VBox createUserStatisticsSection() {
        VBox section = new VBox(10);
        section.setPadding(new Insets(15));
        section.setStyle("-fx-background-color: white; -fx-border-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");

        Label title = new Label("User Emissions Summary");
        title.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        VBox userList = new VBox(8);
        userList.setPadding(new Insets(10));

        ArrayList<String> users = tracker.getUniqueUsers();
        if (users.isEmpty()) {
            Label noData = new Label("No emissions tracked yet");
            noData.setStyle("-fx-text-fill: #999; -fx-font-style: italic;");
            userList.getChildren().add(noData);
        } else {
            double totalOffsets = GreenPrintGUI.getTotalOffsetsAdded();
            for (String user : users) {
                double userEmissions = tracker.GetTotalEmissionsForUser(user);
                HBox userRow = createUserRow(user, userEmissions, totalOffsets);
                userList.getChildren().add(userRow);
            }
        }

        section.getChildren().addAll(title, userList);
        return section;
    }

    /**
     * Creates a single user row for statistics
     */
    private HBox createUserRow(String userName, double emissions, double totalOffsets) {
        HBox row = new HBox(15);
        row.setPadding(new Insets(10));
        row.setStyle("-fx-border-color: #ecf0f1; -fx-border-width: 0 0 1 0;");
        row.setAlignment(Pos.CENTER_LEFT);

        Label userLabel = new Label(userName);
        userLabel.setStyle("-fx-font-size: 12; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        userLabel.setMinWidth(100);

        // Calculate net emissions after offsets
        double netEmissions = Math.max(0, emissions - totalOffsets);
        String emissionText;
        String emissionColor;
        
        if (netEmissions == 0 && emissions > 0 && totalOffsets > 0) {
            emissionText = "✅ NEUTRAL";
            emissionColor = "#2ecc71";
        } else if (netEmissions == 0) {
            emissionText = String.format("%.2f kg CO2", emissions);
            emissionColor = "#e74c3c";
        } else if (totalOffsets > 0) {
            emissionText = String.format("%.2f kg CO2 (%.2f offset)", netEmissions, totalOffsets);
            emissionColor = "#f39c12";
        } else {
            emissionText = String.format("%.2f kg CO2", emissions);
            emissionColor = "#e74c3c";
        }

        Label emissionsLabel = new Label(emissionText);
        emissionsLabel.setStyle("-fx-font-size: 12; -fx-text-fill: " + emissionColor + "; -fx-font-weight: bold;");

        row.getChildren().addAll(userLabel, new Separator(), emissionsLabel);
        HBox.setHgrow(row.getChildren().get(1), Priority.ALWAYS);

        return row;
    }

    /**
     * Creates a type statistics box
     */
    private VBox createTypeBox(String label, String value, String color) {
        VBox box = new VBox(8);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(15));
        box.setStyle("-fx-background-color: rgba(255,255,255,0.7); -fx-border-color: " + color + "; -fx-border-width: 2; -fx-border-radius: 8;");

        Label typeLabel = new Label(label);
        typeLabel.setStyle("-fx-font-size: 13; -fx-font-weight: bold; -fx-text-fill: #555;");

        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-font-size: 14; -fx-font-weight: bold; -fx-text-fill: " + color + ";");

        box.getChildren().addAll(typeLabel, valueLabel);
        return box;
    }

    /**
     * Calculates total emissions by emission type
     */
    private double calculateEmissionsByType(Class<?> type) {
        double total = 0;
        for (EmissionSource entry : tracker.getEmissions()) {
            if (type.isInstance(entry)) {
                total += entry.calculateEmission();
            }
        }
        return total;
    }

    public VBox getPanel() {
        return panel;
    }

    public void refresh() {
        // Update all statistics with latest data
        if (totalEmissionsLabel != null) {
            double totalEmissions = tracker.GetTotalEmissions();
            totalEmissionsLabel.setText(String.format("%.2f kg CO2", totalEmissions));
        }
        
        if (netEmissionsLabel != null) {
            double total = tracker.GetTotalEmissions();
            double offsets = GreenPrintGUI.getTotalOffsetsAdded();
            double net = Math.max(0, total - offsets);
            
            if (net == 0 && offsets > 0) {
                netEmissionsLabel.setText("✅ NEUTRAL");
                netEmissionsLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: #27ae60;");
            } else {
                netEmissionsLabel.setText(String.format("%.2f kg CO2", net));
                netEmissionsLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: #2ecc71;");
            }
        }
        
        if (totalEntriesLabel != null) {
            totalEntriesLabel.setText(String.valueOf(tracker.getTotalEntries()));
        }
        if (uniqueUsersLabel != null) {
            uniqueUsersLabel.setText(String.valueOf(tracker.getUniqueUsers().size()));
        }
        if (highestEmitterLabel != null) {
            String highest = tracker.getHighestTotalEmissionUser();
            highestEmitterLabel.setText(highest.isEmpty() ? "N/A" : highest.split(" ")[0]);
        }

        // Update Live Emission Dashboard
        if (emissionFlowPane != null) {
            emissionFlowPane.getChildren().clear();
            for (EmissionSource emission : tracker.getEmissions()) {
                VBox element = createEmissionElement(emission);
                emissionFlowPane.getChildren().add(element);
            }
        }
    }
}
