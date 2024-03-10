package Adapter;

import Class.Car;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.car_note.R;

import java.util.ArrayList;
import java.util.Objects;

public class CarAdapter extends BaseAdapter {

    Context context;
    ArrayList<Car> cars;
    LayoutInflater inflater;
    public CarAdapter(Context context, ArrayList<Car> cars) {
        this.context = context;
        this.cars = cars;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return cars.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.car_pick_list_view, null);
        TextView brand = convertView.findViewById(R.id.carPickParentTextViewBrand);
        TextView model = convertView.findViewById(R.id.carPickParentTextViewModel);
        TextView plate = convertView.findViewById(R.id.carPickParentTextViewPlate);
        ImageView icon = convertView.findViewById(R.id.carPickParentImageViewIcon);

        brand.setText(cars.get(position).getBrand());
        model.setText(cars.get(position).getModel());
        plate.setText(cars.get(position).getPlateNumber());

        icon.setImageResource(Objects.requireNonNull(Objects.requireNonNull(Car.BODY_TYPE_MAP.get(cars.get(position).getType()))));

        return convertView;
    }
}
