package es.upv.sdm.labs.bikeroutes.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import es.upv.sdm.labs.bikeroutes.R;
import es.upv.sdm.labs.bikeroutes.model.EventType;
import es.upv.sdm.labs.bikeroutes.model.Event;


public class EventAdapter extends AbstractAdapter<Event> {


    public EventAdapter(Context context, ArrayList<Event> data) {
        super(context, R.layout.list_item_event, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Event event = this.getData().get(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(getResource(), parent, false);
        }

        TextView tvDate = (TextView) convertView.findViewById(R.id.tvEventDate);
        TextView tvTime = (TextView) convertView.findViewById(R.id.tvEventTime);
        ImageView ivEventType = (ImageView) convertView.findViewById(R.id.ivEventType);
        TextView tvStart = (TextView) convertView.findViewById(R.id.tvEventStart);
        TextView tvEnd = (TextView) convertView.findViewById(R.id.tvEventEnd);

        tvDate.setText(event.getDate().toString());
        tvTime.setText(event.getDate().getTime()+"");

        EventType type = event.getType();
        int img = (type.getType()== EventType.Type.HIKE) ? R.drawable.hike : (type.getType()==EventType.Type.RUN) ? R.drawable.running : R.drawable.bike;
        ivEventType.setImageResource(img);


        Log.d("EventAdapter", "StartName: " + event.getDeparture());
        tvStart.setText(event.getDeparture().toString());
        tvEnd.setText(event.getArrival().toString());

        // Return the completed view to render on screen
        return convertView;
    }
}
