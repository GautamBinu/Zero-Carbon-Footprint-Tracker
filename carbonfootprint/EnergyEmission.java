public class EnergyEmission extends EmissionSource{
    private Double kWHused;
    private String energySource;
    
    public EnergyEmission(String sourceID, String category, String date, String userName,Double kWHused, String energySource){
        super(sourceID, category, date, userName);
        this.kWHused=kWHused;
        this.energySource=energySource;

    }

    public Double kWHused(){
        return this.kWHused;
    }
    public void setkWHused(Double kWHused){
        this.kWHused=kWHused;
    }
    public String energySource(){
        return this.energySource;
    }
    public void energySource(String energySource){
        this.energySource=energySource;
    }

    @Override
    public double calculateEmission(){
        double energyFactor=1.0;
        energySource=energySource.toLowerCase();

        if (energySource.equals("Grid")){
            energyFactor=2.0;

        }
        else if (energySource.equals(("Solar"))){
            energyFactor=0.65;

        }
        else if (energySource.equals("Wind")){
            energyFactor=0.75;
        }
        else{
            return energyFactor;
        }
        return kWHused*energyFactor;
    }

    @Override
    public String toString(){
        return super.toString() + "Energy Source"+energySource+"kWH used"+kWHused;
    }
            
    
}