package Class;

public class Car {
    private static final String[] BODY_TYPE = {"motorbike", "suv", "sedan", "coupe", "hatchback"};
    private int userId;
    private String brand;
    private String model;
    private String color;
    private String plateNumber;
    private String type;


    public Car(
            int userId,
            String brand,
            String model,
            String color,
            String plateNumber,
            String type
    ) {
        this.userId = userId;
        this.brand = brand;
        this.model = model;
        this.color = color;
        this.plateNumber = plateNumber;
        this.type = type;
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
    public String getType() {
        return this.type;
    }
}
