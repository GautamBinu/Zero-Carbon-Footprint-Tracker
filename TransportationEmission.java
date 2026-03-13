public class TransportationEmission extends EmissionSource {
   
    private String vehicleType; // e.g., "Car", "Bus", "Train"
    private double distanceKM; // Distance traveled in kilometers

    /**
     * Constructor for TransportationEmission
     * @param sourceID
     * @param category
     * @param date
     * @param userName
     * @param distanceKM
     * @param vehicleType
     */

    // Constructor
    public TransportationEmission(String sourceID, String category, String date, String userName, double distanceKM, String vehicleType) {
        super(sourceID, category, date, userName); // already initializes the common attributes from EmissionSource
        this.distanceKM = distanceKM; // initialize the distance attribute
        this.vehicleType = vehicleType; // initialize the vehicleType attribute
    }

    /**
     * Get the value of distanceKM and VehicleType
     * @return the distance traveled in kilometers and the type of vehicle used for transportation.
     */

    // Getters and Setters
    public double getDistanceKM() {
        return distanceKM;
    }

    public void setDistanceKM(double distanceKM) {
        this.distanceKM = distanceKM;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    /**
     * Calculate the emission based on the distance traveled and the type of vehicle used.
     * The emission factors for different vehicle types are as follows:
     * Car: 0.12 kg CO2 per km
     * Bus: 0.05 kg CO2 per km
     * Train: 0.03 kg CO2 per km
     * Cycle: 0.0 kg CO2 per km
     * @return the total emission in kg CO2 based on the distance traveled and the vehicle type. If the vehicle type is not recognized, an IllegalArgumentException is thrown.
     */

    // Implement the abstract method to calculate emissions based on distance and vehicle type
    @Override // Override the calculateEmission method to compute emissions based on distance and vehicle type
    public double calculateEmission() {
        double emissionFactor = 0.0;
        String vehicleTypeLower = vehicleType.toLowerCase(); // Convert vehicle type to lowercase for case-insensitive comparison
        if (vehicleTypeLower.equals("car")) {
            emissionFactor = 0.120; // // kg CO2 per km average emissions for cars, can vary based on fuel type and efficiency but this is a common estimate for gasoline cars
        } else if (vehicleTypeLower.equals("bus")) {
            emissionFactor = 0.060; //  kg CO2 per km lower emissions for buses compared to cars as they can carry more passengers and are often more fuel-efficient per passenger
        } else if (vehicleTypeLower.equals("train")) {
            emissionFactor = 0.045; // kg CO2 per km lower emissions for trains compared to cars and buses as they are more efficient and often use electricity which can be generated from renewable sources
        } else if (vehicleTypeLower.equals("cycle")) {
            emissionFactor = 0.0; // Bicycles have zero emissions
        } else {
            throw new IllegalArgumentException("Unknown vehicle type: " + vehicleType); // Handle unknown vehicle types
        }

        return distanceKM * emissionFactor; // Total emissions in kg CO2
    }

     /**
     * Override the toString method to include distance and vehicle type information in the string representation of the TransportationEmission object.
     * @return a formatted string representation of the TransportationEmission entry, including distance traveled and vehicle type.
     */

    @Override // Override the toString method to include distance and vehicle type information
    public String toString() {
        return String.format("%s | %s, %.1f km | %.2f kg CO2", 
                super.toString(), 
                vehicleType, 
                distanceKM, 
                calculateEmission());
        }
}
