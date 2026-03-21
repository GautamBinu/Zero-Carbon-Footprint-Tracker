import java.io.IOException;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tab;
import javafx.geometry.Side;


public class GreenPrintCLI extends Application {

    static FootprintTracker tracker = new FootprintTracker();
    static Tab dashboardTab;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("GreenPrint CLI");

        // Save state when window closes
        primaryStage.setOnCloseRequest(event -> {
            Logger.saveState();
        });

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

    public static void main(String[] args) throws IOException {

        // Load saved state on startup
        Logger.loadState();

        
        // Launch the JavaFX application
        launch(args);
    }
}

 