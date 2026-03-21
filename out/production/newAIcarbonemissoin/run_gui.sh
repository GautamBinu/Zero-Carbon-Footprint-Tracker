#!/bin/bash
# GreenPrint GUI - Compilation and Execution Script
# This script compiles and runs the GreenPrint GUI application

# Color codes for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}  GreenPrint GUI - Setup & Launch${NC}"
echo -e "${BLUE}========================================${NC}"

# Check if we're on macOS or Linux
if [[ "$OSTYPE" == "darwin"* ]]; then
    # macOS
    JAVAFX_PATH="/usr/local/Cellar/javafx-sdk/lib"
    echo -e "${GREEN}✓ Detected macOS${NC}"
elif [[ "$OSTYPE" == "linux-gnu"* ]]; then
    # Linux
    JAVAFX_PATH="/opt/javafx-sdk/lib"
    echo -e "${GREEN}✓ Detected Linux${NC}"
else
    # Windows (MSYS or Cygwin)
    JAVAFX_PATH="C:\\Program Files\\javafx-sdk\\lib"
    echo -e "${GREEN}✓ Detected Windows${NC}"
fi

echo -e "${BLUE}JavaFX SDK Path: ${JAVAFX_PATH}${NC}"

# Check if JavaFX SDK exists
if [ ! -d "$JAVAFX_PATH" ]; then
    echo -e "${RED}✗ JavaFX SDK not found at: $JAVAFX_PATH${NC}"
    echo -e "${BLUE}Please install JavaFX SDK and update the path in this script${NC}"
    exit 1
fi

echo -e "${GREEN}✓ JavaFX SDK found${NC}"

# Compile the project
echo -e "${BLUE}\n[1/2] Compiling Java files...${NC}"
javac *.java --module-path "$JAVAFX_PATH" --add-modules javafx.controls,javafx.fxml

if [ $? -eq 0 ]; then
    echo -e "${GREEN}✓ Compilation successful${NC}"
else
    echo -e "${RED}✗ Compilation failed${NC}"
    exit 1
fi

# Run the application
echo -e "${BLUE}\n[2/2] Launching GreenPrint GUI...${NC}"
java --module-path "$JAVAFX_PATH" --add-modules javafx.controls,javafx.fxml GreenPrintGUI

echo -e "${BLUE}\n========================================${NC}"
echo -e "${GREEN}Application closed${NC}"
echo -e "${BLUE}========================================${NC}"
