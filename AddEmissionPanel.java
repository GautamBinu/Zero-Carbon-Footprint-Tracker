import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Add Emission Panel - Interface for adding new emission entries
 */
public class AddEmissionPanel {
    private FootprintTracker tracker;
    private GreenPrintGUI mainApp;
    private VBox panel;
    private StackPane contentPane;
    private ComboBox<String> emissionTypeCombo;

    public AddEmissionPanel(FootprintTracker tracker, GreenPrintGUI mainApp) {
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
        Label title = new Label("Add New Emission Entry");
        title.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Separator separator = new Separator();
        separator.setPrefHeight(2);
        separator.setStyle("-fx-padding: 10; -fx-background-color: #3498db;");

        // Emission type selector
        HBox typeSelector = createTypeSelector();

        // Content pane for different emission types
        contentPane = new StackPane();
        contentPane.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-border-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");

        // Create emission type panels
        VBox energyForm = createEnergyForm();
        VBox foodForm = createFoodForm();
        VBox transportForm = createTransportForm();

        contentPane.getChildren().addAll(energyForm, foodForm, transportForm);
        energyForm.setManaged(true);
        energyForm.setVisible(true);
        foodForm.setManaged(false);
        foodForm.setVisible(false);
        transportForm.setManaged(false);
        transportForm.setVisible(false);

        // Type selection handler
        emissionTypeCombo.setOnAction(e -> {
            energyForm.setManaged(false);
            energyForm.setVisible(false);
            foodForm.setManaged(false);
            foodForm.setVisible(false);
            transportForm.setManaged(false);
            transportForm.setVisible(false);

            String selected = emissionTypeCombo.getValue();
            if (selected.contains("Energy")) {
                energyForm.setManaged(true);
                energyForm.setVisible(true);
            } else if (selected.contains("Food")) {
                foodForm.setManaged(true);
                foodForm.setVisible(true);
            } else if (selected.contains("Transportation")) {
                transportForm.setManaged(true);
                transportForm.setVisible(true);
            }
        });

        ScrollPane scrollPane = new ScrollPane();
        VBox content = new VBox(20);
        content.setPadding(new Insets(10));
        content.setStyle("-fx-background-color: #f5f5f5;");
        content.getChildren().addAll(title, separator, typeSelector, contentPane);
        scrollPane.setContent(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: #f5f5f5;");

        panel.getChildren().add(scrollPane);
    }

    /**
     * Creates emission type selector
     */
    private HBox createTypeSelector() {
        HBox box = new HBox(15);
        box.setPadding(new Insets(10));
        box.setAlignment(Pos.CENTER_LEFT);

        Label label = new Label("Select Emission Type:");
        label.setStyle("-fx-font-size: 13; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        emissionTypeCombo = new ComboBox<>();
        emissionTypeCombo.getItems().addAll("⚡ Energy Emission", "🍔 Food Emission", "🚗 Transportation Emission");
        emissionTypeCombo.setValue("⚡ Energy Emission");
        emissionTypeCombo.setPrefWidth(250);
        emissionTypeCombo.setStyle("-fx-font-size: 12;");

        box.getChildren().addAll(label, emissionTypeCombo);
        return box;
    }

    /**
     * Creates energy emission form
     */
    private VBox createEnergyForm() {
        VBox form = new VBox(12);
        form.setPadding(new Insets(15));

        Label formTitle = new Label("⚡ Energy Emission Details");
        formTitle.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: #f39c12;");

        // ID Field
        HBox idBox = createFormField("Emission ID (E-XXX):", new TextField());
        TextField idField = (TextField) ((HBox) idBox.getChildren().get(1)).getChildren().get(0);

        // User Name
        HBox userBox = createFormField("User Name:", new TextField());
        TextField userField = (TextField) ((HBox) userBox.getChildren().get(1)).getChildren().get(0);

        // Date with DatePicker
        HBox dateBox = createDatePickerField("Date:");
        DatePicker datePicker = (DatePicker) ((HBox) dateBox.getChildren().get(1)).getChildren().get(0);

        // kWh Used
        HBox kwhBox = createFormField("kWh Used:", new TextField());
        TextField kwhField = (TextField) ((HBox) kwhBox.getChildren().get(1)).getChildren().get(0);

        // Energy Source
        HBox sourceBox = new HBox(15);
        sourceBox.setPadding(new Insets(10));
        Label sourceLabel = new Label("Energy Source:");
        sourceLabel.setStyle("-fx-font-size: 12; -fx-min-width: 150;");
        ComboBox<String> sourceCombo = new ComboBox<>();
        sourceCombo.getItems().addAll("Grid", "Solar", "Wind");
        sourceCombo.setValue("Grid");
        sourceCombo.setPrefWidth(250);
        sourceBox.getChildren().addAll(sourceLabel, sourceCombo);

        // Submit Button
        Button submitBtn = new Button("Add Energy Emission");
        submitBtn.setStyle("-fx-padding: 12 30 12 30; -fx-font-size: 13; -fx-font-weight: bold; -fx-background-color: #f39c12; -fx-text-fill: white; -fx-cursor: hand;");
        submitBtn.setPrefWidth(200);
        submitBtn.setOnAction(e -> {
            if (validateEnergyFormWithDatePicker(idField, userField, datePicker, kwhField)) {
                try {
                    String id = idField.getText().toUpperCase();
                    String user = userField.getText().toLowerCase();
                    String date = datePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    double kwh = Double.parseDouble(kwhField.getText());
                    String source = sourceCombo.getValue();

                    EnergyEmission emission = new EnergyEmission(id, "Energy", date, user, kwh, source);
                    tracker.addEntry(emission);

                    showSuccess("Energy emission added successfully!");
                    clearEnergyForm(idField, userField, datePicker, kwhField);
                    mainApp.refreshAllPanels();
                } catch (Exception ex) {
                    showError("Error: " + ex.getMessage());
                }
            }
        });

        form.getChildren().addAll(
            formTitle,
            new Separator(),
            idBox,
            userBox,
            dateBox,
            kwhBox,
            sourceBox,
            new VBox(),
            submitBtn
        );

        VBox.setVgrow(form.getChildren().get(form.getChildren().size() - 2), Priority.ALWAYS);
        return form;
    }

    /**
     * Creates food emission form
     */
    private VBox createFoodForm() {
        VBox form = new VBox(12);
        form.setPadding(new Insets(15));

        Label formTitle = new Label("🍔 Food Emission Details");
        formTitle.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: #e74c3c;");

        // ID Field
        HBox idBox = createFormField("Emission ID (F-XXX):", new TextField());
        TextField idField = (TextField) ((HBox) idBox.getChildren().get(1)).getChildren().get(0);

        // User Name
        HBox userBox = createFormField("User Name:", new TextField());
        TextField userField = (TextField) ((HBox) userBox.getChildren().get(1)).getChildren().get(0);

        // Date with DatePicker
        HBox dateBox = createDatePickerField("Date:");
        DatePicker datePicker = (DatePicker) ((HBox) dateBox.getChildren().get(1)).getChildren().get(0);

        // Meal Type
        HBox mealBox = new HBox(15);
        mealBox.setPadding(new Insets(10));
        Label mealLabel = new Label("Meal Type:");
        mealLabel.setStyle("-fx-font-size: 12; -fx-min-width: 150;");
        ComboBox<String> mealCombo = new ComboBox<>();
        mealCombo.getItems().addAll("Vegan", "Vegetarian", "Poultry", "Beef");
        mealCombo.setValue("Vegetarian");
        mealCombo.setPrefWidth(250);
        mealBox.getChildren().addAll(mealLabel, mealCombo);

        // Number of Meals
        HBox mealsBox = createFormField("Number of Meals:", new TextField());
        TextField mealsField = (TextField) ((HBox) mealsBox.getChildren().get(1)).getChildren().get(0);

        // Submit Button
        Button submitBtn = new Button("Add Food Emission");
        submitBtn.setStyle("-fx-padding: 12 30 12 30; -fx-font-size: 13; -fx-font-weight: bold; -fx-background-color: #e74c3c; -fx-text-fill: white; -fx-cursor: hand;");
        submitBtn.setPrefWidth(200);
        submitBtn.setOnAction(e -> {
            if (validateFoodFormWithDatePicker(idField, userField, datePicker, mealsField)) {
                try {
                    String id = idField.getText().toUpperCase();
                    String user = userField.getText().toLowerCase();
                    String date = datePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    int meals = Integer.parseInt(mealsField.getText());
                    String mealType = mealCombo.getValue();

                    FoodEmission emission = new FoodEmission(id, "Food", date, user, mealType, meals);
                    tracker.addEntry(emission);

                    showSuccess("Food emission added successfully!");
                    clearFoodForm(idField, userField, datePicker, mealsField);
                    mainApp.refreshAllPanels();
                } catch (Exception ex) {
                    showError("Error: " + ex.getMessage());
                }
            }
        });

        form.getChildren().addAll(
            formTitle,
            new Separator(),
            idBox,
            userBox,
            dateBox,
            mealBox,
            mealsBox,
            new VBox(),
            submitBtn
        );

        VBox.setVgrow(form.getChildren().get(form.getChildren().size() - 2), Priority.ALWAYS);
        return form;
    }

    /**
     * Creates transportation emission form
     */
    private VBox createTransportForm() {
        VBox form = new VBox(12);
        form.setPadding(new Insets(15));

        Label formTitle = new Label("🚗 Transportation Emission Details");
        formTitle.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: #3498db;");

        // ID Field
        HBox idBox = createFormField("Emission ID (T-XXX):", new TextField());
        TextField idField = (TextField) ((HBox) idBox.getChildren().get(1)).getChildren().get(0);

        // User Name
        HBox userBox = createFormField("User Name:", new TextField());
        TextField userField = (TextField) ((HBox) userBox.getChildren().get(1)).getChildren().get(0);

        // Date with DatePicker
        HBox dateBox = createDatePickerField("Date:");
        DatePicker datePicker = (DatePicker) ((HBox) dateBox.getChildren().get(1)).getChildren().get(0);

        // Distance
        HBox distanceBox = createFormField("Distance (km):", new TextField());
        TextField distanceField = (TextField) ((HBox) distanceBox.getChildren().get(1)).getChildren().get(0);

        // Vehicle Type
        HBox vehicleBox = new HBox(15);
        vehicleBox.setPadding(new Insets(10));
        Label vehicleLabel = new Label("Vehicle Type:");
        vehicleLabel.setStyle("-fx-font-size: 12; -fx-min-width: 150;");
        ComboBox<String> vehicleCombo = new ComboBox<>();
        vehicleCombo.getItems().addAll("Car", "Bus", "Train", "Cycle");
        vehicleCombo.setValue("Car");
        vehicleCombo.setPrefWidth(250);
        vehicleBox.getChildren().addAll(vehicleLabel, vehicleCombo);

        // Submit Button
        Button submitBtn = new Button("Add Transportation Emission");
        submitBtn.setStyle("-fx-padding: 12 30 12 30; -fx-font-size: 13; -fx-font-weight: bold; -fx-background-color: #3498db; -fx-text-fill: white; -fx-cursor: hand;");
        submitBtn.setPrefWidth(200);
        submitBtn.setOnAction(e -> {
            if (validateTransportFormWithDatePicker(idField, userField, datePicker, distanceField)) {
                try {
                    String id = idField.getText().toUpperCase();
                    String user = userField.getText().toLowerCase();
                    String date = datePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    double distance = Double.parseDouble(distanceField.getText());
                    String vehicle = vehicleCombo.getValue();

                    TransportationEmission emission = new TransportationEmission(id, "Transportation", date, user, distance, vehicle);
                    tracker.addEntry(emission);

                    showSuccess("Transportation emission added successfully!");
                    clearTransportForm(idField, userField, datePicker, distanceField);
                    mainApp.refreshAllPanels();
                } catch (Exception ex) {
                    showError("Error: " + ex.getMessage());
                }
            }
        });

        form.getChildren().addAll(
            formTitle,
            new Separator(),
            idBox,
            userBox,
            dateBox,
            distanceBox,
            vehicleBox,
            new VBox(),
            submitBtn
        );

        VBox.setVgrow(form.getChildren().get(form.getChildren().size() - 2), Priority.ALWAYS);
        return form;
    }

    /**
     * Creates a labeled form field
     */
    private HBox createFormField(String label, TextField textField) {
        HBox box = new HBox(15);
        box.setPadding(new Insets(10));
        box.setAlignment(Pos.CENTER_LEFT);

        Label fieldLabel = new Label(label);
        fieldLabel.setStyle("-fx-font-size: 12; -fx-min-width: 150;");

        HBox fieldBox = new HBox();
        textField.setPrefWidth(250);
        textField.setStyle("-fx-padding: 8; -fx-font-size: 12; -fx-border-color: #bdc3c7; -fx-border-radius: 4;");
        fieldBox.getChildren().add(textField);

        box.getChildren().addAll(fieldLabel, fieldBox);
        return box;
    }

    private HBox createDatePickerField(String label) {
        HBox box = new HBox(15);
        box.setPadding(new Insets(10));
        box.setAlignment(Pos.CENTER_LEFT);

        Label fieldLabel = new Label(label);
        fieldLabel.setStyle("-fx-font-size: 12; -fx-min-width: 150;");

        DatePicker datePicker = new DatePicker(LocalDate.now());
        datePicker.setPrefWidth(250);
        datePicker.setStyle("-fx-padding: 8; -fx-font-size: 12;");

        HBox fieldBox = new HBox();
        fieldBox.getChildren().add(datePicker);

        box.getChildren().addAll(fieldLabel, fieldBox);
        return box;
    }

    // Validation Methods
    private boolean validateEnergyForm(TextField id, TextField user, TextField date, TextField kwh) {
        if (id.getText().isEmpty() || user.getText().isEmpty() || date.getText().isEmpty() || kwh.getText().isEmpty()) {
            showError("All fields must be filled!");
            return false;
        }
        
        // Validate ID pattern [EFT]-\d{3}
        if (!id.getText().matches("[EFT]-\\d{3}")) {
            showError("Invalid Emission ID format!\nMust be: E-001, F-123, or T-456\n(Letter dash three digits)");
            return false;
        }
        
        // Validate username - alphabets only
        if (!user.getText().matches("[a-zA-Z]+")) {
            showError("Invalid User Name!\nMust contain only alphabets (a-z, A-Z)");
            return false;
        }
        
        // Validate kWh - must be positive number
        try {
            double kwhValue = Double.parseDouble(kwh.getText());
            if (kwhValue <= 0) {
                showError("kWh must be a positive number!");
                return false;
            }
        } catch (NumberFormatException e) {
            showError("kWh must be a valid number (e.g., 50.5)");
            return false;
        }
        
        return true;
    }

    private boolean validateFoodForm(TextField id, TextField user, TextField date, TextField meals) {
        if (id.getText().isEmpty() || user.getText().isEmpty() || date.getText().isEmpty() || meals.getText().isEmpty()) {
            showError("All fields must be filled!");
            return false;
        }
        
        // Validate ID pattern [EFT]-\d{3}
        if (!id.getText().matches("[EFT]-\\d{3}")) {
            showError("Invalid Emission ID format!\nMust be: E-001, F-123, or T-456\n(Letter dash three digits)");
            return false;
        }
        
        // Validate username - alphabets only
        if (!user.getText().matches("[a-zA-Z]+")) {
            showError("Invalid User Name!\nMust contain only alphabets (a-z, A-Z)");
            return false;
        }
        
        // Validate meals - must be positive integer only
        try {
            int mealCount = Integer.parseInt(meals.getText());
            if (mealCount <= 0) {
                showError("Number of meals must be a positive integer!");
                return false;
            }
        } catch (NumberFormatException e) {
            showError("Number of meals must be a valid integer (e.g., 3)\nLetters are not allowed!");
            return false;
        }
        
        return true;
    }

    private boolean validateTransportForm(TextField id, TextField user, TextField date, TextField distance) {
        if (id.getText().isEmpty() || user.getText().isEmpty() || date.getText().isEmpty() || distance.getText().isEmpty()) {
            showError("All fields must be filled!");
            return false;
        }
        
        // Validate ID pattern [EFT]-\d{3}
        if (!id.getText().matches("[EFT]-\\d{3}")) {
            showError("Invalid Emission ID format!\nMust be: E-001, F-123, or T-456\n(Letter dash three digits)");
            return false;
        }
        
        // Validate username - alphabets only
        if (!user.getText().matches("[a-zA-Z]+")) {
            showError("Invalid User Name!\nMust contain only alphabets (a-z, A-Z)");
            return false;
        }
        
        // Validate distance - must be positive number
        try {
            double distValue = Double.parseDouble(distance.getText());
            if (distValue <= 0) {
                showError("Distance must be a positive number!");
                return false;
            }
        } catch (NumberFormatException e) {
            showError("Distance must be a valid number (e.g., 50.5)");
            return false;
        }
        
        return true;
    }

    // DatePicker Validation Methods
    private boolean validateEnergyFormWithDatePicker(TextField id, TextField user, DatePicker datePicker, TextField kwh) {
        if (id.getText().isEmpty() || user.getText().isEmpty() || kwh.getText().isEmpty()) {
            showError("All fields must be filled!");
            return false;
        }
        
        if (datePicker.getValue() == null) {
            showError("Please select a date!");
            return false;
        }
        
        // Validate ID pattern [EFT]-\d{3}
        if (!id.getText().matches("[EFT]-\\d{3}")) {
            showError("Invalid Emission ID format!\nMust be: E-001, F-123, or T-456\n(Letter dash three digits)");
            return false;
        }
        
        // Validate username - alphabets only
        if (!user.getText().matches("[a-zA-Z]+")) {
            showError("Invalid User Name!\nMust contain only alphabets (a-z, A-Z)");
            return false;
        }
        
        // Validate kWh - must be positive number
        try {
            double kwhValue = Double.parseDouble(kwh.getText());
            if (kwhValue <= 0) {
                showError("Energy consumption must be a positive number!");
                return false;
            }
        } catch (NumberFormatException e) {
            showError("Energy consumption must be a valid number (e.g., 15.5)");
            return false;
        }
        
        return true;
    }

    private boolean validateFoodFormWithDatePicker(TextField id, TextField user, DatePicker datePicker, TextField meals) {
        if (id.getText().isEmpty() || user.getText().isEmpty() || meals.getText().isEmpty()) {
            showError("All fields must be filled!");
            return false;
        }
        
        if (datePicker.getValue() == null) {
            showError("Please select a date!");
            return false;
        }
        
        // Validate ID pattern [EFT]-\d{3}
        if (!id.getText().matches("[EFT]-\\d{3}")) {
            showError("Invalid Emission ID format!\nMust be: E-001, F-123, or T-456\n(Letter dash three digits)");
            return false;
        }
        
        // Validate username - alphabets only
        if (!user.getText().matches("[a-zA-Z]+")) {
            showError("Invalid User Name!\nMust contain only alphabets (a-z, A-Z)");
            return false;
        }
        
        // Validate meals - must be positive integer only
        try {
            int mealCount = Integer.parseInt(meals.getText());
            if (mealCount <= 0) {
                showError("Number of meals must be a positive integer!");
                return false;
            }
        } catch (NumberFormatException e) {
            showError("Number of meals must be a valid integer (e.g., 3)\nLetters are not allowed!");
            return false;
        }
        
        return true;
    }

    private boolean validateTransportFormWithDatePicker(TextField id, TextField user, DatePicker datePicker, TextField distance) {
        if (id.getText().isEmpty() || user.getText().isEmpty() || distance.getText().isEmpty()) {
            showError("All fields must be filled!");
            return false;
        }
        
        if (datePicker.getValue() == null) {
            showError("Please select a date!");
            return false;
        }
        
        // Validate ID pattern [EFT]-\d{3}
        if (!id.getText().matches("[EFT]-\\d{3}")) {
            showError("Invalid Emission ID format!\nMust be: E-001, F-123, or T-456\n(Letter dash three digits)");
            return false;
        }
        
        // Validate username - alphabets only
        if (!user.getText().matches("[a-zA-Z]+")) {
            showError("Invalid User Name!\nMust contain only alphabets (a-z, A-Z)");
            return false;
        }
        
        // Validate distance - must be positive number
        try {
            double distValue = Double.parseDouble(distance.getText());
            if (distValue <= 0) {
                showError("Distance must be a positive number!");
                return false;
            }
        } catch (NumberFormatException e) {
            showError("Distance must be a valid number (e.g., 50.5)");
            return false;
        }
        
        return true;
    }

    // Clearing Methods
    private void clearEnergyForm(TextField id, TextField user, DatePicker datePicker, TextField kwh) {
        id.clear();
        user.clear();
        datePicker.setValue(LocalDate.now());
        kwh.clear();
    }

    private void clearFoodForm(TextField id, TextField user, DatePicker datePicker, TextField meals) {
        id.clear();
        user.clear();
        datePicker.setValue(LocalDate.now());
        meals.clear();
    }

    private void clearTransportForm(TextField id, TextField user, DatePicker datePicker, TextField distance) {
        id.clear();
        user.clear();
        datePicker.setValue(LocalDate.now());
        distance.clear();
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public VBox getPanel() {
        return panel;
    }

    public void refresh() {
        // No refresh needed for input form
    }
}
