## Zero Carbon Footprint Tracker

A Java-based command-line application designed to track and calculate carbon emissions across multiple categories for different users. This tool helps organizations and individuals monitor their environmental impact by tracking emissions from energy consumption, food choices, and transportation.

📋 Overview
The Zero Carbon Footprint Tracker provides a structured approach to measuring carbon dioxide (CO2) emissions across three key areas:

Energy Consumption: Track emissions from different energy sources (Grid, Solar, Wind)
Food Choices: Calculate emissions based on meal types (Vegan, Vegetarian, Poultry, Beef)
Transportation: Monitor emissions from various vehicle types (Car, Bus, Train, Cycle)

🌟 Features

Multi-User Support: Track emissions for multiple users simultaneously
Category-Based Tracking: Organize emissions by Energy, Food, and Transportation
Flexible Emission Calculation: Each category uses specific emission factors based on real-world data
Daily Reporting: Generate comprehensive reports showing per-user and total emissions
User-Specific Analytics: Calculate total emissions for individual users
Exception Handling: Robust error management for invalid inputs

🏗️ Project Structure

Zero-Carbon-Footprint-Tracker/
├── EmissionSource.java              # Abstract base class for all emission types
├── EnergyEmission.java              # Energy consumption emissions
├── FoodEmission.java                # Food-related emissions
├── TransportationEmission.java      # Transportation emissions
├── FootprintTracker.java            # Core tracking and reporting logic
└── GreenPrintCLI.java               # Command-line interface and entry point


📊 Emission Factors

Energy Sources
Source	Emission Factor (kg CO2/kWh)
Grid	0.404
Solar	0.050
Wind	0.025

Meal Types
Meal Type	Emission Factor (kg CO2/meal)
Vegan	0.07
Vegetarian	0.09
Poultry	1.29
Beef	2.04

Vehicle Types
Vehicle	Emission Factor (kg CO2/km)
Car	0.120
Bus	0.060
Train	0.045
Cycle	0.000

🚀 Getting Started

Prerequisites
Java Development Kit (JDK) 8 or higher
A Java IDE or command-line compiler

👥 Contributors

Made by Gautam, Dhruv, Catherine and Reya

