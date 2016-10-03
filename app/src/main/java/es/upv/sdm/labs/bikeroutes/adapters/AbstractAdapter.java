package es.upv.sdm.labs.bikeroutes.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by anderson on 11/04/2016.
 */
public abstract class AbstractAdapter<T> extends ArrayAdapter {

    private Context context;
    private int resource;
    private ArrayList<T> data;

    public AbstractAdapter(Context context, int resource, ArrayList<T> data) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
        this.data = data;
    }

    @Override
    public abstract View getView(int position, View convertView, ViewGroup parent);

    @Override
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public int getResource() {
        return resource;
    }

    public void setResource(int resource) {
        this.resource = resource;
    }

    public ArrayList<T> getData() {
        return data;
    }

    public void setData(ArrayList<T> data) {
        this.data = data;
    }
}
