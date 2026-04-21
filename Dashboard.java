/**
 * Dashboard.java
 * This class is responsible for creating the user interface for the dashboard tab in the GreenPrint CLI application. It provides methods to generate the dashboard content, including an overview section with total entries, total emissions, and the user with the highest emissions, as well as a detailed section that lists all emission entries grouped by user. The class utilizes JavaFX components to create a visually appealing and interactive dashboard that allows users to view their carbon footprint data in an organized manner.
 * 
 */


import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.TextAlignment;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.ScrollPane;
import javafx.geometry.Side;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

/**
 * The Dashboard class is responsible for creating the user interface for the dashboard tab in the GreenPrint CLI application. It provides methods to generate the dashboard content, including an overview section with total entries, total emissions, and the user with the highest emissions, as well as a detailed section that lists all emission entries grouped by user. The class utilizes JavaFX components to create a visually appealing and interactive dashboard that allows users to view their carbon footprint data in an organized manner.
 */

public class Dashboard {

    


    /**
     * Creates the title for the emission entries section of the dashboard with a styled label.
     * @return a VBox containing the "Emission Entries" title label, styled with a larger font size, bold weight, green text color, and padding for spacing. The VBox is centered to ensure the title is prominently displayed above the list of emission entries in the dashboard.
     */
    public static VBox MakeTitleLabel(String string) {
        Label label = new Label(string);
        label.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: green; -fx-padding: 20px;");
        VBox content = new VBox(10);
        content.setAlignment(Pos.CENTER);
        content.getChildren().addAll(label);
        return content;
    }


    /**
     * Helper method to create a styled label for the dashboard metrics. The label is designed with a white background, green border, rounded corners, bold text, and padding for better visual appeal. It also allows the label to grow with the window size for improved responsiveness. This method is used to create the labels for total entries, total emissions, and the highest emissions user in the dashboard overview section.
     * @param string
     * @return a styled Label object with the specified text, designed for use in the dashboard metrics section, providing a consistent and visually appealing format for displaying key information to users.
     */

    public static Label makeLabel(String string) {
        Label label = new Label(string);

        label.setAlignment(Pos.CENTER);

        // Center the multiline text itself (centers the bottom line underneath the top line)
        label.setTextAlignment(TextAlignment.CENTER);

        // Apply styling for green text, green outline, white background, and rounded corners
        label.setStyle(
            "-fx-background-color: white; " +
            "-fx-border-color: green; " +
            "-fx-border-width: 3px; " +
            "-fx-border-radius: 20px; " +  
            "-fx-background-radius: 20px; " +  
            "-fx-text-fill: black; " +
            "-fx-font-size: 20px; " +  
            "-fx-font-weight: bold; " +  
            "-fx-padding: 50px 40px;" 
        );

        // Make labels grow with window size
        label.setMaxWidth(Double.MAX_VALUE);
        label.setMaxHeight(Double.MAX_VALUE);
        label.setPrefWidth(300);  
        label.setPrefHeight(180);  

        return label;
    }

    public static String getEmissionStatus() {
    double emissions = OffsetTransactionGUI.roundTo2Decimals(GreenPrintGUI.tracker.GetTotalEmissions());
    double offsets = OffsetTransactionGUI.roundTo2Decimals(Logger.CalculateTotalOffsetAmount());
    double net = emissions - offsets;

    if (emissions == offsets) {
        return "Carbon Neutral (0.00 kg CO2)";
    } else if (emissions > offsets) {
        // Returns the positive difference as a String
        return String.format("Carbon Positive (%.2f kg CO2)", net); 
    } else {
        return String.format("Carbon Negative (%.2f kg CO2)", net);
    }
}


    
    /**
     * Creates the main content section of the dashboard, which includes three key metrics: total entries, total emissions, and the user with the highest emissions. Each metric is displayed in a styled label with a white background, green border, rounded corners, and bold text. The labels are arranged horizontally with spacing between them and are designed to grow with the window size for better responsiveness. This section provides users with a quick overview of their carbon footprint data at a glance.
     * @return an HBox containing the three metric labels, styled and arranged for a visually
     */

