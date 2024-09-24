package com.example.car_note;

import Adapter.EventPictureAdapter;
import Class.Car;
import Class.Event;
import Class.Picture;
import Class.User;
import Database.Database;
import Manager.CarManager;
import Manager.EventManager;
import Manager.UserManager;
import Service.EventPictureService;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import static Class.Picture.PICTURE_FILE_NAME;
import static Class.Picture.PICTURE_FORMAT;
import static Class.Event.EVENT_TYPES;

public class EventEdit extends AppCompatActivity {
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
    Event currentEvent;
    private static final int CAPTURE_IMAGE_ACTIVITY = 22;
    private static final int SELECT_IMAGE_ACTIVITY = 222;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_edit);

        currentUser = UserManager.getInstance().getCurrentUser();
        currentCar = CarManager.getInstance().getCurrentCar();
        currentEvent = EventManager.getInstance().getCurrentEvent();

        pictures = getPictures(currentEvent.getId());

        EditText title = findViewById(R.id.eventEditTitle);
        title.setText(currentEvent.getTitle());

        EditText description = findViewById(R.id.eventEditDescription);
        description.setText(currentEvent.getDescription());

        Spinner spinner = findViewById(R.id.eventEditSpinnerEventType);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, types);
        spinner.setAdapter(adapter);

        picturesBitmap = getPicturesBitmapFromFiles(pictures);
        picturesList = findViewById(R.id.eventEditListEventPictures);
        eventPictureAdapter = new EventPictureAdapter(getApplicationContext(), pictures, picturesBitmap);
        picturesList.setAdapter(eventPictureAdapter);

        Button makePictureButton = findViewById(R.id.eventEditButtonMakePicture);
        Button addPictureButton = findViewById(R.id.eventEditButtonAddNewPicture);
        Button deletePictureButton = findViewById(R.id.eventEditButtonDeletePicture);

        Button editEvent = findViewById(R.id.eventEditButtonEditEvent);
        Button back = findViewById(R.id.eventEditButtonBack);

        editEvent.setOnClickListener(v -> {
            String eventTitle = title.getText().toString();
            String eventDescription = description.getText().toString();
            int eventTypeId = (int) spinner.getSelectedItemId();
            String eventType = EVENT_TYPES[eventTypeId];

            editEvent(currentUser.getId(), currentCar.getId(), eventTitle, eventDescription, eventType, currentEvent.getId());
        });

        makePictureButton.setOnClickListener(v -> {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAPTURE_IMAGE_ACTIVITY);
        });
        picturesList.setOnItemClickListener((adapterView, view, position, id) -> pickedPictureId = position);

        addPictureButton.setOnClickListener(v -> pickPicture());

        deletePictureButton.setOnClickListener(v -> {
            pictures = service.deletePicture(getApplicationContext(), pictures, pickedPictureId);
            pickedPictureId = -1;
            picturesList.setAdapter(eventPictureAdapter);
        });

        back.setOnClickListener(v -> returnToCarMainDashboard());
    }

    private void editEvent(
            Integer userId,
            Integer carId,
            String title,
            String description,
            String type,
            int pickedEventId
    ) {
        if (title.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "To edit picked event you need to fill " +
                    "up all presented text fields", Toast.LENGTH_LONG).show();
            return;
        }

        if (type.isEmpty()) {
            Toast.makeText(this, "Need to choose type of your modified event", Toast.LENGTH_LONG).show();
            return;
        }

        String datePattern = "dd-MM-yyyy";
        @SuppressLint("SimpleDateFormat") DateFormat simpleDateFormat = new SimpleDateFormat(datePattern);
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        String eventDateInString = simpleDateFormat.format(now);

        Event event = new Event(null, userId, carId, title, description, eventDateInString, type, null);
        updateEvent(event);

        if (pictures.isEmpty()) {
            Toast.makeText(this, "Event has been added without any picture", Toast.LENGTH_LONG).show();
            return;
        }

        updatePictures(pictures, pickedEventId);
        Toast.makeText(this, "Event has been added with new pictures", Toast.LENGTH_LONG).show();
        returnToCarMainDashboard();
    }

    private void updateEvent(Event event) {
        try {
            db.updateEvent(event);

            Toast.makeText(this, "event has been edited", Toast.LENGTH_LONG).show();
            returnToCarMainDashboard();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private ArrayList<Picture> getPictures(int eventId) {
        ArrayList<Picture> pictures = null;

        try {
            pictures = db.getPictures(eventId);

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        return pictures;
    }

    private ArrayList<Bitmap> getPicturesBitmapFromFiles(ArrayList<Picture> pictures) {
        return service.getPicturesBitmapFromFiles(pictures);
    }

    private void updatePictures(ArrayList<Picture> pictures, int pickedEventId) {
        try {
            db.deleteAllEventPictures(pickedEventId);

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        try {
            db.insertPictures(pictures, pickedEventId);

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        Toast.makeText(this, "Picture has been updated", Toast.LENGTH_LONG).show();
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
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY && resultCode == Activity.RESULT_OK && data != null) {
            try {
                Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");

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

                    File imageFile = new File(pictureDirectory, PICTURE_FILE_NAME);

                    try (FileOutputStream out = new FileOutputStream(imageFile)) {
                        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                        Toast.makeText(this, "Picture saved !", Toast.LENGTH_LONG).show();
                    }

                    Picture picture = new Picture(
                            null,
                            null,
                            PICTURE_FILE_NAME,
                            pictureDirectory.getAbsolutePath(),
                            ".jpg",
                            false);

                    picturesBitmap.add(imageBitmap);
                    pictures.add(picture);
                    picturesList.setAdapter(eventPictureAdapter);
                } else {
                    Toast.makeText(this, "Picture not taken", Toast.LENGTH_LONG).show();
                }
            } catch (IOException e) {
                Toast.makeText(this, "Error saving picture", Toast.LENGTH_LONG).show();
                e.getMessage();
            }
        } else {
            Toast.makeText(this, "Picture not taken", Toast.LENGTH_LONG).show();
        }

        if (requestCode == SELECT_IMAGE_ACTIVITY && resultCode == Activity.RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            String picturePath = imageUri.getPath();
            Picture picture = new Picture(
                    null,
                    null,
                    PICTURE_FILE_NAME,
                    picturePath,
                    PICTURE_FORMAT,
                    false
                    );
            Bitmap imageBitmap = uriToBitmap(imageUri);
            if (imageBitmap != null) {
                picturesBitmap.add(imageBitmap);
            } else {
                Toast.makeText(this, "Picture not selected", Toast.LENGTH_LONG).show();
            }
            pictures.add(picture);
            picturesList.setAdapter(eventPictureAdapter);
        } else {
            Toast.makeText(this, "Picture not selected", Toast.LENGTH_LONG).show();
        }
    }

    private Bitmap uriToBitmap(Uri selectedFileUri) {
        try {
            ParcelFileDescriptor parcelFileDescriptor =
                    getContentResolver().openFileDescriptor(selectedFileUri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);

            parcelFileDescriptor.close();
            return image;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void returnToCarMainDashboard() {
        Intent intent = new Intent(getApplicationContext(), CarMainDashboard.class);
        startActivity(intent);
    }
}
