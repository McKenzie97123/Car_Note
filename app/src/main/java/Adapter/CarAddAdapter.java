package Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.car_note.R;
import org.jetbrains.annotations.NotNull;

public class CarAddAdapter extends ArrayAdapter<String> {
    int dropDownResource;
    Context context;
    String[] types;
    LayoutInflater inflater;

    public CarAddAdapter(Context context, int resource, String[] types) {
        super(context, resource, types);
        this.context = context;
        this.types = types;
        this.dropDownResource = resource;
        inflater = LayoutInflater.from(context);
    }

    public void setDropDownViewResource(int resource) {
        this.dropDownResource = resource;
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.car_add_custom_item, null);
        TextView type = convertView.findViewById(R.id.carAddType);
        type.setText(types[position]);

        return convertView;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getDropDownView(int position, View convertView, @NotNull ViewGroup parent) {
        convertView = inflater.inflate(R.layout.car_add_custom_item, null);
        TextView type = convertView.findViewById(R.id.carAddType);
        type.setText(types[position]);

        return convertView;
    }
}
