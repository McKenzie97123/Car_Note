package com.example.car_note;

import Adapter.EventPictureAdapter;
import Class.Car;
import Class.Event;
import Class.Picture;
import Class.User;
import Database.Database;
import Manager.CarManager;
import Manager.UserManager;
import Service.EventPictureService;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class EventAdd extends AppCompatActivity {
    private static final String[] EVENT_TYPES = Event.EVENT_TYPES;
    Database db = new Database(this);
    EventPictureService service = new EventPictureService(this);
    ArrayList<String> types = new ArrayList<>(Arrays.asList(EVENT_TYPES));
    ArrayList<Picture> pictures = new ArrayList<>();
    EventPictureAdapter eventPictureAdapter;
    ArrayList<Bitmap> picturesBitmap = new ArrayList<>();
    ListView picturesList;
    private int pickedPictureId = -1;
    User currentUser;
    Car currentCar;
    private static final int CAPTURE_IMAGE_ACTIVITY = 22;
    private static final int SELECT_IMAGE_ACTIVITY = 222;
    ImageView IVPreviewImage;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_add);

        currentUser = UserManager.getInstance().getCurrentUser();
        currentCar = CarManager.getInstance().getCurrentCar();

        EditText title = findViewById(R.id.eventAddTitle);
        EditText description = findViewById(R.id.eventAddDescription);

        Spinner spinnerEventType = findViewById(R.id.eventAddSpinnerEventType);
        ArrayAdapter<String> eventAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, types);
        spinnerEventType.setAdapter(eventAdapter);

        picturesList = findViewById(R.id.eventAddListEventPictures);
        eventPictureAdapter = new EventPictureAdapter(getApplicationContext(), pictures, picturesBitmap);
        picturesList.setAdapter(eventPictureAdapter);

        Button makePictureButton = findViewById(R.id.eventAddButtonMakePicture);
        Button addPictureButton = findViewById(R.id.eventAddButtonAddNewPicture);
        Button deletePictureButton = findViewById(R.id.eventAddButtonDeletePicture);

        Button addEvent = findViewById(R.id.eventAddButtonAddNewEvent);
        Button back = findViewById(R.id.eventAddButtonBack);

        addEvent.setOnClickListener(v -> {
            String eventTitle = title.getText().toString();
            String eventDescription = description.getText().toString();
            int eventTypeId = (int) spinnerEventType.getSelectedItemId();
            String eventType = EVENT_TYPES[eventTypeId];

            addNewEvent(currentUser.getId(), currentCar.getId(), eventTitle, eventDescription, eventType, pictures);
        });

        makePictureButton.setOnClickListener(v -> {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAPTURE_IMAGE_ACTIVITY);
        });
        picturesList.setOnItemClickListener((adapterView, view, position, id) -> pickedPictureId = position);

        addPictureButton.setOnClickListener(v -> pickPicture());

        deletePictureButton.setOnClickListener(v -> {
            pictures = service.deletePicture(this, pictures, pickedPictureId);
            pickedPictureId = -1;
            picturesList.setAdapter(eventPictureAdapter);
        });

        back.setOnClickListener(v -> returnToCarMainDashboard());
    }

    private void addNewEvent(
            Integer userId,
            Integer carId,
            String title,
            String description,
            String type,
            ArrayList<Picture> pictures
    ) {
        if (title.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "To add new event you need to fill " +
                    "up all presented text fields", Toast.LENGTH_LONG).show();
            return;
        }

        if (type.isEmpty()) {
            Toast.makeText(this, "Need to choose type of your new event", Toast.LENGTH_LONG).show();
            return;
        }

        String datePattern = "dd-MM-yyyy";
        @SuppressLint("SimpleDateFormat") DateFormat simpleDateFormat = new SimpleDateFormat(datePattern);
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        String eventDateInString = simpleDateFormat.format(now);

        Event event = new Event(null, userId, carId, title, description, eventDateInString, type, null);
        int eventId = insertEvent(event);

        if (pictures.isEmpty()) {
            Toast.makeText(this, "Event has been added without any picture", Toast.LENGTH_LONG).show();
            returnToCarMainDashboard();
            return;
        }

        insertPictures(pictures, eventId);
        Toast.makeText(this, "Event has been added with " + pictures.size() + " pictures"
                , Toast.LENGTH_LONG).show();

        returnToCarMainDashboard();
    }

    private int insertEvent(Event event) {
        int eventId = 0;
        try {
            eventId = db.insertEvent(event);

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return eventId;
    }

    private void insertPictures(ArrayList<Picture> pictures, int eventId) {
        try {
            db.insertPictures(pictures, eventId);

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void pickPicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_IMAGE_ACTIVITY);
    }

    @Override
    protected void onActivityResult(
            int requestCode,
            int resultCode,
            @Nullable @org.jetbrains.annotations.Nullable
            Intent data
    ) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY && resultCode == Activity.RESULT_OK && data != null) {
            try {
                Bitmap imageBitmap = (Bitmap)data.getExtras().get("data");

                if (imageBitmap != null) {
                    File pictureDirectory = new File(
                            getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                            , currentUser.getId() + "/" + currentCar.getId()
                    );
                    if (!pictureDirectory.exists()) {
                        boolean mkdirResult = pictureDirectory.mkdirs();
                        if (mkdirResult) {
                            Toast.makeText(this, "Created directory to keep picture: "
                                    + pictureDirectory.getAbsolutePath(), Toast.LENGTH_LONG).show();
                        }
                    }

                    String pictureFileName = "IMG_" + System.currentTimeMillis() + ".jpg";
                    File imageFile = new File(pictureDirectory, pictureFileName);

                    try (FileOutputStream out = new FileOutputStream(imageFile)) {
                        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                        Toast.makeText(this, "Picture saved !", Toast.LENGTH_LONG).show();
                    }

                    Picture picture = new Picture(
                            null,
                            null,
                            pictureFileName,
                            pictureDirectory.getAbsolutePath(),
                            ".jpg",
                            false);

                    picturesBitmap.add(imageBitmap);
                    pictures.add(picture);
                    picturesList.setAdapter(eventPictureAdapter);
                } else {
                    Toast.makeText(this, "Picture Not taken", Toast.LENGTH_LONG).show();
                }
            } catch (IOException e) {
                Toast.makeText(this, "Error saving picture", Toast.LENGTH_LONG).show();
                e.getMessage();
            }
        } else {
            Toast.makeText(this, "Picture Not taken", Toast.LENGTH_LONG).show();
        }

        if (requestCode == SELECT_IMAGE_ACTIVITY && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (null != selectedImageUri) {
                IVPreviewImage.setImageURI(selectedImageUri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void returnToCarMainDashboard() {
        Intent intent = new Intent(getApplicationContext(), CarMainDashboard.class);
        startActivity(intent);
    }
}
