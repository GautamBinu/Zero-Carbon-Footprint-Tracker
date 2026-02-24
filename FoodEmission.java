public class FoodEmission extends EmissionSource {
    private String mealType;
    private int numberOfMeals;


    // constructor
    public FoodEmission(String sourceID, String category, String date, String userName, String mealType, int numberOfMeals){
        super(sourceID, category, date, userName);
        this.mealType = mealType;
        this.numberOfMeals = numberOfMeals;
        
    }


    // getters and setters
    public String getMealType(){return mealType;}

    public void setMealType(String mealType){this.mealType=mealType;}

    public int getNumberOfMeals(){return numberOfMeals;}

    public void setNumberOfMeals(int numberOfMeals){this.numberOfMeals=numberOfMeals;}


    @Override
    public double calculateEmission(){
        double mealFactor;
        mealType = mealType.toLowerCase();

        if (mealType.equals("vegan")){mealFactor=0.67;}

        else if(mealType.equals("vegetarian")){mealFactor=0.80;}

        else if(mealType.equals("poultry")){mealFactor=2.40;}

        else if(mealType.equals("beef")){mealFactor=3.02;}

        else{mealFactor=0.0;}

        return numberOfMeals*mealFactor;

    }


    @Override
    public String toString(){
        return String.format("%s | %s, %d meals | %.2f kg CO2", 
                super.toString(), 
                mealType, 
                numberOfMeals, 
                calculateEmission());
    
    }

    
}
