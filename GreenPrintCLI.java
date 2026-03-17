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
        tabPane.setSide(Side.TOP);

        // Create the Dashboard tab
        Tab dashboardTab = new Tab();
        dashboardTab.setText("Dashboard");
        dashboardTab.setContent(Dashboard.createDashboardContent());
        
        // Create the Data Operations tab
        Tab dataOpsTab = new Tab();
        dataOpsTab.setText("Data Operations");
        
        
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

    public static void main(String[] args) throws IOException {

        
        // Launch the JavaFX application
        launch(args);
    }
}

 