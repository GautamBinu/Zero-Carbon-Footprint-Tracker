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
 * Emissions Viewer Panel - Displays and manages emissions entries
 */
public class EmissionsViewerPanel {
    private FootprintTracker tracker;
    private GreenPrintGUI mainApp;
    private VBox panel;
    private TableView<EmissionSource> emissionsTable;
    private ComboBox<String> userFilterCombo;
    private Label totalFilteredLabel;

    public EmissionsViewerPanel(FootprintTracker tracker, GreenPrintGUI mainApp) {
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
        Label title = new Label("View Emissions");
        title.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Separator separator = new Separator();
        separator.setPrefHeight(2);
        separator.setStyle("-fx-padding: 10; -fx-background-color: #3498db;");

        // Filter bar
        HBox filterBar = createFilterBar();

        // Emissions table
        emissionsTable = createEmissionsTable();

        // Stats bar
        HBox statsBar = createStatsBar();

        VBox tableContainer = new VBox(10);
        tableContainer.setStyle("-fx-background-color: white; -fx-padding: 15; -fx-border-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        tableContainer.getChildren().addAll(
            new Label("All Emissions"),
            emissionsTable
        );
        VBox.setVgrow(emissionsTable, Priority.ALWAYS);

        VBox.setVgrow(tableContainer, Priority.ALWAYS);
        panel.getChildren().addAll(title, separator, filterBar, tableContainer, statsBar);

        // Set up continuous auto-refresh (every 1 second)
        Timeline refreshTimer = new Timeline(
            new KeyFrame(Duration.millis(1000), event -> refresh())
        );
        refreshTimer.setCycleCount(Timeline.INDEFINITE);
        refreshTimer.play();
    }

    /**
     * Creates the filter bar
     */
    private HBox createFilterBar() {
        HBox filterBar = new HBox(15);
        filterBar.setPadding(new Insets(12));
        filterBar.setAlignment(Pos.CENTER_LEFT);
        filterBar.setStyle("-fx-background-color: white; -fx-border-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");

        Label filterLabel = new Label("Filter by User:");
        filterLabel.setStyle("-fx-font-size: 12; -fx-font-weight: bold;");

        userFilterCombo = new ComboBox<>();
        userFilterCombo.setPrefWidth(200);
        userFilterCombo.setStyle("-fx-font-size: 12;");
        userFilterCombo.getItems().add("All Users");
        userFilterCombo.setValue("All Users");

        userFilterCombo.setOnAction(e -> refreshTable());

        Button exportBtn = new Button("📥 Export Summary");
        exportBtn.setStyle("-fx-padding: 8 15 8 15; -fx-font-size: 11; -fx-background-color: #2ecc71; -fx-text-fill: white; -fx-cursor: hand;");
        exportBtn.setOnAction(e -> exportSummary());

        filterBar.getChildren().addAll(filterLabel, userFilterCombo, exportBtn);
        HBox.setHgrow(filterBar, Priority.ALWAYS);

        return filterBar;
    }

