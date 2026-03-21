# 🌍 GreenPrint GUI - Professional Carbon Footprint Tracker

A beautiful, modern JavaFX-based GUI application for tracking and managing carbon emissions across energy, food, and transportation categories.

## 🎯 What's New

Your professional, production-ready GUI has been created with **7 new Java classes** and **2,000+ lines of code**.

### New Files Created:
- ✅ **GreenPrintGUI.java** - Main application window
- ✅ **DashboardPanel.java** - Statistics dashboard
- ✅ **AddEmissionPanel.java** - Emission input forms
- ✅ **EmissionsViewerPanel.java** - Data browser
- ✅ **ReportsPanel.java** - Analytics & reports
- ✅ **OffsetsPanel.java** - Carbon offset calculator
- ✅ **GreenPrintCLI.java** - Application initializer
- ✅ **run_gui.sh / run_gui.bat** - Launch scripts
- ✅ **Documentation files** - Complete guides

### All Protected Files Remain Unchanged:
- EmissionSource.java ✓
- EnergyEmission.java ✓
- FoodEmission.java ✓
- TransportationEmission.java ✓
- FootprintTracker.java ✓

---

## 🚀 Quick Start (3 Steps)

### 1️⃣ Ensure JavaFX is Installed
```bash
# macOS
brew install javafx-sdk

# Windows: Download from https://gluonhq.com/products/javafx/
```

### 2️⃣ Navigate to Project
```bash
cd /Users/dhruvmer/Desktop/newAIcarbonemissoin
```

### 3️⃣ Run the Launcher Script
```bash
# macOS/Linux
chmod +x run_gui.sh && ./run_gui.sh

# Windows
run_gui.bat
```

---

## 📊 Key Features

| Feature | Description |
|---------|-------------|
| **📊 Dashboard** | Real-time statistics, emissions breakdown, user summary |
| **➕ Add Entry** | Easy-to-use forms for Energy, Food, Transportation |
| **📋 View Data** | Filterable table with user-based filtering |
| **📈 Reports** | Detailed analytics with progress visualizations |
| **♻️ Offsets** | Cost calculator, receipt generator, payment tracking |
| **🎨 Modern UI** | Professional green/blue design, responsive layout |
| **💾 Data Sync** | Automatic save/load, operation logging |

---

## 📖 Documentation

Three comprehensive guides are included:

1. **QUICK_START.md** - Get running in 5 minutes
2. **GUI_SETUP_GUIDE.md** - Complete setup & feature reference  
3. **BUILD_SUMMARY.md** - Technical details & architecture

---

## 🎨 UI Highlights

✨ **Professional Design**
- Modern, clean interface
- Green/blue color scheme
- Intuitive tab navigation
- Real-time updates
- Responsive layouts

📱 **User Experience**
- Emoji icons for quick recognition
- Form validation with error messages
- Success confirmations
- Data export functionality
- Receipt generation

---

## 📈 What You Can Track

### ⚡ Energy
- Grid, Solar, Wind sources
- kWh consumption
- Automatic CO2 calculation

### 🍔 Food
- Vegan, Vegetarian, Poultry, Beef
- Meal counting
- Dietary impact tracking

### 🚗 Transportation
- Car, Bus, Train, Cycle
- Distance tracking
- Mode comparison

### ♻️ Carbon Offsets
- Real-time cost calculation
- Multiple payment methods
- Receipt generation
- Purchase tracking

---

## 💻 System Requirements

- **Java**: 17 or higher
- **JavaFX SDK**: 17 or higher
- **RAM**: 512 MB minimum
- **Disk**: 50 MB available
- **OS**: macOS, Linux, or Windows

---

## 🔧 Manual Compile & Run

If scripts don't work:

```bash
# Compile (replace path to your JavaFX SDK)
javac *.java --module-path /path/to/javafx-sdk/lib \
  --add-modules javafx.controls,javafx.fxml

# Run
java --module-path /path/to/javafx-sdk/lib \
  --add-modules javafx.controls,javafx.fxml GreenPrintGUI
```

---

## 📁 Project Structure

```
newAIcarbonemissoin/
├── Core Classes (Protected)
│   ├── EmissionSource.java
│   ├── EnergyEmission.java
│   ├── FoodEmission.java
│   ├── TransportationEmission.java
│   ├── FootprintTracker.java
│   ├── Logger.java
│   ├── Offsets.java
│   └── EmissionIDValidator.java
│
├── GUI Application (New)
│   ├── GreenPrintGUI.java
│   ├── GreenPrintCLI.java
│   ├── DashboardPanel.java
│   ├── AddEmissionPanel.java
│   ├── EmissionsViewerPanel.java
│   ├── ReportsPanel.java
│   └── OffsetsPanel.java
│
├── Launch Scripts
│   ├── run_gui.sh
│   └── run_gui.bat
│
└── Documentation
    ├── README.md (this file)
    ├── QUICK_START.md
    ├── GUI_SETUP_GUIDE.md
    └── BUILD_SUMMARY.md
```

---

## 🎯 Usage Example

### Adding an Entry
1. Click **"➕ Add Emission"** tab
2. Select emission type
3. Fill in the form
4. Click submit
5. See it reflected in Dashboard

### Viewing Data
1. Click **"📋 View Emissions"** tab
2. Filter by user (optional)
3. Sort and explore data
4. Export summary if needed

### Analyzing Impact
1. Click **"📈 Reports"** tab
2. View breakdown charts
3. Compare users
4. Check daily summaries

### Offsetting Emissions
1. Click **"♻️ Carbon Offsets"** tab
2. Enter amount to offset
3. Review cost
4. Select payment method
5. Complete purchase

---

## 🐛 Troubleshooting

**Issue: "Module not found"**
- Ensure JavaFX SDK path is correct in script

**Issue: Application won't start**
- Check Java version: `java -version`
- Verify JavaFX installation

**Issue: Data not saving**
- Check write permissions in project directory
- Verify file paths in Logger.java

For more help, see **GUI_SETUP_GUIDE.md**

---

## 📊 Technical Specs

| Aspect | Details |
|--------|---------|
| Framework | JavaFX 17+ |
| Language | Java 17+ |
| Pattern | MVC-inspired |
| Data Store | Text files (CSV-like) |
| UI Updates | Real-time |
| Persistence | Automatic |
| Lines of Code | 2,000+ |
| Classes | 7 new + 8 existing |

---

## 🌟 Highlights

✅ **No Core Files Modified** - All original files untouched  
✅ **Production Ready** - Professional quality code  
✅ **Well Documented** - Comprehensive guides included  
✅ **Cross-Platform** - Works on macOS, Linux, Windows  
✅ **User Friendly** - Intuitive interface  
✅ **Extensible** - Easy to add new features  
✅ **Data Persistent** - Automatic backup  
✅ **Beautiful Design** - Modern, professional styling  

---

## 📞 Support

- **Quick Questions**: See `QUICK_START.md`
- **Detailed Help**: See `GUI_SETUP_GUIDE.md`
- **Technical Details**: See `BUILD_SUMMARY.md`
- **Code Reference**: Check comments in `.java` files

---

## 🎉 You're All Set!

Your professional GreenPrint GUI is complete and ready to use. 

**Next Step**: Run `./run_gui.sh` (macOS/Linux) or `run_gui.bat` (Windows)

**Happy Carbon Tracking! 🌍🌱**

---

**Version**: 1.0.0  
**Status**: ✅ Complete & Ready  
**Date**: March 21, 2026
