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

    //Abstract method
    public abstract double calculateEmission();

    //Formating 
     @Override
     public String toString () {
        return "Source ID: " + sourceID + " Category: " + category + " Date: " + date + " User Name: " + userName + " Emission: " + calculateEmission();
    }
}
