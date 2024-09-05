package Class;

import com.example.car_note.R;

import java.util.HashMap;
import java.util.Map;

public class Car {
    public static final String[] BODY_TYPES = {
            "suv",
            "sedan",
            "coupe",
            "hatchback",
            "bus",
            "convertible",
            "estate",
            "van",
            "pickup"
    };

    public static final int[] BODY_TYPE_ICONS = {
            R.drawable.suv,
            R.drawable.sedan,
            R.drawable.coupe,
            R.drawable.hatchback,
            R.drawable.bus,
            R.drawable.convertible,
            R.drawable.estate,
            R.drawable.van,
            R.drawable.pickup,
    };

    public static final Map<String, Integer> BODY_TYPE_MAP = new HashMap<String, Integer>() {{
        put(BODY_TYPES[0], BODY_TYPE_ICONS[0]);
        put(BODY_TYPES[1], BODY_TYPE_ICONS[1]);
        put(BODY_TYPES[2], BODY_TYPE_ICONS[2]);
        put(BODY_TYPES[3], BODY_TYPE_ICONS[3]);
        put(BODY_TYPES[4], BODY_TYPE_ICONS[4]);
        put(BODY_TYPES[5], BODY_TYPE_ICONS[5]);
        put(BODY_TYPES[6], BODY_TYPE_ICONS[6]);
        put(BODY_TYPES[7], BODY_TYPE_ICONS[7]);
        put(BODY_TYPES[8], BODY_TYPE_ICONS[8]);
    }};


    private Integer id = null;
    private Integer userId = null;
    private String brand;
    private String model;
    private String color;
    private String plateNumber;
    private String type;


    public Car(
            Integer id,
            Integer userId,
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

    public Integer getUserId() {
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
