package Adapter;

import Class.Event;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.car_note.R;

import java.util.ArrayList;

public class CarMainDashboardAdapter extends BaseAdapter {

    Context context;
    ArrayList<Event> events;
    LayoutInflater inflater;
    public CarMainDashboardAdapter(Context context, ArrayList<Event> events) {
        this.context = context;
        this.events = events;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public Event getItem(int position) {
        return events.get(position);
    }

    @Override
    public long getItemId(int position) {
        return events.get(position).getId();
    }


    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.main_dashboard_list_view, null);
        TextView title = convertView.findViewById(R.id.mainDashboardParentTextViewTitle);
        TextView date = convertView.findViewById(R.id.mainDashboardParentTextViewDate);
        TextView category = convertView.findViewById(R.id.mainDashboardParentTextViewType);
        TextView description = convertView.findViewById(R.id.mainDashboardParentTextViewDescription);

        title.setText(events.get(position).getTitle());
        date.setText(String.valueOf(events.get(position).getDate()));
        category.setText(events.get(position).getType());
        description.setText(events.get(position).getDescription());

        return convertView;
    }
}
