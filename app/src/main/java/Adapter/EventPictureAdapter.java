package Adapter;

import Class.Picture;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.car_note.R;

import java.util.ArrayList;

public class EventPictureAdapter extends BaseAdapter {

    Context context;
    ArrayList<Picture> pictures;
    LayoutInflater inflater;
    ArrayList<Bitmap> picturesBitmap;
    public EventPictureAdapter(
            Context context,
            ArrayList<Picture> pictures,
            ArrayList<Bitmap> picturesBitmap
    ) {
        this.context = context;
        this.pictures = pictures;
        this.picturesBitmap = picturesBitmap;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return pictures.size();
    }

    @Override
    public Picture getItem(int position) {
        return pictures.get(position - 1);
    }

    @Override
    public long getItemId(int position) {
        return position - 1;
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.event_picture_list_view, null);
        TextView index = convertView.findViewById(R.id.eventPictureParentTextViewIndex);
        ImageView image = convertView.findViewById(R.id.eventPictureParentImageViewPicture);
        TextView name = convertView.findViewById(R.id.eventPictureParentTextViewName);

        index.setText(String.valueOf(position + 1));
        image.setImageBitmap(picturesBitmap.get(position));
        name.setText(pictures.get(position).getName());


        return convertView;
    }
}
