package Class;

import com.example.car_note.R;

import java.util.HashMap;
import java.util.Map;

public class Car {
    public static final String[] BODY_TYPE = {
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
        put(BODY_TYPE[0], BODY_TYPE_ICONS[0]);
        put(BODY_TYPE[1], BODY_TYPE_ICONS[1]);
        put(BODY_TYPE[2], BODY_TYPE_ICONS[2]);
        put(BODY_TYPE[3], BODY_TYPE_ICONS[3]);
        put(BODY_TYPE[4], BODY_TYPE_ICONS[4]);
        put(BODY_TYPE[5], BODY_TYPE_ICONS[5]);
        put(BODY_TYPE[6], BODY_TYPE_ICONS[6]);
        put(BODY_TYPE[7], BODY_TYPE_ICONS[7]);
        put(BODY_TYPE[8], BODY_TYPE_ICONS[8]);
    }};


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