    /**
     * Creates the emissions table
     */
    private TableView<EmissionSource> createEmissionsTable() {
        TableView<EmissionSource> table = new TableView<>();
        table.setPrefHeight(400);
        table.setStyle("-fx-font-size: 11;");

        // ID Column
        TableColumn<EmissionSource, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getSourceID()));
        idCol.setPrefWidth(80);

        // User Column
        TableColumn<EmissionSource, String> userCol = new TableColumn<>("User");
        userCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getUserName()));
        userCol.setPrefWidth(100);

        // Category Column
        TableColumn<EmissionSource, String> categoryCol = new TableColumn<>("Category");
        categoryCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getCategory()));
        categoryCol.setPrefWidth(100);

        // Type Column
        TableColumn<EmissionSource, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
            getEmissionType(cellData.getValue())
        ));
        typeCol.setPrefWidth(120);

        // Date Column
        TableColumn<EmissionSource, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDate()));
        dateCol.setPrefWidth(100);

        // Emissions Column
        TableColumn<EmissionSource, String> emissionCol = new TableColumn<>("CO2 (kg)");
        emissionCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
            String.format("%.2f", cellData.getValue().calculateEmission())
        ));
        emissionCol.setPrefWidth(100);

        table.getColumns().addAll(idCol, userCol, categoryCol, typeCol, dateCol, emissionCol);

        return table;
    }

    /**
     * Creates the statistics bar
     */
    private HBox createStatsBar() {
        HBox statsBar = new HBox(15);
        statsBar.setPadding(new Insets(15));
        statsBar.setAlignment(Pos.CENTER_LEFT);
        statsBar.setStyle("-fx-background-color: white; -fx-border-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");

        Label countLabel = new Label("Total Entries: " + tracker.getTotalEntries());
        countLabel.setStyle("-fx-font-size: 12; -fx-font-weight: bold;");

        totalFilteredLabel = new Label("Filtered Total: 0.00 kg CO2");
        totalFilteredLabel.setStyle("-fx-font-size: 12; -fx-font-weight: bold; -fx-text-fill: #e74c3c;");

        statsBar.getChildren().addAll(countLabel, new Separator(), totalFilteredLabel);
        HBox.setHgrow(statsBar.getChildren().get(1), Priority.ALWAYS);

        return statsBar;
    }

    /**
     * Gets the type of emission as a string
     */
    private String getEmissionType(EmissionSource entry) {
        if (entry instanceof EnergyEmission e) {
            return "Energy - " + e.getEnergySource();
        } else if (entry instanceof FoodEmission f) {
            return "Food - " + f.getMealType();
        } else if (entry instanceof TransportationEmission t) {
            return "Transport - " + t.getVehicleType();
        }
        return "Unknown";
    }

    /**
     * Refreshes the table with current data
     */
    private void refreshTable() {
        ObservableList<EmissionSource> data = FXCollections.observableArrayList();

        String selectedUser = userFilterCombo.getValue();
        double totalEmissions = 0;

        // Handle null selection during updates
        if (selectedUser == null) {
            selectedUser = "All Users";
            userFilterCombo.setValue("All Users");
        }

        if (selectedUser.equals("All Users")) {
            data.addAll(tracker.getEmissions());
            totalEmissions = tracker.GetTotalEmissions();
        } else {
            data.addAll(tracker.getEntriesByUser(selectedUser));
            totalEmissions = tracker.GetTotalEmissionsForUser(selectedUser);
        }

        emissionsTable.setItems(data);
        totalFilteredLabel.setText(String.format("Filtered Total: %.2f kg CO2", totalEmissions));
    }

    /**
     * Exports emissions summary
     */
    private void exportSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("=== GREENPRINT EMISSIONS SUMMARY ===\n\n");

        String filter = userFilterCombo.getValue();
        if (filter.equals("All Users")) {
            summary.append("ALL EMISSIONS\n");
            summary.append("Total: ").append(String.format("%.2f kg CO2\n\n", tracker.GetTotalEmissions()));

            for (EmissionSource entry : tracker.getEmissions()) {
                summary.append(entry.toString()).append(" | ").append(String.format("%.2f kg CO2", entry.calculateEmission())).append("\n");
            }
        } else {
            summary.append("EMISSIONS FOR USER: ").append(filter).append("\n");
            summary.append("Total: ").append(String.format("%.2f kg CO2\n\n", tracker.GetTotalEmissionsForUser(filter)));

            for (EmissionSource entry : tracker.getEntriesByUser(filter)) {
                summary.append(entry.toString()).append(" | ").append(String.format("%.2f kg CO2", entry.calculateEmission())).append("\n");
            }
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Emissions Summary");
        alert.setHeaderText(null);
        alert.getDialogPane().setPrefWidth(600);
        TextArea textArea = new TextArea(summary.toString());
        textArea.setEditable(false);
        textArea.setWrapText(true);
        alert.getDialogPane().setContent(textArea);
        alert.showAndWait();
    }

    public VBox getPanel() {
        return panel;
    }

    public void refresh() {
        // Update user filter combo with all unique users
        String currentSelection = userFilterCombo.getValue();
        if (currentSelection == null) {
            currentSelection = "All Users";
        }
        
        // Temporarily remove the event handler to prevent triggering during updates
        userFilterCombo.setOnAction(null);
        
        userFilterCombo.getItems().clear();
        userFilterCombo.getItems().add("All Users");
        
        java.util.ArrayList<String> users = tracker.getUniqueUsers();
        for (String user : users) {
            userFilterCombo.getItems().add(user);
        }
        
        // Restore previous selection if it still exists
        if (currentSelection.equals("All Users") || users.contains(currentSelection)) {
            userFilterCombo.setValue(currentSelection);
        } else {
            userFilterCombo.setValue("All Users");
        }
        
        // Restore the event handler
        userFilterCombo.setOnAction(e -> refreshTable());

        // Refresh the table with updated data
        refreshTable();
    }
}
