import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;

/**
 * Reports Panel - Displays detailed analytics and reports
 */
public class ReportsPanel {
    private FootprintTracker tracker;
    private GreenPrintGUI mainApp;
    private VBox panel;
    private ScrollPane scrollPane;
    private VBox content;

    public ReportsPanel(FootprintTracker tracker, GreenPrintGUI mainApp) {
        this.tracker = tracker;
        this.mainApp = mainApp;
        this.panel = new VBox();
        initializePanel();
    }

    private void initializePanel() {
        panel.setStyle("-fx-background-color: #f5f5f5;");
        panel.setPadding(new Insets(20));
        panel.setSpacing(15);

        // ScrollPane reference for refresh
        scrollPane = new ScrollPane();
        content = new VBox(20);
        content.setPadding(new Insets(10));
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
        
        // Initial refresh
        refreshContent();
    }
    
    private void refreshContent() {
        content.getChildren().clear();
        
        Label title = new Label("Reports & Analytics");
        title.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Separator separator = new Separator();
        separator.setPrefHeight(2);
        separator.setStyle("-fx-padding: 10; -fx-background-color: #3498db;");
        
        content.getChildren().addAll(
            title,
            separator,
            createEmissionsByTypeReport(),
            createEmissionsByUserReport(),
            createDetailedReport()
        );
    }

    /**
     * Creates emissions breakdown by type report
     */
    private VBox createEmissionsByTypeReport() {
        VBox report = new VBox(12);
        report.setPadding(new Insets(15));
        report.setStyle("-fx-background-color: white; -fx-border-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");

        Label reportTitle = new Label("Emissions by Type");
        reportTitle.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Calculate totals
        double energyTotal = 0;
        double foodTotal = 0;
        double transportTotal = 0;

        for (EmissionSource entry : tracker.getEmissions()) {
            if (entry instanceof EnergyEmission) {
                energyTotal += entry.calculateEmission();
            } else if (entry instanceof FoodEmission) {
                foodTotal += entry.calculateEmission();
            } else if (entry instanceof TransportationEmission) {
                transportTotal += entry.calculateEmission();
            }
        }

        double grandTotal = energyTotal + foodTotal + transportTotal;

        VBox typeBreakdown = new VBox(10);
        typeBreakdown.setPadding(new Insets(10));

        if (grandTotal > 0) {
            // Energy
            HBox energyRow = createProgressRow("⚡ Energy", energyTotal, grandTotal, "#f39c12");
            typeBreakdown.getChildren().add(energyRow);

            // Food
            HBox foodRow = createProgressRow("🍔 Food", foodTotal, grandTotal, "#e74c3c");
            typeBreakdown.getChildren().add(foodRow);

            // Transportation
            HBox transportRow = createProgressRow("🚗 Transportation", transportTotal, grandTotal, "#3498db");
            typeBreakdown.getChildren().add(transportRow);

            // Summary
            HBox summaryBox = new HBox(15);
            summaryBox.setPadding(new Insets(10));
            summaryBox.setStyle("-fx-border-color: #ecf0f1; -fx-border-width: 1; -fx-border-radius: 4;");

            Label summaryLabel = new Label("Grand Total:");
            summaryLabel.setStyle("-fx-font-size: 13; -fx-font-weight: bold;");

            Label totalLabel = new Label(String.format("%.2f kg CO2", grandTotal));
            totalLabel.setStyle("-fx-font-size: 14; -fx-font-weight: bold; -fx-text-fill: #e74c3c;");

            summaryBox.getChildren().addAll(summaryLabel, new Separator(), totalLabel);
            HBox.setHgrow(summaryBox.getChildren().get(1), Priority.ALWAYS);

            typeBreakdown.getChildren().add(summaryBox);
        } else {
            Label noData = new Label("No emissions data available");
            noData.setStyle("-fx-text-fill: #999; -fx-font-style: italic;");
            typeBreakdown.getChildren().add(noData);
        }

        report.getChildren().addAll(reportTitle, new Separator(), typeBreakdown);
        return report;
    }

