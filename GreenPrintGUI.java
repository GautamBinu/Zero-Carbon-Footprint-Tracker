/**
 * GreenPrintGUI.java
 * This class serves as the main entry point for the GreenPrint GUI application. It extends the
 * JavaFX Application class and sets up the primary stage with a TabPane containing three tabs: Dashboard, Data Operations, and Offset Transactions. Each tab is populated with content from their respective classes (Dashboard, DataOperationIO, and OffsetTransactionGUI). The class also includes a method to refresh the dashboard content and handles saving the application state when the window is closed. The main method loads any saved state and launches the JavaFX application.
 * 
 */


import java.io.IOException;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tab;
import javafx.geometry.Side;


public class GreenPrintGUI extends Application {

    static FootprintTracker tracker = new FootprintTracker();
    static Tab dashboardTab;
    static String FontFamily = "-fx-font-family: 'Segoe UI', Helvetica, Arial, sans-serif; ";

    /**
     * The start method is the main entry point for the JavaFX application. It sets up the primary stage with a TabPane containing three tabs: Dashboard, Data Operations, and Offset Transactions. Each tab is populated with content from their respective classes (Dashboard, DataOperationIO, and OffsetTransactionGUI). The method also includes an event handler to save the application state when the window is closed. Finally, it creates a scene with the TabPane and displays it on the primary stage.
     * @param primaryStage the primary stage for this application, onto which the application scene can be set.
     * 
     */
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("GreenPrint GUI");

        // Save state when window closes
        primaryStage.setOnCloseRequest(event -> {
            Logger.saveState();
        });

        // Create the TabPane
        TabPane tabPane = new TabPane();
        
        tabPane.setStyle( 
        FontFamily +
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
        offsetTab.setContent(OffsetTransactionGUI.createOffsetTabPane());
        
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

    /**
     * The main method serves as the entry point for the application. It first loads any saved state using the Logger class, ensuring that the application can restore previous data when launched. After loading the state, it calls the launch method to start the JavaFX application, which will eventually invoke the start method to set up the user interface and display the primary stage.
     * @param args
     * @throws IOException
     */

    public static void main(String[] args) throws IOException {

        // Load saved state on startup
        Logger.loadState();

        
        // Launch the JavaFX application
        launch(args);
    }
}

 