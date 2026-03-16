import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.geometry.Side;
import javafx.geometry.Insets;

public class Dashboard extends Application {

   public void start(Stage primaryStage) {
        // This method is intentionally left blank as the main application logic is handled in GreenPrintCLI
    }

    public static VBox createDashboardContent() {
        System.out.println("→ Creating Dashboard tab content...");
        
        Label titleLabel = new Label("Dashboard Overview");
       
        VBox content = new VBox(10);
        content.getChildren().addAll(titleLabel);
        
        return content;
    }

    
    public static void main(String[] args) {
        System.out.println("\n========================================");
        System.out.println("Starting Zero-Carbon Footprint Tracker");
        System.out.println("========================================\n");

        launch(args);
    }
}
