package Class;

public class Car {
    private final int userId;
    private final String brand;
    private final String model;
    private final String color;
    private final String plateNumber;

    public Car(
            int userId,
            String brand,
            String model,
            String color,
            String plateNumber
    ) {
        this.userId = userId;
        this.brand = brand;
        this.model = model;
        this.color = color;
        this.plateNumber = plateNumber;
    }

    public int getUserId() {
        return this.userId;
    }

    public String getBrand() {
        return this.brand;
    }

    public String getModel() {
        return this.model;
    }

    public String getColor() {
        return this.color;
    }

    public String getPlateNumber() {
        return this.plateNumber;
    }
}
