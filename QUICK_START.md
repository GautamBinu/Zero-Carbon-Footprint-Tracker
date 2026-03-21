# GreenPrint GUI - Quick Start Guide

## 🚀 5-Minute Setup

### For macOS/Linux Users

1. **Install JavaFX SDK (if not installed)**
   ```bash
   brew install javafx-sdk
   ```

2. **Navigate to project directory**
   ```bash
   cd /Users/dhruvmer/Desktop/newAIcarbonemissoin
   ```

3. **Make script executable and run**
   ```bash
   chmod +x run_gui.sh
   ./run_gui.sh
   ```

### For Windows Users

1. **Install JavaFX SDK**
   - Download from: https://gluonhq.com/products/javafx/
   - Extract to: `C:\Program Files\javafx-sdk`

2. **Navigate to project directory**
   ```cmd
   cd C:\Users\YourUsername\Desktop\newAIcarbonemissoin
   ```

3. **Run the batch file**
   ```cmd
   run_gui.bat
   ```

### Manual Compilation & Execution

If scripts don't work, compile and run manually:

```bash
# Replace /path/to/javafx-sdk with your JavaFX path
javac *.java --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml
java --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml GreenPrintGUI
```

---

## 📖 First Steps in the Application

### Tab 1: Dashboard 📊
- **What you see**: Overview statistics including total emissions, number of entries, active users, and top emitter
- **Green boxes**: Breaking down emissions by type (Energy, Food, Transportation)
- **Blue boxes**: User-wise emission summaries

### Tab 2: Add Emission ➕
- **Step 1**: Select emission type from dropdown (Energy, Food, or Transportation)
- **Step 2**: Fill in the form with your details
  - Must use format like "E-001", "F-123", "T-456"
  - Date format: "2026-03-21"
- **Step 3**: Click submit button - green = Energy, red = Food, blue = Transportation

### Tab 3: View Emissions 📋
- **See all**: Table showing every emission entry
- **Filter**: Select a user to see only their emissions
- **Export**: Copy summary data to clipboard

### Tab 4: Reports 📈
- **By Type**: See pie chart breakdown (Energy, Food, Transport)
- **By User**: Compare emissions across users
- **Daily**: Detailed daily report with subtotals

### Tab 5: Offsets ♻️
- **Calculate**: Enter CO2 amount to offset
- **Cost**: Automatic cost calculation at $0.015 per kg
- **Purchase**: Select payment method and complete transaction

---

## 💡 Pro Tips

1. **Bulk Import**: To add multiple entries at once, prepare a CSV and manually enter each row
2. **Data Backup**: Your data is saved to `greenprint_save_state.txt` automatically
3. **Check Logs**: View all operations in `greenprint_log.txt`
4. **Peak Hours**: Use during off-peak times for best performance

---

## ⚠️ Common Issues & Fixes

| Issue | Solution |
|-------|----------|
| "Module not found" error | Ensure JavaFX SDK path is correct in script |
| Application won't start | Check Java version (need 17+): `java -version` |
| Data not saving | Verify file permissions in project directory |
| UI looks broken | Update JavaFX to latest version |

---

## 📊 Example Usage

### Adding an Energy Emission
```
Type: Energy Emission
ID: E-001
User: john
Date: 2026-03-21
kWh: 45.5
Source: Grid
Result: 45.5 × 0.404 = 18.38 kg CO2
```

### Adding a Food Emission
```
Type: Food Emission
ID: F-001
User: jane
Date: 2026-03-21
Meal Type: Vegetarian
Meals: 3
Result: 3 × 0.09 = 0.27 kg CO2
```

### Adding a Transportation Emission
```
Type: Transportation
ID: T-001
User: bob
Date: 2026-03-21
Distance: 50 km
Vehicle: Car
Result: 50 × 0.12 = 6.0 kg CO2
```

---

## 🎨 Features Highlight

✨ **Modern UI**
- Professional green/blue color scheme
- Responsive layout with smooth animations
- Intuitive tab-based navigation

📊 **Real-time Analytics**
- Live statistics updates
- Visual progress bars
- Comparative charts

🔒 **Data Management**
- Automatic persistence
- Operation logging
- User filtering

♻️ **Carbon Offsets**
- Instant cost calculation
- Multiple payment methods
- Receipt generation

---

## 📞 Support

For detailed documentation, see: `GUI_SETUP_GUIDE.md`

For code reference, check inline documentation in each `.java` file

---

**Happy Carbon Tracking! 🌍🌱**