    public static HBox createDashboardContent() {
        HBox dashboardContent = new HBox(15); 
        dashboardContent.setPadding(new Insets(20));
        dashboardContent.setAlignment(Pos.CENTER);

        

        // Create labels
        Label totalEntriesLabel = makeLabel("Total Entries: \n\n" + GreenPrintGUI.tracker.getTotalEntries());
        Label totalEmissionsLabel = makeLabel(String.format("Total Emissions:  \n\n %.2f kg CO2", GreenPrintGUI.tracker.GetTotalEmissions()));
        Label NetEmissionsLabel = makeLabel(String.format("Net Emissions:  \n\n %s", getEmissionStatus()));
        Label highestUserLabel = makeLabel("Highest Emissions User: \n\n" + GreenPrintGUI.tracker.getHighestTotalEmissionUser());

        // Make labels grow with window size
        HBox.setHgrow(totalEntriesLabel, Priority.ALWAYS);
        HBox.setHgrow(totalEmissionsLabel, Priority.ALWAYS);
        HBox.setHgrow(NetEmissionsLabel, Priority.ALWAYS);
        HBox.setHgrow(highestUserLabel, Priority.ALWAYS);

        dashboardContent.getChildren().addAll(totalEntriesLabel, totalEmissionsLabel, NetEmissionsLabel, highestUserLabel);

        return dashboardContent;
    }

    /**
     * Creates a FlowPane to hold the emission entry labels for a specific user. The FlowPane is styled with padding, alignment, and a light background color to visually separate it from other sections of the dashboard. This method is used to generate the container for each user's emission entries in the detailed section of the dashboard, allowing for an organized and visually appealing display of individual emission data.
     * @return
     */
    public static FlowPane createUserEntriesFlowPane() {
      FlowPane emissionPane = new FlowPane(10, 10);
                emissionPane.setPadding(new Insets(10));
                emissionPane.setAlignment(Pos.CENTER);
                emissionPane.setStyle(
                    "-fx-background-color: #f5f5f5; " +
                    "-fx-background-radius: 5px;"
                );

        return emissionPane;
    }

    /**
     * Creates a styled label for a user's name in the dashboard, designed to visually separate the user's section from their emission entries. The label features a white background, black border, rounded corners, bold text, and green text color to maintain the overall aesthetic of the dashboard. This method is used to generate the labels for each user in the detailed section of the dashboard, providing a clear and visually appealing way to group emission entries by user.
     * @param user
     * @return
     */
    public static Label UserEntryLabel(String user) {
        Label userLabel = new Label(user);

         userLabel.setAlignment(Pos.CENTER);

        userLabel.setStyle("-fx-font-size: 18px; " +
                    "-fx-font-weight: bold; " +
                    "-fx-border-width: 2px; " +
                    "-fx-background-color: white; " +
                    "-fx-border-color: black; " +
                     "-fx-border-radius: 10px; " +  
                     "-fx-background-radius: 10px; " +  
                    "-fx-text-fill: #2c5f2d; " +
                    "-fx-padding: 10px;");

        
        
        return userLabel;
    }

