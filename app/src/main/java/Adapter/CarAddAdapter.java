package Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.car_note.R;


public class CarAddAdapter extends BaseAdapter {

    Context context;
    String[] types;
    LayoutInflater inflater;

    public CarAddAdapter(Context context, String[] types) {
        this.context = context;
        this.types = types;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return types.length;
    }

    @Override
    public String getItem(int position) {
        return types[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.car_add_custom_item, null);
        TextView type = convertView.findViewById(R.id.carAddType);
        type.setText(types[position]);

        return convertView;
    }
}
