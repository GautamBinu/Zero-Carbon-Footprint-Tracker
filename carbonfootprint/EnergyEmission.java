public class EnergyEmission extends EmissionSource{
    private double kWHused;
    private String energySource;
    
    public EnergyEmission(String sourceID, String category, String date, String userName,double  kWHused, String energySource){
        super(sourceID, category, date, userName);
        this.kWHused=kWHused;
        this.energySource=energySource;

    }

    public double  getKwhused(){
        return this.kWHused;
    }
    public void setKwhused(double  kWHused){
        this.kWHused=kWHused;
    }
    public String getEnergySource(){
        return this.energySource;
    }
    public void setEnergySource(String energySource){
        this.energySource=energySource;
    }

    @Override
    public double calculateEmission(){

        if (kWHused == null || energySource == null) {
            return 0.0;
        }
        double  energyFactor=0.0;
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
        return kWHused*energyFactor;
    }

    @Override
    public String toString(){
        return super.toString() + "Energy Source: "+energySource+"kWH used: "+kWHused;
    }
            
    
}