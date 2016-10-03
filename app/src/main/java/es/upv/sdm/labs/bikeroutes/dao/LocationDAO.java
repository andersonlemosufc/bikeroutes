package es.upv.sdm.labs.bikeroutes.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import es.upv.sdm.labs.bikeroutes.model.Location;

/**
 * Created by anderson on 12/04/2016.
 */
public class LocationDAO extends AbstractDAO<Location> {

    public LocationDAO(Context context) {
        super(context);
    }

    @Override
    public void insert(Location location) {
        SQLiteDatabase database = this.mySQLiteOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", location.getId());
        values.put("latitude", location.getLatitude());
        values.put("longitude", location.getLongitude());
        values.put("address", location.getAddress());
        database.insert("location", null, values);
        database.close();
    }

    @Override
    public void update(Location location) {
        SQLiteDatabase database = this.mySQLiteOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("latitude", location.getLatitude());
        values.put("longitude", location.getLongitude());
        values.put("address", location.getAddress());
        database.update("location", values, "id=?", new String[]{String.valueOf(location.getId())});
        database.close();
    }

    @Override
    public void remove(int id) {
        SQLiteDatabase database = this.mySQLiteOpenHelper.getWritableDatabase();
        database.delete("location", "id=?", new String[]{String.valueOf(id)});
        database.close();
    }

    @Override
    public Location findById(int id) {
        ArrayList<Location> aux = find("id=?", new String[]{String.valueOf(id)});
        return (aux.isEmpty() ? null : aux.get(0));
    }

    @Override
    public ArrayList<Location> findAll() {
        return find(null, null);
    }

    private ArrayList<Location> find(String select, String values[]){
        ArrayList<Location> locations = new ArrayList<>();
        SQLiteDatabase database = this.mySQLiteOpenHelper.getReadableDatabase();
        Cursor cursor = database.query("location", new String[]{"id", "latitude", "longitude", "address"},
                select, values, null, null, null);
        while(cursor.moveToNext()){
            Location location = new Location();
            location.setId(cursor.getInt(0));
            location.setLatitude(cursor.getInt(1));
            location.setLongitude(cursor.getInt(2));
            location.setAddress(cursor.getString(3));
            locations.add(location);
        }
        cursor.close();
        database.close();
        return locations;
    }
}
