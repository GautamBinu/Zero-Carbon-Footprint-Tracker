import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.geometry.Side;
import javafx.geometry.Insets;

public class GreenPrintCLI extends Application {

    static FootprintTracker tracker = new FootprintTracker();
    static Tab dashboardTab;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("GreenPrint CLI");
        
        
        // Create the TabPane
        TabPane tabPane = new TabPane();
        
        tabPane.setStyle( 
        "-fx-font-family: 'Segoe UI', Helvetica, Arial, sans-serif; " +
        "-fx-font-size: 13.5px; " +
        "-fx-font-weight: bold;" 
          
        );      
        
        
        tabPane.setTabMinHeight(40);
        tabPane.setTabMinWidth(150);
       
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.setSide(Side.BOTTOM);

        // Create the Dashboard tab
        dashboardTab = new Tab();
        dashboardTab.setText("Dashboard");
        dashboardTab.setContent(Dashboard.createDashboard());
        
        // Create the Data Operations tab
        Tab dataOpsTab = new Tab();
        dataOpsTab.setText("Data Operations");
        dataOpsTab.setContent(DataOperationIO.DataOperationsTab());
        
        
        // Create the Offset Transactions tab
        Tab offsetTab = new Tab();
        offsetTab.setText("Offset Transactions");
        
        // Add tabs to the TabPane
        tabPane.getTabs().addAll(dashboardTab, dataOpsTab, offsetTab);
        
        // Create the scene and add the TabPane
        Scene scene = new Scene(tabPane, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void refreshDashboard() {
        if (dashboardTab != null) {
            dashboardTab.setContent(Dashboard.createDashboard());
        }
       
    }

    public static void main(String[] args) throws IOException {

        tracker.addEntry(new EnergyEmission("E-001", "Energy", "2024-06-01", "Alice", 15.0, "Grid"));
        tracker.addEntry(new FoodEmission("F-002", "Food", "2024-06-01", "Alice", "Vegetarian", 2));
        tracker.addEntry(new FoodEmission("F-001", "Food", "2024-06-01", "Bob", "Vegan", 3));
        tracker.addEntry(new TransportationEmission("T-002", "Transportation", "2024-06-01", "Bob", 15.0, "Bus"));
        tracker.addEntry(new TransportationEmission("T-001", "Transportation", "2024-06-01", "Charlie", 10.0, "Car"));
        tracker.addEntry(new EnergyEmission("E-002", "Energy", "2024-06-01", "Charlie", 25.0, "solar"));
        tracker.addEntry(new TransportationEmission("T-003", "Transportation", "2024-06-01", "Jacob", 10.0, "Car"));
         tracker.addEntry(new FoodEmission("F-003", "Food", "2024-06-01", "Jacob", "poultry", 2));
        tracker.addEntry(new EnergyEmission("E-003", "Energy", "2024-06-01", "Jacob", 15.0, "Grid"));

        tracker.generateDailyReport();
        System.out.println("User with highest total emissions: " + tracker.getHighestTotalEmissionUser());

        
        // Launch the JavaFX application
        launch(args);
    }
}

 