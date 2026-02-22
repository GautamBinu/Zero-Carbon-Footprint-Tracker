public class TransportationEmission extends EmissionSource {
   
    private String vehicleType; // e.g., "Car", "Bus", "Train"
    private double distanceKM; // Distance traveled in kilometers

    // Constructor
    public TransportationEmission(String sourceID, String category, String date, String userName, double distanceKM, String vehicleType) {
        super(sourceID, category, date, userName); // already initializes the common attributes from EmissionSource
        this.distanceKM = distanceKM; // initialize the distance attribute
        this.vehicleType = vehicleType; // initialize the vehicleType attribute
    }

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

    // Implement the abstract method to calculate emissions based on distance and vehicle type
    @Override // Override the calculateEmission method to compute emissions based on distance and vehicle type
    public double calculateEmission() {
        double emissionFactor = 0.0;

        switch (vehicleType.toLowerCase()) {
            case "car":
                emissionFactor = 0.12; // kg CO2 per km
                break;
            case "bus":
                emissionFactor = 0.05; // kg CO2 per km
                break;
            case "train":
                emissionFactor = 0.03; // kg CO2 per km
                break;
            default:
                throw new IllegalArgumentException("Unknown vehicle type: " + vehicleType); // Handle unknown vehicle types appropriately by throwing an exception
        }

        return distanceKM * emissionFactor; // Total emissions in kg CO2
    }

    @Override // Override the toString method to include distance and vehicle type information
    public String toString() {
        return super.toString() + " Distance: " + distanceKM + " km Vehicle Type: " + vehicleType;
        }
}
