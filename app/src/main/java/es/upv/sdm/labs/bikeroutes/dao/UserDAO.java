package es.upv.sdm.labs.bikeroutes.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import es.upv.sdm.labs.bikeroutes.enumerations.Gender;
import es.upv.sdm.labs.bikeroutes.model.User;
import es.upv.sdm.labs.bikeroutes.util.ImgSerializer;

/**
 * Created by anderson on 12/04/2016.
 */
public class UserDAO extends AbstractDAO<User> {

    public UserDAO(Context context) {
        super(context);
    }

    @Override
    public void insert(User user) {
        SQLiteDatabase database = this.mySQLiteOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", user.getId());
        values.put("name", user.getName());
        values.put("mail", user.getMail());
        values.put("password", user.getPassword());
        values.put("description", user.getDescription());
        values.put("gender", user.getGender().ordinal());
        values.put("image", ImgSerializer.toByteArray(user.getImage()));
        database.insert("user", null, values);
        database.close();
    }

    @Override
    public void update(User user) {
        SQLiteDatabase database = this.mySQLiteOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", user.getName());
        values.put("mail", user.getMail());
        values.put("password", user.getPassword());
        values.put("description", user.getDescription());
        values.put("gender", user.getGender().ordinal());
        values.put("image", ImgSerializer.toByteArray(user.getImage()));
        database.update("user", values, "id=?", new String[]{String.valueOf(user.getId())});
        database.close();
    }

    @Override
    public void remove(int id) {
        SQLiteDatabase database = this.mySQLiteOpenHelper.getWritableDatabase();
        database.delete("user", "id=?", new String[]{String.valueOf(id)});
        database.close();
    }

    @Override
    public User findById(int id) {
        ArrayList<User> l = find("id=?", new String[]{String.valueOf(id)});
        return (l.isEmpty() ? null : l.get(0));
    }

    public User findByMail(String mail) {
        ArrayList<User> l = find("mail=?", new String[]{mail});
        return (l.isEmpty() ? null : l.get(0));
    }

    @Override
    public ArrayList<User> findAll() {
        return find(null, null);
    }

    private ArrayList<User> find(String select, String values[]){
        ArrayList<User> users = new ArrayList<>();
        SQLiteDatabase database = this.mySQLiteOpenHelper.getReadableDatabase();
        Cursor cursor = database.query("user", new String[]{"id", "name", "mail", "password", "description", "gender", "image"},
                select, values, null, null, null);
        while(cursor.moveToNext()){
            User user = new User();
            user.setId(cursor.getInt(0));
            user.setName(cursor.getString(1));
            user.setMail(cursor.getString(2));
            user.setPassword(cursor.getString(3));
            user.setDescription(cursor.getString(4));
            user.setGender(Gender.values()[cursor.getInt(5)]);
            user.setImage(ImgSerializer.fromByteArray(cursor.getBlob(6)));
            users.add(user);
        }
        cursor.close();
        database.close();
        return users;
    }
}
