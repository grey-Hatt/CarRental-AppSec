package customer;

public class Car {

    private final int regId;
    private final String name;
    private final String brand;
    private final String model;
    private final String engineNo;
    private final String chassisNo;
    private final String status;
    private final double price;

    public Car(int regId, String name, String brand, String model, String engineNo, String chassisNo, String status,
            double price) {
        this.regId = regId;
        this.name = name;
        this.brand = brand;
        this.model = model;
        this.engineNo = engineNo;
        this.chassisNo = chassisNo;
        this.status = status;
        this.price = price;
    }

    public int getRegId() {
        return regId;
    }

    public String getName() {
        return name;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public String getEngineNo() {
        return engineNo;
    }

    public String getChassisNo() {
        return chassisNo;
    }

    public String getStatus() {
        return status;
    }

    public double getPrice() {
        return price;
    }
}
