package Class;

import java.util.ArrayList;

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
    private String date;
    private String type;
    private ArrayList<Picture> pictures = null;


    public Event(
            Integer id,
            int userId,
            int carId,
            String title,
            String description,
            String date,
            String type,
            ArrayList<Picture> pictures
    ) {
        this.id = id;
        this.userId = userId;
        this.carId = carId;
        this.title = title;
        this.description = description;
        this.date = date;
        this.type = type;
        this.pictures = pictures;
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

    public String getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    public ArrayList<Picture> getPictures() {
        return pictures;
    }

    public void setPictures(ArrayList<Picture> pictures) {
        this.pictures = pictures;
    }
}
