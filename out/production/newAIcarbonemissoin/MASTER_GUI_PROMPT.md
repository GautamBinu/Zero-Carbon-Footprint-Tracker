# Master Prompt: Zero-Carbon Footprint Tracker - GUI Development

## ⚠️ CRITICAL: DO NOT MODIFY THESE FILES
The following files serve as the foundation and must **NEVER** be changed or modified:
- `EmissionSource.java`
- `EnergyEmission.java`
- `FoodEmission.java`
- `TransportationEmission.java`
- `FootprintTracker.java`

## Core Architecture Reference

### 1. EmissionSource (Abstract Base Class)
```java
public abstract class EmissionSource {
    private String sourceID;
    private String category;
    private String date;
    private String userName;

    public EmissionSource(String sourceID, String category, String date, String userName)
    public String getSourceID() 
    public void setSourceID(String sourceID)
    public String getCategory()
    public void setCategory(String category)
    public String getDate()
    public void setDate(String date)
    public String getUserName()
    public void setUserName(String userName)
    public abstract double calculateEmission() // Returns kg CO2
    public String toString() // Format: "sourceID | userName | category | date"
}
```

### 2. EnergyEmission (extends EmissionSource)
```java
public class EnergyEmission extends EmissionSource{
    private double kWhused;
    private String energySource; // "grid", "solar", "wind"
    
    // Emission Factors (kg CO2 per kWh):
    // - Grid: 0.404
    // - Solar: 0.050
    // - Wind: 0.025
    
    public EnergyEmission(String sourceID, String category, String date, 
                         String userName, double kWhused, String energySource)
    public double getKwhused()
    public void setKwhused(double kWhused)
    public String getEnergySource()
    public void setEnergySource(String energySource)
    public double calculateEmission() // Returns: kWhused * energyFactor
}
```

### 3. FoodEmission (extends EmissionSource)
```java
public class FoodEmission extends EmissionSource {
    private String mealType; // "vegan", "vegetarian", "poultry", "beef"
    private int numberOfMeals;
    
    // Emission Factors (kg CO2 per meal):
    // - Vegan: 0.67
    // - Vegetarian: 0.80
    // - Poultry: 2.40
    // - Beef: 3.02
    
    public FoodEmission(String sourceID, String category, String date, 
                       String userName, String mealType, int numberOfMeals)
    public String getMealType()
    public void setMealType(String mealType)
    public int getNumberOfMeals()
    public void setNumberOfMeals(int numberOfMeals)
    public double calculateEmission() // Returns: numberOfMeals * mealFactor
}
```

### 4. TransportationEmission (extends EmissionSource)
```java
public class TransportationEmission extends EmissionSource {
    private String vehicleType; // "car", "bus", "train", "cycle"
    private double distanceKM;
    
    // Emission Factors (kg CO2 per km):
    // - Car: 0.120
    // - Bus: 0.060
    // - Train: 0.045
    // - Cycle: 0.0
    
    public TransportationEmission(String sourceID, String category, String date, 
                                 String userName, double distanceKM, String vehicleType)
    public double getDistanceKM()
    public void setDistanceKM(double distanceKM)
    public String getVehicleType()
    public void setVehicleType(String vehicleType)
    public double calculateEmission() // Returns: distanceKM * emissionFactor
}
```

### 5. FootprintTracker (Main Data Management Class)
```java
public class FootprintTracker {
    private String trackerName = "RIT GreenPrint 2026";
    private ArrayList<EmissionSource> emissions;
    
    public FootprintTracker()
    public ArrayList<EmissionSource> getEmissions()
    public void setEmissions(ArrayList<EmissionSource> loadedEmissions)
    public void addEntry(EmissionSource entry) // Logs entry addition
    public Integer getTotalEntries() // Returns size of emissions list
    public ArrayList<EmissionSource> getEntriesByUser(String userName)
    public double GetTotalEmissions() // Returns total kg CO2 for all entries
    public double GetTotalEmissionsForUser(String userName) // Returns total kg CO2 for specific user
    public List<String> extractID() // Returns list of valid emission IDs matching pattern [EFT]-\\d{3}
    public String TypeofEmission(EmissionSource entry) // Returns formatted string with type-specific details
}
```

---

## GUI Development Guidelines

### Allowed Operations
✅ **You CAN:**
- Create new GUI classes (e.g., `EnergyEmissionGUI.java`, `FoodEmissionGUI.java`, `TransportationEmissionGUI.java`)
- Create input dialogs, forms, and data entry screens
- Create visualization components (charts, graphs, dashboards)
- Extend existing GUI files like `GreenPrintCLI.java`, `Dashboard.java`, `DataOperationIO.java`, `OffsetTransactionGUI.java`
- Create new utility classes for GUI-specific functionality
- Call methods from the core classes to retrieve and display data
- Add event listeners and interaction handlers
- Create data validation classes (separate from core emission classes)

