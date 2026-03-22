import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * GreenPrintGUI - Main GUI Application for Carbon Footprint Tracking
 * A modern, professional interface for tracking and managing carbon emissions.
 */
public class GreenPrintGUI extends Application {
    private FootprintTracker tracker;
    private BorderPane mainLayout;
    private TabPane tabPane;
    private Scene scene;

    // Panels
    private DashboardPanel dashboardPanel;
    private AddEmissionPanel addEmissionPanel;
    private EmissionsViewerPanel emissionsViewerPanel;
    private ReportsPanel reportsPanel;
    private OffsetsPanel offsetsPanel;
    
    // Offset tracking (shared across all panels)
    private static double totalOffsetsAdded = 0;

    @Override
    public void start(Stage primaryStage) {
        this.tracker = GreenPrintCLI.getTracker();

        // Load previously saved offsets from log file
        loadTotalOffsetsFromLog();

        // Initialize all panels
        initializePanels();

        // Create main layout
        mainLayout = new BorderPane();
        mainLayout.setStyle("-fx-background-color: #f5f5f5;");

        // Top bar with title and user info
        VBox topBar = createTopBar();
        mainLayout.setTop(topBar);

        // Tab pane for main content
        tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.setStyle("-fx-font-size: 12;");

        // Create tabs
        createTabs();

        mainLayout.setCenter(tabPane);

        // Create scene with CSS
        scene = new Scene(mainLayout, 1200, 750);
        applyStylesheet(scene);

        primaryStage.setTitle("GreenPrint - Carbon Footprint Tracker");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(e -> {
            GreenPrintCLI.saveApplication();
        });

        primaryStage.show();

        // Refresh all panels on start
        refreshAllPanels();
    }

    /**
     * Creates the top navigation bar
     */
    private VBox createTopBar() {
        VBox topBar = new VBox();
        topBar.setStyle("-fx-background-color: linear-gradient(to right, #2ecc71, #27ae60); -fx-padding: 20;");
        topBar.setSpacing(10);
        topBar.setPadding(new Insets(15, 20, 15, 20));

        HBox titleBox = new HBox();
        titleBox.setAlignment(Pos.CENTER_LEFT);
        titleBox.setSpacing(15);

        Label mainTitle = new Label("🌍 GreenPrint");
        mainTitle.setStyle("-fx-font-size: 28; -fx-font-weight: bold; -fx-text-fill: white;");

        Label subtitle = new Label("Carbon Footprint Tracking System");
        subtitle.setStyle("-fx-font-size: 14; -fx-text-fill: rgba(255,255,255,0.9);");

        titleBox.getChildren().addAll(mainTitle, subtitle);
        topBar.getChildren().add(titleBox);

        return topBar;
    }

    /**
     * Initializes all panel components
     */
    private void initializePanels() {
        dashboardPanel = new DashboardPanel(tracker, this);
        addEmissionPanel = new AddEmissionPanel(tracker, this);
        emissionsViewerPanel = new EmissionsViewerPanel(tracker, this);
        reportsPanel = new ReportsPanel(tracker, this);
        offsetsPanel = new OffsetsPanel(tracker, this);
    }

    /**
     * Creates tabs for the application
     */
    private void createTabs() {
        // Dashboard Tab
        Tab dashboardTab = new Tab("📊 Dashboard", dashboardPanel.getPanel());
        dashboardTab.setDisable(false);

        // Add Emission Tab
        Tab addTab = new Tab("➕ Add Emission", addEmissionPanel.getPanel());
        addTab.setDisable(false);

        // View Emissions Tab
        Tab viewTab = new Tab("📋 View Emissions", emissionsViewerPanel.getPanel());
        viewTab.setDisable(false);

        // Reports Tab
        Tab reportsTab = new Tab("📈 Reports", reportsPanel.getPanel());
        reportsTab.setDisable(false);

        // Offsets Tab
        Tab offsetsTab = new Tab("♻️ Carbon Offsets", offsetsPanel.getPanel());
        offsetsTab.setDisable(false);

        tabPane.getTabs().addAll(dashboardTab, addTab, viewTab, reportsTab, offsetsTab);
    }

