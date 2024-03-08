package Database;

import Class.Car;
import Class.User;
import Exception.UserNotFoundException;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "carnote.db";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

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
                "userId INT," +
                "brand TEXT," +
                "model TEXT," +
                "color TEXT," +
                "type TEXT," +
                "plateNumber TEXT UNIQUE)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS user");
        db.execSQL("DROP TABLE IF EXISTS car");
        onCreate(db);
    }

    public void insertUser(User user) throws SQLException {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put("email", user.getEmail());
            values.put("name", user.getName());
            values.put("lastName", user.getLastName());
            values.put("password", user.getPassword());

            db.insert("user", null, values);
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
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
        }
    }

    public void insertCar(Car car, int userId) throws SQLException {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put("userId", userId);
            values.put("brand", car.getBrand());
            values.put("model", car.getModel());
            values.put("color", car.getColor());
            values.put("plateNumber", car.getPlateNumber());
            values.put("type", car.getType());

            db.insert("car", null, values);
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }

    public List<Car> getListOfCars(int userId) throws UserNotFoundException, SQLException {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM car WHERE userid = ?";
        List<Car> cars = new ArrayList<>();
        try (Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(userId)})) {
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
                        cars.add(id, car);
                    }

                } while (cursor.moveToNext());
                return cars;
            } else {
                throw new UserNotFoundException("User not found");
            }
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }
}
