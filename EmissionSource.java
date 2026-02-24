/**
 * The EmissionSource class is an abstract class that serves as a blueprint for different types of emission sources, such as transportation, energy, and food. It contains common attributes like sourceID, category, date, and userName, along with an abstract method calculateEmission() that must be implemented by subclasses to calculate the specific emissions based on their unique attributes.
 */

public abstract class EmissionSource {
    private String sourceID;
    private String category;
    private String date;
    private String userName;

    //Constructor
    public EmissionSource(String sourceID, String category, String date, String userName)
    {
        this.sourceID = sourceID;
        this.category = category;
        this.date = date;
        this.userName = userName;
    }
    
    //Getters and Setters 
    public String getSourceID() {
        return sourceID;
    }

    public void setSourceID(String sourceID) {
        this.sourceID = sourceID;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Abstract method to calculate emissions. This method must be implemented by all subclasses of EmissionSource to provide specific logic for calculating emissions based on the attributes of the subclass.
     * @return the calculated emission in kg CO2 based on the specific attributes of the subclass. The implementation of this method will vary depending on the type of emission source (e.g., transportation, energy, food) and the relevant factors for calculating emissions for that source.
     */

    //Abstract method
    public abstract double calculateEmission();

    //Formating 

    /**
     * Override the toString method to provide a formatted string representation of the EmissionSource object, including the sourceID, category, and date. This method can be used to display the details of an emission entry in a readable format when printing or logging the object.
     * @return a formatted string representation of the EmissionSource entry, including sourceID, category, and date.
     */
     @Override
     public String toString () {
       return String.format("%s | %s | %s", sourceID, category, date);
    }
}
