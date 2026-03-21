# GreenPrint GUI - Setup and User Guide

## Overview

GreenPrint is a professional, modern Java-based GUI application for tracking and managing carbon emissions. It provides a comprehensive platform for individuals and organizations to monitor their carbon footprint across energy, food, and transportation categories.

## Architecture

### Core Classes (Unchanged - Do NOT Modify)
- **EmissionSource.java** - Abstract base class for all emission types
- **EnergyEmission.java** - Energy consumption emissions
- **FoodEmission.java** - Food consumption emissions
- **TransportationEmission.java** - Transportation emissions
- **FootprintTracker.java** - Core tracking engine

### New GUI Components
- **GreenPrintCLI.java** - Application initialization and static tracker reference
- **GreenPrintGUI.java** - Main application window and tab management
- **DashboardPanel.java** - Statistics and overview dashboard
- **AddEmissionPanel.java** - Form for adding new emissions
- **EmissionsViewerPanel.java** - Table view and filtering
- **ReportsPanel.java** - Detailed analytics and reports
- **OffsetsPanel.java** - Carbon offset calculator

### Supporting Classes
- **Logger.java** - Logging and persistence
- **Offsets.java** - Offset calculations
- **EmissionIDValidator.java** - ID validation

## Features

### 📊 Dashboard
- Real-time statistics overview
- Total emissions summary
- Emissions breakdown by type (Energy, Food, Transportation)
- User statistics and top emitter identification
- Visual progress indicators

### ➕ Add Emissions
- **Energy Emissions**: Track kWh usage with source selection (Grid, Solar, Wind)
- **Food Emissions**: Log meals by type (Vegan, Vegetarian, Poultry, Beef)
- **Transportation**: Record distance traveled by vehicle type (Car, Bus, Train, Cycle)
- Built-in validation and error checking
- Automatic ID validation and duplicate prevention

### 📋 View Emissions
- Comprehensive table view of all emissions
- Filter by user
- Sort and search capabilities
- Summary statistics
- Export functionality

### 📈 Reports
- Detailed breakdowns by emission type
- User-wise emissions analysis
- Progress visualizations
- Daily report generation
- Historical tracking

### ♻️ Carbon Offsets
- Offset cost calculator
- Multiple payment methods (Credit Card, Digital Wallet, Bank Transfer)
- Purchase history tracking
- Receipt generation

## Installation & Setup

### Prerequisites
- Java 17 or higher
- JavaFX SDK 17 or higher

### Step 1: Ensure Files are in Place

The following files must be in your project directory:
```
/Users/dhruvmer/Desktop/newAIcarbonemissoin/
├── EmissionSource.java
├── EnergyEmission.java
├── FoodEmission.java
├── TransportationEmission.java
├── FootprintTracker.java
├── Logger.java
├── Offsets.java
├── EmissionIDValidator.java
├── GreenPrintCLI.java (new)
├── GreenPrintGUI.java (new)
├── DashboardPanel.java (new)
├── AddEmissionPanel.java (new)
├── EmissionsViewerPanel.java (new)
├── ReportsPanel.java (new)
└── OffsetsPanel.java (new)
```

### Step 2: Compile the Project

From the project directory, compile all Java files:

```bash
javac *.java --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml
```

Replace `/path/to/javafx-sdk/lib` with your JavaFX SDK lib path.

**On macOS:**
```bash
javac *.java --module-path /usr/local/Cellar/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml
```

**On Windows:**
```bash
javac *.java --module-path "C:\Program Files\javafx-sdk\lib" --add-modules javafx.controls,javafx.fxml
```

**On Linux:**
```bash
javac *.java --module-path /opt/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml
```

### Step 3: Run the Application

```bash
java --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml GreenPrintGUI
```

## Usage Guide

### Adding Your First Emission

1. **Navigate to "➕ Add Emission" tab**
2. **Select Emission Type**: Choose between Energy, Food, or Transportation
3. **Fill in Details**:
   - **Emission ID**: Format prefixes (E-, F-, or T-) followed by 3 digits (e.g., E-001)
   - **User Name**: Your name or identifier
   - **Date**: YYYY-MM-DD format
   - **Type-Specific Fields**: Vary by emission category
