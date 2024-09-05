package Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.car_note.R;

import java.util.ArrayList;

public class AddEventAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> types;
    LayoutInflater inflater;
    public AddEventAdapter(Context context, ArrayList<String> events) {
        this.context = context;
        this.types = events;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return types.size();
    }

    @Override
    public String getItem(int position) {
        return types.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.add_event_custom_item, null);
        TextView title = convertView.findViewById(R.id.carAddType);

        title.setText(types.get(position));

        return convertView;
    }
}
