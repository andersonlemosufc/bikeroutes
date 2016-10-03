package es.upv.sdm.labs.bikeroutes.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

import es.upv.sdm.labs.bikeroutes.adapters.EventAdapter;
import es.upv.sdm.labs.bikeroutes.R;
import es.upv.sdm.labs.bikeroutes.adapters.EventAdapter2;
import es.upv.sdm.labs.bikeroutes.dao.UserDAO;
import es.upv.sdm.labs.bikeroutes.model.Event;
import es.upv.sdm.labs.bikeroutes.model.User;
import es.upv.sdm.labs.bikeroutes.services.EventService;
import es.upv.sdm.labs.bikeroutes.services.ServerInfo;
import es.upv.sdm.labs.bikeroutes.util.async.PostExecute;

public class DashboardActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    ListView recentEventsListView;
    User user;


    Context context;

    final int MY_PERMISSIONS_REQUEST_COARSE_LOCATION = 5;

    final ArrayList<Event> arrayOfEvents = new ArrayList<>();

    int eventID;

    /**
     * Provides the entry point to Google Play services.
     */
    protected GoogleApiClient mGoogleApiClient;

    /**
     * Represents a geographical location.
     */
    protected Location mLastLocation;

    protected static final String TAG = "DashboardActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        recentEventsListView = (ListView) findViewById(R.id.lvRecentEvents);
        UserDAO dao = new UserDAO(this);
        this.user = dao.findById(PreferenceManager.getDefaultSharedPreferences(this).getInt("user_id", 0));
        dao.close();

        context = this;

        buildGoogleApiClient();


        // When an item in the list is clicked
        recentEventsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("DashboardActivity", "Item " + position + " clicked");

                eventID = arrayOfEvents.get(position).getId();
                Intent intent = new Intent(context, EventDescriptionActivity.class);
                intent.putExtra("eventID", eventID);
                startActivity(intent);

            }
        });

//        populateEventsList();


    }

    /**
     * Builds a GoogleApiClient. Uses the addApi() method to request the LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
//        Log.d(TAG, "GAPIClient built");
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
//        Log.d(TAG, "GAPIClient connected");
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    private void populateEventsList() {
        //Construct data source

        arrayOfEvents.clear();
        new EventService().findNearbyEvents(PreferenceManager.getDefaultSharedPreferences(this).getInt("user_id", 0), es.upv.sdm.labs.bikeroutes.model.Location.getLocationFromAndroidLocation(mLastLocation, context), arrayOfEvents, new PostExecute() {
            @Override
            public void postExecute(int option) {
                if (ServerInfo.RESPONSE_CODE == ServerInfo.RESPONSE_OK) {

                    Log.d("DashboardActivity", arrayOfEvents.size()+"");

                    //Create the adapter to convert the array to views
                    EventAdapter2 adapter = new EventAdapter2(getApplicationContext(), arrayOfEvents);
                    //recentEventsListView = (ListView) findViewById(R.id.lvRecentEvents);
                    //attach the adapter to the listview
                    recentEventsListView.setAdapter(adapter);

                    //ok
                    Log.d("DashboardActivity", "Event searched location based!");


                } else {
                    //not ok
                    Log.d("DashboardActivity", "Error searching event location based!");
                    Toast.makeText(context, R.string.error_searching_event, Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    private void populateAllEventsList() {
        //Construct data source

        arrayOfEvents.clear();
        new EventService().findAvailable(PreferenceManager.getDefaultSharedPreferences(this).getInt("user_id", 0),arrayOfEvents, new PostExecute() {
            @Override
            public void postExecute(int option) {
                if (ServerInfo.RESPONSE_CODE == ServerInfo.RESPONSE_OK) {
                    //Create the adapter to convert the array to views
                    EventAdapter2 adapter = new EventAdapter2(getApplicationContext(), arrayOfEvents);
                    //recentEventsListView = (ListView) findViewById(R.id.lvRecentEvents);
                    //attach the adapter to the listview
                    recentEventsListView.setAdapter(adapter);

                    //ok
                    Log.d("DashboardActivity", "All Events searched!");


                } else {
                    //not ok
                    Log.d("DashboardActivity", "Error searching all events!");
                    Toast.makeText(context, R.string.error_searching_event, Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    public void dashboardButtonClicked(View view) {

        Intent intent = null;
        switch (view.getId()) {
            case R.id.btn_create_route:
                intent = new Intent(this, CreateEventActivity.class);
                break;
            case R.id.btn_search_route:
                intent = new Intent(this, SearchEventActivity.class);
                break;

        }
        startActivity(intent);
    }


    /*
This method is executed when the activity is created to populate the ActionBar with actions
*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.dashboard_menu, menu);
        menu.findItem(R.id.menuMyAccount).setVisible(true);
        if (user.getImage() != null)
            menu.findItem(R.id.menuMyAccount).setIcon(new BitmapDrawable(getResources(), user.getImage()));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.menuMyAccount:
                intent = new Intent(this, UserDerscriptionActivity.class);
                startActivity(intent);
                break;

            case R.id.menuMyEvents:
                startActivity(new Intent(this, MyEventsActivity.class));
                break;
            case R.id.menuSettings:
                startActivity(new Intent(this, MySettingsActivity.class));
                break;
            case R.id.menuLogOut:
                UserDAO dao = new UserDAO(this);
                dao.remove(user.getId());
                dao.close();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
                editor.remove("user_id");
                editor.remove("language");
                editor.apply();
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;

            case R.id.menuCredits:
                intent = new Intent(this, CreditActivity.class);
                startActivity(intent);
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        // Provides a simple way of getting a device's location and is well suited for
        // applications that do not require a fine-grained location and that do not need location
        // updates. Gets the best and most recent location currently available, which may be null
        // in rare cases when a location is not available.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "No permission");
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_COARSE_LOCATION);

            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            populateEventsList();
        } else {
            Toast.makeText(this, getString(R.string.no_location_detected), Toast.LENGTH_LONG).show();
            populateAllEventsList();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }


    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_COARSE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    if (mLastLocation != null) {
                        //use location based search
                        populateEventsList();
                    } else {
                        Toast.makeText(this, getString(R.string.no_location_detected), Toast.LENGTH_LONG).show();
                        populateAllEventsList();
                    }



                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


}
