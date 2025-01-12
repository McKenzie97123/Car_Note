package Database;

import Class.Car;
import Class.Event;
import Class.Picture;
import Class.User;
import Exception.UserNotFoundException;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Database extends SQLiteOpenHelper {

    public static final String DB_NAME = "carnote.db";

    public Database(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE user (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "email TEXT UNIQUE," +
                "name TEXT," +
                "lastName TEXT," +
                "password TEXT)"
        );
        db.execSQL("CREATE TABLE car (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "userId INTEGER," +
                "brand TEXT," +
                "model TEXT," +
                "color TEXT," +
                "type TEXT," +
                "plateNumber TEXT UNIQUE)"
        );
        db.execSQL("CREATE TABLE event (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "userId INTEGER," +
                "carId INTEGER," +
                "title TEXT," +
                "description TEXT," +
                "date TEXT," +
                "type TEXT)"
        );
        db.execSQL("CREATE TABLE picture (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "eventId INTEGER," +
                "name TEXT," +
                "path TEXT," +
                "format TEXT," +
                "deleted INTEGER CHECK (deleted IN (0, 1)))"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS user");
        db.execSQL("DROP TABLE IF EXISTS car");
        db.execSQL("DROP TABLE IF EXISTS event");
        db.execSQL("DROP TABLE IF EXISTS picture");
        onCreate(db);
    }

    public void insertUser(User user) throws SQLException {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("email", user.getEmail());
        values.put("name", user.getName());
        values.put("lastName", user.getLastName());
        values.put("password", user.getPassword());

        try {
            db.insert("user", null, values);

        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        } finally {
            db.close();
        }
    }

    public User getUser(String persistedEmail) throws UserNotFoundException, SQLException {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM user WHERE email = ?";

        try (Cursor cursor = db.rawQuery(sql, new String[]{persistedEmail})) {
            if (cursor.moveToFirst()) {
                @SuppressLint("Range") Integer id = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String email = cursor.getString(cursor.getColumnIndex("email"));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                @SuppressLint("Range") String lastName = cursor.getString(cursor.getColumnIndex("lastName"));
                @SuppressLint("Range") String password = cursor.getString(cursor.getColumnIndex("password"));
                return new User(id, email, name, lastName, password);
            } else {
                throw new UserNotFoundException("User not found");
            }
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        } finally {
            db.close();
        }
    }

    public String loginUser(String persistedEmail) throws UserNotFoundException, SQLException {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT password FROM USER WHERE email = ?";

        try (Cursor cursor = db.rawQuery(sql, new String[]{persistedEmail})) {
            if (cursor.moveToFirst()) {
                @SuppressLint("Range") String hashedPassword = cursor.getString(cursor.getColumnIndex("password"));
                return hashedPassword;
            } else {
                throw new UserNotFoundException("User not found");
            }
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        } finally {
            db.close();
        }
    }

    public void insertCar(Car car, int userId) throws SQLException {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("userId", userId);
        values.put("brand", car.getBrand());
        values.put("model", car.getModel());
        values.put("color", car.getColor());
        values.put("plateNumber", car.getPlateNumber());
        values.put("type", car.getType());

        try {
            db.insert("car", null, values);

        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }

    public ArrayList<Car> getListOfCars(int userId) throws SQLException {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM car WHERE userid = ?";
        try (Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(userId)})) {
            ArrayList<Car> cars = new ArrayList<>();
            if (cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
                    @SuppressLint("Range") String carBrand = cursor.getString(cursor.getColumnIndex("brand"));
                    @SuppressLint("Range") String carModel = cursor.getString(cursor.getColumnIndex("model"));
                    @SuppressLint("Range") String carColor = cursor.getString(cursor.getColumnIndex("color"));
                    @SuppressLint("Range") String carPlateNumber = cursor.getString(cursor.getColumnIndex("plateNumber"));
                    @SuppressLint("Range") String carType = cursor.getString(cursor.getColumnIndex("type"));
                    Car car = new Car(id, userId, carBrand, carModel, carColor, carPlateNumber, carType);

                    if (car.getId() != null) {
                        cars.add(car);
                    }

                } while (cursor.moveToNext());
                return cars;
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        } finally {
            db.close();
        }
    }

    public ArrayList<Event> getListOfEvents(int userId, int carId) throws SQLException {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM event WHERE userId = ? AND carId = ?";
        try (Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(userId), String.valueOf(carId)})) {
            ArrayList<Event> events = new ArrayList<>();
            if (cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") Integer id = cursor.getInt(cursor.getColumnIndex("id"));
                    @SuppressLint("Range") String eventTitle = cursor.getString(cursor.getColumnIndex("title"));
                    @SuppressLint("Range") String eventDescription = cursor.getString(cursor.getColumnIndex("description"));
                    @SuppressLint("Range") String eventType = cursor.getString(cursor.getColumnIndex("type"));
                    @SuppressLint("Range") String eventDateInString = cursor.getString(cursor.getColumnIndex("date"));

                    Event event = new Event(id, userId, carId, eventTitle, eventDescription, eventDateInString, eventType, null);

                    if (event.getId() != null) {
                        events.add(event);
                    }

                } while (cursor.moveToNext());
                return events;
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        } finally {
            db.close();
        }
    }

    public int insertEvent(Event event) throws SQLException {
        int eventId;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("userId", event.getUserId());
        values.put("carId", event.getCarId());
        values.put("title", event.getTitle());
        values.put("description", event.getDescription());
        values.put("date", event.getDate());
        values.put("type", event.getType());

        try {
            eventId = (int) db.insert("event", null, values);

        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        } finally {
            db.close();
        }

        return eventId;
    }

    public Event getSingleEvent(int eventId) throws SQLException {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM event WHERE id = ?";

        try (Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(eventId)})) {
            if (cursor.moveToFirst()) {
                @SuppressLint("Range") Integer id = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range") int userId = cursor.getInt(cursor.getColumnIndex("userId"));
                @SuppressLint("Range") int carId = cursor.getInt(cursor.getColumnIndex("carId"));
                @SuppressLint("Range") String eventTitle = cursor.getString(cursor.getColumnIndex("title"));
                @SuppressLint("Range") String eventDescription = cursor.getString(cursor.getColumnIndex("description"));
                @SuppressLint("Range") String eventType = cursor.getString(cursor.getColumnIndex("type"));
                @SuppressLint("Range") String eventDateInString = cursor.getString(cursor.getColumnIndex("date"));

                return new Event(id, userId, carId, eventTitle, eventDescription, eventDateInString, eventType, null);
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        } finally {
            db.close();
        }
    }

    public void updateEvent(Event event) throws SQLException {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("userId", event.getUserId());
        values.put("carId", event.getCarId());
        values.put("title", event.getTitle());
        values.put("description", event.getDescription());
        values.put("type", event.getType());
        values.put("date", event.getDate());

        String whereClause = "id = ?";
        String[] queryArgs = new String[]{String.valueOf(event.getId())};

        try {
            db.update("event", values, whereClause, queryArgs);
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        } finally {
            db.close();
        }
    }

    public void deleteEvent(int eventId) throws SQLException {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = "id = ?";
        String[] queryArgs = new String[]{String.valueOf(eventId)};

        try {
            db.delete("event", whereClause, queryArgs);
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }

    public void insertPictures(ArrayList<Picture> pictures, int eventId) {
        for (Picture picture : pictures) {
            try {
                insertPicture(picture, eventId);
            } catch (SQLException e) {
                throw new SQLException(e.getMessage());
            }
        }
    }

    private void insertPicture(Picture picture, int eventId) throws SQLException {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("eventId", eventId);
        values.put("name", picture.getName());
        values.put("path", picture.getPath());
        values.put("format", picture.getFormat());
        values.put("deleted", 0);

        try {
            db.insert("picture", null, values);

        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        } finally {
            db.close();
        }
    }

    public ArrayList<Picture> getPictures(int eventId) throws SQLException {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM picture WHERE eventId = ?";

        ArrayList<Picture> pictures = new ArrayList<>();
        try (Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(eventId)})) {
            if (cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") Integer id = cursor.getInt(cursor.getColumnIndex("id"));
                    @SuppressLint("Range") String pictureName = cursor.getString(cursor.getColumnIndex("name"));
                    @SuppressLint("Range") String picturePath = cursor.getString(cursor.getColumnIndex("path"));
                    @SuppressLint("Range") String pictureFormat = cursor.getString(cursor.getColumnIndex("format"));
                    @SuppressLint("Range") Boolean pictureDeleted = cursor.getInt(cursor.getColumnIndex("format")) == 1;

                    Picture picture = new Picture(id, eventId, pictureName, picturePath, pictureFormat, pictureDeleted);

                    if (picture.getId() != null) {
                        pictures.add(picture);
                    }

                } while (cursor.moveToNext());
                return pictures;
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        } finally {
            db.close();
        }
    }

    public void deletePicture(int pictureId) throws SQLException {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = "pictureId = ?";
        String[] queryArgs = new String[]{String.valueOf(pictureId)};

        try {
            db.delete("event", whereClause, queryArgs);
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        } finally {
            db.close();
        }
    }

    public void deleteAllEventPictures(int eventId) throws SQLException {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = "eventId = ?";
        String[] queryArgs = new String[]{String.valueOf(eventId)};

        try {
            db.delete("picture", whereClause, queryArgs);
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        } finally {
            db.close();
        }
    }
}
