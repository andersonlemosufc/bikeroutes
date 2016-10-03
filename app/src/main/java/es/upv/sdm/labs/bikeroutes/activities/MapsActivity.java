package es.upv.sdm.labs.bikeroutes.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

import es.upv.sdm.labs.bikeroutes.R;
import es.upv.sdm.labs.bikeroutes.model.Event;
import es.upv.sdm.labs.bikeroutes.model.EventType;
import es.upv.sdm.labs.bikeroutes.services.EventService;
import es.upv.sdm.labs.bikeroutes.services.ServerInfo;
import es.upv.sdm.labs.bikeroutes.util.DateHelper;
import es.upv.sdm.labs.bikeroutes.util.async.PostExecute;

public class MapsActivity extends AppCompatActivity {

    private GoogleMap mMap;
    Polyline route = null;
    ProgressBar progressBar;
    UiSettings uiSettings;

    TextView tvDistance;
    TextView tvDuration;

    Intent intent;

    String distValueStr;
    String durValueStr;

    Event event;
    Context context;
    int eventID;

    double latP1;
    double longP1;
    double latP2;
    double longP2;

    String pos1Title = "StartName";
    String pos1Desc  = "StartDescription";
    String pos2Title = "FinishName";
    String pos2Desc  = "FinishDescription";
    EventType.Type type = EventType.Type.HIKE;
//    int img = (type==EventType.HIKE) ? R.drawable.hike : (type==EventType.RUN) ? R.drawable.running : R.drawable.bike;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        context = this;


        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        tvDistance = (TextView) findViewById(R.id.tvDistance);
        tvDuration = (TextView) findViewById(R.id.tvDuration);

        intent = getIntent();


        eventID = intent.getIntExtra("eventID", 0 );
        Log.d("MapsActivity", "EvID: " + Integer.toString(eventID));


        event = new Event();

        new EventService().findById(eventID, event, new PostExecute() {
            @Override
            public void postExecute(int option) {
                if(ServerInfo.RESPONSE_CODE == ServerInfo.RESPONSE_OK){
                    //ok

                    type = event.getType().getType();

                    latP1 = event.getDeparture().getLatitude();
                    longP1 = event.getDeparture().getLongitude();

                    Log.d("MapsActivity", "P1: " + latP1 + "," + longP1);

                    latP2 = event.getArrival().getLatitude();
                    longP2 = event.getArrival().getLongitude();

                    Log.d("MapsActivity", "P2: " + latP2 + "," + longP2);
                    
                    pos1Title = event.getDeparture().getAddress();
                    pos2Title = event.getArrival().getAddress();

                    Log.d("MapsActivity", "Event searched!");
                    Toast.makeText(context, R.string.event_searched, Toast.LENGTH_LONG).show();

                    // Obtain the SupportMapFragment and get notified when the map is ready to be used.
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map);
                    if (mapFragment != null) {
                        mapFragment.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(GoogleMap googleMap) {
                                mMap = googleMap;
                                uiSettings = mMap.getUiSettings();
                                uiSettings.setCompassEnabled(true);
                                uiSettings.setZoomControlsEnabled(true);



                                // Set the camera to the greatest possible zoom level that includes the bounds
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latP1, longP1), 10));

                                addMarker(latP1, longP1, pos1Title,
                                        pos1Desc, BitmapDescriptorFactory.HUE_GREEN);

                                addMarker(latP2, longP2, pos2Title,
                                        pos2Desc, BitmapDescriptorFactory.HUE_BLUE);

                                supportInvalidateOptionsMenu();


                            }
                        });
                    } else {
                        Toast.makeText(context, R.string.map_fragment_null, Toast.LENGTH_SHORT).show();
                    }

                    if (isConnectionAvailable()) {
                        Log.d("MapsActivity", "P1: " + latP1 + "," + longP1);
                        (new RouteAsyncTask()).execute(new LatLng(latP1, longP1), new LatLng(latP2, longP2));
                    }


                }
            }
        });




    }


    private void addMarker(double latitude, double longitude, String title, String snippet, float color) {
        MarkerOptions options = new MarkerOptions();
        options.position(new LatLng(latitude, longitude));
        options.title(title);
        options.snippet(snippet);
        options.icon(BitmapDescriptorFactory.defaultMarker(color));

        mMap.addMarker(options);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mMap != null) {
            getMenuInflater().inflate(R.menu.maps_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mNormalMap:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case R.id.mTerrainMap:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
            case R.id.mSatelliteMap:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            default:
                finish();
        }
        return true;
    }

    /**
     * Check whether Internet connectivity is available.
     */
    private boolean isConnectionAvailable() {

        // Get a reference to the ConnectivityManager
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        // Get information for the current default data network
        NetworkInfo info = manager.getActiveNetworkInfo();
        // Return true if there is network connectivity
        return ((info != null) && info.isConnected());
    }


    private class RouteAsyncTask extends AsyncTask<LatLng, Void, List<LatLng>> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<LatLng> doInBackground(LatLng... params) {

            List<LatLng> pointsList = null;

            String typeStr = (type==EventType.Type.BIKE) ? "bicycling" : "walking";

            String uri = String.format(Locale.US, "https://maps.googleapis.com/maps/api/directions/json?" +
                    "origin=%1$f,%2$f&destination=%3$f,%4$f&mode=" + typeStr + "&key="
                    + getResources().getString(R.string.google_maps_key) ,
                    params[0].latitude, params[0].longitude, params[1].latitude, params[1].longitude );

            Log.d("MapsActivity", Double.toString(params[0].latitude));
            Log.d("MapsActivity", uri);
            try {
                URL url = new URL(uri);
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setDoInput(true);

                if (connection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    JSONObject object = new JSONObject(response.toString());
                    JSONArray routesArray = object.getJSONArray("routes");
                    JSONObject route = routesArray.getJSONObject(0);
                    JSONObject polyline = route.getJSONObject("overview_polyline");
                    pointsList = PolyUtil.decode(polyline.getString("points"));

                    JSONArray legsArray = route.getJSONArray("legs");
                    JSONObject legs = legsArray.getJSONObject(0);

                    JSONObject distance = legs.getJSONObject("distance");
                    JSONObject duration = legs.getJSONObject("duration");

                    int distValue = (int) distance.get("value");
                    distValueStr = (String) distance.get("text");

                    int durValue = (int) duration.get("value");
                    durValueStr = (String) duration.get("text");

//                    Log.i("MapsActivity", Integer.toString(distValue));
                    Log.i("MapsActivity", distValueStr);

//                    Log.i("MapsActivity", Integer.toString(durValue));
                    Log.i("MapsActivity", durValueStr);

                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return pointsList;
        }

        @Override
        protected void onPostExecute(List<LatLng> result) {
            if (result != null) {
                if (route != null) {
                    route.remove();
                }
                route = mMap.addPolyline(new PolylineOptions()
                        .addAll(result)
                        .color(Color.parseColor("#FF0000"))
                        .width(12)
                        .geodesic(true));

                tvDistance.setText(String.format(getString(R.string.distance_with_value), distValueStr));
                tvDuration.setText(String.format(getString(R.string.duration_with_value), durValueStr));

            } else {
                Toast.makeText(MapsActivity.this, "Could not display route", Toast.LENGTH_SHORT).show();
            }
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}
