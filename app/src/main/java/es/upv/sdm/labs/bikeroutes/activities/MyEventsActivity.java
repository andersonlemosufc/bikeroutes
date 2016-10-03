package es.upv.sdm.labs.bikeroutes.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

import java.util.ArrayList;

import es.upv.sdm.labs.bikeroutes.R;
import es.upv.sdm.labs.bikeroutes.adapters.EventAdapter;
import es.upv.sdm.labs.bikeroutes.adapters.EventAdapter2;
import es.upv.sdm.labs.bikeroutes.dao.UserDAO;
import es.upv.sdm.labs.bikeroutes.model.Event;
import es.upv.sdm.labs.bikeroutes.model.User;
import es.upv.sdm.labs.bikeroutes.services.EventService;
import es.upv.sdm.labs.bikeroutes.services.ServerInfo;
import es.upv.sdm.labs.bikeroutes.services.UserService;
import es.upv.sdm.labs.bikeroutes.util.async.PostExecute;

public class MyEventsActivity extends AppCompatActivity {

    TabHost tabHost;
    ListView upcomingEventsListView;
    ListView recentEventsListView;
    ListView invitationsListView;

    Context context;
    int eventID;

    ArrayList<Event> arrayOfEvents = new ArrayList<>();

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_events);

        context = this;
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = prefs.edit();

        tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();

        TabHost.TabSpec spec = tabHost.newTabSpec(("TAB1"));
        //Tab indicator specified as Label and Icon
        spec.setIndicator(getString(R.string.tabUpcomingEvents), getResources().getDrawable(R.mipmap.ic_launcher));
        spec.setContent(R.id.layoutUpcomingEvents);
        tabHost.addTab(spec);

        spec = tabHost.newTabSpec(("TAB2"));

        //Tab indicator specified as Label and Icon
        spec.setIndicator(getString(R.string.tabRecentEvents), getResources().getDrawable(R.mipmap.ic_launcher));
        spec.setContent(R.id.layoutRecentEvents);
        tabHost.addTab(spec);

        spec = tabHost.newTabSpec(("TAB3"));

        //Tab indicator specified as Label and Icon
        spec.setIndicator(getString(R.string.tabEventInvitations), getResources().getDrawable(R.mipmap.ic_launcher));
        spec.setContent(R.id.layoutInvitations);
        tabHost.addTab(spec);

        upcomingEventsListView = (ListView) findViewById(R.id.lvMyUpcomingEvents);
        recentEventsListView = (ListView) findViewById(R.id.lvMyRecentEvents);
        invitationsListView = (ListView) findViewById(R.id.lvMyInvitations);

        upcomingEventsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("MyEventsActivity", "Item " + position + " clicked");
                eventID = arrayOfEvents.get(position).getId();
                Intent intent = new Intent(context, EventDescriptionActivity.class);
                intent.putExtra("eventID", eventID);
                startActivity(intent);
            }
        });

        recentEventsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("MyEventsActivity", "Item " + position + " clicked");
                eventID = arrayOfEvents.get(position).getId();
                Intent intent = new Intent(context, EventDescriptionActivity.class);
                intent.putExtra("eventID", eventID);
                startActivity(intent);
            }
        });

        invitationsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("MyEventsActivity", "Item " + position + " clicked");
                eventID = arrayOfEvents.get(position).getId();
                Intent intent = new Intent(context, EventDescriptionActivity.class);
                intent.putExtra("eventID", eventID);
                startActivity(intent);
            }
        });


        tabHost.setCurrentTab(0);

        final UserDAO dao = new UserDAO(context);
        final User user = dao.findById(prefs.getInt("user_id", 0));

        new UserService().findEventsConfirmed(user, new PostExecute() {
            @Override
            public void postExecute(int option) {
                if (ServerInfo.RESPONSE_CODE == ServerInfo.RESPONSE_OK) {

                    arrayOfEvents = user.getConfirmedEvents();

                    Log.d("MyEventsActivity", arrayOfEvents.size()+"");
                    //Create the adapter to convert the array to views
                    EventAdapter2 adapter = new EventAdapter2(context, arrayOfEvents);
                    recentEventsListView = (ListView) findViewById(R.id.lvMyUpcomingEvents);
                    //attach the adapter to the listview
                    recentEventsListView.setAdapter(adapter);
                    //ok
                    Log.d("MyEventsActivity", "Confirmed Events Searched");
                } else {
                    //not ok
                    Log.d("MyEventsActivity", "Error searching confirmed events!");
                }
            }
        });


        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {

                //upcoming events
                if (tabHost.getCurrentTab() == 0) {
                    Log.d("MyEventsActivity", "Tab 0 selected");
                    new UserService().findEventsConfirmed(user, new PostExecute() {
                        @Override
                        public void postExecute(int option) {
                            if (ServerInfo.RESPONSE_CODE == ServerInfo.RESPONSE_OK) {

                                arrayOfEvents = user.getConfirmedEvents();

                                Log.d("MyEventsActivity", arrayOfEvents.size()+"");
                                //Create the adapter to convert the array to views
                                EventAdapter2 adapter = new EventAdapter2(context, arrayOfEvents);
                                recentEventsListView = (ListView) findViewById(R.id.lvMyUpcomingEvents);
                                //attach the adapter to the listview
                                recentEventsListView.setAdapter(adapter);
                                //ok
                                Log.d("MyEventsActivity", "Confirmed Events Searched");
                            } else {
                                //not ok
                                Log.d("MyEventsActivity", "Error searching confirmed events!");
                            }
                        }
                    });

                }
                //recent events
                if (tabHost.getCurrentTab() == 1) {
                    Log.d("MyEventsActivity", "Tab 1 selected");
                    new UserService().findHistoric(user, new PostExecute() {
                        @Override
                        public void postExecute(int option) {
                            if (ServerInfo.RESPONSE_CODE == ServerInfo.RESPONSE_OK) {

                                arrayOfEvents = user.getHistoric();

                                Log.d("MyEventsActivity", arrayOfEvents.size()+"");
                                //Create the adapter to convert the array to views
                                EventAdapter2 adapter = new EventAdapter2(context, arrayOfEvents);
                                recentEventsListView = (ListView) findViewById(R.id.lvMyRecentEvents);
                                //attach the adapter to the listview
                                recentEventsListView.setAdapter(adapter);
                                //ok
                                Log.d("MyEventsActivity", "Recent Events Searched");
                            } else {
                                //not ok
                                Log.d("MyEventsActivity", "Error searching recent events!");
                            }
                        }
                    });

                }
                //invited events
                if (tabHost.getCurrentTab() == 2) {
                    Log.d("MyEventsActivity", "Tab 2 selected");
                    new UserService().findEventsInvited(user, new PostExecute() {
                        @Override
                        public void postExecute(int option) {
                            if (ServerInfo.RESPONSE_CODE == ServerInfo.RESPONSE_OK) {

                                arrayOfEvents = user.getInvited();

                                Log.d("MyEventsActivity", arrayOfEvents.size()+"");
                                //Create the adapter to convert the array to views
                                EventAdapter2 adapter = new EventAdapter2(context, arrayOfEvents);
                                recentEventsListView = (ListView) findViewById(R.id.lvMyInvitations);
                                //attach the adapter to the listview
                                recentEventsListView.setAdapter(adapter);
                                //ok
                                Log.d("MyEventsActivity", "Invited Events Searched");
                            } else {
                                //not ok
                                Log.d("MyEventsActivity", "Error searching invited events!");
                            }
                        }
                    });

                }
            }
        });

        dao.close();
    }

}
