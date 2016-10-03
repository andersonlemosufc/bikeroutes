package es.upv.sdm.labs.bikeroutes.model;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by anderson on 12/04/2016.
 */
public class Location {

    public static final int DEFAULT_DISTANCE = 10;

    int id;
    private double latitude;
    private double longitude;
    private String address;

    public Location(){
        this(0,0,0,"");
    }

    public Location(String address, Context context){
        this(Location.getLocationFromAddress(address, context));
    }

    public Location(double latitude, double longitude, Context context){
        this(Location.getLocationFromCoordinates(latitude, longitude, context));
    }

    public Location(int id, String address, Context context){
        this(Location.getLocationFromAddress(address, context));
        this.id = id;
    }

    public Location(int id, double latitude, double longitude, Context context){
        this(Location.getLocationFromCoordinates(latitude, longitude, context));
        this.id = id;
    }


    public Location(Location location){
        this(location.getLatitude(), location.getLongitude(), location.getAddress());
    }

    public Location(double latitude, double longitude, String address) {
        this(0, latitude, longitude, address);
    }

    public Location(int id, double latitude, double longitude, String address) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }

    public int getId(){
        return this.id;
    }

    public void setId(int id){
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Location{" +
                "id="+id+
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", address='" + address + '\'' +
                '}';
    }

    public static Location getLocationFromAddress(String address, Context context){
        List<Address> l = null;
        double latitude = 0;
        double longitude = 0;
        try {
            l = new Geocoder(context, Locale.getDefault()).getFromLocationName(address, 1);
            if(l.size()>0){
                latitude = l.get(0).getLatitude();
                longitude = l.get(0).getLongitude();
                address = l.get(0).getAddressLine(0)+", "+l.get(0).getAddressLine(1)+", "+l.get(0).getCountryName();
            }
        } catch (IOException  e) {
            e.printStackTrace();
        }
        if(l==null) latitude = longitude = 0;

        return new Location(latitude, longitude, address);
    }

    public static Location getLocationFromCoordinates(double latitude, double longitude, Context context){
        List<Address> l = null;
        String address = "";
        try {
            l = new Geocoder(context, Locale.getDefault()).getFromLocation(latitude, longitude, 1);
            if(l.size()>0){
                Address a = l.get(0);
                address += a.getAddressLine(0)+", "+a.getAddressLine(1)+", "+a.getCountryName();
            }
        } catch (IOException  e) {
            e.printStackTrace();
        }
        if(l==null) address = "";
        return new Location(latitude, longitude, address);
    }

    public static Location getLocationFromAndroidLocation(android.location.Location aLoc, Context context){

        Location l = new Location(aLoc.getLatitude(), aLoc.getLongitude(), context);
        return l;

    }
}
