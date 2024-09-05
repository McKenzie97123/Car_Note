package Class;

import java.util.Date;

public class Event {
    public static final String[] EVENT_TYPES = {
            "service",
            "fueling",
            "fees",
            "insurance",
            "modifications",
            "other"
    };

    private Integer id = null;
    private Integer userId;
    private Integer carId;
    private String title;
    private String description;
    private Date date;
    private String type;


    public Event(
            Integer id,
            int userId,
            int carId,
            String title,
            String description,
            Date date,
            String type
    ) {
        this.id = id;
        this.userId = userId;
        this.carId = carId;
        this.title = title;
        this.description = description;
        this.date = date;
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public int getCarId() {
        return carId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Date getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

}