### Prohibited Operations
❌ **You CANNOT:**
- Modify the calculation logic in any emission class
- Add new fields to EmissionSource, EnergyEmission, FoodEmission, or TransportationEmission
- Change method signatures in the core classes
- Modify FootprintTracker's core business logic
- Move constructor parameters around
- Change property types (e.g., double to float, String to int)

---

## Static Reference: Global Tracker Instance

```java
// Available in GreenPrintCLI.java
public static FootprintTracker tracker = new FootprintTracker();
```

**Usage Pattern:**
```java
// Adding entries to the global tracker
GreenPrintCLI.tracker.addEntry(new EnergyEmission(...));
GreenPrintCLI.tracker.addEntry(new FoodEmission(...));
GreenPrintCLI.tracker.addEntry(new TransportationEmission(...));

// Retrieving data
ArrayList<EmissionSource> allEmissions = GreenPrintCLI.tracker.getEmissions();
double totalCO2 = GreenPrintCLI.tracker.GetTotalEmissions();
ArrayList<EmissionSource> userEntries = GreenPrintCLI.tracker.getEntriesByUser("username");
```

---

## Data Flow Architecture

```
GUI Layer (JavaFX)
    ↓
Create EmissionSource instances
    ↓
Add to FootprintTracker.tracker via addEntry()
    ↓
Retrieve/Display via FootprintTracker methods
    ↓
Logger (logs all operations)
```

---

## ID Generation Rules

Valid emission IDs follow pattern: `[EFT]-\d{3}`
- `E`: Energy Emission
- `F`: Food Emission
- `T`: Transportation Emission
- Examples: `E-001`, `F-042`, `T-156`

---

## Key Integration Points

### 1. Data Entry Forms
Create GUI forms that accept:
- **Energy:** username, date, kWh, energySource
- **Food:** username, date, mealType, numberOfMeals
- **Transportation:** username, date, vehicleType, distanceKM

### 2. Display/Dashboard
Show:
- Total emissions (all users)
- Emissions by user
- Emissions by type
- Individual entry details

### 3. Data Modification
- Allow editing existing entries (create new versions, don't modify in-place)
- Allow deleting entries (remove from tracker)
- Implement confirmation dialogs

### 4. Persistence
- Leverage existing `Logger` and `DataOperationIO` classes
- Save state when application closes (see `GreenPrintCLI.start()`)
- Load state on startup (see `GreenPrintCLI.main()`)

---

## JavaFX Tab Structure (Current Pattern)

```java
GreenPrintCLI (extends Application)
    ├── Dashboard Tab (Dashboard.createDashboard())
    ├── Data Operations Tab (DataOperationIO.DataOperationsTab())
    └── Offset Transactions Tab (OffsetTransactionGUI.createOffsetTabPane())
```

**Refresh Pattern:**
```java
GreenPrintCLI.refreshDashboard(); // Call after data changes
```

---

## Best Practices for New GUI Components

1. **Error Handling**: Always wrap data access in try-catch blocks
2. **Type Safety**: Explicitly create correct emission type (use instanceof checks when needed)
3. **User Experience**: 
   - Provide clear error messages
   - Disable submit buttons until valid input
   - Show calculation results immediately
   - Use color/visual indicators (🟢 success, 🔴 error)
4. **State Management**: 
   - Always use `GreenPrintCLI.tracker` as single source of truth
   - Call `GreenPrintCLI.refreshDashboard()` after modifications
5. **Testing**: Create entries and verify with `tracker.GetTotalEmissions()`

---

## Example: Adding a New Entry (Template)

```java
// DO THIS:
try {
    EnergyEmission entry = new EnergyEmission(
        "E-001",
        "Energy",
        "2024-03-21",
        "john_doe",
        25.5,
        "solar"
    );
    GreenPrintCLI.tracker.addEntry(entry);
    double emissions = entry.calculateEmission(); // 25.5 * 0.050 = 1.275 kg CO2
    GreenPrintCLI.refreshDashboard();
} catch (IllegalArgumentException e) {
    System.err.println("Invalid emission data: " + e.getMessage());
}

// DON'T DO THIS:
// Don't try to modify EnergyEmission internal logic
// Don't create custom calculateEmission methods
// Don't extend emission classes for GUI purposes
```

---

## Summary

**Your GUI code will:**
1. Create instances of the emission classes with valid parameters
2. Add them to `GreenPrintCLI.tracker`
3. Retrieve data using FootprintTracker methods
4. Display results in JavaFX components
5. Call `GreenPrintCLI.refreshDashboard()` to update views

**The core emission classes remain untouched**, serving as reliable, tested business logic that your GUI consumes.

---

## Questions to Validate Your Approach

Before implementing any new GUI feature, ask:
- [ ] Does this feature create new EmissionSource instances without modifying the class?
- [ ] Does this feature retrieve data using FootprintTracker public methods?
- [ ] Does this feature use the global `GreenPrintCLI.tracker` instance?
- [ ] Do all calculated values come from the `calculate*()` methods?
- [ ] Does the feature properly refresh the dashboard after changes?
- [ ] Are all error messages helpful to the user?
