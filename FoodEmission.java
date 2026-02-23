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

    public void setMealType(){this.mealType=mealType;}

    public int getNumberOfMeals(){return numberOfMeals;}

    public void setNumberOfMeals(){this.numberOfMeals=numberOfMeals;}


    @Override
    public double calculateEmission(){
        double mealFactor = 1.0;
        mealType = mealType.toLowerCase();

        if (mealType.equals("vegan")){mealFactor=0.2;}

        else if(mealType.equals("vegetarian")){mealFactor=0.3;}

        else if(mealType.equals("poultry")){mealFactor=0.4;}

        else if(mealType.equals("beef")){mealFactor=0.5;}

        else{return mealFactor;}

        return numberOfMeals*mealFactor;

    }


    @Override
    public String toString(){
        return super.toString() + "mealType: " + mealType + " numberOfMeals: " + numberOfMeals;
    }

    
}
