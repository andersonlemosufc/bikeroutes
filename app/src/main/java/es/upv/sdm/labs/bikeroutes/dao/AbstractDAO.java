package es.upv.sdm.labs.bikeroutes.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by anderson on 11/04/2016.
 */
public abstract class AbstractDAO<T> {

    private Context context;
    protected MySQLiteOpenHelper mySQLiteOpenHelper;

    public abstract void insert(T t);

    public abstract void update(T t);

    public abstract void remove(int id);

    public abstract T findById(int id);

    public abstract ArrayList<T> findAll();

    public AbstractDAO(Context context){
        this.mySQLiteOpenHelper = new MySQLiteOpenHelper(context);
    }

    public Context getContext(){
        return this.context;
    }

    public void setContext(Context context){
        this.context = context;
    }

    public void close(){
        this.mySQLiteOpenHelper.close();
    }
}
