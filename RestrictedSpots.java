import java.util.Random;

public class RestrictedSpots {
    Random r = new Random();

    private String spotID;
    private String spotName;
    private double spotWidth;
    private double spotHeight;
    private int permittedAverageTimePerRobot;
    private int maximumCapacity;
    private int currentOccupancy;

    public RestrictedSpots(String spotID, String spotName, double spotWidth, double spotHeight, int permittedAverageTimePerRobot, double tableArea) {
        this.spotID = spotID;
        this.spotName = spotName;
        this.spotWidth = spotWidth;
        this.spotHeight = spotHeight;
        this.permittedAverageTimePerRobot = permittedAverageTimePerRobot;
        this.maximumCapacity = (int)(spotWidth*spotHeight/tableArea);
        this.currentOccupancy = r.nextInt(1, maximumCapacity+5);
    }

    public void setMaximumCapacity(int capacity) {
        this.maximumCapacity = capacity;
    }

    public void setCurrentOccupancy(int occupancy) {
        this.currentOccupancy = occupancy;
    }

    public String getSpotID() {
        return spotID;
    }

    public void setSpotID(String spotID) {
        this.spotID = spotID;
    }

    public String getSpotName() {
        return spotName;
    }

    public void setSpotName(String spotName) {
        this.spotName = spotName;
    }

    public double getSpotWidth() {
        return spotWidth;
    }

    public void setSpotWidth(double spotWidth) {
        this.spotWidth = spotWidth;
    }

    public double getSpotHeight() {
        return spotHeight;
    }

    public void setSpotHeight(double spotHeight) {
        this.spotHeight = spotHeight;
    }

    public int getPermittedAverageTimePerRobot() {
        return permittedAverageTimePerRobot;
    }

    public void setPermittedAverageTimePerRobot(int permittedAverageTimePerRobot) {
        this.permittedAverageTimePerRobot = permittedAverageTimePerRobot;
    }

    public int getMaximumCapacity() {
        return maximumCapacity;
    }

    public int getCurrentOccupancy() {
        return currentOccupancy;
    }
}