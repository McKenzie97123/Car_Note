package Adapter;

import Class.Car;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.car_note.R;

import java.util.List;

public class CarAdapter extends ArrayAdapter<Car> {

    private Context Context;
    private List<Car> Cars;

    public CarAdapter(Context context, List<Car> cars) {
        super(context, 0, cars);
        Context = context;
        Cars = cars;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(Context).inflate(R.layout.car_pick, parent, false);
        }

        Car currentCar = Cars.get(position);

        TextView carId = listItem.findViewById(R.id.carPickListOfCars);
        carId.setText(currentCar.getId());

        return listItem;
    }
}
