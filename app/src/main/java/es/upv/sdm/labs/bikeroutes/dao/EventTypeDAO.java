package es.upv.sdm.labs.bikeroutes.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import es.upv.sdm.labs.bikeroutes.model.EventType;
import es.upv.sdm.labs.bikeroutes.model.Location;

/**
 * Created by anderson on 12/04/2016.
 */
public class EventTypeDAO extends AbstractDAO<EventType> {

    public EventTypeDAO(Context context) {
        super(context);
    }

    @Override
    public void insert(EventType eventType) {
        if(!eventType.getType().equals(EventType.Type.OTHER)){
            eventType.setId(eventType.getType().ordinal()+1);
        } else {
            SQLiteDatabase database = this.mySQLiteOpenHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("id", eventType.getId());
            values.put("type", eventType.getType().ordinal());
            values.put("specification", eventType.getSpecification());
            database.insert("event_type", null, values);
            database.close();
        }
    }

    @Override
    public void update(EventType eventType) {
        EventType old = this.findById(eventType.getId());
        if(old==null) {insert(eventType); return;}
        if(eventType.getType().equals(old.getType())){
            if(eventType.getType().equals(EventType.Type.OTHER)){
                SQLiteDatabase database = this.mySQLiteOpenHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("specification", eventType.getSpecification());
                database.update("event_type", values, "id=?", new String[]{String.valueOf(eventType.getId())});
                database.close();
            }
        } else {
            if(eventType.getType().equals(EventType.Type.OTHER)){
                insert(eventType);
            } else if(old.getType().equals(EventType.Type.OTHER)){
                remove(old.getId());
                eventType.setId(eventType.getType().ordinal() + 1);
            } else {
                eventType.setId(eventType.getType().ordinal()+1);
            }
        }
    }

    @Override
    public void remove(int id) {
        if(id==1 || id==2 || id==3) return;
        SQLiteDatabase database = this.mySQLiteOpenHelper.getWritableDatabase();
        database.delete("event_type", "id=?", new String[]{String.valueOf(id)});
        database.close();
    }

    @Override
    public EventType findById(int id) {
        ArrayList<EventType> l = find("id=?", new String[]{String.valueOf(id)});
        return (l.isEmpty() ? null : l.get(0));
    }

    @Override
    public ArrayList<EventType> findAll() {
        return find(null, null);
    }

    private ArrayList<EventType> find(String select, String values[]){
        ArrayList<EventType> eventTypes = new ArrayList<>();
        SQLiteDatabase database = this.mySQLiteOpenHelper.getReadableDatabase();
        Cursor cursor = database.query("event_type", new String[]{"id", "type", "specification"},
                select, values, null, null, null);
        while(cursor.moveToNext()){
            EventType eventType = new EventType();
            eventType.setId(cursor.getInt(0));
            eventType.setType(EventType.Type.values()[cursor.getInt(1)]);
            eventType.setSpecification(cursor.getString(2));
            eventTypes.add(eventType);
        }
        cursor.close();
        database.close();
        return eventTypes;
    }

}