    /**
     * Creates emissions breakdown by user report
     */
    private VBox createEmissionsByUserReport() {
        VBox report = new VBox(12);
        report.setPadding(new Insets(15));
        report.setStyle("-fx-background-color: white; -fx-border-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");

        Label reportTitle = new Label("Emissions by User");
        reportTitle.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        VBox userBreakdown = new VBox(8);
        userBreakdown.setPadding(new Insets(10));

        java.util.ArrayList<String> users = tracker.getUniqueUsers();

        if (users.isEmpty()) {
            Label noData = new Label("No user data available");
            noData.setStyle("-fx-text-fill: #999; -fx-font-style: italic;");
            userBreakdown.getChildren().add(noData);
        } else {
            double grandTotal = tracker.GetTotalEmissions();

            for (String user : users) {
                double userEmissions = tracker.GetTotalEmissionsForUser(user);
                int userEntries = tracker.getEntriesByUser(user).size();

                HBox userRow = createUserStatsRow(user, userEmissions, userEntries, grandTotal);
                userBreakdown.getChildren().add(userRow);
            }
        }

        report.getChildren().addAll(reportTitle, new Separator(), userBreakdown);
        return report;
    }

    /**
     * Creates detailed daily report
     */
    private VBox createDetailedReport() {
        VBox report = new VBox(12);
        report.setPadding(new Insets(15));
        report.setStyle("-fx-background-color: white; -fx-border-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");

        Label reportTitle = new Label("Detailed Daily Report");
        reportTitle.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        VBox reportContent = new VBox(10);
        reportContent.setPadding(new Insets(10));

        java.util.ArrayList<String> users = tracker.getUniqueUsers();

        if (users.isEmpty()) {
            Label noData = new Label("No data available");
            noData.setStyle("-fx-text-fill: #999; -fx-font-style: italic;");
            reportContent.getChildren().add(noData);
        } else {
            for (String user : users) {
                // User section
                VBox userSection = new VBox(8);
                userSection.setPadding(new Insets(10));
                userSection.setStyle("-fx-border-color: #ecf0f1; -fx-border-width: 1; -fx-border-radius: 4;");

                Label userLabel = new Label("👤 " + user);
                userLabel.setStyle("-fx-font-size: 13; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

                VBox entriesList = new VBox(5);
                entriesList.setPadding(new Insets(5));

                for (EmissionSource entry : tracker.getEntriesByUser(user)) {
                    HBox entryRow = createEntryRow(entry);
                    entriesList.getChildren().add(entryRow);
                }

                // Subtotal
                double userTotal = tracker.GetTotalEmissionsForUser(user);
                HBox subtotalBox = new HBox(15);
                subtotalBox.setPadding(new Insets(5));
                subtotalBox.setStyle("-fx-border-color: #bdc3c7; -fx-border-width: 0 0 1 0;");

                Label subtotalLabel = new Label("Subtotal for " + user + ":");
                subtotalLabel.setStyle("-fx-font-size: 11; -fx-font-weight: bold;");

                Label subtotalValue = new Label(String.format("%.2f kg CO2", userTotal));
                subtotalValue.setStyle("-fx-font-size: 11; -fx-font-weight: bold; -fx-text-fill: #e74c3c;");

                subtotalBox.getChildren().addAll(subtotalLabel, new Separator(), subtotalValue);
                HBox.setHgrow(subtotalBox.getChildren().get(1), Priority.ALWAYS);

                userSection.getChildren().addAll(userLabel, entriesList, subtotalBox);
                reportContent.getChildren().add(userSection);
            }

            // Grand total
            HBox grandTotalBox = new HBox(15);
            grandTotalBox.setPadding(new Insets(10));
            grandTotalBox.setStyle("-fx-background-color: #ecf0f1; -fx-border-radius: 4;");

            Label grandLabel = new Label("GRAND TOTAL:");
            grandLabel.setStyle("-fx-font-size: 13; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

            Label grandValue = new Label(String.format("%.2f kg CO2", tracker.GetTotalEmissions()));
            grandValue.setStyle("-fx-font-size: 14; -fx-font-weight: bold; -fx-text-fill: #e74c3c;");

            grandTotalBox.getChildren().addAll(grandLabel, new Separator(), grandValue);
            HBox.setHgrow(grandTotalBox.getChildren().get(1), Priority.ALWAYS);

            reportContent.getChildren().add(grandTotalBox);
        }

        ScrollPane scrollPane = new ScrollPane(reportContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: #f5f5f5;");
        scrollPane.setPrefHeight(300);

        report.getChildren().addAll(reportTitle, new Separator(), scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        return report;
    }

    /**
     * Creates a progress bar row for type breakdown
     */
    private HBox createProgressRow(String label, double value, double total, String color) {
        HBox row = new HBox(10);
        row.setPadding(new Insets(8));
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle("-fx-border-color: #ecf0f1; -fx-border-width: 0 0 1 0;");

        Label typeLabel = new Label(label);
        typeLabel.setStyle("-fx-font-size: 12; -fx-font-weight: bold; -fx-min-width: 120;");

        ProgressBar progressBar = new ProgressBar(value / total);
        progressBar.setPrefWidth(200);
        progressBar.setStyle("-fx-accent: " + color + ";");

        Label valueLabel = new Label(String.format("%.2f kg (%.1f%%)", value, (value / total) * 100));
        valueLabel.setStyle("-fx-font-size: 11; -fx-text-fill: " + color + ";");

        row.getChildren().addAll(typeLabel, progressBar, valueLabel);
        HBox.setHgrow(progressBar, Priority.ALWAYS);

        return row;
    }

    /**
     * Creates user statistics row
     */
    private HBox createUserStatsRow(String user, double emissions, int count, double total) {
        HBox row = new HBox(15);
        row.setPadding(new Insets(10));
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle("-fx-border-color: #ecf0f1; -fx-border-width: 0 0 1 0;");

        Label userLabel = new Label(user);
        userLabel.setStyle("-fx-font-size: 12; -fx-font-weight: bold; -fx-min-width: 100;");

        Label countLabel = new Label(count + " entries");
        countLabel.setStyle("-fx-font-size: 11; -fx-text-fill: #666;");

        ProgressBar progressBar = new ProgressBar(total > 0 ? emissions / total : 0);
        progressBar.setPrefWidth(150);
        progressBar.setStyle("-fx-accent: #2ecc71;");

        Label emissionLabel = new Label(String.format("%.2f kg CO2", emissions));
        emissionLabel.setStyle("-fx-font-size: 12; -fx-font-weight: bold; -fx-text-fill: #e74c3c;");

        row.getChildren().addAll(userLabel, countLabel, progressBar, emissionLabel);
        HBox.setHgrow(progressBar, Priority.ALWAYS);

        return row;
    }

    /**
     * Creates a single entry row
     */
    private HBox createEntryRow(EmissionSource entry) {
        HBox row = new HBox(10);
        row.setPadding(new Insets(5));
        row.setAlignment(Pos.CENTER_LEFT);

        Label idLabel = new Label(entry.getSourceID());
        idLabel.setStyle("-fx-font-size: 10; -fx-text-fill: #666; -fx-min-width: 50;");

        String typeStr = getTypeString(entry);
        Label typeLabel = new Label(typeStr);
        typeLabel.setStyle("-fx-font-size: 10; -fx-text-fill: #555;");

        Label dateLabel = new Label(entry.getDate());
        dateLabel.setStyle("-fx-font-size: 10; -fx-text-fill: #666;");

        Label emissionLabel = new Label(String.format("%.2f kg", entry.calculateEmission()));
        emissionLabel.setStyle("-fx-font-size: 10; -fx-font-weight: bold; -fx-text-fill: #e74c3c;");

        row.getChildren().addAll(idLabel, typeLabel, dateLabel, emissionLabel);
        HBox.setHgrow(typeLabel, Priority.ALWAYS);

        return row;
    }

    /**
     * Gets type string for entry
     */
    private String getTypeString(EmissionSource entry) {
        if (entry instanceof EnergyEmission e) {
            return String.format("Energy (%s, %.1f kWh)", e.getEnergySource(), e.getKwhused());
        } else if (entry instanceof FoodEmission f) {
            return String.format("Food (%s, %d meals)", f.getMealType(), f.getNumberOfMeals());
        } else if (entry instanceof TransportationEmission t) {
            return String.format("Transport (%s, %.1f km)", t.getVehicleType(), t.getDistanceKM());
        }
        return "Unknown";
    }

    public VBox getPanel() {
        return panel;
    }

    public void refresh() {
        // Update report content with latest data
        refreshContent();
    }
}