    /**
     * Creates a label for an individual emission entry, styled with a background color that reflects the magnitude of the emission (green for low, yellow for medium, red for high) and includes an event handler that shows a detailed alert with the entry's information when clicked. This method is used to generate the labels for each emission entry in the detailed section of the dashboard, providing users with an interactive way to view their emission data and understand the specifics of each entry by clicking on it.
     * @param entry
     * @return a styled Label object representing an individual emission entry, designed to visually indicate the magnitude of the emission and provide interactivity for users to access detailed information about the entry through an alert dialog.
     */
    public static Label EmissionEntryLabel(EmissionSource entry) {
        Label emissionLabel = new Label(entry.getSourceID());
        emissionLabel.setPrefWidth(150);
        emissionLabel.setPrefHeight(60);
        emissionLabel.setAlignment(Pos.CENTER);

        String bgColor;
        Double emission = entry.calculateEmission();
        String textColor = "white";
        if (emission < 1.0) {
            bgColor = "#4CAF50"; // Green
        } else if (emission <= 3.0) {
            bgColor = "#FFC107"; // Yellow
            textColor = "black";
        } else {
            bgColor = "#F44336"; // Red
            }

        emissionLabel.setStyle(
            "-fx-background-color: " + bgColor + "; " +
            "-fx-background-color: " + bgColor + "; " +
            "-fx-text-fill: " + textColor + "; " +
            "-fx-font-weight: bold; " +
            "-fx-font-size: 12px; " +
            "-fx-background-radius: 5px; " +
            "-fx-border-color: #333; " +
            "-fx-border-width: 1px; " +
            "-fx-border-radius: 5px; " 
        );

        /**
         * Adds a mouse click event handler to the emission label that displays an alert with detailed information about the emission entry when clicked. The alert includes the category, date, user name, specific details (e.g., KWH for energy, meal type for food), and the calculated emission in kg CO2. The alert is styled with a subtle off-white/greenish background and uses different text colors to differentiate between keys and values for better readability. This interactivity allows users to easily access and understand the specifics of each emission entry directly from the dashboard.
         * @param event the MouseEvent triggered when the emission label is clicked, which initiates the display of an Alert dialog containing detailed information about the emission entry, including its category, date, user name, specific details, and calculated emissions, all styled for clarity and visual appeal.
         */
        emissionLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            
            public void handle(MouseEvent event){

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Emission Details");
                alert.setHeaderText("Details for " + entry.getSourceID());
                
                // 1. Get the DialogPane to customize its overall style
                DialogPane dialogPane = alert.getDialogPane();
                dialogPane.setPrefWidth(450); 
                dialogPane.setStyle("-fx-background-color: #f9fbf9; -fx-font-size: 14px;"); // Subtle off-white/greenish background

                // 2. Set up styles for our labels
                String headerStyle = "-fx-font-weight: bold; -fx-text-fill: #2c5f2d;"; // Dark green for keys
                String valueStyle = "-fx-font-weight: bold; -fx-text-fill: #000000; -fx-font-size: 15px;"; // Dark gray for values

                // 3. Create a GridPane to perfectly align the details
                GridPane grid = new GridPane();
                grid.setHgap(15); // Horizontal spacing between columns
                grid.setVgap(10); // Vertical spacing between rows
                grid.setPadding(new javafx.geometry.Insets(10, 0, 10, 0));

                // Row 0: Category
                Label catLbl = new Label("Category:"); catLbl.setStyle(headerStyle);
                Label catVal = new Label(entry.getCategory()); catVal.setStyle(valueStyle);
                grid.addRow(0, catLbl, catVal);

                // Row 1: Date
                Label dateLbl = new Label("Date:"); dateLbl.setStyle(headerStyle);
                Label dateVal = new Label(entry.getDate()); dateVal.setStyle(valueStyle);
                grid.addRow(1, dateLbl, dateVal);

                // Row 2: User
                Label userLbl = new Label("User:"); userLbl.setStyle(headerStyle);
                Label userVal = new Label(entry.getUserName()); userVal.setStyle(valueStyle);
                grid.addRow(2, userLbl, userVal);

                // Row 3: Dynamic Emission Details (e.g., KWH, Grid type, or Food type)
                Label specificLbl = new Label("Specifics:"); specificLbl.setStyle(headerStyle);
                Label specificVal = new Label(GreenPrintGUI.tracker.TypeofEmission(entry)); 
                specificVal.setStyle(valueStyle);
                grid.addRow(3, specificLbl, specificVal);

                // Row 4: Calculated Emission (Making this pop!)
                Label calcLbl = new Label("Calculated Emission:"); calcLbl.setStyle(headerStyle);
                Label calcVal = new Label(String.format("%.2f kg CO2", emission));
                calcVal.setStyle(headerStyle + "-fx-text-fill: #d9534f;"); // Reddish color to highlight footprint
                grid.addRow(4, calcLbl, calcVal);

                // 4. Create a VBox to hold the grid, a separator line, and the raw entry string at the bottom
                javafx.scene.layout.VBox contentBox = new javafx.scene.layout.VBox(15);
                
                javafx.scene.control.Separator separator = new javafx.scene.control.Separator();
                
                Label totalLbl = new Label("Raw Entry Data:"); 
                totalLbl.setStyle(headerStyle);
                
                Label totalVal = new Label(entry.toString());
                
                totalVal.setStyle(valueStyle + "-fx-font-size: 12px");

                // Add everything to the VBox
                contentBox.getChildren().addAll(grid, separator, totalLbl, totalVal);

                // 5. Inject our custom VBox into the Alert's content area
                alert.getDialogPane().setContent(contentBox);

                alert.showAndWait();
                
            }
    });
        return emissionLabel;

        
    }

   
    /**
     * Generates the detailed emission entries section of the dashboard, which lists all emission entries grouped by user. For each user, a label is created to display the user's name, followed by a FlowPane that contains labels for each of the user's emission entries. Each entry label is styled with a background color that reflects the magnitude of the emission (green for low, yellow for medium, red for high) and includes an event handler that shows a detailed alert with the entry's information when clicked. This method provides users with an organized and interactive way to view their individual emission entries on the dashboard.
     * @return a VBox containing the user labels and their corresponding emission entry labels, styled and
     */
    public static VBox GenerateEmissionEntries() {
        VBox entriesBox = new VBox(20);
        entriesBox.setPadding(new Insets(20));
        entriesBox.setAlignment(Pos.CENTER);
        try{
            ArrayList<String> users = GreenPrintGUI.tracker.getUniqueUsers();
            for (String user : users) {
                Label userLabel = UserEntryLabel(user);
                entriesBox.getChildren().add(userLabel);
                FlowPane userEmissionsPane = createUserEntriesFlowPane();

                for (EmissionSource entry : GreenPrintGUI.tracker.getEntriesByUser(user)) {
                    Label emissionLabel = EmissionEntryLabel(entry);
                    userEmissionsPane.getChildren().add(emissionLabel);
                }
                entriesBox.getChildren().add(userEmissionsPane);
            }


        } catch (Exception e) {
            System.err.println("An Error Occurred while generating emission entries: " + e.getMessage());
        }
        
        return entriesBox;
    }


    /**
     * Generates the detailed emission entries section of the dashboard, which lists all emission entries grouped by user. For each user, a label is created to display the user's name, followed by a FlowPane that contains labels for each of the user's emission entries. Each entry label is styled with a background color that reflects the magnitude of the emission (green for low, yellow for medium, red for high) and includes an event handler that shows a detailed alert with the entry's information when clicked. This method provides users with an organized and interactive way to view their individual emission entries on the dashboard.
     * @return a VBox containing the user labels and their corresponding emission entry labels, styled and arranged for clarity and interactivity, allowing users to easily navigate through their emission data on the dashboard.
     */

    public static ScrollPane createDashboard() {
        
        VBox content = new VBox(10);


        if (GreenPrintGUI.tracker.getTotalEntries() == 0) {
            Label noDataLabel = makeLabel("No Emission Data Added Yet! \n\n Please add entries in the Data Operations tab.");
            content.getChildren().add(noDataLabel);
            
        } else {

            content.getChildren().addAll(
            MakeTitleLabel("Dashboard Overview"),
            createDashboardContent(),
            MakeTitleLabel("Emission Entries"),
            GenerateEmissionEntries()
            );
        }

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: white; -fx-background-color: white;");

        return scrollPane;
    }

  
}
