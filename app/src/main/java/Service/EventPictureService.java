package Service;

import Class.Picture;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import java.util.ArrayList;

public class EventPictureService {
    public ArrayList<Picture> deletePicture(
            Context context,
            ArrayList<Picture> pictures,
            int pickedPictureId
    ) {
        if (pictures.isEmpty()) {
            Toast.makeText(context,
                    "Firstly try to add picture, then delete it",
                    Toast.LENGTH_LONG
            ).show();
            return pictures;
        }

        if (pickedPictureId < 0) {
            Toast.makeText(context,
                    "You haven't selected a picture to delete, click on the picture you want to delete",
                    Toast.LENGTH_LONG
            ).show();
             return pictures;
        }

        pictures.remove(pickedPictureId);
        Toast.makeText(context,
                "Picked picture has been removed from list",
                Toast.LENGTH_LONG
        ).show();
        return pictures;
    }

    public ArrayList<Bitmap> getPicturesBitmapFromFiles(
            ArrayList<Picture> pictures
    ) {
        ArrayList<Bitmap> picturesBitmap = new ArrayList<>();
        for (Picture picture : pictures) {
            String dirPath = picture.getPath();
            String nameWithSlash = "/" + picture.getName();
            String absolutePath = dirPath + nameWithSlash;

            Bitmap fileBitmap = BitmapFactory.decodeFile(absolutePath);
            picturesBitmap.add(fileBitmap);
        }

        return picturesBitmap;
    }
}
