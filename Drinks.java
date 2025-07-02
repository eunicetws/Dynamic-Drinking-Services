public class Drinks {
    private String name;
    private String description;
    private double price;
    private int waitTime;  //small, medium, large

    public Drinks(String name, String description, double price, int waitTime) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.waitTime = waitTime;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public int getWaitTime() {
        return waitTime;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }
}

