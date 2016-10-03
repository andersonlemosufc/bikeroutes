package es.upv.sdm.labs.bikeroutes.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import es.upv.sdm.labs.bikeroutes.model.Event;
import es.upv.sdm.labs.bikeroutes.model.User;
import es.upv.sdm.labs.bikeroutes.util.DateHelper;

/**
 * Created by anderson on 12/04/2016.
 */
public class EventDAO extends AbstractDAO<Event> {

    private EventTypeDAO etDao;
    private LocationDAO lDao;

    public EventDAO(Context context) {
        super(context);
        this.etDao = new EventTypeDAO(context);
        this.lDao = new LocationDAO(context);
    }

    @Override
    public void insert(Event event) {
        lDao.insert(event.getArrival());
        lDao.insert(event.getDeparture());
        etDao.insert(event.getType());
        SQLiteDatabase database = this.mySQLiteOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", event.getId());
        values.put("type_id", event.getType().getId());
        values.put("date", DateHelper.toFormatString(event.getDate()));
        values.put("departure_id", event.getDeparture().getId());
        values.put("arrival_id", event.getArrival().getId());
        values.put("description", event.getDescription());
        values.put("secret", event.isSecret() ? 1 : 0);
        values.put("organizer_id", event.getOrganizer().getId());
        values.put("over", event.isOver() ? 1 : 0);
        database.insert("event", null, values);
        database.close();
    }

    @Override
    public void update(Event event) {
        lDao.update(event.getArrival());
        lDao.update(event.getDeparture());
        etDao.update(event.getType());
        SQLiteDatabase database = this.mySQLiteOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("type_id", event.getType().getId());
        values.put("date", DateHelper.toFormatString(event.getDate()));
        values.put("departure_id", event.getDeparture().getId());
        values.put("arrival_id", event.getArrival().getId());
        values.put("description", event.getDescription());
        values.put("secret", event.isSecret() ? 1 : 0);
        values.put("organizer_id", event.getOrganizer().getId());
        values.put("over", event.isOver() ? 1 : 0);
        database.update("event", values, "id=?", new String[]{String.valueOf(event.getId())});
        database.close();
    }

    @Override
    public void remove(int id) {
        Event event = this.findById(id);
        lDao.remove(event.getArrival().getId());
        lDao.remove(event.getDeparture().getId());
        etDao.remove(event.getType().getId());
        SQLiteDatabase database = this.mySQLiteOpenHelper.getWritableDatabase();
        database.delete("event", "id=?", new String[]{String.valueOf(id)});
        database.close();
    }

    @Override
    public Event findById(int id) {
        ArrayList<Event> l = find("id=?", new String[]{String.valueOf(id)});
        return (l.isEmpty() ? null : l.get(0));
    }

    @Override
    public ArrayList<Event> findAll() {
        return find(null, null);
    }

    private ArrayList<Event> find(String select, String values[]){
        ArrayList<Event> events = new ArrayList<>();
        SQLiteDatabase database = this.mySQLiteOpenHelper.getReadableDatabase();
        Cursor cursor = database.query("event", new String[]{"id", "type_id", "date", "departure_id", "arrival_id", "description", "secret", "organizer_id", "over"},
                select, values, null, null, null);
        while(cursor.moveToNext()){
            Event event = new Event();
            event.setOrganizer(new User());
            event.setId(cursor.getInt(0));
            event.setType(etDao.findById(cursor.getInt(1)));
            event.setDate(DateHelper.toDate(cursor.getString(2)));
            event.setDeparture(lDao.findById(cursor.getInt(3)));
            event.setArrival(lDao.findById(cursor.getInt(4)));
            event.setDescription(cursor.getString(5));
            event.setSecret(cursor.getInt(6) == 1);
            event.getOrganizer().setId(cursor.getInt(7));
            event.setOver(cursor.getInt(8) == 1);
            events.add(event);
        }
        cursor.close();
        database.close();
        return events;
    }

    @Override
    public void close() {
        super.close();
        lDao.close();
        etDao.close();
    }
}
