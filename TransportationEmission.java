public class TransportationEmission {
    private String modeOfTransport;
    private double distanceTraveled;

    
    // Constructor
    public TransportationEmission(String modeOfTransport, double distanceTraveled) {
        this.modeOfTransport = modeOfTransport;
        this.distanceTraveled = distanceTraveled;
    }

    // Getters and Setters
    public String getModeOfTransport() {
        return modeOfTransport;
    }

    public void setModeOfTransport(String modeOfTransport) {
        this.modeOfTransport = modeOfTransport;
    }

    public double getDistanceTraveled() {
        return distanceTraveled;
    }

    public void setDistanceTraveled(double distanceTraveled) {
        this.distanceTraveled = distanceTraveled;
    }


    // Method to calculate emissions based on mode of transport and distance traveled
    
    public double calculateEmission() {
        double emissionFactor = 0.0;

        switch (modeOfTransport.toLowerCase()) {
            case "car":
                emissionFactor = 0.24; // kg CO2 per km
                break;
            case "bus":
                emissionFactor = 0.05; // kg CO2 per km
                break;
            case "train":
                emissionFactor = 0.03; // kg CO2 per km
                break;
            case "bicycle":
                emissionFactor = 0.0; // kg CO2 per km
                break;
            default:
                System.out.println("Unknown mode of transport. Emission factor set to 0.");
                break;
        }

        return emissionFactor * distanceTraveled; // Total emissions in kg CO2
    }

    @Override
    public String toString() {
        return "Mode of Transport: " + modeOfTransport + " Distance Traveled: " + distanceTraveled + " km Emission: " + calculateEmission() + " kg CO2";
    }


    
}
