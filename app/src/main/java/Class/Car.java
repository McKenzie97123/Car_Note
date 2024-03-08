package Class;

public class Car {
    public static final String[] BODY_TYPE = {
            "suv",
            "sedan",
            "coupe",
            "hatchback",
            "bus",
            "convertible",
            "estate",
            "van"
    };
    private Integer id = null;
    private int userId;
    private String brand;
    private String model;
    private String color;
    private String plateNumber;
    private String type;


    public Car(
            Integer id,
            int userId,
            String brand,
            String model,
            String color,
            String plateNumber,
            String type
    ) {
        this.id = id;
        this.userId = userId;
        this.brand = brand;
        this.model = model;
        this.color = color;
        this.plateNumber = plateNumber;
        this.type = type;
    }
    public Integer getId() {
        return this.id;
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
