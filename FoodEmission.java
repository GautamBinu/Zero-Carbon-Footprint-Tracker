public class FoodEmission extends EmissionSource {
    private String mealType;
    private int numberOfMeals;

    /**
     * Constructor for FoodEmission
     * @param sourceID
     * @param category
     * @param date
     * @param userName
     * @param mealType
     * @param numberOfMeals
     */
    // constructor
    public FoodEmission(String sourceID, String category, String date, String userName, String mealType, int numberOfMeals){
        super(sourceID, category, date, userName);
        this.mealType = mealType;
        this.numberOfMeals = numberOfMeals;
        
    }

    /**
     * Get the value of mealType
     * @return number of meals and meal type
     */
    // getters and setters
    public String getMealType(){return mealType;}

    public void setMealType(String mealType){this.mealType=mealType;}

    public int getNumberOfMeals(){return numberOfMeals;}

    public void setNumberOfMeals(int numberOfMeals){this.numberOfMeals=numberOfMeals;}

    /**
     * Calculate the emission based on the meal type and the number of meals.
     * The emission factor for each meal type is as follows:
     * Vegan: 0.67 kg CO2
     * Vegetarian: 0.80 kg CO2
     * Poultry: 2.40 kg CO2
     * Beef: 3.02 kg CO2
     * @return the total emission in kg CO2
     */
    @Override
    public double calculateEmission(){
        double mealFactor;
        mealType = mealType.toLowerCase();

        if (mealType.equals("vegan")){mealFactor=0.07;}

        else if(mealType.equals("vegetarian")){mealFactor=0.09;}

        else if(mealType.equals("poultry")){mealFactor=1.29;}

        else if(mealType.equals("beef")){mealFactor=2.04;}

        else{throw new IllegalArgumentException("Invalid meal type: "+mealType);}

        return numberOfMeals*mealFactor;

    }

    /**
     * Override the toString method to include meal type, number of meals, and calculated emissions.
     * @return a formatted string representation of the FoodEmission entry, including meal type, number of meals, and calculated emissions in kg CO2.
     */

    @Override
    public String toString(){
        return String.format("%s | %s, %d meals | %.2f kg CO2", 
                super.toString(), 
                mealType, 
                numberOfMeals, 
                calculateEmission());
    
    }

    
}
