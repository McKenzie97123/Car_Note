package Class;

public class Picture {
    private Integer id = null;
    private Integer eventId = null;
    private String name;
    private String path;
    private String format;
    private Boolean deleted;
    public static final String PICTURE_FORMAT = ".jpg";
    public static final String PICTURE_FILE_NAME = "IMG_" + System.currentTimeMillis() + PICTURE_FORMAT;

    public Picture(
            Integer id,
            Integer eventId,
            String name,
            String path,
            String format,
            Boolean deleted
    ) {
       this.id = id;
       this.eventId = eventId;
       this.name = name;
       this.path = path;
       this.format = format;
       this.deleted = deleted;
    }

    public Integer getId() {
        return id;
    }

    public Integer getEventId() {
        return eventId;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public String getFormat() {
        return format;
    }

    public Boolean getDeleted() {
        return deleted;
    }
}