    /**
     * Applies custom CSS styling
     */
    private void applyStylesheet(Scene scene) {
        String css = """
            .root {
                -fx-font-family: 'Segoe UI', 'Ubuntu', sans-serif;
            }
            
            .tab {
                -fx-padding: 12 30 12 30;
                -fx-font-size: 13;
                -fx-font-weight: bold;
                -fx-text-fill: #555;
            }
            
            .tab:selected {
                -fx-background-color: #2ecc71;
                -fx-text-fill: white;
            }
            
            .tab-pane .tab-header-background {
                -fx-background-color: #ecf0f1;
            }
            
            .button {
                -fx-padding: 10 20 10 20;
                -fx-font-size: 12;
                -fx-font-weight: bold;
                -fx-border-radius: 5;
                -fx-background-color: #3498db;
                -fx-text-fill: white;
                -fx-cursor: hand;
            }
            
            .button:hover {
                -fx-background-color: #2980b9;
            }
            
            .button:pressed {
                -fx-background-color: #1f618d;
            }
            
            .button-success {
                -fx-background-color: #2ecc71;
            }
            
            .button-success:hover {
                -fx-background-color: #27ae60;
            }
            
            .button-danger {
                -fx-background-color: #e74c3c;
            }
            
            .button-danger:hover {
                -fx-background-color: #c0392b;
            }
            
            .text-field {
                -fx-padding: 10;
                -fx-font-size: 12;
                -fx-border-color: #bdc3c7;
                -fx-border-radius: 4;
                -fx-control-inner-background: white;
            }
            
            .text-field:focused {
                -fx-border-color: #3498db;
                -fx-border-width: 2;
            }
            
            .combo-box {
                -fx-padding: 10;
                -fx-font-size: 12;
                -fx-border-color: #bdc3c7;
                -fx-border-radius: 4;
            }
            
            .label {
                -fx-text-fill: #2c3e50;
                -fx-font-size: 12;
            }
            
            .label-title {
                -fx-font-size: 18;
                -fx-font-weight: bold;
                -fx-text-fill: #2c3e50;
            }
            
            .label-subtitle {
                -fx-font-size: 14;
                -fx-font-weight: bold;
                -fx-text-fill: #34495e;
            }
            
            .table-view {
                -fx-font-size: 11;
                -fx-table-cell-border-color: #ecf0f1;
            }
            
            .table-view:focused {
                -fx-background-color: white;
            }
            
            .table-column {
                -fx-alignment: CENTER;
            }
            
            .table-view .column-header {
                -fx-background-color: #34495e;
                -fx-padding: 8;
            }
            
            .table-view .column-header .label {
                -fx-text-fill: white;
                -fx-font-weight: bold;
            }
            
            .separator {
                -fx-padding: 10;
            }
            
            .titled-border {
                -fx-text-fill: #2c3e50;
                -fx-border-color: #bdc3c7;
                -fx-border-width: 1;
                -fx-padding: 15;
            }
            """;

        scene.getStylesheets().add("data:text/css," + css.replace("\"", "'").replace("\n", ""));
    }

    /**
     * Refreshes all panels with latest data
     */
    public void refreshAllPanels() {
        if (dashboardPanel != null) dashboardPanel.refresh();
        if (addEmissionPanel != null) addEmissionPanel.refresh();
        if (emissionsViewerPanel != null) emissionsViewerPanel.refresh();
        if (reportsPanel != null) reportsPanel.refresh();
        if (offsetsPanel != null) offsetsPanel.refresh();
    }
    
    /**
     * Gets the total offsets added globally
     */
    public static double getTotalOffsetsAdded() {
        return totalOffsetsAdded;
    }
    
    /**
     * Adds to the total offsets
     */
    public static void addToTotalOffsets(double amount) {
        totalOffsetsAdded += amount;
    }
    
    /**
     * Resets the total offsets
     */
    public static void resetTotalOffsets() {
        totalOffsetsAdded = 0;
    }

    /**
     * Loads total offsets from the log file on startup
     */
    public static void loadTotalOffsetsFromLog() {
        totalOffsetsAdded = Logger.loadTotalOffsetsFromLog();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
