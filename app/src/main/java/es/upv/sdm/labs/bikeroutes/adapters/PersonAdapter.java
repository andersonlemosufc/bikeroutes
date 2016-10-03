package es.upv.sdm.labs.bikeroutes.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import es.upv.sdm.labs.bikeroutes.R;
import es.upv.sdm.labs.bikeroutes.activities.UserDerscriptionActivity;
import es.upv.sdm.labs.bikeroutes.dao.UserDAO;
import es.upv.sdm.labs.bikeroutes.model.User;
import es.upv.sdm.labs.bikeroutes.services.EventService;
import es.upv.sdm.labs.bikeroutes.services.ServerInfo;
import es.upv.sdm.labs.bikeroutes.services.UserService;
import es.upv.sdm.labs.bikeroutes.util.async.PostExecute;


public class PersonAdapter extends ArrayAdapter<User> {

    public static final int LIST_FRIENDS = 0;
    public static final int FIND_FRIENDS = 1;
    public static final int LIST_USERS_EVENT = 2;
    public static final int INVITE_FRIENDS = 3;


    int type, idOrganizer, idEvent;
    public static User user;
    Context context;

    public PersonAdapter(Context context, ArrayList<User> users, int type) {
        this(context, users, type, 0,0);
    }

    public PersonAdapter(Context context, ArrayList<User> users, int type, int idOrganizer, int idEvent) {
        super(context, 0, users);
        this.type = type;
        this.idOrganizer = idOrganizer;
        UserDAO dao = new UserDAO(context);
        user = dao.findById(PreferenceManager.getDefaultSharedPreferences(context).getInt("user_id",0));
        dao.close();
        this.context = context;
        this.idEvent = idEvent;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //ImageView ivUserPhoto, ivAddFriend, ivDeleteFriend, ivOrganizador, ivInviteFriend;
        //TextView tvName;


        // Get the data item for this position
        final User item = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_person, parent, false);
        }
        setVisibilityIcons(convertView, item.getId());
        TextView tvName = (TextView) convertView.findViewById(R.id.tvNameUser);
        ImageView ivDeleteFriend = (ImageView) convertView.findViewById(R.id.ivDeleteFriend);
        ImageView ivInviteFriend = (ImageView) convertView.findViewById(R.id.ivInviteFriend);
        ImageView ivAddFriend = (ImageView) convertView.findViewById(R.id.ivAddFriend);
        ImageView ivUserPhoto = (ImageView) convertView.findViewById(R.id.ivUserPhoto);

        tvName.setText(item.getName());
        if(item.getImage()!=null) ivUserPhoto.setImageBitmap(item.getImage());

        ivDeleteFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFriend(item);
            }
        });

        ivAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFriend(item);
            }
        });

        ivInviteFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inviteFriend(item.getId());
            }
        });

        ivUserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAccount(item.getId());
            }
        });

        tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAccount(item.getId());
            }
        });

        return convertView;
    }

    private void showAccount(int id){
        Intent intent = new Intent(context, UserDerscriptionActivity.class);
        intent.putExtra("id_person", id);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void inviteFriend(int idFriend){
        new EventService().invite(idEvent, idFriend, new PostExecute() {
            @Override
            public void postExecute(int option) {
                if(ServerInfo.RESPONSE_CODE==ServerInfo.RESPONSE_OK){
                    Toast.makeText(context, context.getString(R.string.invited_friend), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, context.getString(R.string.error_connection), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void addFriend(final User newFriend){
        UserService us = new UserService();
        us.addFriend(user, newFriend, new PostExecute() {
            @Override
            public void postExecute(int option) {
                if(ServerInfo.RESPONSE_CODE==UserService.ERROR_ALREADY_FRIENDS) {
                    Toast.makeText(context, context.getString(R.string.already_friend) + " " + newFriend.getName(), Toast.LENGTH_LONG).show();
                } else {
                    new AlertDialog.Builder(context)
                            .setTitle(context.getString(R.string.add_friend))
                            .setMessage(context.getString(R.string.do_you_really_want_to_add) + newFriend.getName() + context.getString(R.string.ad_your_friend))
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    Toast.makeText(context, newFriend.getName()+" "+context.getString(R.string.added_as_friend), Toast.LENGTH_LONG).show();

                                }})
                            .setNegativeButton(android.R.string.no, null).show();
                }
            }
        });
    }

    private void deleteFriend(final User oldFriend){
        UserService us = new UserService();
        us.removeFriend(user, oldFriend, new PostExecute() {
            @Override
            public void postExecute(int option) {
                new AlertDialog.Builder(context)
                        .setTitle(context.getString(R.string.remove_friend))
                        .setMessage(context.getString(R.string.do_you_really_want_to_remove) + oldFriend.getName() + context.getString(R.string.of_your_friends))
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                           public void onClick(DialogInterface dialog, int whichButton) {
                               Toast.makeText(context, oldFriend.getName() + " " + context.getString(R.string.removed_of_friends), Toast.LENGTH_LONG).show();

                           }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });
    }

    private void setVisibilityIcons(View view, int userId){
        switch (type){
            case FIND_FRIENDS:
                view.findViewById(R.id.ivDeleteFriend).setVisibility(View.GONE);
                view.findViewById(R.id.ivInviteFriend).setVisibility(View.GONE);
                view.findViewById(R.id.ivOrganizer).setVisibility(View.GONE);
                break;
            case INVITE_FRIENDS:
                view.findViewById(R.id.ivDeleteFriend).setVisibility(View.GONE);
                view.findViewById(R.id.ivAddFriend).setVisibility(View.GONE);
                view.findViewById(R.id.ivOrganizer).setVisibility(View.GONE);
                break;
            case LIST_FRIENDS:
                view.findViewById(R.id.ivAddFriend).setVisibility(View.GONE);
                view.findViewById(R.id.ivInviteFriend).setVisibility(View.GONE);
                view.findViewById(R.id.ivOrganizer).setVisibility(View.GONE);
                break;
            case LIST_USERS_EVENT:
                view.findViewById(R.id.ivDeleteFriend).setVisibility(View.GONE);
                view.findViewById(R.id.ivAddFriend).setVisibility(View.GONE);
                if(userId!= idOrganizer) view.findViewById(R.id.ivOrganizer).setVisibility(View.GONE);
                view.findViewById(R.id.ivInviteFriend).setVisibility(View.GONE);
                break;
        }
    }


}
