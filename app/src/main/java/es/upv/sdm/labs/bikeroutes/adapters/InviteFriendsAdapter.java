package es.upv.sdm.labs.bikeroutes.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import es.upv.sdm.labs.bikeroutes.model.User;

/**
 * Created by anderson on 11/04/2016.
 */
public class InviteFriendsAdapter extends AbstractAdapter<User> {


    public InviteFriendsAdapter(Context context, int resource, ArrayList<User> data) {
        super(context, resource, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}