4. **Submit**: Click the type-specific submit button

### Viewing Your Data

1. **Dashboard**: See aggregate statistics and trends
2. **View Emissions**: Browse all entries in a filterable table
3. **Reports**: Analyze data by type and user with visualizations

### Managing Carbon Offsets

1. Navigate to **♻️ Carbon Offsets** tab
2. Enter the amount of CO2 you want to offset (in kg)
3. Click "🧮 Calculate Cost" to see pricing
4. Select payment method
5. Click "💰 Purchase Offsets" to complete purchase
6. A formatted receipt will be generated and logged

## Data Persistence

The application automatically:
- Saves all entries to `greenprint_save_state.txt`
- Logs all operations to `greenprint_log.txt`
- Loads previously saved data on startup
- Updates files on application exit

## Emission Factors (Used in Calculations)

### Energy (kg CO2 per kWh)
- Grid: 0.404
- Solar: 0.050
- Wind: 0.025

### Food (kg CO2 per meal)
- Vegan: 0.07
- Vegetarian: 0.09
- Poultry: 1.29
- Beef: 2.04

### Transportation (kg CO2 per km)
- Car: 0.120
- Bus: 0.060
- Train: 0.045
- Cycle: 0.0

## Styling & Customization

The GUI uses a professional green and blue color scheme:
- **Primary Green**: #2ecc71 (for positive actions)
- **Secondary Blue**: #3498db (for information)
- **Alert Red**: #e74c3c (for warnings)
- **Accent Orange**: #f39c12 (for energy)

All styling is defined in the `applyStylesheet()` method of GreenPrintGUI.java and can be modified.

## File Structure

### Configuration Files
- `greenprint_log.txt` - Application operation log
- `greenprint_save_state.txt` - Persistent emission data

### Log Format
Each operation is logged with timestamp:
```
{OPERATION_TYPE} : Details : [TIMESTAMP]
```

Example:
```
{ENTRY_ADDED} : E-001 | john | Energy | 2024-06-01 | Grid, 50.0 kWh | 20.20 kg CO2 : [2024-06-01T10:30:45.123456789]
```

## Troubleshooting

### Issue: "Module not found: javafx.controls"
**Solution**: Ensure JavaFX SDK path is correctly set in compilation and execution commands

### Issue: "Connection refused" when loading data
**Solution**: Check that the log and state file paths in Logger.java match your system's directory structure

### Issue: Duplicate ID not caught
**Solution**: IDs must match the pattern `[EFT]-\d{3}`. Verify your ID format is correct

### Issue: Calculations seem off
**Solution**: Verify the emission factors in the calculation methods match the requirements

## Extending the Application

To add new features:

1. **New Emission Type**: Create a new class extending `EmissionSource`
2. **New Report**: Add a new method to `ReportsPanel.java`
3. **New Panel**: Create a new class and add it to `GreenPrintGUI.java`
4. **Custom Styling**: Modify the CSS in `GreenPrintGUI.applyStylesheet()`

## API Reference

### GreenPrintCLI
```java
GreenPrintCLI.getTracker()      // Get tracker instance
GreenPrintCLI.getLogger()       // Get logger instance
GreenPrintCLI.saveApplication() // Save state
```

### FootprintTracker
```java
tracker.addEntry(emission)                    // Add new entry
tracker.getEmissions()                        // Get all entries
tracker.GetTotalEmissions()                   // Total CO2
tracker.GetTotalEmissionsForUser(userName)    // User total
tracker.getUniqueUsers()                      // List users
tracker.getEntriesByUser(userName)            // User entries
tracker.getHighestTotalEmissionUser()          // Top emitter
```

### Offsets
```java
Offsets.calculateOffsetCost(emissionsKg)      // Get cost
Offsets.getOffsetRatePerKg()                  // Get rate
Offsets.getOffsetReceipt(amount, method, user) // Generate receipt
```

## Performance Notes

- Application loads all data into memory at startup
- Large datasets (>10,000 entries) may experience slight UI delays
- Database integration recommended for production deployments

## License & Support

This is an educational project for the GreenPrint Carbon Footprint Tracking System.

For support or questions, refer to the inline code documentation in each Java class.

---

**Version**: 1.0.0  
**Last Updated**: March 21, 2026
