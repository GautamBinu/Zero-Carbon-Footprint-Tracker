public class EnergyEmission extends EmissionSource{
    private double kWhused;
    private String energySource;
    
    public EnergyEmission(String sourceID, String category, String date, String userName,double  kWhused, String energySource){
        super(sourceID, category, date, userName);
        this.kWhused=kWhused;
        this.energySource=energySource;

    }

    /**
     * Get the value of kWHused
     * @return return the value of kWhused
     */
    public double getKwhused(){
        return this.kWhused;
    }
    /**
     * Set the value of kWhused
     *  @param kWhused
     */
    public void setKwhused(double  kWhused){
        this.kWhused=kWhused;
    }
    /**
     * Get the value of energySource
     * @return return the value of energySource
     */
    public String getEnergySource(){
        return this.energySource;
    }
    /**
     * Set the value of energySource
     * @param energySource
     */
    public void setEnergySource(String energySource){
        this.energySource=energySource;
    }


    /**
     * 
     * 
     * Calculate the emission based on the energy source and the kWH used. 
     * The emission factor for;
     * grid=2.0
     * solar=0.65 
     * wind= 0.75
     * If the energy source is not recognized, the emission factor is 0.0.
     */
    @Override
    public double calculateEmission(){

        double energyFactor=0.0;
        energySource=energySource.toLowerCase();

        if (energySource.equals("grid")){
            energyFactor=2.0;

        }
        else if (energySource.equals("solar")){
            energyFactor=0.65;

        }
        else if (energySource.equals("wind")){
            energyFactor=0.75;
        }
        else{
            throw new IllegalArgumentException("Invalid energy source: "+energySource);
        }
        return kWhused*energyFactor;
    }

    /**
     * Override the toString method to include the energy source and kWH used in the string representation of the EnergyEmission object.
     */
    @Override
    public String toString(){
        return String.format("%s | %s, %.1f kWh | %.2f kg CO2", 
                super.toString(), 
                energySource, 
                kWhused, 
                calculateEmission());
    }
            
    
}
